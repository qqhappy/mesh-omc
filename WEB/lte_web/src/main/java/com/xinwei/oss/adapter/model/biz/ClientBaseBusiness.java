/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端BaseBusiness类
 * 
 * @author chenshaohua
 * 
 */

public class ClientBaseBusiness {

	public String businessType;// 基本业务类型
	
	private byte switchs;// 开关

	private byte attribute;// 属性

	public byte getSwitchs() {
		return switchs;
	}

	public void setSwitchs(byte switchs) {
		this.switchs = switchs;
	}

	public byte getAttribute() {
		return attribute;
	}

	public void setAttribute(byte attribute) {
		this.attribute = attribute;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
