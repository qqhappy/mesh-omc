/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-26	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端SupplementCallOutLimitType实体类
 * 
 * @author chenshaohua
 * 
 */

public class ClientSupplementCallOutLimitType {

	private byte callOutLimitType;// 呼出限制类型(1字节)

	private String callOutLimitPsw;// 呼出限制密码(4字节)

	private byte regStatus;// 登记状态(1字节)

	public byte getCallOutLimitType() {
		return callOutLimitType;
	}

	public void setCallOutLimitType(byte callOutLimitType) {
		this.callOutLimitType = callOutLimitType;
	}

	public String getCallOutLimitPsw() {
		return callOutLimitPsw;
	}

	public void setCallOutLimitPsw(String callOutLimitPsw) {
		this.callOutLimitPsw = callOutLimitPsw;
	}

	public byte getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(byte regStatus) {
		this.regStatus = regStatus;
	}
}
