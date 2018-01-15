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
 * ��Ԫͨ����Ϣ����ID������
 * 
 * @author chenjunhua
 * 
 */

public class TransactionIdGenerator {

	/**
	 * ͬ�����͵�ʵʱ������Ϣ(moc=0214)��Ҫ�ô�����ID
	 */
	public static final int SYNC_REAL_TIME_PERF = 2;

	private static int next = 10;

	/**
	 * ��ȡ��һ����ID
	 * 
	 * @return ����ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 30000;
		if (next < 10) {
			next = 10;
		}
		return next;
	}
}
