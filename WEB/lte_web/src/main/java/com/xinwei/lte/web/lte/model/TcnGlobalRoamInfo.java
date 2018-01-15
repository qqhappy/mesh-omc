/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-30	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * TCN1000全局漫游信息模型
 * 
 * @author chenjunhua
 * 
 */
public class TcnGlobalRoamInfo {

	// 对端TCN1000设备标识
	private String sdcId;

	// 对端IP
	private String peerIp;

	// 对端SIP端口
	private String peerSipPort;

	// 数据同步标识 0-不同步; 1-同步
	private String dataSyncFlag;

	// 描述信息
	private String comment;

	// SIP链路状态 0-未连接; 1-已连接
	private String sipState;

	// 数据同步状态 0-未连接; 1-已连接
	private String dataSyncState;

	public TcnGlobalRoamInfo() {
		
	}
	
	public TcnGlobalRoamInfo(Map data) {
		setSdcId((String) data.get("sdcId"));
		setPeerIp((String) data.get("peerIp"));
		setPeerSipPort((String) data.get("peerSipPort"));
		setDataSyncFlag((String) data.get("dataSyncFlag"));
		setComment((String) data.get("comment"));
		setSipState((String) data.get("sipState"));
		setDataSyncState((String) data.get("dataSyncState"));
	}
	
	public String getSdcId() {
		return sdcId;
	}

	public void setSdcId(String sdcId) {
		this.sdcId = sdcId;
	}

	public String getPeerIp() {
		return peerIp;
	}

	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	public String getPeerSipPort() {
		return peerSipPort;
	}

	public void setPeerSipPort(String peerSipPort) {
		this.peerSipPort = peerSipPort;
	}

	public String getDataSyncFlag() {
		return dataSyncFlag;
	}

	public void setDataSyncFlag(String dataSyncFlag) {
		this.dataSyncFlag = dataSyncFlag;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSipState() {
		return sipState;
	}

	public void setSipState(String sipState) {
		this.sipState = sipState;
	}

	public String getDataSyncState() {
		return dataSyncState;
	}

	public void setDataSyncState(String dataSyncState) {
		this.dataSyncState = dataSyncState;
	}

	public Map<String, Object> toMapData() {
		Map<String, Object> data = new HashMap();
		data.put("sdcId", getSdcId());
		data.put("peerIp", getPeerIp());
		data.put("peerSipPort", getPeerSipPort());
		data.put("dataSyncFlag", getDataSyncFlag());
		data.put("comment", getComment());
		return data;
	}

	@Override
	public String toString() {
		return "TcnGlobalRoamInfo [sdcId=" + sdcId + ", peerIp=" + peerIp
				+ ", peerSipPort=" + peerSipPort + ", dataSyncFlag="
				+ dataSyncFlag + ", comment=" + comment + ", sipState="
				+ sipState + ", dataSyncState=" + dataSyncState + "]";
	}


	
}
