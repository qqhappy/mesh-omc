/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */

package com.xinwei.minas.server.hlr.net.oss.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.common.utils.ByteArrayUtil;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * HLR OSS ҵ����Ϣ˽�б�ͷ
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssBizMessagePrivateHeader implements HlrOssPrivateHeader {
	private  Log log = LogFactory.getLog(HlrOssBizMessagePrivateHeader.class);
	public static final int LEN = 24;
	
	// ��������
	private int operObject; // 1 Byte

	// ��������
	private int operType; // 1 Byte

	// �ɹ���ʶ
	private int successFlag; // 1 Byte

	// ��������ʶ
	private int completeFlag; // 1 Byte

	// �����
	private int packageNo; // 1 Byte

	// ����Ա����
	private String operName = "admin"; // 17 Byte

	// ��Ϣ�峤��
	private int infoLen; // 2 Byte

	public HlrOssBizMessagePrivateHeader() {

	}

	public HlrOssBizMessagePrivateHeader(byte[] appBuf, int offset) {
		//
		operObject = ByteArrayUtil.toInt(appBuf, offset, 1);
		offset += 1;
		//
		operType = ByteArrayUtil.toInt(appBuf, offset, 1);
		offset += 1;
		//
		successFlag = ByteArrayUtil.toInt(appBuf, offset, 1);
		offset += 1;
		//
		completeFlag = ByteArrayUtil.toInt(appBuf, offset, 1);
		offset += 1;
		//
		packageNo = ByteArrayUtil.toInt(appBuf, offset, 1);
		offset += 1;
		//
		byte[] operNameBytes = new byte[17];
		System.arraycopy(appBuf, offset, operNameBytes, 0, 17);
		offset += 17;
		try {
			operName = new String(operNameBytes,
					System.getProperty("file.encoding"));
			operName = operName.trim();
		} catch (Exception e) {
			log.error(e);
		}
		//
		infoLen = ByteArrayUtil.toInt(appBuf, offset, 2);
		offset += 2;
	}

	@Override
	public byte[] toBytes() {
		byte[] buf = new byte[LEN];
		int offset = 0;
		//
		buf[offset++] = (byte) (operObject);
		//
		buf[offset++] = (byte) (operType);
		//
		buf[offset++] = (byte) (successFlag);
		//
		buf[offset++] = (byte) (completeFlag);
		//
		buf[offset++] = (byte) (packageNo);
		// TODO: ��Ҫȷ���ַ����ı����ʽ
		ByteUtils.putString(buf, offset, operName, 17, '\0', null);
		offset += 17;
		//
		buf[offset++] = (byte) (infoLen >>> 8);
		buf[offset++] = (byte) (infoLen);
		
		return buf;
	}

	/**
	 * �ж��Ƿ������һ��ҵ����Ϣ
	 * 
	 * @return ��������һ��ҵ����Ϣ������true�����򷵻�false
	 */
	public boolean isLastPackage() {
		return (completeFlag == 1);
	}

	public int getOperObject() {
		return operObject;
	}

	public void setOperObject(int operObject) {
		this.operObject = operObject;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public int getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(int successFlag) {
		this.successFlag = successFlag;
	}

	public int getCompleteFlag() {
		return completeFlag;
	}

	public void setCompleteFlag(int completeFlag) {
		this.completeFlag = completeFlag;
	}

	public int getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(int packageNo) {
		this.packageNo = packageNo;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public int getInfoLen() {
		return infoLen;
	}

	public void setInfoLen(int infoLen) {
		this.infoLen = infoLen;
	}


	

}
