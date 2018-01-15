/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-09-02	| fanhaoyu 	| 	create the file                    
 */

package com.xinwei.minas.server.mem.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.arrowping.mcwill.ems.front.datacomm.msg.MemRptReq;
import com.arrowping.mcwill.ems.front.datacomm.msg.MemRptRsp;
import com.arrowping.mcwill.ems.front.datacomm.msg.MemUpthManager;
import com.arrowping.mcwill.ems.shlrut.ossmsg.OSSMessageEntry;
import com.arrowping.mcwill.ems.shlrut.ossmsg.OSSMessageUtil;
import com.xinwei.common.udp.DefaultUdpServer;
import com.xinwei.common.udp.UdpServer;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessagePrivateHeader;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssMessageBody;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssPublicHeader;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mem.MemBizConstants;
import com.xinwei.minas.server.ut.proxy.UTBasicProxy;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * MEMҵ������
 * 
 * @author fanhaoyu
 * 
 */

public class MemBizHandler {

	private static final Log log = LogFactory.getLog(MemBizHandler.class);

	private String memServerIp;

	private int memServerPort;

	private String emsServerIp;
	// ems��memServer����udp���ӵĶ˿�
	private int emsServerPort;

	private UTBasicProxy proxy;

	private UdpServer udpServer;

	private McBtsConnector mcBtsConnector;

	private PacketReceiver pktReceiver;

	private int transId = 32767;

	public void initialize() {
		try {
			log.info("Initialize MemBizHandler ...");
			// ���ն��л��� ��λ��byte
			int bufferSize = 2 * 1024 * 1024;
			// ���ݰ����ܶ���
			BlockingQueue<DatagramPacket> receiveQueue = new LinkedBlockingQueue<DatagramPacket>(
					4096);
			// ���ݰ����Ͷ���
			BlockingQueue<DatagramPacket> sendQueue = new LinkedBlockingQueue<DatagramPacket>(
					4096);
			if (emsServerIp == null || emsServerIp.equals("")
					|| emsServerIp.equals("127.0.0.1")) {
				udpServer = new DefaultUdpServer(emsServerPort, bufferSize,
						receiveQueue, sendQueue, null);
			} else {
				udpServer = new DefaultUdpServer(emsServerIp, emsServerPort,
						bufferSize, receiveQueue, sendQueue, null);
			}
			MemBizMessageLogger logger = new MemBizMessageLogger();
			udpServer.setUdpMessageLogger(logger);
			// �������ݰ������߳�
			pktReceiver = new PacketReceiver(receiveQueue);

			this.start();
			log.info("OK.");
		} catch (Exception e) {
			log.error(e);
		}
	}

	// ���ݰ������߳�
	class PacketReceiver extends Thread {

		private volatile boolean isRunning = true;

		private BlockingQueue<DatagramPacket> receiveQueue;

		public PacketReceiver(BlockingQueue<DatagramPacket> receiveQueue) {
			this.receiveQueue = receiveQueue;
		}

		public void run() {
			while (isRunning()) {
				doWork();
			}
		}

		private void doWork() {
			int sessionId = 0;
			try {
				// ��������MemServer����Ϣ
				DatagramPacket packet = (DatagramPacket) receiveQueue.take();
				log.debug("Receive a msg from MemServer, Addr:"
						+ packet.getSocketAddress());
				byte[] data = packet.getData();

				HlrOssBizMessage msg = new HlrOssBizMessage(data, 4);

				// �����·�����վ����Ϣ
				McBtsMessage message = createMcBtsMessage(data, msg);

				if (message != null) {
					// ����Ự
					sessionId = msg.getSessionId();
					MemBizSessionCache.getInstance().addSession(transId,
							sessionId);
					// ����Ϣת����BTS���ȴ��ظ�
					McBtsMessage response = mcBtsConnector.syncInvoke(message);

					// �����ظ���Ϣ��MemServer
					sendToMemServer(response);
				}
			} catch (Exception e) {
				log.error(e);
			} finally {
				// �Ƴ��Ự
				MemBizSessionCache.getInstance().removeSession(sessionId);
			}
		}

		public void startup() {
			receiveQueue.clear();
			MemBizSessionCache.getInstance().clear();
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
	 * �����·�����վ����Ϣ
	 * 
	 * @param data
	 *            ����MemServer����Ϣ����
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage createMcBtsMessage(byte[] data, HlrOssBizMessage msg)
			throws Exception {
		McBtsMessage message = null;
		// ��������
		OSSMessageEntry entry = OSSMessageUtil.getParser().unpack(
				msg.getBody().toBytes());

		long uid = Long.valueOf(entry.getValue(OSSMessageUtil.TAG_UID)
				.toString(), 16);
		long pid = Long.valueOf(entry.getValue(OSSMessageUtil.TAG_PID)
				.toString(), 16);
		long btsId = Long.valueOf(entry.getValue(OSSMessageUtil.TAG_BTSID4)
				.toString(), 16);

		// ���btsIdΪ0�������pid��hlr��ѯ
		if (btsId == 0) {
			btsId = queryBtsIdByPid(entry.getValue(OSSMessageUtil.TAG_PID)
					.toString());
		}

		// ������վ͸������Ϣ
		if (msg.getPublicHeader().getPackageType() == MemBizConstants.SERVICE_REQUEST
				&& msg.getPrivateHeader().getOperObject() == MemBizConstants.OPER_OBJ_MEM) {

			MemRptReq req = new MemRptReq(null);

			int operType = msg.getPrivateHeader().getOperType();

			// MEM��Ϣ�ϱ�����
			if (operType == MemBizConstants.OPERTYPE_MEMINFO) {
				req.setUid(uid);
				req.setPid(pid);
				req.setBtsID(btsId);
				// ��������ID
				req.setTransID(getTransId());

				byte[] bt = req.encode();
				message = new McBtsMessage(bt);
			} else if (operType == MemBizConstants.OPERTYPE_MEM_MANAGE
					|| operType == MemBizConstants.OPERTYPE_MEM_MANAGE1) {
				// MEM�ۺϹ�����Ϣ
				req.setUid(uid);
				req.setPid(pid);
				req.setBtsID(btsId);
				// ����MOC
				if (operType == MemBizConstants.OPERTYPE_MEM_MANAGE)
					req.setMoc(0x0A03);
				else if (operType == MemBizConstants.OPERTYPE_MEM_MANAGE1)
					req.setMoc(0x0A05);
				// ��������ID
				req.setTransID(getTransId());
				// ������Ϣ�е�MsgSubContentLen���ݵ�����
				Integer msgSubContentLen = ByteUtils.toInt(data, 54, 2);
				byte[] bt = req.encode();
				byte[] btsByte = new byte[bt.length + msgSubContentLen + 6];
				System.arraycopy(bt, 0, btsByte, 0, bt.length);
				System.arraycopy(data, 50, btsByte, bt.length, btsByte.length
						- bt.length);
				message = new McBtsMessage(btsByte);
			}
		}
		return message;
	}

	/**
	 * �ѻ�վ�ظ���Ϣת����MemServer
	 * 
	 * @param response
	 * @throws Exception
	 */
	private void sendToMemServer(McBtsMessage response) throws Exception {

		try {
			// ���ݻ�վ�Ļظ���������MemServer����Ϣ
			HlrOssBizMessage memBizMsg = createMessageToMemServer(response);

			// �������͸�MemServer���ֽ���
			byte[] buf = new byte[memBizMsg.getTotalLength() + 6];

			int offset = 0;
			ByteUtils.putNumber(buf, offset, String.valueOf(0x7ea5), 2);
			offset += 2;
			ByteUtils.putNumber(buf, offset, String.valueOf(buf.length - 4), 2);
			offset += 2;
			System.arraycopy(memBizMsg.toBytes(), 0, buf, offset,
					memBizMsg.getTotalLength());
			offset += memBizMsg.getTotalLength();
			ByteUtils.putNumber(buf, offset, String.valueOf(0x7e0d), 2);

			// ���͸�MemServer
			log.debug("Send response to MemServer, Addr:/" + memServerIp + ":"
					+ memServerPort);

			udpServer.send(InetAddress.getByName(memServerIp), memServerPort,
					buf);
		} catch (Exception e) {
			throw e;
		} finally {
			// �Ƴ��Ự
			MemBizSessionCache.getInstance().removeSession(
					response.getTransactionId());
		}
	}

	/**
	 * �������͸�MemServer����Ϣ
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private HlrOssBizMessage createMessageToMemServer(McBtsMessage response)
			throws Exception {

		HlrOssBizMessage memBizMsg = null;

		byte[] respBuf = response.toBytes();
		// MEM��Ϣ�ϱ�Ӧ��
		if (response.getMoc() == MemBizConstants.MOC_MEMINFO_RSP) {

			MemRptRsp memRptRsp = new MemRptRsp(null);
			memRptRsp.decode(respBuf);

			// private header
			HlrOssBizMessagePrivateHeader privateHeader = new HlrOssBizMessagePrivateHeader();
			privateHeader.setPackageNo(0);
			privateHeader.setCompleteFlag(MemBizConstants.LAST_PACKAGE);
			privateHeader.setOperObject(MemBizConstants.OPER_OBJ_MEM);
			privateHeader.setOperType(MemBizConstants.OPERTYPE_MEMINFO);
			privateHeader.setSuccessFlag(memRptRsp.getActionResult());
			// privateHeader.setOperName(operName);

			OSSMessageEntry entry = OSSMessageUtil.getParser().newEntry();
			entry.setValue(OSSMessageUtil.TAG_MEMINFO, memRptRsp.getMemInfo());
			entry.setValue(OSSMessageUtil.TAG_FLAG, memRptRsp.getActionResult());
			// ����content����body
			byte[] content = OSSMessageUtil.getParser().pack(entry);
			HlrOssMessageBody body = new HlrOssMessageBody(content, 0);
			memBizMsg = new HlrOssBizMessage(privateHeader, body);

			// public header
			HlrOssPublicHeader publicHeader = memBizMsg.getPublicHeader();
			publicHeader.setPackageType(MemBizConstants.SERVICE_RESP);
			publicHeader.setSessionId(MemBizSessionCache.getInstance()
					.getSession(response.getTransactionId()));

		} else if (response.getMoc() == MemBizConstants.MOC_MEM_MANAGE_RSP
				|| response.getMoc() == MemBizConstants.MOC_MEM_MANAGE1_RSP) {
			// MEM�ۺϹ�����Ϣ
			MemUpthManager mum = new MemUpthManager(null);
			mum.decode(respBuf);

			HlrOssBizMessagePrivateHeader privateHeader = new HlrOssBizMessagePrivateHeader();
			privateHeader.setOperType(MemBizConstants.OPERTYPE_MEM_MANAGE);

			// private header
			privateHeader.setPackageNo(0);
			privateHeader.setCompleteFlag(MemBizConstants.LAST_PACKAGE);
			privateHeader.setOperObject(MemBizConstants.OPER_OBJ_MEM);
			if (response.getMoc() == MemBizConstants.MOC_MEM_MANAGE_RSP)
				privateHeader.setOperType(MemBizConstants.OPERTYPE_MEM_MANAGE);
			if (response.getMoc() == MemBizConstants.MOC_MEM_MANAGE1_RSP)
				privateHeader.setOperType(MemBizConstants.OPERTYPE_MEM_MANAGE1);
			privateHeader.setSuccessFlag(mum.getActionResult());
			// privateHeader.setOperName(operName);

			OSSMessageEntry entry = OSSMessageUtil.getParser().newEntry();
			entry.setValue(OSSMessageUtil.TAG_FLAG, mum.getActionResult());
			entry.setValue(OSSMessageUtil.TAG_BTSID4, mum.getBtsID());

			byte[] flagBtsId = OSSMessageUtil.getParser().pack(entry);
			byte[] content = new byte[mum.getMsgSubContentLen()
					+ flagBtsId.length + 6];
			System.arraycopy(flagBtsId, 0, content, 0, flagBtsId.length);
			System.arraycopy(respBuf, MemUpthManager.HEAD_LENGTH, content,
					flagBtsId.length, content.length - flagBtsId.length);
			// ����content����body
			HlrOssMessageBody body = new HlrOssMessageBody(content, 0);

			memBizMsg = new HlrOssBizMessage(privateHeader, body);

			// public header
			HlrOssPublicHeader publicHeader = memBizMsg.getPublicHeader();
			publicHeader.setPackageType(MemBizConstants.SERVICE_RESP);
			publicHeader.setSessionId(MemBizSessionCache.getInstance()
					.getSession(response.getTransactionId()));

		} else {
			throw new Exception("Invalid Response from BTS: MOC="
					+ response.getMoc());
		}
		return memBizMsg;
	}

	/**
	 * �����ն�pid��ѯbtsId
	 * 
	 * @param pidStr
	 * @return
	 * @throws Exception
	 */
	private long queryBtsIdByPid(String pidStr) throws Exception {
		UTCondition utc = new UTCondition();
		utc.setPid(pidStr);
		utc.setQueryCount(20);
		UTQueryResult result = proxy.queryUTByCondition(utc);
		if (result == null) {
			throw new Exception("UserTerminal of pid=" + pidStr
					+ " has no connection with BTS!");
		}
		List<UserTerminal> utList = result.getUtList();
		if (utList == null || utList.isEmpty()) {
			throw new Exception("UserTerminal of pid=" + pidStr
					+ " has no connection with BTS!");
		}
		return utList.get(0).getBtsId();
	}

	private int getTransId() {

		if (transId < 65535) {
			transId++;
		} else {
			transId = 32767;
		}
		return transId;
	}

	public void setMemServerIp(String memServerIp) {
		this.memServerIp = memServerIp;
	}

	public void setMemServerPort(int memServerPort) {
		this.memServerPort = memServerPort;
	}

	public void setEmsServerIp(String emsServerIp) {
		this.emsServerIp = emsServerIp;
	}

	public void setEmsServerPort(int emsServerPort) {
		this.emsServerPort = emsServerPort;
	}

	public void setProxy(UTBasicProxy proxy) {
		this.proxy = proxy;
	}

	public void setMcBtsConnector(McBtsConnector mcBtsConnector) {
		this.mcBtsConnector = mcBtsConnector;
	}

	private void start() throws Exception {
		udpServer.start();
		pktReceiver.start();
	}

	private void stop() throws Exception {
		udpServer.shutdown();
		pktReceiver.shutdown();
	}

	public void dispose() {
		try {
			this.stop();
		} catch (Exception e) {
		}
	}

	/**
	 * �����������˿� ������Ϣ������McBtsMessage ������վ���ȴ��ظ� �յ��ظ� ����OssMessage������MemServer
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// ����MemBizHandler
			MemBizHandler handler = new MemBizHandler();
			handler.setMemServerIp("127.0.0.1");
			handler.setMemServerPort(20000);
			handler.setEmsServerIp("127.0.0.1");
			handler.setEmsServerPort(20001);
			handler.initialize();

			simMemServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 // ģ��MemServer��EMS����MemManage��Ϣ
	 */
	public static void simMemServer() {
		try {
			byte[] buf = new byte[] {
					// publicHeader
					0x00, // pktType req
					0x00, // ossId
					0x00,
					0x01,// sessionId
					// privateHeader
					0x02, 0x32, 0x01, 0x01,
					0x01, // operObj, operType, sf, cf, pNo
					0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67,
					0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67, // operName
					0x00, 0x19, // infoLen
					// body
					0x00, 0x00, 0x00, 0x00, // uid
					0x00, 0x00, 0x00, 0x01, // pid
					0x00, 0x00, 0x00, 0x02, // btsId
					0x00, 0x0b, // contentLen
					0x05, // msgType
					0x65, 0x66, 0x67, 0x68, 0x69, // userName
					0x48, 0x49, 0x50, 0x51, 0x52 // password
			};
			DatagramSocket memSocket = new DatagramSocket();
			memSocket.send(new DatagramPacket(buf, buf.length, InetAddress
					.getByName("172.16.8.62"), 20001));
			memSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ģ���վ�ظ���Ϣ
	 * 
	 * @param message
	 * @return
	 */
	public static McBtsMessage createResponse(McBtsMessage message) {

		McBtsMessage response = new McBtsMessage();
		response.setMoc(MemBizConstants.MOC_MEM_MANAGE_RSP);
		response.setActionType(0);// �ظ�
		response.setMa(MemBizConstants.MA_MEM);
		response.setMsgArea(1);
		response.setBtsId(message.getBtsId());
		byte[] buf = new byte[] { 0x00, 0x06, 0x00, 0x01, 0x00, 0x00, 0x01,
				0x15 // len, transId, memResult, len, subType
		};
		response.setContent(buf);
		return response;
	}

	/**
	 * ��ӡ�ֽ���
	 * 
	 * @param data
	 */
	// private void printBytes(byte[] data) {
	// for (int i = 0; i < data.length; i++) {
	// if (i == data.length - 1) {
	// System.out.println(data[i]);
	// break;
	// }
	// System.out.print(data[i] + ", ");
	// }
	// }
}
