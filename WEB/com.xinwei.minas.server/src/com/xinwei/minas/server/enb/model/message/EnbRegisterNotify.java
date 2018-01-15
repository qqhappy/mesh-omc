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

/**
 * 
 * �����վע��֪ͨ��Ϣ��
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterNotify {
	
	private EnbAppMessage enbAppMessage;
	
	public EnbRegisterNotify(EnbAppMessage enbAppMessage) {
		this.enbAppMessage = enbAppMessage;		
	}
	
	public Long getEnbId() {
		return enbAppMessage.getEnbId();
	}

	
}
