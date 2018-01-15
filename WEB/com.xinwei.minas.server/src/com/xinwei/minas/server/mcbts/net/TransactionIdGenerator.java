/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

/**
 * 网元通信消息事务ID生成器
 * 
 * @author chenjunhua
 * 
 */

public class TransactionIdGenerator {

	/**
	 * 同步发送的实时性能消息(moc=0214)需要用此事务ID
	 */
	public static final int SYNC_REAL_TIME_PERF = 2;

	private static int next = 10;

	/**
	 * 获取下一事务ID
	 * 
	 * @return 事务ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 30000;
		if (next < 10) {
			next = 10;
		}
		return next;
	}
}
