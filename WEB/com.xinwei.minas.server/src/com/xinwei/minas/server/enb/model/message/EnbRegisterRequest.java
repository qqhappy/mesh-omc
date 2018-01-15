/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.model.message;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;

/**
 * 
 * eNB×¢²áÇëÇó
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterRequest {

	private Long enbId;

	public EnbRegisterRequest(Long enbId) {
		this.enbId = enbId;
	}

	public EnbAppMessage toEnbAppMessage() {
		EnbAppMessage req = new EnbAppMessage();
		req.setEnbId(enbId);
		req.setMa(EnbMessageConstants.MA_SECU);
		req.setMoc(EnbMessageConstants.MOC_REGISTER);
		req.setActionType(EnbMessageConstants.ACTION_OTHERS);
		req.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		return req;
	}

}
