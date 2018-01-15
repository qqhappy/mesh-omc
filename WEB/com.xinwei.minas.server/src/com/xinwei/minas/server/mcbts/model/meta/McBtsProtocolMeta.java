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
 * McBtsЭ�� Ԫ����
 * 
 * @author chenjunhua
 * 
 */

public class McBtsProtocolMeta {
	
	// ҵ������
	private String bizName;
	
	// ҵ�����
	private String operation;
	
	// ҵ������
	private String desc;
	
	// ������Ϣ
	private McBtsProtocolRequestMeta request;
	
	// Ӧ����Ϣ
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
