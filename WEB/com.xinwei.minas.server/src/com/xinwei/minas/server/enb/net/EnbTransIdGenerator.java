/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2014-5-29	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

/**
 * eNB网元通信消息事务ID生成器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbTransIdGenerator {

	private static int next = 0;

	/**
	 * 获取下一事务ID
	 * 
	 * @return 事务ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 30000;
		return next;
	}
}
