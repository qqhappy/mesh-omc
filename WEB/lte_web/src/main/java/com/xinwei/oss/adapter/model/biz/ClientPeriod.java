/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-26	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端ClientPeriod实体类
 * 
 * @author chenshaohua
 * 
 */

public class ClientPeriod {

	private byte period;// 值

	private byte value;// 周期

	public byte getPeriod() {
		return period;
	}

	public void setPeriod(byte period) {
		this.period = period;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
}
