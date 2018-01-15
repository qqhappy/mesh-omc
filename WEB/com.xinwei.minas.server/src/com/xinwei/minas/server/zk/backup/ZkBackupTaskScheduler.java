/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.xinwei.minas.server.zk.dao.ZkBackupTaskDAO;
import com.xinwei.minas.server.zk.service.ZkBackupService;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * NK集群数据定时备份任务调度器
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupTaskScheduler {

	private Log log = LogFactory.getLog(ZkBackupTaskScheduler.class);

	private static ZkBackupTaskScheduler instance = new ZkBackupTaskScheduler();

	private Scheduler scheduler;

	private JobDetail jobDetail;

	private ZkBackupTaskTrigger trigger;

	private ZkBackupTask task;

	private ZkBackupTaskDAO zkBackupTaskDAO;

	private ZkBackupService zkBackupService;

	public static ZkBackupTaskScheduler getInstance() {
		return instance;
	}

	private ZkBackupTaskScheduler() {
	}

	public void initialize() throws Exception {
		try {
			List<ZkBackupTask> taskList = zkBackupTaskDAO.queryBackupTasks();
			if (taskList.size() > 0) {
				ZkBackupTask task = taskList.get(0);
				if (task.getState() == ZkBackupTask.STATE_OPEN) {
					task.setState(ZkBackupTask.STATE_CLOSED);
					zkBackupTaskDAO.modifyBackupTask(task);
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void setTask(ZkBackupTask task) {
		this.task = task;
	}

	public ZkBackupTask getTask() {
		return this.task;
	}

	/**
	 * 调度器做准备，设置执行间隔
	 * 
	 * @throws Exception
	 */
	private void prepare() throws Exception {
		reset();
		scheduler.scheduleJob(this.jobDetail, this.trigger);
	}

	/**
	 * 开始定时备份任务
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		setTrigger();
		prepare();
		if (this.task == null) {
			throw new Exception(
					OmpAppContext
							.getMessage("ZkBackupTaskScheduler.start_failed_null_task"));
		}
		scheduler.start();
		if (!scheduler.isStarted()) {
			throw new Exception(
					OmpAppContext
							.getMessage("ZkBackupTaskScheduler.task_start_failed"));
		}
	}

	/**
	 * 停止定时备份任务
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		scheduler.shutdown();
	}

	private void setJobDetail() {
		ZkBackupTaskJob job = new ZkBackupTaskJob();
		ZkBackupTaskJob.setService(zkBackupService);
		jobDetail = job.getZkBackupTaskDetail();
	}

	private void setTrigger() {
		trigger = new ZkBackupTaskTrigger();
		trigger.setRepeatInterval(ZkBackupUtil.getInterval(task.getInterval()));
	}

	private void reset() throws Exception {
		scheduler = new StdSchedulerFactory().getScheduler();
		setJobDetail();
	}

	public void setZkBackupTaskDAO(ZkBackupTaskDAO zkBackupTaskDAO) {
		this.zkBackupTaskDAO = zkBackupTaskDAO;
	}

	public void setZkBackupService(ZkBackupService zkBackupService) {
		this.zkBackupService = zkBackupService;
	}
}