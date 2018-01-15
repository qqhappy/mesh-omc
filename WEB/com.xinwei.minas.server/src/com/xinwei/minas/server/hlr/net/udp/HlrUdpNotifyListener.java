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
 * HLR UDP消息通知侦听器接口
 * 
 * @author chenjunhua
 * 
 */
public interface HlrUdpNotifyListener {
	/**
	 * 接收HLR消息
	 * 
	 * @param message
	 *            HLR消息
	 */
	public void receive(HlrUdpMessage message) throws Exception;
}
