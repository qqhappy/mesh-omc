/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.zk.service.ZkDataSyncService;

/**
 * 
 * 数据同步定时器
 * 
 * @author fanhaoyu
 * 
 */

public class ZkDataSyncTimer extends QuartzJobBean {
	private  Log log = LogFactory.getLog(ZkDataSyncTimer.class);
	private ZkDataSyncService service;

	public ZkDataSyncTimer() {
		service = AppContext.getCtx().getBean(ZkDataSyncService.class);
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			service.updateData();
		} catch (Exception e) {
			log.error(e);
		}
	}
}