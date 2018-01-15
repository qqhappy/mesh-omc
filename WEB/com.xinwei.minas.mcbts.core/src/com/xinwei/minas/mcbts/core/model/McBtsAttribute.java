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
 * ��վ������
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsAttribute implements Serializable {

	public enum Key {
		ALARM_LEVEL(0), MCU_VERSION(1), FPGA_VERSION(2),
		// ǰ�����к�,ֵΪInteger
		SEQ_ID(3),
		// ��֤���������Ƿ��w0��ƥ��,ֵΪboolean,trueΪƥ��,falseΪ��ƥ��
		W0_CONFIG(4);

		int index;

		Key(int index) {
			this.index = index;
		}
	}
}
