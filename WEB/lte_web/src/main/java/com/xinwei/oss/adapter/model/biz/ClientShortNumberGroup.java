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
 * 前端ShortNumberGroup
 * 
 * @author chenshaohua
 * 
 */

public class ClientShortNumberGroup {

	private String shortNumber;// 短号

	private String realNumber;// 真实号码

	public String getShortNumber() {
		return shortNumber;
	}

	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}

	public String getRealNumber() {
		return realNumber;
	}

	public void setRealNumber(String realNumber) {
		this.realNumber = realNumber;
	}
}
