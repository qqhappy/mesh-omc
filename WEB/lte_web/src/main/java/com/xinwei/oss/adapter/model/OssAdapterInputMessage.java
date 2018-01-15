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
 * OssAdapter请求消息模型
 * 
 * @author chenjunhua
 * 
 */
public class OssAdapterInputMessage extends OssAdapterMessage {

	public OssAdapterInputMessage() {
		
	}
	
	public OssAdapterInputMessage(int operId, int action, Map<String, Object> data) {
		super(operId, action, data);
	}
	
}
