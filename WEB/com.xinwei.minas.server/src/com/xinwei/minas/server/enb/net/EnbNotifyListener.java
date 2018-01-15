/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2014-04-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

/**
 * eNB消息通知侦听器接口
 * 
 * @author chenjunhua
 * 
 */
public interface EnbNotifyListener {
	/**
	 * 接收eNB消息
	 * 
	 * @param message
	 *            eNB消息
	 */
	public void receive(EnbAppMessage message) throws Exception;
}
