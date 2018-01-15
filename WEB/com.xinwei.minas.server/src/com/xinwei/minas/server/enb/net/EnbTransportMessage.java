/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;


/**
 * 
 * eNB�������Ϣ
 * 
 * @author chenjunhua
 * 
 */

public class EnbTransportMessage {

	public static final int HEADER_LEN = 20;
	// �汾��2���ֽ�
	private int version;

	// ������ֽڳ���(������ͷ+�غ�)��2���ֽ�
	private int transportLen;

	// �ܵ�Ӧ�ñ��Ĳ��ֽڳ��ȣ�δ���ǰ��4���ֽ�
	private int totalAppLen;

	// ��ϢID��4���ֽڣ�����Ψһ��ʾһ��Ӧ�ò���Ϣ������������ʹ��
	private long messageId;

	// �ܰ�����4���ֽ�
	private int totalPacketNum;

	// ���ڰ���ţ�4���ֽڣ���0��ʼ����
	private int packetNo;

	// ������غ�
	private byte[] payload = new byte[0];

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getTransportLen() {
		return transportLen;
	}

	public void setTransportLen(int transportLen) {
		this.transportLen = transportLen;
	}

	public int getTotalAppLen() {
		return totalAppLen;
	}

	public void setTotalAppLen(int totalAppLen) {
		this.totalAppLen = totalAppLen;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public int getTotalPacketNum() {
		return totalPacketNum;
	}

	public void setTotalPacketNum(int totalPacketNum) {
		this.totalPacketNum = totalPacketNum;
	}

	public int getPacketNo() {
		return packetNo;
	}

	public void setPacketNo(int packetNo) {
		this.packetNo = packetNo;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

}
