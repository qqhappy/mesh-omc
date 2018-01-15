/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

/**
 * HLR ��ϢTransactionId������
 * 
 * @author chenjunhua
 * 
 */

public class HlrTransactionIdGenerator {
	
	private static int next = 0;

	/**
	 * ��ȡ��һ����ID
	 * 
	 * @return ����ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 32760;
		return next;
	}
}
