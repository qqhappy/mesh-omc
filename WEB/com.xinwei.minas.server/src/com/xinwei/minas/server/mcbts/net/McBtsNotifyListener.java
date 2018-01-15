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
 * McBts消息通知侦听器接口
 * 
 * @author chenjunhua
 * 
 */
public interface McBtsNotifyListener {
	/**
	 * 接收McBts消息
	 * 
	 * @param message
	 *            McBts消息
	 */
	public void receive(McBtsMessage message) throws Exception;
}
