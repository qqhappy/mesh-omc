/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpAddress;
import com.xinwei.common.udp.UdpMessageLogger;
import com.xinwei.common.udp.UdpServer;

/**
 * 
 * HLR UDP�ײ����ӽӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpConnectorImpl implements HlrUdpConnector {
	private static final Logger logger = Logger
			.getLogger(HlrUdpConnectorImpl.class);

	private List<HlrUdpNotifyListener> listeners = new LinkedList();

	private List<HlrUdpNotifyListener> ossListeners = new LinkedList();

	// ����IP
	private String localIp;

	// ����UDP�����˿�
	private int localPort;

	// UDP Server
	private UdpServer udpServer;

	// �ڲ����մ�����
	private InnerReceiver innerReceiver;

	// ��ʱʱ������λ�����룩
	private long timeout = 2000;
	
	private HlrUdpMessageRegistry hlrUdpMessageRegistry;

	/**
	 * ���캯��
	 * 
	 * @param localIp
	 * @param localPort
	 * @param remoteIp
	 * @param remotePort
	 * @throws Exception
	 */
	public HlrUdpConnectorImpl(String localIp, int localPort) throws Exception {
		this.localIp = localIp;
		this.localPort = localPort;

		// ���ն��л��� ��λ��byte
		int bufferSize = 2 * 1024 * 1024;
		BlockingQueue receiveQueue = new LinkedBlockingQueue(4096);
		BlockingQueue sendQueue = new LinkedBlockingQueue(4096);
		if (localIp == null || localIp.equals("")
				|| localIp.equals("127.0.0.1")) {
			udpServer = new DefaultUdpServer(localPort, bufferSize,
					receiveQueue, sendQueue, null);
		} else {
			udpServer = new DefaultUdpServer(localIp, localPort, bufferSize,
					receiveQueue, sendQueue, null);
		}

		// ����Udp��Ϣ��¼��
		UdpMessageLogger udpMessageLogger = new HlrUdpMessageLogger();
		udpServer.setUdpMessageLogger(udpMessageLogger);

		// ��ʼ�������߳�
		innerReceiver = new InnerReceiver(receiveQueue);

		this.start();
	}

	@Override
	public void addListener(HlrUdpNotifyListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(HlrUdpNotifyListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void nofityListener(HlrUdpMessage message) {
		for (HlrUdpNotifyListener listener : listeners) {
			try {
				listener.receive(message);
			} catch (Exception e) {
				logger.debug("nofityListener error: "
						, e);
			}
		}
	}
	
	@Override
	public void addOssListener(HlrUdpNotifyListener listener) {
		if (!ossListeners.contains(listener)) {
			ossListeners.add(listener);
		}
	}

	@Override
	public void removeOssListener(HlrUdpNotifyListener listener) {
		ossListeners.remove(listener);
	}

	@Override
	public void nofityOssListener(HlrUdpMessage message) {
		for (HlrUdpNotifyListener listener : ossListeners) {
			try {
				listener.receive(message);
			} catch (Exception e) {
				logger.debug("nofityOssListener error: "
						, e);
			}
		}

	}

	@Override
	public List<HlrUdpMessage> syncInvoke(HlrUdpMessage request)
			throws TimeoutException, Exception {
		// ���ɲ���������ID
		int transactionId = HlrTransactionIdGenerator.next();
		request.setTransactionId(transactionId);
		// ����Ϣ����ע���
		FutureResult futureResult = new FutureResult();
		hlrUdpMessageRegistry.register(
				request.getTransactionId(), futureResult);
		// ��¼��Ϣ��־
		logMessage(request);

		// ������Ϣ
		List<UdpAddress> target = getTarget(request);
		udpServer.send(target, request.toBytes());
		try {
			List<HlrUdpMessage> responses = (List<HlrUdpMessage>) futureResult
					.timedGet(timeout);
			return responses;
		} catch (EDU.oswego.cs.dl.util.concurrent.TimeoutException ex) {
			throw new TimeoutException("");
		} finally {
			hlrUdpMessageRegistry.unregister(transactionId);
		}
	}
	
	
	@Override
	public void asyncInvoke(HlrUdpMessage request) throws Exception {
		// ���ɲ���������ID
		int transactionId = HlrTransactionIdGenerator.next();
		request.setTransactionId(transactionId);
		List<UdpAddress> target = getTarget(request);
		udpServer.send(target, request.toBytes());
		// ��¼��Ϣ��־
		logMessage(request);

	}

	/**
	 * ��¼��Ϣ��־����HLR�ֱ��¼��
	 * 
	 * @param message
	 */
	private void logMessage(HlrUdpMessage message) {
		// do nothing
	}

	/**
	 * ��ȡ����Ŀ��
	 * 
	 * @param request
	 * @return
	 */
	private List<UdpAddress> getTarget(HlrUdpMessage request) {
		// ����������Ϣ����HLR��ip�Ͷ˿�
		String hlrIp = request.getHlrIp();
		int hlrPort = request.getHlrPort();
		List<UdpAddress> udpAddressList = new LinkedList();
		UdpAddress udpAddress = new UdpAddress(hlrIp, hlrPort);
		udpAddressList.add(udpAddress);
		return udpAddressList;
	}

	private void start() throws Exception {
		udpServer.start();
		innerReceiver.startup();
	}

	private void stop() throws Exception {
		udpServer.shutdown();
		innerReceiver.shutdown();
	}

	public void setHlrUdpMessageRegistry(HlrUdpMessageRegistry hlrUdpMessageRegistry) {
		this.hlrUdpMessageRegistry = hlrUdpMessageRegistry;
	}

	/**
	 * ������Ϣ�����߳�
	 * 
	 * @author chenjunhua
	 * 
	 */
	class InnerReceiver extends Thread {

		private volatile boolean isRunning = true;

		private BlockingQueue receiveQueue;

		public InnerReceiver(BlockingQueue receiveQueue) {
			this.receiveQueue = receiveQueue;
		}

		public void run() {
			while (isRunning()) {
				try {
					doWork();
				} catch (InterruptedException ex) {
					break;
				} catch (Throwable t) {
					logger.error("process message error", t);
				}
			}
			doShutdown();
		}

		/**
		 * ���������Ϣ
		 * 
		 * @throws Exception
		 */
		private void doWork() throws Exception {
			DatagramPacket dp = (DatagramPacket) receiveQueue.take();
			// ��ȡUDP��ָʾ��IP�Ͷ˿�
			String hlrIp = "127.0.0.1";
			if (!dp.getAddress().isLoopbackAddress()) {
				hlrIp = dp.getAddress().getHostAddress();
			}
			int hlrPort = dp.getPort();
			// ��ȡ�ֽ���Ϣ
			byte[] data = dp.getData();
			int len = dp.getLength();
			byte[] buf = new byte[len];
			System.arraycopy(data, 0, buf, 0, len);
			HlrUdpMessage message = new HlrUdpMessage(buf);
			message.setHlrIp(hlrIp);
			message.setHlrPort(hlrPort);
			// ��¼��Ϣ��־
			logMessage(message);
			// �첽��Ϣ��֪ͨ�ӿ�
			if (message.isOssMessage()) {
				HlrUdpConnectorImpl.this.nofityOssListener(message);
			} else if (message.isAsyncMessage()) {
				HlrUdpConnectorImpl.this.nofityListener(message);
			} else {
				hlrUdpMessageRegistry.addResponse(message);
			}
		}

		private void doShutdown() {

		}

		public void startup() {
			receiveQueue.clear();
			this.start();
		}

		/**
		 * ֹͣ�߳�
		 * 
		 */
		public void shutdown() {
			this.setRunning(false);
			this.interrupt();
		}

		public boolean isRunning() {
			return isRunning;
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
	
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
