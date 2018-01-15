/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.task.EnbHeartbeatTaskManager;
import com.xinwei.minas.server.enb.task.EnbRegisterTaskManager;

/**
 * 
 * eNB��ȫ��Ϣ��������
 * 
 * @author chenjunhua
 * 
 */

public class EnbSecuProcessor extends EnbMessageProcessor {

	/**
	 * ִ������
	 */
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// ע��֪ͨ
		case EnbMessageConstants.MOC_REGISTER_NOTIFY:
			EnbRegisterTaskManager.getInstance()
					.handleRegisterNotify(message);
			break;
		// ����Ӧ��
		case EnbMessageConstants.MOC_HEARTBEAT:
			EnbHeartbeatTaskManager.getInstance().handleResponse(message);
			break;		
		}
	}
	
	/**
	 * ��������
	 *
	 */
	@Override
	public void finishWork() {
		
	}
}
