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
 * 前端SupplementItem实体
 * 
 * @author chenshaohua
 * 
 */

public class ClientSupplementItem {

	private String supplementName;// 补充业务名称

	private int supplementNo;// 业务编号

	private byte supplementValue;// 业务属性

	public String getSupplementName() {
		return supplementName;
	}

	public void setSupplementName(String supplementName) {
		this.supplementName = supplementName;
	}

	public int getSupplementNo() {
		return supplementNo;
	}

	public void setSupplementNo(int supplementNo) {
		this.supplementNo = supplementNo;
	}

	public byte getSupplementValue() {
		return supplementValue;
	}

	public void setSupplementValue(byte supplementValue) {
		this.supplementValue = supplementValue;
	}
}
