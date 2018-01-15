/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-08	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.oss.adapter.model;

import java.util.Map;

/**
 * 
 * OssAdapter应答消息模型
 * 
 * @author chenjunhua
 * 
 */
public class OssAdapterOutputMessage extends OssAdapterMessage {

	private String result;
	
	private String reason;
	
	public OssAdapterOutputMessage() {

	}

	public OssAdapterOutputMessage(int operId, int action, Map<String, Object> data) {
		super(operId, action, data);
	}
	
	public boolean isSuccesful() {
		return "0".equals(result);
	}
	
	public boolean isFailed() {
		return !isSuccesful();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
