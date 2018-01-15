/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-15	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.task.EnbAssetTaskManager;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;

/**
 * 
 * enb配置消息处理任务
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbConfProcessor extends EnbMessageProcessor  {

	@Override
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch(moc) {
		//整表配置结果通知
		case EnbMessageConstants.MOC_FULLTABLECONFIG_NOTIFY:
			EnbFullTableTaskManager.getInstance()
					.handleFullTableConfigResultNotify(message);
			break;
		// 整表反构结果通知
		case EnbMessageConstants.MOC_FULLTABLEREVERSE_NOTIFY:
			EnbFullTableTaskManager.getInstance()
					.handleFullTableReverseResultNotify(message);
			break;
		// 资产信息上报
		case EnbMessageConstants.MOC_ASSET_INFO_NOTIFY:
			EnbAssetTaskManager.getInstance().handler(message);
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
