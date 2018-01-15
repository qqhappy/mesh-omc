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
 * enb�ļ���Ϣ��������
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
		// ��վ�汾���ؽ��ȴ���
		case EnbMessageConstants.MOC_ENB_VERSION_PROGRESS_NOTIFY:
			McBtsCodeDownloadTaskManager.getInstance().handleEnbResponse(message);
			break;
		// ��վ�汾���ؽ������
		case EnbMessageConstants.MOC_ENB_VERSION_RESULT_NOTIFY:
			McBtsCodeDownloadTaskManager.getInstance().handleEnbResponse(message);
			   //��վ�汾���������Ĵ���
			McBtsBatchUpgradeTaskManager instance = AppContext.getCtx()
					.getBean(McBtsBatchUpgradeTaskManager.class);
			if (instance != null)
				instance.handleEnbDLFinishNotify(message);
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
