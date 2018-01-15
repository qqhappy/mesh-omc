/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.sim;

import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.EnbTransportMessage;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeItemConfigCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.mcbts.net.McBtsUdpMessageLogger;
import com.xinwei.minas.server.mcbts.net.TransactionIdGenerator;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * @author fanhaoyu
 * 
 */

public class SimEnb {

	private Log log = LogFactory.getLog(SimEnb.class);

	private long enbId;

	// 本端IP
	private String localIp = "172.16.8.62";

	// 本端UDP侦听端口
	private int localPort = 19022;

	private String emsIp = "172.16.8.62";

	private int emsPort = 4999;

	// UDP Server
	private UdpServer udpServer;

	// 内部接收处理器
	private InnerReceiver innerReceiver;

	// 超时时长（单位：毫秒）
	private long timeout = 2000;

	private boolean connected = false;

	private long lastHeartbeat;
	// 30s未收到心跳，断开连接
	private long cutPeriod = 30000;

	private Timer registerTimer;

	private Timer reportTimer;

	public SimEnb(long enbId) throws Exception {
		this.enbId = enbId;
		int portAdd = (int) enbId;
		// 接收队列缓存 单位：byte
		int bufferSize = 2 * 1024 * 1024;
		BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>(
				4096);
		BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
				4096);
		if (localIp == null || localIp.equals("")
				|| localIp.equals("127.0.0.1")) {
			udpServer = new DefaultUdpServer(localPort + portAdd, bufferSize,
					receiveQueue, sendQueue, null);
		} else {
			udpServer = new DefaultUdpServer(localIp, localPort + portAdd,
					bufferSize, receiveQueue, sendQueue, null);
		}

		// 设置Udp消息记录器
		UdpMessageLogger udpMessageLogger = new McBtsUdpMessageLogger();
		udpServer.setUdpMessageLogger(udpMessageLogger);

		// 初始化接收线程
		innerReceiver = new InnerReceiver(receiveQueue);

		// 初始化注册定时器
		registerTimer = new Timer();
		registerTimer.scheduleAtFixedRate(new RegisterTask(this.enbId), 10000,
				5000);

		this.start();
	}

	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage)
			throws TimeoutException, Exception {
		return syncInvoke(enbAppMessage, timeout);
	}

	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage, long timeout)
			throws TimeoutException, Exception {
		List<EnbTransportMessage> transMsgList = prepare(enbAppMessage);

		// 注册发送的消息
		FutureResult result = new FutureResult();
		SimEnbMsgRegistry.getInstance().register(
				enbAppMessage.getTransactionId(), result);

		// 获取ems目标地址
		List<UdpAddress> target = getTarget();

		// 发送消息
		for (EnbTransportMessage transMsg : transMsgList) {
			udpServer.send(target, EnbMessageHelper.encode(transMsg));
		}
		try {
			// 等待回复
			EnbAppMessage response = (EnbAppMessage) result.timedGet(timeout);
			return response;
		} catch (EDU.oswego.cs.dl.util.concurrent.TimeoutException ex) {
			throw new TimeoutException("");
		} catch (InvocationTargetException e) {
			Exception ex = (Exception) e.getTargetException();
			if (ex instanceof UnsupportedOperationException) {
				throw new UnsupportedOperationException();
			} else {
				throw ex;
			}
		} finally {
			SimEnbMsgRegistry.getInstance().unregister(
					enbAppMessage.getTransactionId());
		}
	}

	public void asyncInvoke(EnbAppMessage enbAppMessage) throws Exception {
		List<EnbTransportMessage> transMsgList = prepare(enbAppMessage);
		List<UdpAddress> target = getTarget();
		// 记录消息日志
		for (EnbTransportMessage transMsg : transMsgList) {
			udpServer.send(target, EnbMessageHelper.encode(transMsg));
		}
	}

	private List<EnbTransportMessage> prepare(EnbAppMessage enbAppMessage)
			throws Exception {
		// 设置transId
		if (enbAppMessage.getTransactionId() == 0) {
			enbAppMessage.setTransactionId(TransactionIdGenerator.next());
		}
		// 拆包
		List<EnbTransportMessage> transMsgList = EnbMessageHelper
				.splitMessage(enbAppMessage);

		return transMsgList;
	}

	/**
	 * 获取目标基站的网络地址
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private List<UdpAddress> getTarget() throws Exception {
		List<UdpAddress> udpAddressList = new LinkedList<UdpAddress>();
		UdpAddress udpAddress = new UdpAddress(emsIp, emsPort);
		udpAddressList.add(udpAddress);
		return udpAddressList;
	}

	private void start() throws Exception {
		udpServer.start();
		innerReceiver.startup();
	}

	/**
	 * 处理异步消息
	 * 
	 * @param appMessage
	 */
	private void handleAsyncMessage(EnbAppMessage appMessage) {
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_RESPONSE);
		int moc = appMessage.getMoc();
		if (moc == EnbMessageConstants.MOC_REGISTER) {
			appMessage.addTagValue(TagConst.VERSION, "2.1.4.0");
			appMessage.addTagValue(TagConst.DATA_LOAD_STATUS, 1);
			try {
				log.debug("send register response.");
				asyncInvoke(appMessage);
				connected = true;
				lastHeartbeat = System.currentTimeMillis();
			} catch (Exception e) {
				log.error("send register response failed.");
			}
		} else if (moc == EnbMessageConstants.MOC_HEARTBEAT) {
			try {
				// log.debug("send heartbeat response.");
				lastHeartbeat = System.currentTimeMillis();
				asyncInvoke(appMessage);
			} catch (Exception e) {
				log.error("send heartbeat response failed.");
			}
		} else if (moc == EnbMessageConstants.MOC_INCREMENTAL_CONFIG) {
			String sqlText = appMessage.getStringValue(TagConst.SQL_TEXT);
			appMessage.getFieldMap().clear();
			appMessage.addTagValue(TagConst.RESULT, 0);
			// 判断是否是查询请求
			if (sqlText != null && sqlText.contains("select")) {
				appMessage.addTagValue(TagConst.ROW_NUM, 0);
				// appMessage.addTagValue(TagConst.RESULT_SET, "");
			}
			try {
				log.debug("send incremental config response.");
				asyncInvoke(appMessage);
			} catch (Exception e) {
				log.error("send incremental config response failed.");
			}

		} else if (moc == EnbMessageConstants.MOC_REALTIME_MONITOR_START_REQ_AND_RES) {
			try {
				if (reportTimer == null) {
					reportTimer = new Timer();
					// 暂定5s报一次
					reportTimer.scheduleAtFixedRate(
							new ReportRealtimeDataTask(), 5000, 1000);
				}
				appMessage.addTagValue(TagConst.RESULT, 0);
				asyncInvoke(appMessage);
			} catch (Exception e) {
				log.error("send start monitor response failed.");
			}
		} else if (moc == EnbMessageConstants.MOC_REALTIME_MONITOR_STOP_REQ_AND_RES) {
			try {
				if (reportTimer != null) {
					reportTimer.cancel();
					reportTimer = null;
				}
				appMessage.addTagValue(TagConst.RESULT, 0);
				asyncInvoke(appMessage);
			} catch (Exception e) {
				log.error("send start monitor response failed.");
			}
		}
	}

	class ReportRealtimeDataTask extends TimerTask {

		private int msgId = 0;

		@Override
		public void run() {
			try {
				EnbBizDataValidateHelper helper = OmpAppContext.getCtx()
						.getBean(EnbBizDataValidateHelper.class);
				Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
				List<Integer> cellIds = helper.getAllCellIdsByMoId(enb
						.getMoId());
				// typedef struct _T_Ei_MsgHead
				// {
				// U32 u32EiMsgId;
				// U32 u32AirTime;
				// U32 u32Sn;
				// U16 u16TlvNum;
				// U16 u16MsgLen; /* Tlv Head + Tlv Data */
				// }T_Ei_MsgHead;
				int length = 16;
				EnbRealtimeItemConfigCache configCache = EnbRealtimeItemConfigCache
						.getInstance();

				Map<Integer, EnbRealtimeItemConfig> map = configCache
						.getAllItemConfigs();

				List<Integer> tagList = configCache.getTagList();

				int allTagLength = 0;

				// 计算tag的数据长度
				Map<Integer, Integer> tagLengthMap = new HashMap<Integer, Integer>();
				for (Integer tagId : tagList) {
					List<Integer> itemList = configCache
							.getTagInnerItems(tagId);
					int tagLength = 0;
					for (Integer itemId : itemList) {
						EnbRealtimeItemConfig config = map.get(itemId);
						String dataType = config.getDataType();
						tagLength += Integer.valueOf(dataType.substring(1));
					}
					tagLength = tagLength / 8;
					tagLengthMap.put(tagId, tagLength);
					allTagLength += allTagLength + tagLength;
				}

				int msgLen = cellIds.size() * allTagLength;

				byte[] bytes = new byte[length + msgLen];
				int offset = 0;
				ByteUtils.putInt(bytes, offset, 301);
				offset += 4;
				// airtime
				ByteUtils.putInt(bytes, offset, msgId * 16);
				offset += 4;
				// sn
				ByteUtils.putInt(bytes, offset, 0);
				offset += 4;
				// tlvNum
				int tlvNum = tagList.size() * cellIds.size();
				ByteUtils.putNumber(bytes, offset, String.valueOf(tlvNum), 2);
				offset += 2;
				// msgLen
				ByteUtils.putNumber(bytes, offset, String.valueOf(msgLen), 2);
				offset += 2;
				Random random = new Random(System.currentTimeMillis());
				for (Integer cellId : cellIds) {

					for (Integer tagId : tagList) {
						List<Integer> itemList = configCache
								.getTagInnerItems(tagId);
						// typedef struct _T_Ei_TlvHead
						// {
						// U16 u16TlvId;
						// U16 u16Id;
						// U16 u16TlvLen;
						// U8 u8CellId;
						// U8 u8Rcv;
						// }T_Ei_TlvHead;
						ByteUtils.putNumber(bytes, offset,
								String.valueOf(tagId), 2);
						offset += 2;
						ByteUtils.putNumber(bytes, offset,
								String.valueOf(tagId), 2);
						offset += 2;
						int tlvLen = tagLengthMap.get(tagId);
						ByteUtils.putNumber(bytes, offset,
								String.valueOf(tlvLen), 2);
						offset += 2;
						ByteUtils.putNumber(bytes, offset,
								String.valueOf(cellId), 1);
						offset += 1;
						ByteUtils.putNumber(bytes, offset, "0", 1);
						offset += 1;

						for (Integer itemId : itemList) {
							EnbRealtimeItemConfig config = map.get(itemId);
							String dataType = config.getDataType();
							int dataLength = Integer.valueOf(dataType
									.substring(1)) / 8;
							int value = 0;
							if (itemId % 2 == 0) {
								value = random.nextInt(100);
							} else {
								value = random.nextInt(10000);
							}
							int flag = 1;
							if (dataType.startsWith("S")) {
								flag = random.nextInt(2) == 0 ? 1 : -1;
							}
							value = flag * value;
							ByteUtils.putNumber(bytes, offset,
									String.valueOf(value), dataLength);
							offset += dataLength;
						}
					}
				}
				EnbAppMessage appMessage = createRealtimeReportMessage(bytes);
				asyncInvoke(appMessage);

				msgId++;
			} catch (Exception e) {
				log.error("generate realtime data failed.", e);
			}
		}
	}

	class RegisterTask extends TimerTask {

		private long enbId;

		public RegisterTask(long enbId) {
			this.enbId = enbId;
		}

		@Override
		public void run() {
			// 未连接状态，发送注册通知
			if (!connected) {
				try {
					log.debug("send register notify to ems.");
					// 异步发送注册通知
					asyncInvoke(createRegisterNotify());
				} catch (Exception e) {
					connected = false;
				}
			} else {
				// 30s未收到心跳，断开连接
				if (System.currentTimeMillis() - lastHeartbeat >= cutPeriod) {
					log.debug("heartbeat timeout.");
					connected = false;
				}
			}
		}

		private EnbAppMessage createRegisterNotify() {
			EnbAppMessage appMessage = new EnbAppMessage();
			appMessage.setEnbId(enbId);
			appMessage.setMessageType(EnbMessageConstants.MESSAGE_NOTIFY);
			appMessage.setMoc(EnbMessageConstants.MOC_REGISTER_NOTIFY);
			appMessage.setPublicIp(localIp);
			appMessage.setPublicPort(localPort);
			appMessage.setMa(EnbMessageConstants.MA_SECU);
			appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
			appMessage.setTransactionId(-1);
			return appMessage;
		}

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
			while (isRunning) {
				try {
					doWork();
				} catch (InterruptedException ex) {
					break;
				} catch (Throwable t) {
				}
			}
		}

		/**
		 * 处理接收消息
		 * 
		 * @throws Exception
		 */
		private void doWork() throws Exception {
			DatagramPacket dp = (DatagramPacket) receiveQueue.take();
			// 获取字节消息
			byte[] data = dp.getData();
			int len = dp.getLength();
			byte[] buf = new byte[len];
			// 获取UDP包指示的IP和端口
			String ip = "127.0.0.1";
			if (!dp.getAddress().isLoopbackAddress()) {
				ip = dp.getAddress().getHostAddress();
			}
			int port = dp.getPort();
			System.arraycopy(data, 0, buf, 0, len);
			EnbTransportMessage message = null;
			try {
				message = EnbMessageHelper.parse(buf);
			} catch (Exception e) {
				return;
			}
			// 找到该消息所属的集合
			EnbTransportMessage[] messageList = transMsgMap.get(message
					.getMessageId());
			if (messageList == null) {
				messageList = new EnbTransportMessage[message
						.getTotalPacketNum()];
				// 放入消息缓存
				transMsgMap.put(message.getMessageId(), messageList);
			}
			messageList[message.getPacketNo()] = message;
			// 判断是否集齐，如果集齐则进行组包，然后对应用报文进行解析处理
			if (EnbMessageHelper.receiveAllSubPackets(messageList)) {
				transMsgMap.remove(message.getMessageId());
				EnbAppMessage appMessage = EnbMessageHelper
						.mergeMessage(messageList);
				appMessage.setPublicIp(ip);
				appMessage.setPublicPort(port);
				System.out.println("eNB receive msg. moc=0x"
						+ Long.toHexString(appMessage.getMoc()));

				if (isAsyncMessage(appMessage)) {
					handleAsyncMessage(appMessage);
				} else {
					FutureResult futureResult = SimEnbMsgRegistry.getInstance()
							.unregister(appMessage.getTransactionId());
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
		 * 停止线程
		 * 
		 */
		public void shutdown() {
			isRunning = false;
			this.interrupt();
		}

		private boolean isAsyncMessage(EnbAppMessage appMessage) {
			return true;
		}
	}

	private EnbAppMessage createRealtimeReportMessage(byte[] data) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enbId);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_NOTIFY);
		appMessage.setMoc(EnbMessageConstants.MOC_REPORT_REALTIME_DATA_NOTIRY);
		appMessage.setPublicIp(localIp);
		appMessage.setPublicPort(localPort);
		appMessage.setMa(EnbMessageConstants.MA_PERF);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setTransactionId(-1);
		appMessage.addTagValue(TagConst.REALTIME_REPORT_DATA, data);
		return appMessage;
	}

	public static void main(String[] args) {
		try {
			new SimEnb(1L);
		} catch (Exception e) {
			System.out.println("create sim enb failed.");
		}
	}

}
