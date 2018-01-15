/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * »ùÕ¾×¢²áÇëÇó
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterRequest {

	private Long btsId;

	private int result;

	private byte[] encryptedSessionId = new byte[32];

	private String btsPublicIp;

	private int btsPublicPort;

	private int heartbeatInterval;



	public McBtsRegisterRequest(Long btsId, int result, String btsPublicIp,
			int btsPublicPort, int heartbeatInterval) {
		this.btsId = btsId;
		this.result = result;
		this.btsPublicIp = btsPublicIp;
		this.btsPublicPort = btsPublicPort;
		this.heartbeatInterval = heartbeatInterval;		
	}
	
	
	public McBtsMessage toMcBtsMessage() {
		McBtsMessage request = new McBtsMessage();
		request.setBtsId(btsId);
		request.setMa(McBtsMessage.MA_BTS_SECU);
		request.setMoc(McBtsMessageConstants.MOC_REGISTER_REQUEST);
		request.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		request.setContent(getContentByte());
		return request;
	}
	

	private byte[] getContentByte() {
		byte[] buf = new byte[2 + 32 + 4 + 2 + 2];
		int offset = 0;
		// result
		ByteUtils.putShort(buf, offset, (short) result);
		offset += 2;
		// encrypted sessionId
		System.arraycopy(encryptedSessionId, 0, buf, offset, 32);
		offset += 32;
		// ip
		ByteUtils.putIp(buf, offset, btsPublicIp);
		offset += 4;
		// port
		ByteUtils.putShort(buf, offset, (short) btsPublicPort);
		offset += 2;
		// heartbeat interval
		ByteUtils.putShort(buf, offset, (short) heartbeatInterval);
		offset += 2;
		return buf;
	}

}
