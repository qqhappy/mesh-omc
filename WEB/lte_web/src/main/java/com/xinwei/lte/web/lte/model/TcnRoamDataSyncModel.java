/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-08	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.model;

/**
 * 
 * TCN1000漫游组网数据同步模型
 * 
 * @author chenjunhua
 * 
 */
public class TcnRoamDataSyncModel {

	// MASTER服务器的IP地址
	private String masterIp;
	
	// MASTER服务器的端口号
	private String masterPort;
	
	// 描述信息
	private String comment;
	
	// 数据库同步状态
	private String syncStatus = "-1";

	public String getMasterIp() {
		return masterIp;
	}

	public void setMasterIp(String masterIp) {
		this.masterIp = masterIp;
	}

	public String getMasterPort() {
		return masterPort;
	}

	public void setMasterPort(String masterPort) {
		this.masterPort = masterPort;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}
	
	
	
}
