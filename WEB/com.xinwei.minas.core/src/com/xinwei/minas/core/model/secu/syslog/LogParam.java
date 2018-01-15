/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-9	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu.syslog;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.OperSignature;

/**
 * 
 * 记录日志需要的参数
 * 
 * @author fanhaoyu
 * 
 */

public class LogParam {

	private String sessionId;

	private OperSignature signature;

	private OperObject operObject;

	private Object[] params;

	public LogParam(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) {
		this.sessionId = sessionId;
		this.signature = signature;
		this.operObject = operObject;
		this.params = params;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public OperSignature getSignature() {
		return signature;
	}

	public void setSignature(OperSignature signature) {
		this.signature = signature;
	}

	public OperObject getOperObject() {
		return operObject;
	}

	public void setOperObject(OperObject operObject) {
		this.operObject = operObject;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

}
