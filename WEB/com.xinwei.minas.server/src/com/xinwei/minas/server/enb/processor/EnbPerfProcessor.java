/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.processor;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.stat.net.EnbRealtimeMsgHandler;

/**
 * 
 * eNB性能消息处理器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbPerfProcessor extends EnbMessageProcessor {

	@Override
	public void doWork(EnbAppMessage appMessage) {
		int moc = appMessage.getMoc();
		switch (moc) {
		// 在线终端列表应答
		case EnbMessageConstants.MOC_REPORT_REALTIME_DATA_NOTIRY:
			EnbRealtimeMsgHandler.getInstance().handle(appMessage);
			break;
		}
	}

}
