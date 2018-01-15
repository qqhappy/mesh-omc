/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu.syslog;

import java.io.Serializable;
import java.util.Date;

import com.xinwei.minas.core.model.OperObject;

/**
 * 
 * 系统日志
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SystemLog implements Serializable {
	// 用户名
	private String username;
	// 登录IP地址
	private String loginIp;
	// 操作对象
	private OperObject operObject;
	// 操作时间
	private Date operTime;

	private String operDesc;

	private String actionDesc;
	// 数据描述
	private String dataDesc;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public OperObject getOperObject() {
		return operObject;
	}

	public void setOperObject(OperObject operObject) {
		this.operObject = operObject;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperDesc() {
		return operDesc;
	}

	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
}
