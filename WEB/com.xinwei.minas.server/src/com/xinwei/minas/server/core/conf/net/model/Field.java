/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

/**
 * 
 * Field
 * 
 * @author chenjunhua
 * 
 */

public interface Field {

	/**
	 * 返回字节编码数组
	 * @return
	 */
	public byte[] toBytes();
	
}
