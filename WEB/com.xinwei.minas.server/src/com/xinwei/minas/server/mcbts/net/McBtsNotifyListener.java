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
 * McBts��Ϣ֪ͨ�������ӿ�
 * 
 * @author chenjunhua
 * 
 */
public interface McBtsNotifyListener {
	/**
	 * ����McBts��Ϣ
	 * 
	 * @param message
	 *            McBts��Ϣ
	 */
	public void receive(McBtsMessage message) throws Exception;
}
