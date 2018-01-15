/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.task.McBtsDownloadDataTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsHeartbeatTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsRegisterTaskManager;

/**
 * 
 * 基站安全消息处理任务
 * 
 * @author chenjunhua
 * 
 */

public class McBtsSecuProcessor extends McBtsMessageProcessor {

	/**
	 * 执行任务
	 */
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// 注册通知
		case McBtsMessageConstants.MOC_REGISTER_NOTIFY:
			McBtsRegisterTaskManager.getInstance()
					.handleRegisterNotify(message);
			break;
		// 心跳应答
		case McBtsMessageConstants.MOC_HEARTBEAT_RESPONSE:
			McBtsHeartbeatTaskManager.getInstance().handleResponse(message);
			break;
		// 下载数据通知	
		case McBtsMessageConstants.MOC_DOWNLOAD_DATA_NOTIFY:
			McBtsDownloadDataTaskManager.getInstance().handleDownloadDataNotify(message);
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
