/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;

/**
 * 
 * ÐÄÌøÇëÇó
 * 
 * @author chenjunhua
 * 
 */

public class McBtsHeartbeatRequest {

	private Long btsId;
	
	public McBtsHeartbeatRequest(Long btsId) {
		this.btsId = btsId;
	}
	
	public McBtsMessage toMcBtsMessage() {
		McBtsMessage request = new McBtsMessage();
		request.setBtsId(btsId);
		request.setMa(McBtsMessage.MA_BTS_SECU);
		request.setMoc(McBtsMessageConstants.MOC_HEARTBEAT_REQUEST);
		request.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);		
		request.setContent(getContentByte());
		return request;
	}
	private byte[] getContentByte() {
		return new byte[80];
	}
	
}
