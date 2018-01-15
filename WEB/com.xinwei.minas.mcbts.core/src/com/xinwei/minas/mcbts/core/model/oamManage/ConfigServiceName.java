/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-19	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.oamManage;

import java.io.Serializable;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * 
 * @author tiance
 * 
 */

public class ConfigServiceName implements Serializable {
	private String name;
	private String desc;
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
