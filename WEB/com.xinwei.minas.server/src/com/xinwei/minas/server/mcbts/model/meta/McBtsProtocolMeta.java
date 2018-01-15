/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.model.meta;

/**
 * 
 * McBts协议 元数据
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolMeta {
	
	// 业务名称
	private String bizName;
	
	// 业务操作
	private String operation;
	
	// 业务描述
	private String desc;
	
	// 请求消息
	private McBtsProtocolRequestMeta request;
	
	// 应答消息
	private McBtsProtocolResponseMeta response;

	
	
	
	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public McBtsProtocolRequestMeta getRequest() {
		return request;
	}

	public void setRequest(McBtsProtocolRequestMeta request) {
		this.request = request;
	}

	public McBtsProtocolResponseMeta getResponse() {
		return response;
	}

	public void setResponse(McBtsProtocolResponseMeta response) {
		this.response = response;
	}
	

}
