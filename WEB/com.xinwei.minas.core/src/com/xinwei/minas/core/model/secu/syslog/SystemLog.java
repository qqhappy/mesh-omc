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
 * ϵͳ��־
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SystemLog implements Serializable {
	// �û���
	private String username;
	// ��¼IP��ַ
	private String loginIp;
	// ��������
	private OperObject operObject;
	// ����ʱ��
	private Date operTime;

	private String operDesc;

	private String actionDesc;
	// ��������
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
