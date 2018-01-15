/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-4	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.rruManage;

import java.io.Serializable;

/**
 * rru Ó²¼þ°æ±¾
 * 
 * @author chenshaohua
 * 
 */

public class RRUHardwareInfo implements Serializable {

	private String rfId;
	
	private String hardwareVersion;
	
	private String hardwareUserId;

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = Integer.toHexString(Integer.valueOf(hardwareVersion));
	}

	public String getHardwareUserId() {
		return hardwareUserId;
	}

	public void setHardwareUserId(String hardwareUserId) {
		this.hardwareUserId = hardwareUserId;
	}

	public String getRfId() {
		return rfId;
	}

	public void setRfId(String rfId) {
		this.rfId = rfId;
	}
}
