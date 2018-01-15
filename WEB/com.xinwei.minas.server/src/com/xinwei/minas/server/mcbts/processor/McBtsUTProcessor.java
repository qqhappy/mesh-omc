/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.task.McBtsUTRegisterTaskManager;

/**
 * 
 * 基站终端消息处理模型
 * 
 * @author tiance
 * 
 */

public class McBtsUTProcessor extends McBtsMessageProcessor {

	@Override
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		case McBtsMessageConstants.MOC_UT_REGISTER_NOTIFY:
			McBtsUTRegisterTaskManager.getInstance().handleUTRegisterNotify(message);
			break;
		}

	}

}
