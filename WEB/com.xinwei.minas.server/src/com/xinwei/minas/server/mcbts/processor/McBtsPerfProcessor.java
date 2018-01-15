/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-21	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.processor;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.minas.server.mcbts.model.message.McBtsOnlineTerminalList;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsOnlineTerminalListCache;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsOnlineTerminalListRegistry;
import com.xinwei.minas.server.stat.net.RealTimeMsgHandler;

/**
 * 
 * 性能消息处理任务
 * 
 * 
 * @author fangping
 * 
 */

public class McBtsPerfProcessor extends McBtsMessageProcessor {

	@Override
	public void doWork(McBtsMessage message) {
		int moc = message.getMoc();
		switch (moc) {
		// 在线终端列表应答
		case McBtsMessageConstants.MOC_ONLINE_USERLIST_RESPONSE:
			// 把McBtsMessage模型转换为McBtsOnlineTerminalList
			byte[] buf = message.getContent();
			McBtsOnlineTerminalList mcBtsOnlineTerminalList = new McBtsOnlineTerminalList(
					message.getBtsId(), buf);
			mcBtsOnlineTerminalList.setTransID(message.getTransactionId());
			// 组包:以resp.getTransID()为key,以McBtsOnlineTerminalList resp为value
			McBtsOnlineTerminalList resp = McBtsOnlineTerminalListCache
					.getInstance().receive(mcBtsOnlineTerminalList);
			if (resp != null) {
				FutureResult futureResult = McBtsOnlineTerminalListRegistry
						.getInstance().get(resp.getTransID());
				if (futureResult != null) {
					futureResult.set(resp);
				}
			}
			break;
		case McBtsMessageConstants.MOC_REAL_TIME_PERF_RESPONSE:
			RealTimeMsgHandler.getInstance().handle(message);
			break;
		}
	}
}
