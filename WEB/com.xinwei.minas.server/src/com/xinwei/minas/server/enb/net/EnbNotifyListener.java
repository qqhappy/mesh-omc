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
 * eNB��Ϣ֪ͨ�������ӿ�
 * 
 * @author chenjunhua
 * 
 */
public interface EnbNotifyListener {
	/**
	 * ����eNB��Ϣ
	 * 
	 * @param message
	 *            eNB��Ϣ
	 */
	public void receive(EnbAppMessage message) throws Exception;
}
