/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端SupplementFwdType实体
 * 
 * @author chenshaohua
 * 
 */

public class ClientSupplementFwdType {

	private String fwdTelNumber;// 前转号码

	private byte notifyCalling;// 通知主叫

	private byte notifyFwd;// 通知前转方

	private byte fwdTime;// 前转时间

	private byte regStatus;// 登记状态

	private String supplementFwdTypeName = "";// 当前补充业务名称

	public String getFwdTelNumber() {
		return fwdTelNumber;
	}

	public void setFwdTelNumber(String fwdTelNumber) {
		this.fwdTelNumber = fwdTelNumber;
	}

	public byte getNotifyCalling() {
		return notifyCalling;
	}

	public void setNotifyCalling(byte notifyCalling) {
		this.notifyCalling = notifyCalling;
	}

	public byte getNotifyFwd() {
		return notifyFwd;
	}

	public void setNotifyFwd(byte notifyFwd) {
		this.notifyFwd = notifyFwd;
	}

	public byte getFwdTime() {
		return fwdTime;
	}

	public void setFwdTime(byte fwdTime) {
		this.fwdTime = fwdTime;
	}

	public byte getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(byte regStatus) {
		this.regStatus = regStatus;
	}

	public String getSupplementFwdTypeName() {
		return supplementFwdTypeName;
	}

	public void setSupplementFwdTypeName(String supplementFwdTypeName) {
		this.supplementFwdTypeName = supplementFwdTypeName;
	}

	
}
