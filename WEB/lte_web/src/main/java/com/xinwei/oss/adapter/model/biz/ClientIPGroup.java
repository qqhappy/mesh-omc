/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端ipGroups实体
 * 
 * @author chenshaohua
 * 
 */

public class ClientIPGroup {

	private String ip;

	private String anchorBtsId;

	private String mac;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAnchorBtsId() {
		return anchorBtsId;
	}

	public void setAnchorBtsId(String anchorBtsId) {
		this.anchorBtsId = anchorBtsId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}
