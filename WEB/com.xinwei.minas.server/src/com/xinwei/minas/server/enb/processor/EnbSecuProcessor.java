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
 * eNB安全消息处理任务
 * 
 * @author chenjunhua
 * 
 */

public class EnbSecuProcessor extends EnbMessageProcessor {

	/**
	 * 执行任务
	 */
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// 注册通知
		case EnbMessageConstants.MOC_REGISTER_NOTIFY:
			EnbRegisterTaskManager.getInstance()
					.handleRegisterNotify(message);
			break;
		// 心跳应答
		case EnbMessageConstants.MOC_HEARTBEAT:
			EnbHeartbeatTaskManager.getInstance().handleResponse(message);
			break;		
		}
	}
	
	/**
	 * 结束任务
	 *
	 */
	@Override
	public void finishWork() {
		
	}
}
