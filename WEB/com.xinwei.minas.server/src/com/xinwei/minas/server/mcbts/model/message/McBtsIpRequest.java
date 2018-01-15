/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.message;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * »ùÕ¾IPÇëÇó
 * 
 * @author chenjunhua
 * 
 */

public class McBtsIpRequest {

	private Long btsId;
	
	private Long targetBtsId;
	
	private String targetBtsPublicIp = "0.0.0.0";

	private int targetBtsPublicPort = 0;
	
	
	public McBtsMessage toMcBtsMessage() {
		McBtsMessage request = new McBtsMessage();
		request.setBtsId(btsId);
		request.setMa(McBtsMessage.MA_CONF);
		request.setMoc(McBtsMessageConstants.MOC_BTS_IP_REQ);
		request.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		request.setContent(getContentByte());
		return request;
	}
	

	private byte[] getContentByte() {
		byte[] buf = new byte[4 + 4 + 2];
		int offset = 0;
		// target bts id
		ByteUtils.putNumber(buf, offset, String.valueOf(targetBtsId), 4);
		offset += 4;
		// target bts ip
		ByteUtils.putIp(buf, offset, targetBtsPublicIp);
		offset += 4;
		// target bts port
		ByteUtils.putShort(buf, offset, (short) targetBtsPublicPort);
		offset += 2;
		return buf;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public Long getTargetBtsId() {
		return targetBtsId;
	}

	public void setTargetBtsId(Long targetBtsId) {
		this.targetBtsId = targetBtsId;
	}

	public String getTargetBtsPublicIp() {
		return targetBtsPublicIp;
	}

	public void setTargetBtsPublicIp(String targetBtsPublicIp) {
		this.targetBtsPublicIp = targetBtsPublicIp;
	}

	public int getTargetBtsPublicPort() {
		return targetBtsPublicPort;
	}

	public void setTargetBtsPublicPort(int targetBtsPublicPort) {
		this.targetBtsPublicPort = targetBtsPublicPort;
	}
	
	
	
}
