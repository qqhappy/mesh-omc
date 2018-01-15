/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-2	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu;

/**
 * ��¼�û�
 * 
 * @author liuzhongyan
 * 
 */

public class LoginUser extends User {
	
	private String sessionId;
	
	//��¼ip
	private String loginIp;

	// ��¼ʱ��
	private long loginTime;
	
	// �ϴ�����ʱ��
	private long lastHandshakeTime;
	
	//�Ƿ�����
	private boolean isOnline = false;

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public long getLastHandshakeTime() {
		return lastHandshakeTime;
	}

	public void setLastHandshakeTime(long lastHandshakeTime) {
		this.lastHandshakeTime = lastHandshakeTime;
	}

	@Override
	public String toString() {
		return "LoginUser [sessionId=" + sessionId + ", loginIp=" + loginIp
				+ ", loginTime=" + loginTime + ", lastHandshakeTime="
				+ lastHandshakeTime + ", isOnline=" + isOnline
				+ ", username=" + getUsername() + "]";
	}


	

}
