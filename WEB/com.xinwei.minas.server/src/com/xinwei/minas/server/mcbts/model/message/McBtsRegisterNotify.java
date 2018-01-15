/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 宽带基站注册通知消息体
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterNotify {
	
	// 基站ID
	private Long btsId;
	
	// 硬件版本
	private String hardwareVersion;
	
	// 软件版本
	private String sortwareVersion;
	
	// 基站配置的IP
	private String btsConfigIp;
	
	public McBtsRegisterNotify(Long btsId, byte[] buf) {
		// 基站ID
		this.btsId = btsId;
		int offset = 0;
		// 硬件版本
		hardwareVersion = ByteUtils.toVersion(buf, offset, 4);
		offset += 4;
		// 软件版本
		sortwareVersion = ByteUtils.toVersion(buf, offset, 4);
		offset += 4;
		// 跳过32字节的加密基站id
		offset += 32;
		// 基站配置的IP
		btsConfigIp = ByteUtils.toIp(buf, offset, 4);
		offset += 4;
		//
	}

	


	public Long getBtsId() {
		return btsId;
	}


	public String getBtsConfigIp() {
		return btsConfigIp;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public String getSortwareVersion() {
		return sortwareVersion;
	}


	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("btsConfigIp=").append(btsConfigIp);
		return buf.toString();
	}
	
	
}
