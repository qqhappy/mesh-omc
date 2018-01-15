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
 * HLR UDP��Ϣ֪ͨ�������ӿ�
 * 
 * @author chenjunhua
 * 
 */
public interface HlrUdpNotifyListener {
	/**
	 * ����HLR��Ϣ
	 * 
	 * @param message
	 *            HLR��Ϣ
	 */
	public void receive(HlrUdpMessage message) throws Exception;
}
