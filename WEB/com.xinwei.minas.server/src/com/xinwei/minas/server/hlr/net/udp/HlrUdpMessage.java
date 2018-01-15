/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

import com.xinwei.common.utils.ByteArrayUtil;

/**
 * 
 * HLR UDP ͨ����Ϣ
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpMessage {

	private static final int HEAD_LEN = 4 + 4 + 2 + 1 + 1 + 1 + 1 + 2;

	// ��ѯ�û���Ϣ���� EMS --> HLR
	public static final int QUERY_USER_REQ = 0X01;

	// ��ѯ��վ���û���Ϣ���� EMS --> HLR
	public static final int QUERY_BTS_USER_REQ = 0X02;

	// ��ѯ�û���ϢӦ��HLR --> EMS
	public static final int QUERY_USER_RSP = 0X03;

	// �ն�ע��֪ͨHLR --> EMS
	public static final int UT_REGISTER_NTFY = 0X04;

	// ��ѯ��վ��Ϣ����HLR --> EMS
	public static final int QUERY_BTS_REQ = 0X05;

	// OSS͸������Ϣ
	public static final int OSS_CONTENT = 0X10;

	// HLR IP��ַ
	private String hlrIp;

	// HLR UDPͨ�Ŷ˿�
	private int hlrPort;

	// Ŀ�������
	private long destId = 0; // 4 bytes

	// Դ�����
	private long sourceId = 1; // 4 bytes

	// ���к�
	private int transactionId; // 2 bytes

	// �汾
	private int version = 4;// 1 bytes

	// �ܰ���(ʹ�ñ����ֶ�)
	private int totalPacket = 1;// 1 bytes

	// �Ƿ����һ����־λ
	private int lastPacketFlag;// 1 bytes

	// ��Ϣ����
	private int messageType;// 1 bytes

	// ��Ϣ��
	private byte[] content = new byte[0];

	public HlrUdpMessage() {

	}

	public HlrUdpMessage(byte[] buf) {
		this(buf, 0);
	}

	public HlrUdpMessage(byte[] buf, int offset) {
		// ���ֽ�ת��Ϊģ��
		destId = ByteArrayUtil.toLong(buf, offset, 4);
		offset += 4;
		//
		sourceId = ByteArrayUtil.toLong(buf, offset, 4);
		offset += 4;
		//
		transactionId = ByteArrayUtil.toInt(buf, offset, 2);
		offset += 2;
		//
		version = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;
		//
		totalPacket = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;
		//
		lastPacketFlag = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;
		//
		messageType = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;
		//
		int contentLength = ByteArrayUtil.toInt(buf, offset, 2);
		offset += 2;
		//
		content = new byte[contentLength];
		System.arraycopy(buf, offset, content, 0, buf.length - offset);// TODO:
																		// 2047Ӧ��Ϊbuf��ʣ���ֽڳ�
	}

	/**
	 * ��ģ��ת��Ϊ�ֽ���
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		int contentLength = content.length;
		int totalLength = HEAD_LEN + contentLength;
		byte[] msg = new byte[totalLength];
		int offset = 0;
		//
		ByteArrayUtil.putInt(msg, offset, (int) destId);
		offset += 4;
		//
		ByteArrayUtil.putInt(msg, offset, (int) sourceId);
		offset += 4;
		//
		msg[offset++] = (byte) (transactionId >>> 8);
		msg[offset++] = (byte) (transactionId);
		//
		msg[offset++] = (byte) (version);
		//
		msg[offset++] = (byte) (totalPacket);
		//
		msg[offset++] = (byte) (lastPacketFlag);
		//
		msg[offset++] = (byte) (messageType);
		//
		msg[offset++] = (byte) (contentLength >>> 8);
		msg[offset++] = (byte) (contentLength);
		//
		System.arraycopy(content, 0, msg, offset, content.length);
		offset += content.length;

		return msg;
	}

	/**
	 * �ж��Ƿ����첽��Ϣ������֪ͨ��Ϣ����Ҫ�첽�������Ϣ��
	 * 
	 * @return �����֪ͨ��Ϣ,����true. ���򷵻�false
	 */
	public boolean isAsyncMessage() {
		return messageType == UT_REGISTER_NTFY || messageType == QUERY_BTS_REQ;
	}

	/**
	 * �ж��Ƿ���OSS͸����Ϣ
	 * 
	 * @return
	 */
	public boolean isOssMessage() {
		return messageType == OSS_CONTENT;
	}

	/**
	 * �Ƿ������һ��
	 * 
	 * @return
	 */
	public boolean isLastPacket() {
		return lastPacketFlag == 1;
	}

	public long getDestId() {
		return destId;
	}

	public void setDestId(long destId) {
		this.destId = destId;
	}

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getTotalPacket() {
		return totalPacket;
	}

	public void setTotalPacket(int totalPacket) {
		this.totalPacket = totalPacket;
	}

	public int getLastPacketFlag() {
		return lastPacketFlag;
	}

	public void setLastPacketFlag(int lastPacketFlag) {
		this.lastPacketFlag = lastPacketFlag;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getHlrIp() {
		return hlrIp;
	}

	public void setHlrIp(String hlrIp) {
		this.hlrIp = hlrIp;
	}

	public int getHlrPort() {
		return hlrPort;
	}

	public void setHlrPort(int hlrPort) {
		this.hlrPort = hlrPort;
	}

}
