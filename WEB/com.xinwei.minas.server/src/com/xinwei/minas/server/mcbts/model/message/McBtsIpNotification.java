/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 基站IP请求通知消息模型
 * 
 * @author chenjunhua
 * 
 */

public class McBtsIpNotification {

	// 基站Id
	private long btsId;
	
	// 目标基站ID
	private Long targetBtsId;
	
	public McBtsIpNotification(long btsId, byte[] buf) {
		// 基站ID
		this.btsId = btsId;
		int offset = 0;
		// 目标基站ID
		targetBtsId = ByteUtils.toLong(buf, offset, 4);
		offset += 4;
	}

	public long getBtsId() {
		return btsId;
	}

	public Long getTargetBtsId() {
		return targetBtsId;
	}
	
	
	
}
