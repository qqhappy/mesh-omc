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
 * HLR 消息TransactionId生成器
 * 
 * @author chenjunhua
 * 
 */

public class HlrTransactionIdGenerator {
	
	private static int next = 0;

	/**
	 * 获取下一事务ID
	 * 
	 * @return 事务ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 32760;
		return next;
	}
}
