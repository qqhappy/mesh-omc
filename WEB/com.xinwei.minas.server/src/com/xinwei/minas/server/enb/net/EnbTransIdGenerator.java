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
 * eNB��Ԫͨ����Ϣ����ID������
 * 
 * @author fanhaoyu
 * 
 */

public class EnbTransIdGenerator {

	private static int next = 0;

	/**
	 * ��ȡ��һ����ID
	 * 
	 * @return ����ID
	 */
	public synchronized static int next() {
		next = (next + 1) % 30000;
		return next;
	}
}
