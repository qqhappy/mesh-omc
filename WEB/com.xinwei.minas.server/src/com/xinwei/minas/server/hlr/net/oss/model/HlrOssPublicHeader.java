/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */

package com.xinwei.minas.server.hlr.net.oss.model;

import com.xinwei.common.utils.ByteArrayUtil;

/**
 * 
 * OSS消息公有报头
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssPublicHeader {

	public static final int BIZ_REQ = 0;
	public static final int BIZ_RESP = 1;
	public static final int PUBLIC_REQ = 4;
	public static final int PUBLIC_RESP = 5;
	public static final int HANDSHAKE_REQ = 8;
	public static final int HANDSHAKE_RESP = 9;
	
	public static final int LEN = 4;

	// 包类型
	// 0-业务请求; 1-业务应答
	// 4-公共请求; 5-公共应答
	// 8-联络请求; 9-联络应答
	private int packageType; // 1 Byte

	// 标识前置机id，此id由oss分配，受理台可以不理会这个id
	private int ossId; // 1 Byte

	// 会话ID
	private int sessionId; // 2 Bytes

	public HlrOssPublicHeader() {

	}

	public HlrOssPublicHeader(byte[] buf, int offset) {
		packageType = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;

		ossId = ByteArrayUtil.toInt(buf, offset, 1);
		offset += 1;

		sessionId = ByteArrayUtil.toInt(buf, offset, 2);
		offset += 2;
	}

	public HlrOssPublicHeader(int packageType, int ossId, int sessionId) {
		this.setPackageType(packageType);
		this.setOssId(ossId);
		this.setSessionId(sessionId);
	}

	public byte[] toBytes() {
		byte[] buf = new byte[4];
		int offset = 0;
		//
		buf[offset++] = (byte) (packageType);
		//
		buf[offset++] = (byte) (ossId);
		//
		buf[offset++] = (byte) (sessionId >>> 8);
		buf[offset++] = (byte) (sessionId);
		return buf;
	}

	/**
	 * 判断是否是业务消息
	 * 
	 * @return 如果是业务消息，返回true；否则返回false
	 */
	public boolean isBizMessage() {
		return (packageType == 0 || packageType == 1);
	}

	public int getPackageType() {
		return packageType;
	}

	public void setPackageType(int packageType) {
		this.packageType = packageType;
	}

	public int getOssId() {
		return ossId;
	}

	public void setOssId(int ossId) {
		this.ossId = ossId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

}
