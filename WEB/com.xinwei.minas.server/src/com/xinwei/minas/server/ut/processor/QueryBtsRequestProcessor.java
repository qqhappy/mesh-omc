/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.processor;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.ut.task.QueryBtsRequestTaskManager;

/**
 * 
 * Hlr查询基站请求的处理器
 * 
 * 
 * @author tiance
 * 
 */

public class QueryBtsRequestProcessor extends HlrMessageProcessor {

	public QueryBtsRequestProcessor() {
	}

	@Override
	protected void doWork(HlrUdpMessage message) {
		QueryBtsRequestTaskManager.getInstance().handleRequest(message);

	}

}
