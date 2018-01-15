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
 * HLR OSS 公共消息私有报头
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssPublicMessagePrivateHeader implements HlrOssPrivateHeader {

	private  Log log = LogFactory.getLog(HlrOssPublicMessagePrivateHeader.class);
	private static final int LEN = 19;

	// 操作员名字
	private String operName = ""; // 17 Byte

	// 信息体长度
	private int infoLen; // 2 Byte

	public HlrOssPublicMessagePrivateHeader() {

	}

	public HlrOssPublicMessagePrivateHeader(byte[] appBuf, int offset) {
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
		// TODO: 需要确认字符串的编码格式
		ByteUtils.putString(buf, offset, operName, 17, '\0', null);
		//
		buf[offset++] = (byte) (infoLen >>> 8);
		buf[offset++] = (byte) (infoLen);
		
		return buf;
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
