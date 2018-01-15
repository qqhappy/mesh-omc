/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpAddress;
import com.xinwei.common.udp.UdpMessageLogger;
import com.xinwei.common.udp.UdpServer;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.nms.common.util.RollingFileLogger;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBTS�ײ����ӽӿ�ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsConnectorImpl implements McBtsConnector {

	private static final Log log = LogFactory.getLog(McBtsConnectorImpl.class);

	private List<McBtsNotifyListener> listeners = new LinkedList<McBtsNotifyListener>();

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

	// studyService
	private McBtsStudyService mcBtsStudyService;

	/**
	 * ���캯��
	 * 
	 * @param localIp
	 * @param localPort
	 * @throws Exception
	 */
	public McBtsConnectorImpl(String localIp, int localPort,
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
		UdpMessageLogger udpMessageLogger = new McBtsUdpMessageLogger();
		udpServer.setUdpMessageLogger(udpMessageLogger);

		// ��ʼ�������߳�
		innerReceiver = new InnerReceiver(receiveQueue);

		this.start();
	}

	public void addListener(McBtsNotifyListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeListener(McBtsNotifyListener listener) {
		listeners.remove(listener);
	}

	public void nofityListener(McBtsMessage message) {
		for (McBtsNotifyListener listener : listeners) {
			try {
				listener.receive(message);
			} catch (Exception e) {
				log.debug("EmsConnector receive message error: "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * ͬ�����ýӿڷ���
	 * 
	 * @param request
	 *            ������Ϣ
	 */
	public McBtsMessage syncInvoke(McBtsMessage request)
			throws TimeoutException, UnsupportedOperationException, Exception {
		// �����վΪ���߹���״̬, ���׳��쳣
		if (isOffineManage(request)) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		// �����վδ����, ���׳��쳣
		if (isDisconnected(request)) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		// ������Ƕ�λ������Ϣ����Ϊ��Ϣ��������ID
		if (request.getMoc() != McBtsMessageConstants.MOC_LOCATION_REQUEST) {
			// ���ɲ���������ID
			if (request.getTransactionId() == 0) {
				int transactionId = TransactionIdGenerator.next();
				request.setTransactionId(transactionId);
			}
		}
		// ����Ϣ����ע���
		FutureResult futureResult = new FutureResult();
		MessageRegistry.getInstance().register(request.getTransactionId(),
				futureResult);
		// // ��¼��Ϣ��־
		// logMessage(request);

		// �Ƿ�֧�ָ�ҵ��,���֧��������Ϣ
		if (isSupported(request)) {
			// ��¼��Ϣ��־
			logMessage(request);
			McBts mcBts = McBtsCache.getInstance().queryByBtsId(
					request.getBtsId());
			// ������Ϣ
			List<UdpAddress> target = getTarget(request);
			udpServer.send(target, request.toBytes());
			try {
				McBtsMessage response = (McBtsMessage) futureResult
						.timedGet(timeout);
				// mcBtsStudyService.updateSupportedOperation(mcBts.getBtsType(),
				// mcBts.getSoftwareVersion(), request.getMoc(), 1);
				return response;
			} catch (EDU.oswego.cs.dl.util.concurrent.TimeoutException ex) {
				throw new TimeoutException("");
			} catch (InvocationTargetException e) {
				// new add
				Exception ex = (Exception) e.getTargetException();
				if (ex instanceof UnsupportedOperationException) {
					mcBtsStudyService.addUnsupportedResult(mcBts.getBtsType(),
							mcBts.getSoftwareVersion(), request.getMoc(), 0);
					throw new UnsupportedOperationException();
				} else {
					throw ex;
				}

			} finally {
				MessageRegistry.getInstance().unregister(
						request.getTransactionId());
			}
		} else {
			// // ��֧������һ�� actionResult=0 ��response���ظ�������
			// McBtsMessage response = new McBtsMessage();
			// response.setActionResult(0);
			// return response;
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));

		}
	}

	/**
	 * �첽���ýӿڷ���
	 * 
	 * @param mcBtsMessage
	 *            ������Ϣ
	 */
	public void asyncInvoke(McBtsMessage request) throws Exception {
		// �����վΪ���߹���״̬, ���׳��쳣
		if (isOffineManage(request)) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		// �����վδ�������׳��쳣
		if (isDisconnected(request)) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		// ������Ƕ�λ������Ϣ����Ϊ��Ϣ��������ID
		if (request.getMoc() != McBtsMessageConstants.MOC_LOCATION_REQUEST) {
			// ���ɲ���������ID
			if (request.getTransactionId() == 0) {
				int transactionId = TransactionIdGenerator.next();
				request.setTransactionId(transactionId);
			}
		}
		List<UdpAddress> target = getTarget(request);
		// ��¼��Ϣ��־
		logMessage(request);
		// �жϻ�վ�Ƿ�֧�ָ�ҵ��,���֧��������Ϣ
		if (isSupported(request)) {
			udpServer.send(target, request.toBytes());
		}

	}

	/**
	 * �жϻ�վ�Ƿ����
	 * 
	 * @param request
	 * @return
	 */
	private boolean isBtsExist(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null);
	}

	/**
	 * �жϻ�վ�Ƿ������߹���״̬
	 * 
	 * @param request
	 * @return
	 */
	private boolean isOffineManage(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null && bts.isOfflineManage());
	}

	/**
	 * �жϻ�վ�Ƿ�δ����
	 * 
	 * @param request
	 * @return
	 */
	private boolean isDisconnected(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null && bts.isDisconnected());
	}

	/**
	 * �жϻ�վ�Ƿ�֧�ָ���Ϣ
	 * 
	 * @param request
	 * @return
	 */
	private boolean isSupported(McBtsMessage request) {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		Boolean flag = mcBtsStudyService.isSupportedOperation(
				mcBts.getBtsType(), mcBts.getSoftwareVersion(),
				request.getMoc());

		return flag;
	}

	/**
	 * ��¼��Ϣ��־
	 * 
	 * @param message
	 */
	private void logMessage(McBtsMessage message) {
		if (!logHeartbeatMessage && message.isHeartbeatMessage()) {
			// ������Ϣ����¼
			return;
		}
		RollingFileLogger log = McBtsNetLoggerPool.getInstance().getLoggerBy(
				message.getBtsId());
		byte[] bytes = message.toBytes();
		String direction = null;
		String hexBtsId = StringUtils.to8HexString(message.getBtsId());
		if (message.isRequestMessage()) {
			direction = " --> ";
		} else {
			direction = " <-- ";
		}
		direction += " [0x" + hexBtsId + "]";
		log.log(direction, bytes, 0, bytes.length);
	}

	/**
	 * ��ȡ����Ŀ��
	 * 
	 * @param request
	 * @return
	 */
	private List<UdpAddress> getTarget(McBtsMessage request) {
		Long btsId = request.getBtsId();
		// ���ݻ�վID��ȡ��վ�Ĺ���IP�Ͷ˿�
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		String btsIp = mcBts.getPublicIp();
		int btsPort = mcBts.getPublicPort();
		//
		List<UdpAddress> udpAddressList = new LinkedList<UdpAddress>();
		UdpAddress udpAddress = new UdpAddress(btsIp, btsPort);
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
			String ip = "127.0.0.1";
			if (!dp.getAddress().isLoopbackAddress()) {
				ip = dp.getAddress().getHostAddress();
			}
			int port = dp.getPort();
			// ��ȡ�ֽ���Ϣ
			byte[] data = dp.getData();
			int len = dp.getLength();
			byte[] buf = new byte[len];
			System.arraycopy(data, 0, buf, 0, len);
			McBtsMessage message = new McBtsMessage(buf);
			// ��¼��Ϣ��־
			logMessage(message);
			// ���û�վ�Ĺ���IP�Ͷ˿�
			message.setPublicIp(ip);
			message.setPublicPort(port);

			if (!isBtsExist(message) && !message.isRegisterNotifyMessage()) {
				// �����վ�����ڣ��Ҳ���ע��֪ͨ��Ϣ������Դ���Ϣ
				log.warn("bts not exist, discard message. btsId=0x"
						+ Long.toHexString(message.getBtsId()));
				return;
			}
			if (message.isAsyncMessage() || message.isLocationMessage()
					|| message.isAsyncPerfMessage()) {
				// ֪ͨ��Ϣ
				McBtsConnectorImpl.this.nofityListener(message);
			} else if (message.isResponseMessage()
					|| message.isUnsupportedOperation()) {
				int transactionId = message.getTransactionId();
				FutureResult futureResult = MessageRegistry.getInstance()
						.unregister(transactionId);
				if (futureResult != null) {
					if (message.isUnsupportedOperation()) {
						futureResult
								.setException(new UnsupportedOperationException(
										OmpAppContext
												.getMessage("unsupported_biz_operation")));
					} else {
						futureResult.set(message);
					}

				}
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

	public McBtsStudyService getMcBtsStudyService() {
		return mcBtsStudyService;
	}

	public void setMcBtsStudyService(McBtsStudyService mcBtsStudyService) {
		this.mcBtsStudyService = mcBtsStudyService;
	}

}
