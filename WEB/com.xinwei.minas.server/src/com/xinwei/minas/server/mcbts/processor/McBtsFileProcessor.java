/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.task.McBtsBatchUpgradeTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsCodeDownloadTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsUTCodeDownloadTaskManager;
import com.xinwei.minas.server.mcbts.task.McBtsUTUpgradeTaskManager;
import com.xinwei.minas.server.mcbts.task.TerminalUpgradeBreakpointResumeTaskManager;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * ��վ�ļ���Ϣ��������,MA=4
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsFileProcessor extends McBtsMessageProcessor {
	/**
	 * ִ������
	 */
	@Override
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// ��վ�ļ��������֪ͨ
		case McBtsMessageConstants.MOC_MCBTS_FILE_RESULT_RESPONSE:
			McBtsCodeDownloadTaskManager.getInstance().handleResponse(message);

			McBtsBatchUpgradeTaskManager instance = AppContext.getCtx()
					.getBean(McBtsBatchUpgradeTaskManager.class);
			if (instance != null)
				instance.handleDLFinishNotify(message);
			break;
		// �ն˰汾�������֪ͨ
		case McBtsMessageConstants.MOC_UT_FILE_RESULT_RESPONSE:
			McBtsUTCodeDownloadTaskManager.getInstance()
					.handleResponse(message);
			break;
		// ��վ�ļ����ؽ���֪ͨ
		case McBtsMessageConstants.MOC_MCBTS_FILE_PROGRESS:
			McBtsCodeDownloadTaskManager.getInstance().handleResponse(message);
			break;
		// �ն���������֪ͨ
		case McBtsMessageConstants.MOC_UT_FILE_UPGRADE_PROGRESS_NOTIFY:
			McBtsUTUpgradeTaskManager.getInstance()
					.handleUTUpgradeProgressNotify(message);
			break;
		// �ն��������֪ͨ
		case McBtsMessageConstants.MOC_UT_FILE_UPGRADE_RESULT_NOTIFY:
			McBtsUTUpgradeTaskManager.getInstance()
					.handleUTUpgradeResultNotify(message);
			break;
		// �ն������ϵ�����֪ͨ
		case McBtsMessageConstants.MOC_UT_UPGRADE_BREAKPOINT_RESUME_NOTIFY:
			TerminalUpgradeBreakpointResumeTaskManager.getInstance()
					.handleUTUpgradeBreakpoint(message);
			break;
		}
	}
}
