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
 * 前端SupplementShortNumberType
 * 
 * @author chenshaohua
 * 
 */

public class ClientSupplementShortNumberType {

	private byte regStatus;// 登记状态

	private byte groupNum;// 短号组个数

	private ClientShortNumberGroup[] shortNumberGroups = null;// 缩位号码组

	private String supplementShortNumberTypeName = "";// 当前补充业务名称

	public byte getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(byte regStatus) {
		this.regStatus = regStatus;
	}

	public byte getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(byte groupNum) {
		this.groupNum = groupNum;
	}

	public ClientShortNumberGroup[] getShortNumberGroups() {
		return shortNumberGroups;
	}

	public void setShortNumberGroups(ClientShortNumberGroup[] shortNumberGroups) {
		this.shortNumberGroups = shortNumberGroups;
	}

	public String getSupplementShortNumberTypeName() {
		return supplementShortNumberTypeName;
	}

	public void setSupplementShortNumberTypeName(
			String supplementShortNumberTypeName) {
		this.supplementShortNumberTypeName = supplementShortNumberTypeName;
	}

}
