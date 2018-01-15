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
 * HLR UDP 通信消息
 * 
 * @author chenjunhua
 * 
 */

public class HlrUdpMessage {

	private static final int HEAD_LEN = 4 + 4 + 2 + 1 + 1 + 1 + 1 + 2;

	// 查询用户信息请求 EMS --> HLR
	public static final int QUERY_USER_REQ = 0X01;

	// 查询基站下用户信息请求 EMS --> HLR
	public static final int QUERY_BTS_USER_REQ = 0X02;

	// 查询用户信息应答HLR --> EMS
	public static final int QUERY_USER_RSP = 0X03;

	// 终端注册通知HLR --> EMS
	public static final int UT_REGISTER_NTFY = 0X04;

	// 查询基站信息请求HLR --> EMS
	public static final int QUERY_BTS_REQ = 0X05;

	// OSS透传类消息
	public static final int OSS_CONTENT = 0X10;

	// HLR IP地址
	private String hlrIp;

	// HLR UDP通信端口
	private int hlrPort;

	// 目的信令点
	private long destId = 0; // 4 bytes

	// 源信令点
	private long sourceId = 1; // 4 bytes

	// 序列号
	private int transactionId; // 2 bytes

	// 版本
	private int version = 4;// 1 bytes

	// 总包数(使用保留字段)
	private int totalPacket = 1;// 1 bytes

	// 是否最后一包标志位
	private int lastPacketFlag;// 1 bytes

	// 消息类型
	private int messageType;// 1 bytes

	// 消息体
	private byte[] content = new byte[0];

	public HlrUdpMessage() {

	}

	public HlrUdpMessage(byte[] buf) {
		this(buf, 0);
	}

	public HlrUdpMessage(byte[] buf, int offset) {
		// 将字节转换为模型
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
																		// 2047应该为buf的剩余字节长
	}

	/**
	 * 将模型转换为字节流
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
	 * 判断是否是异步消息（包括通知消息和需要异步处理的消息）
	 * 
	 * @return 如果是通知消息,返回true. 否则返回false
	 */
	public boolean isAsyncMessage() {
		return messageType == UT_REGISTER_NTFY || messageType == QUERY_BTS_REQ;
	}

	/**
	 * 判断是否是OSS透传消息
	 * 
	 * @return
	 */
	public boolean isOssMessage() {
		return messageType == OSS_CONTENT;
	}

	/**
	 * 是否是最后一包
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
