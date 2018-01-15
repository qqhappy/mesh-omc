/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import java.io.Serializable;

/**
 * eNB»ùÕ¾µÄÊôÐÔ
 * 
 * @author fanhaoyu
 * 
 */

public class EnbAttribute implements Serializable {

	public enum Key {
		ALARM_LEVEL(0), MCU_VERSION(1), FPGA_VERSION(2);

		int index;

		Key(int index) {
			this.index = index;
		}
	}

}
