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
 * Hlr OSS消息SessionId生成器
 * 
 * @author chenjunhua
 * 
 */

public class HlrOssSessionIdGenerator {
	
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
