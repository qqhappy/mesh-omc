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
 * eNB传输层消息
 * 
 * @author chenjunhua
 * 
 */

public class EnbTransportMessage {

	public static final int HEADER_LEN = 20;
	// 版本：2个字节
	private int version;

	// 传输层字节长度(传输层包头+载荷)：2个字节
	private int transportLen;

	// 总的应用报文层字节长度（未拆包前）4个字节
	private int totalAppLen;

	// 消息ID：4个字节，用于唯一标示一条应用层消息，传输层拆包组包使用
	private long messageId;

	// 总包数：4个字节
	private int totalPacketNum;

	// 当期包序号：4个字节，从0开始计数
	private int packetNo;

	// 传输层载荷
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
