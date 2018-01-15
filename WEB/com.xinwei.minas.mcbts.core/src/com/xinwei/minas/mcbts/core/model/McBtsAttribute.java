/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-11	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model;

import java.io.Serializable;

/**
 * 
 * 基站的属性
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsAttribute implements Serializable {

	public enum Key {
		ALARM_LEVEL(0), MCU_VERSION(1), FPGA_VERSION(2),
		// 前导序列号,值为Integer
		SEQ_ID(3),
		// 验证天线类型是否和w0相匹配,值为boolean,true为匹配,false为不匹配
		W0_CONFIG(4);

		int index;

		Key(int index) {
			this.index = index;
		}
	}
}
