/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.minas.server.zk.service.ZkBackupService;
import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.facade.ZkBackupCallbackFacade;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * NK集群数据定时备份任务执行类
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupTaskJob implements Job {
	private Log log = LogFactory.getLog(ZkBackupTaskJob.class);
	private static ZkBackupService service;

	public ZkBackupTaskJob() {
	}

	public static void setService(ZkBackupService zkBackupService) {
		service = zkBackupService;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JobDetail getZkBackupTaskDetail() {
		JobDetail detail = new JobDetail();
		detail.setJobClass(ZkBackupTaskJob.class);
		detail.setName("ZkBackupTaskJobDetail");
		Map dataMap = detail.getJobDataMap();
		dataMap.put("ZkBackupTaskJob", this);
		return detail;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String backupName = OmpAppContext
				.getMessage("ZkBackup_default_name_prefix");
		try {
			service.createBackup(backupName, ZkBackup.CREATETYPE_AUTO);
		} catch (final Exception e) {
			log.error(e);
			LoginUserCache.getInstance().callback(new CallbackScript() {
				public void execute(MinasClientFacade minasClientFacade)
						throws Exception {
					ZkBackupCallbackFacade facade = minasClientFacade
							.getFacade(ZkBackupCallbackFacade.class);
					if (facade != null) {
						facade.notifyException(e);
					}
				}
			});
		}
	}
}