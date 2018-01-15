/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2015-1-22	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.model;

/**
 * 
 * TCN VMR(录音服务器) 链路配置模型
 * 
 * @author chenjunhua
 * 
 */
public class TcnVmrLink {

	// 配置信息索引（前台不显示，默认为1）
	private String vmrIndex = "1";
	
	// 录音服务器IP
	private String vmrIp = "";
	
	// 录音服务器端口
	private String vmrPort = "";
	
	// 用户名
	private String username = "";
	
	// 密码
	private String password = "";
	
	// 描述信息
	private String comment = "";
	
	// 链路状态 :  0- 未知; 1-正常; 2-失连
	private String linkStatus = "";

	public String getVmrIndex() {
		return vmrIndex;
	}

	public void setVmrIndex(String vmrIndex) {
		this.vmrIndex = vmrIndex;
	}

	public String getVmrIp() {
		return vmrIp;
	}

	public void setVmrIp(String vmrIp) {
		this.vmrIp = vmrIp;
	}

	public String getVmrPort() {
		return vmrPort;
	}

	public void setVmrPort(String vmrPort) {
		this.vmrPort = vmrPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(String linkStatus) {
		this.linkStatus = linkStatus;
	}

	@Override
	public String toString() {
		return "TcnVmrLink [vmrIndex=" + vmrIndex + ", vmrIp=" + vmrIp
				+ ", vmrPort=" + vmrPort + ", username=" + username
				+ ", password=" + password + ", comment=" + comment
				+ ", linkStatus=" + linkStatus + "]";
	}
	
	
	
	
	
	
	
	
}
