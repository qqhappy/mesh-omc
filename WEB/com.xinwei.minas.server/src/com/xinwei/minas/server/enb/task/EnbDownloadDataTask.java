/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.model.message.McBtsDownloadFinishedRequest;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsSynConfigService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站注册时配置下发任务
 * 
 * @author chenshaohua
 * 
 */

public class EnbDownloadDataTask implements Runnable {

	private Long btsId;

	private static final Log log = LogFactory
			.getLog(EnbDownloadDataTask.class);

	private McBtsSynConfigService syncConfigService;

	private EnbDownloadDataTaskManager manager;

	public EnbDownloadDataTask(Long btsId,
			EnbDownloadDataTaskManager manager) {
		this.btsId = btsId;
		this.manager = manager;
	}

	@Override
	public void run() {
		try {
			doWork();
		} catch (Exception e) {
		} finally {
			finishWork();
		}

	}

	private boolean doWork() throws Exception {
		syncConfigService = AppContext.getCtx().getBean(
				McBtsSynConfigService.class);

		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// 同步配置各个业务
		syncConfigService.config(McBtsSynConfigService.NOT_NEED_RESTUDY,
				mcBts.getMoId());
		// 休息一下，再向基站发送结束消息
		Thread.sleep(5000);
		// 向基站发送下载结束消息
		McBtsMessage request = buildDownloadFinishedMessage();
		if (request == null) {
			return false;
		}
		McBtsConnector connector = AppContext.getCtx().getBean(
				McBtsConnector.class);
		
		try {
			McBtsMessage response = connector.syncInvoke(request);
		} catch (TimeoutException e) {
			log.warn("send download finished message to bts timeout. btsId=0x"
					+ mcBts.getHexBtsId());
		}
		
		return true;
	}

	private void finishWork() {
		manager.removeTask(btsId);
	}

	private McBtsMessage buildDownloadFinishedMessage() {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			return null;
		}
		McBtsDownloadFinishedRequest request = new McBtsDownloadFinishedRequest(
				btsId);
		return request.toMcBtsMessage();
	}
}
