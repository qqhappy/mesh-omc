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
 * McBTS底层连接接口实现
 * 
 * @author chenjunhua
 * 
 */

public class McBtsConnectorImpl implements McBtsConnector {

	private static final Log log = LogFactory.getLog(McBtsConnectorImpl.class);

	private List<McBtsNotifyListener> listeners = new LinkedList<McBtsNotifyListener>();

	// 本端IP
	private String localIp;

	// 本端UDP侦听端口
	private int localPort;

	// 是否记录心跳消息
	private boolean logHeartbeatMessage;

	// UDP Server
	private UdpServer udpServer;

	// 内部接收处理器
	private InnerReceiver innerReceiver;

	// 超时时长（单位：毫秒）
	private long timeout = 2000;

	// studyService
	private McBtsStudyService mcBtsStudyService;

	/**
	 * 构造函数
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
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		// 接收队列缓存 单位：byte
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

		// 设置Udp消息记录器
		UdpMessageLogger udpMessageLogger = new McBtsUdpMessageLogger();
		udpServer.setUdpMessageLogger(udpMessageLogger);

		// 初始化接收线程
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
	 * 同步调用接口方法
	 * 
	 * @param request
	 *            请求消息
	 */
	public McBtsMessage syncInvoke(McBtsMessage request)
			throws TimeoutException, UnsupportedOperationException, Exception {
		// 如果基站为离线管理状态, 则抛出异常
		if (isOffineManage(request)) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		// 如果基站未连接, 则抛出异常
		if (isDisconnected(request)) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		// 如果不是定位请求消息，则为消息生成事务ID
		if (request.getMoc() != McBtsMessageConstants.MOC_LOCATION_REQUEST) {
			// 生成并设置事务ID
			if (request.getTransactionId() == 0) {
				int transactionId = TransactionIdGenerator.next();
				request.setTransactionId(transactionId);
			}
		}
		// 将消息放入注册表
		FutureResult futureResult = new FutureResult();
		MessageRegistry.getInstance().register(request.getTransactionId(),
				futureResult);
		// // 记录消息日志
		// logMessage(request);

		// 是否支持该业务,如果支持则发送消息
		if (isSupported(request)) {
			// 记录消息日志
			logMessage(request);
			McBts mcBts = McBtsCache.getInstance().queryByBtsId(
					request.getBtsId());
			// 发送消息
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
			// // 不支持则构造一个 actionResult=0 的response返回给调用者
			// McBtsMessage response = new McBtsMessage();
			// response.setActionResult(0);
			// return response;
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));

		}
	}

	/**
	 * 异步调用接口方法
	 * 
	 * @param mcBtsMessage
	 *            请求消息
	 */
	public void asyncInvoke(McBtsMessage request) throws Exception {
		// 如果基站为离线管理状态, 则抛出异常
		if (isOffineManage(request)) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		// 如果基站未连接则抛出异常
		if (isDisconnected(request)) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		// 如果不是定位请求消息，则为消息生成事务ID
		if (request.getMoc() != McBtsMessageConstants.MOC_LOCATION_REQUEST) {
			// 生成并设置事务ID
			if (request.getTransactionId() == 0) {
				int transactionId = TransactionIdGenerator.next();
				request.setTransactionId(transactionId);
			}
		}
		List<UdpAddress> target = getTarget(request);
		// 记录消息日志
		logMessage(request);
		// 判断基站是否支持该业务,如果支持则发送消息
		if (isSupported(request)) {
			udpServer.send(target, request.toBytes());
		}

	}

	/**
	 * 判断基站是否存在
	 * 
	 * @param request
	 * @return
	 */
	private boolean isBtsExist(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null);
	}

	/**
	 * 判断基站是否是离线管理状态
	 * 
	 * @param request
	 * @return
	 */
	private boolean isOffineManage(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null && bts.isOfflineManage());
	}

	/**
	 * 判断基站是否未连接
	 * 
	 * @param request
	 * @return
	 */
	private boolean isDisconnected(McBtsMessage request) {
		McBts bts = McBtsCache.getInstance().queryByBtsId(request.getBtsId());
		return (bts != null && bts.isDisconnected());
	}

	/**
	 * 判断基站是否支持该消息
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
	 * 记录消息日志
	 * 
	 * @param message
	 */
	private void logMessage(McBtsMessage message) {
		if (!logHeartbeatMessage && message.isHeartbeatMessage()) {
			// 心跳消息不记录
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
	 * 获取发送目标
	 * 
	 * @param request
	 * @return
	 */
	private List<UdpAddress> getTarget(McBtsMessage request) {
		Long btsId = request.getBtsId();
		// 根据基站ID获取基站的公网IP和端口
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
	 * 接收消息处理线程
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
		 * 处理接收消息
		 * 
		 * @throws Exception
		 */
		private void doWork() throws Exception {
			DatagramPacket dp = (DatagramPacket) receiveQueue.take();
			// 获取UDP包指示的IP和端口
			String ip = "127.0.0.1";
			if (!dp.getAddress().isLoopbackAddress()) {
				ip = dp.getAddress().getHostAddress();
			}
			int port = dp.getPort();
			// 获取字节消息
			byte[] data = dp.getData();
			int len = dp.getLength();
			byte[] buf = new byte[len];
			System.arraycopy(data, 0, buf, 0, len);
			McBtsMessage message = new McBtsMessage(buf);
			// 记录消息日志
			logMessage(message);
			// 设置基站的公网IP和端口
			message.setPublicIp(ip);
			message.setPublicPort(port);

			if (!isBtsExist(message) && !message.isRegisterNotifyMessage()) {
				// 如果基站不存在，且不是注册通知消息，则忽略此消息
				log.warn("bts not exist, discard message. btsId=0x"
						+ Long.toHexString(message.getBtsId()));
				return;
			}
			if (message.isAsyncMessage() || message.isLocationMessage()
					|| message.isAsyncPerfMessage()) {
				// 通知消息
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
		 * 停止线程
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
