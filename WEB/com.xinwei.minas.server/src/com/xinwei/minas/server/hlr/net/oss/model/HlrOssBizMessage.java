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
 * HLRӦ�ò���Ϣͨ��ģ��
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssBizMessage {

	// ���б�ͷ����
	private int publicHeaderLen = HlrOssPublicHeader.LEN;

	// ˽�б�ͷ����
	private int privateHeaderLen = HlrOssBizMessagePrivateHeader.LEN;

	// ���б�ͷ
	private HlrOssPublicHeader publicHeader;

	// ˽�б�ͷ
	private HlrOssBizMessagePrivateHeader privateHeader;

	// TV��Ϣ��
	private HlrOssMessageBody body;

	// TV��Ϣ���ֽڻ���
	private byte[] bodyContent;

	/**
	 * ���캯��
	 * 
	 * @param operObject
	 *            ��������
	 * @param operType
	 *            ��������
	 * @param body
	 *            ��Ϣ��
	 */
	public HlrOssBizMessage(HlrOssBizMessagePrivateHeader privateHeader,
			HlrOssMessageBody body) {		
		// ��������ֽ���
		this.body = body;
		bodyContent = body.toBytes();
		// �������б�ͷ
		buildPublicHeader();
		// ����˽�б�ͷ
		privateHeader.setInfoLen(bodyContent.length);
		this.privateHeader = privateHeader;
	}

	/**
	 * ���캯��
	 * 
	 * @param buf
	 *            �ֽ�����
	 * @param offset
	 *            ƫ����
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
	 * ��ģ��ת��Ϊ�ֽ�����
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		// ��Ϣ�峤��
		int bodyLen = bodyContent.length;
		// �ܳ���
		int totalLen = publicHeaderLen + privateHeaderLen + bodyLen;
		byte[] buf = new byte[totalLen];
		int offset = 0;
		// ���б�ͷ
		System.arraycopy(publicHeader.toBytes(), 0, buf, offset,
				publicHeaderLen);
		offset += publicHeaderLen;
		// ˽�б�ͷ
		System.arraycopy(privateHeader.toBytes(), 0, buf, offset,
				privateHeaderLen);
		offset += privateHeaderLen;
		// ��Ϣ��
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
	 * ���칫�б�ͷ
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
//	 * ����˽�б�ͷ
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
