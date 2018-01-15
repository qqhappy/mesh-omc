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
 * OSS��Ϣ���б�ͷ
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

	// ������
	// 0-ҵ������; 1-ҵ��Ӧ��
	// 4-��������; 5-����Ӧ��
	// 8-��������; 9-����Ӧ��
	private int packageType; // 1 Byte

	// ��ʶǰ�û�id����id��oss���䣬����̨���Բ�������id
	private int ossId; // 1 Byte

	// �ỰID
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
	 * �ж��Ƿ���ҵ����Ϣ
	 * 
	 * @return �����ҵ����Ϣ������true�����򷵻�false
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
