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
 * ��վIP����֪ͨ��Ϣģ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsIpNotification {

	// ��վId
	private long btsId;
	
	// Ŀ���վID
	private Long targetBtsId;
	
	public McBtsIpNotification(long btsId, byte[] buf) {
		// ��վID
		this.btsId = btsId;
		int offset = 0;
		// Ŀ���վID
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
