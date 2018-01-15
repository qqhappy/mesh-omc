package com.xinwei.lte.web.uem.utils;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.utility.BlockingCell;

public class RPCClient {
	private Channel _channel = null;
	private String _exchange = "";
	private String _replyTo = "";
	private int _timeout = 0;

	protected final static int NO_TIMEOUT = -1;

	private Map<String, BlockingCell<Object>> _continuationMap = new HashMap<String, BlockingCell<Object>>();
	private int _correlationId = 0;

	private DefaultConsumer _consumer = null;

	private static Log logger = null;

	public RPCClient(Channel channel) throws IOException {		
		this(channel, 2 * 1000);		
	}

	public RPCClient(Channel channel, int timeout) throws IOException {
		logger = LogFactory.getLog(RPCClient.class);
		_channel = channel;
		_replyTo = channel.queueDeclare().getQueue();
		if (timeout < NO_TIMEOUT)
			throw new IllegalArgumentException(
					"Timeout arguument must be NO_TIMEOUT(-1) or non-negative.");

		_timeout = timeout;
		_correlationId = 0;

		_consumer = setupConsumer();
	}

	protected DefaultConsumer setupConsumer() throws IOException {
		DefaultConsumer consumer = new DefaultConsumer(_channel) {
			@Override
			public void handleShutdownSignal(String consumerTag,
					ShutdownSignalException signal) {
				synchronized (_continuationMap) {
					for (Entry<String, BlockingCell<Object>> entry : _continuationMap
							.entrySet()) {
						entry.getValue().set(signal);
					}
					_consumer = null;
				}
			}

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				synchronized (_continuationMap) {
					String replyId = properties.getCorrelationId();
					BlockingCell<Object> blocker = _continuationMap
							.get(replyId);
					_continuationMap.remove(replyId);

					if (null != body) {
						AbstractInnerMessage aim = new AbstractInnerMessage();
						aim.setMessageId(properties.getMessageId());
						aim.setBody(new String(body, "UTF-8"));

						blocker.set(aim);
					} else {
						blocker.set(body);
					}
				}
			}
		};

		_channel.basicConsume(_replyTo, true, consumer);

		return consumer;
	}

	private AbstractInnerMessage primitiveCall(String target, String msgId,
			byte[] message) throws IOException, ShutdownSignalException,
			TimeoutException {
		AMQP.BasicProperties props = null;
		if (_consumer == null) {
			throw new EOFException("RpcClient is closed");
		}

		BlockingCell<Object> k = new BlockingCell<Object>();
		synchronized (_continuationMap) {
			_correlationId++;
			String replyId = "" + _correlationId;
			props = new AMQP.BasicProperties.Builder().messageId(msgId)
					.correlationId(replyId).replyTo(_replyTo).build();
			_continuationMap.put(replyId, k);
		}

		_channel.basicPublish(_exchange, target, props, message);

		Object reply = k.uninterruptibleGet(_timeout);
		if (reply instanceof ShutdownSignalException) {
			ShutdownSignalException sig = (ShutdownSignalException) reply;
			ShutdownSignalException wrapper = new ShutdownSignalException(
					sig.isHardError(), sig.isInitiatedByApplication(),
					sig.getReason(), sig.getReference());
			wrapper.initCause(sig);
			throw wrapper;
		} else {
			return (AbstractInnerMessage) reply;
		}
	}

	public AbstractInnerMessage InvokeMethod(AbstractInnerMessage message)
			throws Throwable {
		// logger.debug("RPCClient InvokeMethod start");
		AbstractInnerMessage result = null;
		try {
			if (null == message.getBody())
				result = primitiveCall(message.getTarget(),
						message.getMessageId(), null);
			else
				result = primitiveCall(message.getTarget(),
						message.getMessageId(),
						message.getBody().getBytes("UTF-8"));

		} catch (Throwable e) {
			logger.error("InvokeMethod error", e);
			throw e;
		} finally {
			// channel.getConnection().close();
			logger.info("not close rabbit connect");
		}

		return result;
	}

	public void asynchronousCallMethod(AbstractInnerMessage message)
			throws IOException {
		BasicProperties replyProps = new BasicProperties().builder()
				.messageId(message.getMessageId()).build();
		// logger.debug(message.getBody());
		try {
			if (null == message.getBody())
				_channel.basicPublish("", message.getTarget(), replyProps, null);
			else
				_channel.basicPublish("", message.getTarget(), replyProps,
						message.getBody().getBytes());
		} catch (IOException e) {
			logger.error("send message error", e);
			throw e;
		}
	}
}
