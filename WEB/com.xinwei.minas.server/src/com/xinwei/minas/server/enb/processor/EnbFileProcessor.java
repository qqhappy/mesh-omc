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
import com.xinwei.minas.server.mcbts.task.McBtsBatchUpgradeTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsCodeDownloadTaskManager;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * enb文件消息处理任务
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFileProcessor extends EnbMessageProcessor {

	@Override
	public void doWork(EnbAppMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// 基站版本下载进度处理
		case EnbMessageConstants.MOC_ENB_VERSION_PROGRESS_NOTIFY:
			McBtsCodeDownloadTaskManager.getInstance().handleEnbResponse(message);
			break;
		// 基站版本下载结果处理
		case EnbMessageConstants.MOC_ENB_VERSION_RESULT_NOTIFY:
			McBtsCodeDownloadTaskManager.getInstance().handleEnbResponse(message);
			   //基站版本批量升级的处理；
			McBtsBatchUpgradeTaskManager instance = AppContext.getCtx()
					.getBean(McBtsBatchUpgradeTaskManager.class);
			if (instance != null)
				instance.handleEnbDLFinishNotify(message);
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
