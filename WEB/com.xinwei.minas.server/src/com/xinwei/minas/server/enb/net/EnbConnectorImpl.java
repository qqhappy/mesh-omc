/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpAddress;
import com.xinwei.common.udp.UdpServer;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.nms.common.util.RollingFileLogger;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB ͨ�Ŵ���ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class EnbConnectorImpl implements EnbConnector {

	public static final String EMS_TO_NE = " --> ";

	public static final String NE_TO_EMS = " <-- ";

	private static final Log log = LogFactory.getLog(EnbConnectorImpl.class);

	private List<EnbNotifyListener> listeners = new LinkedList<EnbNotifyListener>();

	// ����IP
	private String localIp;

	// ����UDP�����˿�
	private int localPort;

	// �Ƿ��¼������Ϣ
	private boolean logHeartbeatMessage;

	// UDP Server
	private UdpServer udpServer;

	// �ڲ����մ�����
	private InnerReceiver innerReceiver;

	// ��ʱʱ������λ�����룩
	private long timeout = 2000;

	/**
	 * ���캯��
	 * 
	 * @param localIp
	 * @param localPort
	 * @throws Exception
	 */
	public EnbConnectorImpl(String localIp, int localPort,
			boolean logHeartbeatMessage) throws Exception {
		this.localIp = localIp;
		this.localPort = localPort;
		this.logHeartbeatMessage = logHeartbeatMessage;
	}

	/**
	 * ��ʼ��
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		// ���ն��л��� ��λ��byte
		int bufferSize = 2 * 1024 * 1024;
		BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>(
				4096);
		BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
				4096);
		if (localIp == null || localIp.equals("")
				|| localIp.equals("127.0.0.1")) {
			udpServer = new DefaultUdpServer(localPort, bufferSize,
					receiveQueue, sendQueue, null);
		} else {
			udpServer = new DefaultUdpServer(localIp, localPort, bufferSize,
					receiveQueue, sendQueue, null);
		}

		// ��ʼ�������߳�
		innerReceiver = new InnerReceiver(receiveQueue);

		this.start();
	}

	@Override
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage)
			throws TimeoutException, Exception {
		return syncInvoke(enbAppMessage, timeout);
	}

	@Override
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage, long timeout)
			throws TimeoutException, Exception {
		// У����Ԫ�Ƿ������
		this.checkNeConfigurable(enbAppMessage);
		// ���
		List<EnbTransportMessage> transMsgList = prepare(enbAppMessage);
		// ע�ᷢ�͵���Ϣ
		FutureResult result = new FutureResult();
		EnbMessageRegistry.getInstance().register(
				enbAppMessage.getTransactionId(), result);

		// ��ȡĿ���ַ
		List<UdpAddress> target = getTarget(enbAppMessage);
		// ��¼Ӧ�ò���Ϣ��־
		logAppMessage(enbAppMessage, EMS_TO_NE);

		// ������Ϣ
		for (EnbTransportMessage transMsg : transMsgList) {
			byte[] bytes = EnbMessageHelper.encode(transMsg);
			udpServer.send(target, bytes);
			// ��¼�������Ϣ��־
			logTransMessage(transMsg.getMessageId(), bytes, EMS_TO_NE);
		}
		try {
			// �ȴ��ظ�
			EnbAppMessage response = (EnbAppMessage) result.timedGet(timeout);
			return response;
		} catch (EDU.oswego.cs.dl.util.concurrent.TimeoutException ex) {
			throw new TimeoutException(
					OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (InvocationTargetException e) {
			Exception ex = (Exception) e.getTargetException();
			if (ex instanceof UnsupportedOperationException) {
				throw new UnsupportedOperationException();
			} else {
				throw ex;
			}
		} finally {
			EnbMessageRegistry.getInstance().unregister(
					enbAppMessage.getTransactionId());
		}
	}

	@Override
	public void asyncInvoke(EnbAppMessage enbAppMessage) throws Exception {
		// У����Ԫ�Ƿ������
		this.checkNeConfigurable(enbAppMessage);
		// ���
		List<EnbTransportMessage> transMsgList = prepare(enbAppMessage);
		List<UdpAddress> target = getTarget(enbAppMessage);
		// ��¼Ӧ�ò���Ϣ��־
		logAppMessage(enbAppMessage, EMS_TO_NE);
		for (EnbTransportMessage transMsg : transMsgList) {
			byte[] bytes = EnbMessageHelper.encode(transMsg);
			udpServer.send(target, bytes);
			// ��¼�������Ϣ��־
			logTransMessage(transMsg.getMessageId(), bytes, EMS_TO_NE);
		}
	}

	private List<EnbTransportMessage> prepare(EnbAppMessage enbAppMessage)
			throws Exception {
		// ����transId
		if (enbAppMessage.getTransactionId() == 0) {
			enbAppMessage.setTransactionId(EnbTransIdGenerator.next());
		}
		// ���
		List<EnbTransportMessage> transMsgList = EnbMessageHelper
				.splitMessage(enbAppMessage);

		return transMsgList;
	}

	/**
	 * ��ȡĿ���վ�������ַ
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private List<UdpAddress> getTarget(EnbAppMessage message) throws Exception {
		Long enbId = message.getEnbId();
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		// ���ݻ�վID��ȡ��վ�Ĺ���IP�Ͷ˿�
		String btsIp = enb.getPublicIp();
		int btsPort = enb.getPublicPort();

		List<UdpAddress> udpAddressList = new LinkedList<UdpAddress>();
		UdpAddress udpAddress = new UdpAddress(btsIp, btsPort);
		udpAddressList.add(udpAddress);
		return udpAddressList;
	}

	@Override
	public void addListener(EnbNotifyListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(EnbNotifyListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void nofityListener(EnbAppMessage enbAppMessage) {
		for (EnbNotifyListener listener : listeners) {
			try {
				listener.receive(enbAppMessage);
			} catch (Exception e) {
				log.error("EnbConnector receive message error: "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * ��¼�������Ϣ��־
	 * 
	 * @param messageId
	 * @param bytes
	 * @param direct
	 */
	private void logTransMessage(long messageId, byte[] bytes, String direction) {
		RollingFileLogger log = EnbNetLoggerPool.getInstance().getTransLogger();
		direction += " [" + messageId + "]";
		log.log(direction, bytes, 0, bytes.length);

	}

	/**
	 * ��¼Ӧ�ò���Ϣ��־
	 * 
	 * @param message
	 */
	private void logAppMessage(EnbAppMessage message, String direction) {
		if (!logHeartbeatMessage && message.isHeartbeatMessage()) {
			// ������Ϣ����¼
			return;
		}
		RollingFileLogger log = EnbNetLoggerPool.getInstance().getAppLogger();
		byte[] bytes = new byte[0];
		try {
			bytes = EnbMessageHelper.encode(message);
		} catch (Exception e) {
			EnbConnectorImpl.log.error("encoding EnbAppMessage with error.", e);
		}
		String hexEnbId = StringUtils.to8HexString(message.getEnbId());
		direction += " [0x" + hexEnbId + "]";
		log.log(direction, bytes, 0, bytes.length);

	}

	public long getTimeout() {
		return timeout;
	}

	/**
	 * ����eNB��Ӧ��ʱʱ��
	 * 
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	private void start() throws Exception {
		udpServer.start();
		innerReceiver.startup();
	}

	public void stop() throws Exception {
		udpServer.shutdown();
		innerReceiver.shutdown();
	}

	class InnerReceiver extends Thread {

		private volatile boolean isRunning = true;

		private BlockingQueue<DatagramPacket> receiveQueue;

		// messageId -- EnbTransportMessage[]
		private Map<Long, EnbTransportMessage[]> transMsgMap = new HashMap<Long, EnbTransportMessage[]>();

		InnerReceiver(BlockingQueue<DatagramPacket> receiveQueue) {
			this.receiveQueue = receiveQueue;
		}

		@Override
		public void run() {
			while (isRunning()) {
				try {
					doWork();
				} catch (InterruptedException ex) {
					break;
				} catch (Throwable t) {
				}
			}
		}

		/**
		 * ���������Ϣ
		 * 
		 * @throws Exception
		 */
		private void doWork() throws Exception {
			DatagramPacket dp = (DatagramPacket) receiveQueue.take();
			// ��ȡ�ֽ���Ϣ
			byte[] data = dp.getData();
			int len = dp.getLength();
			byte[] buf = new byte[len];
			// ��ȡUDP��ָʾ��IP�Ͷ˿�
			String ip = "127.0.0.1";
			if (!dp.getAddress().isLoopbackAddress()) {
				ip = dp.getAddress().getHostAddress();
			}
			int port = dp.getPort();
			System.arraycopy(data, 0, buf, 0, len);
			EnbTransportMessage message = null;
			try {
				message = EnbMessageHelper.parse(buf);
				// ��¼�������Ϣ��־
				logTransMessage(message.getMessageId(), buf, NE_TO_EMS);
			} catch (Exception e) {
				log.warn("receive illegal EnbTransMessage. messageLength="
						+ len);
				return;
			}
			// �ҵ�����Ϣ�����ļ���
			EnbTransportMessage[] messageList = transMsgMap.get(message
					.getMessageId());
			// ��ǰ��Ϊ�հ���˳��,���յ�0�Ű�ʱ���������б�
			if (message.getPacketNo() == 0) {
				messageList = new EnbTransportMessage[message
						.getTotalPacketNum()];
				// ������Ϣ����
				transMsgMap.put(message.getMessageId(), messageList);
			}
			messageList[message.getPacketNo()] = message;
			// �ж��Ƿ��룬�����������������Ȼ���Ӧ�ñ��Ľ��н�������
			if (EnbMessageHelper.receiveAllSubPackets(messageList)) {
				transMsgMap.remove(message.getMessageId());
				EnbAppMessage appMessage = EnbMessageHelper
						.mergeMessage(messageList);
				appMessage.setPublicIp(ip);
				appMessage.setPublicPort(port);
				// ��¼Ӧ�ò���Ϣ��־
				logAppMessage(appMessage, NE_TO_EMS);
				Enb enb = EnbCache.getInstance().queryByEnbId(
						appMessage.getEnbId());
				// ��վ������ʱ���˻�վ��Ϣ
				if (enb == null) {
					log.warn("eNB is not exists. enbId=0x"
							+ StringUtils.to8HexString(appMessage.getEnbId()));
					return;
				} else {
					// ��վδ��վʱ���˻�վ��Ϣ
					if (!enb.isActive()) {
						log.warn("eNB is not active. enbId=0x"
								+ StringUtils.to8HexString(appMessage
										.getEnbId()));
						return;
					}
				}
				// �ж��Ƿ����첽���յ���Ϣ
				if (appMessage.isAsyncMessage()) {
					EnbConnectorImpl.this.nofityListener(appMessage);
				} else {
					FutureResult futureResult = EnbMessageRegistry
							.getInstance().unregister(
									appMessage.getTransactionId());
					if (futureResult != null) {
						futureResult.set(appMessage);
					}
				}
			}
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

	/**
	 * У����Ԫ�Ƿ������
	 * 
	 * @param enbAppMessage
	 * @throws Exception
	 */
	private void checkNeConfigurable(EnbAppMessage enbAppMessage)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByEnbId(enbAppMessage.getEnbId());
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// �����ǰ��վ��ע��״̬���ҵ�ǰҪ�·�����Ϣ��ע�������������·�
		if (enb.isOnlineManage() && enb.isRegistering()
				&& enbAppMessage.getMoc() == EnbMessageConstants.MOC_REGISTER) {
			return;
		}
		if (!enb.isConfigurable()) {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

}
