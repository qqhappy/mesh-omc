/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */

package com.xinwei.minas.server.hlr.net.oss;

/**
 * Hlr OSS��ϢSessionId������
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssSessionIdGenerator {
	
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
