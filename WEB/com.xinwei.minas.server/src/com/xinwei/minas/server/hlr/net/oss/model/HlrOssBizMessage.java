/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */

package com.xinwei.minas.server.hlr.net.oss.model;

import com.xinwei.minas.server.hlr.net.oss.HlrOssSessionIdGenerator;

/**
 * HLR应用层消息通用模型
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssBizMessage {

	// 公有报头长度
	private int publicHeaderLen = HlrOssPublicHeader.LEN;

	// 私有报头长度
	private int privateHeaderLen = HlrOssBizMessagePrivateHeader.LEN;

	// 公有报头
	private HlrOssPublicHeader publicHeader;

	// 私有报头
	private HlrOssBizMessagePrivateHeader privateHeader;

	// TV消息体
	private HlrOssMessageBody body;

	// TV消息体字节缓存
	private byte[] bodyContent;

	/**
	 * 构造函数
	 * 
	 * @param operObject
	 *            操作对象
	 * @param operType
	 *            操作类型
	 * @param body
	 *            消息体
	 */
	public HlrOssBizMessage(HlrOssBizMessagePrivateHeader privateHeader,
			HlrOssMessageBody body) {		
		// 缓存包体字节码
		this.body = body;
		bodyContent = body.toBytes();
		// 构建公有报头
		buildPublicHeader();
		// 构建私有报头
		privateHeader.setInfoLen(bodyContent.length);
		this.privateHeader = privateHeader;
	}

	/**
	 * 构造函数
	 * 
	 * @param buf
	 *            字节数组
	 * @param offset
	 *            偏移量
	 */
	public HlrOssBizMessage(byte[] buf, int offset) {
		//
		publicHeader = new HlrOssPublicHeader(buf, offset);
		offset += publicHeaderLen;
		//
		privateHeader = new HlrOssBizMessagePrivateHeader(buf, offset);
		offset += privateHeaderLen;
		//
		body = new HlrOssMessageBody(buf, offset);
	}

	/**
	 * 将模型转换为字节数组
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		// 消息体长度
		int bodyLen = bodyContent.length;
		// 总长度
		int totalLen = publicHeaderLen + privateHeaderLen + bodyLen;
		byte[] buf = new byte[totalLen];
		int offset = 0;
		// 公有报头
		System.arraycopy(publicHeader.toBytes(), 0, buf, offset,
				publicHeaderLen);
		offset += publicHeaderLen;
		// 私有报头
		System.arraycopy(privateHeader.toBytes(), 0, buf, offset,
				privateHeaderLen);
		offset += privateHeaderLen;
		// 消息体
		if (bodyContent == null) {
			bodyContent = body.toBytes();
		}
		System.arraycopy(bodyContent, 0, buf, offset, bodyLen);
		offset += bodyLen;
		return buf;
	}

	public int getBodyLength() {
		if (body != null) {
			return body.toBytes().length;
		}
		return 0;
	}

	public int getTotalLength() {
		return getBodyLength() + publicHeaderLen + privateHeaderLen;
	}

	public int getSessionId() {
		return publicHeader.getSessionId();
	}

	public HlrOssPublicHeader getPublicHeader() {
		return publicHeader;
	}

	public void setPublicHeader(HlrOssPublicHeader publicHeader) {
		this.publicHeader = publicHeader;
	}

	public HlrOssBizMessagePrivateHeader getPrivateHeader() {
		return privateHeader;
	}

	public void setPrivateHeader(HlrOssBizMessagePrivateHeader privateHeader) {
		this.privateHeader = privateHeader;
	}

	public HlrOssMessageBody getBody() {
		return body;
	}

	public void setBody(HlrOssMessageBody body) {
		this.body = body;
	}

	/**
	 * 构造公有报头
	 */
	private void buildPublicHeader() {
		publicHeader = new HlrOssPublicHeader();
		//
		publicHeader.setPackageType(HlrOssPublicHeader.BIZ_REQ);
		//
		publicHeader.setOssId(0);
		//
		int sessionId = HlrOssSessionIdGenerator.next();
		publicHeader.setSessionId(sessionId);
	}

//	/**
//	 * 构造私有报头
//	 * 
//	 * @param operObject
//	 * @param operType
//	 * @param bodyLen
//	 */
//	private void buildPrivateHeader(int operObject, int operType, int bodyLen) {
//		privateHeader = new HlrOssBizMessagePrivateHeader();
//		privateHeader.setOperObject(operObject);
//		privateHeader.setOperType(operType);
//		privateHeader.setInfoLen(bodyLen);
//	}

}
