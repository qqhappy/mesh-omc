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
 * HLR OSS 业务消息私有报头
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssBizMessagePrivateHeader implements HlrOssPrivateHeader {
	private  Log log = LogFactory.getLog(HlrOssBizMessagePrivateHeader.class);
	public static final int LEN = 24;
	
	// 操作对象
	private int operObject; // 1 Byte

	// 操作类型
	private int operType; // 1 Byte

	// 成功标识
	private int successFlag; // 1 Byte

	// 包结束标识
	private int completeFlag; // 1 Byte

	// 包序号
	private int packageNo; // 1 Byte

	// 操作员名字
	private String operName = "admin"; // 17 Byte

	// 信息体长度
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
		// TODO: 需要确认字符串的编码格式
		ByteUtils.putString(buf, offset, operName, 17, '\0', null);
		offset += 17;
		//
		buf[offset++] = (byte) (infoLen >>> 8);
		buf[offset++] = (byte) (infoLen);
		
		return buf;
	}

	/**
	 * 判断是否是最后一条业务消息
	 * 
	 * @return 如果是最后一条业务消息，返回true；否则返回false
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
