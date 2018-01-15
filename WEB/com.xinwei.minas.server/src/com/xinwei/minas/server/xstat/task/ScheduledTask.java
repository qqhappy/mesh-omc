/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-24	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 可调度的任务虚基类
 * 
 * @author fanhaoyu
 * 
 */

public abstract class ScheduledTask implements Runnable {

	private Log log = LogFactory.getLog(ScheduledTask.class);

	// 默认延迟10s开始执行
	private long delay = 10;

	// 默认60秒执行一次
	private long period = 60;

	@Override
	public void run() {
		try {
			doWork();
		} catch (Exception e) {
			log.error("Scheduled Task execute with error."
					+ e.getLocalizedMessage());
		}
	}

	public abstract void doWork() throws Exception;

	/**
	 * 设置任务首次执行时的延时
	 * 
	 * @param delay
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getDelay() {
		return delay;
	}

	/**
	 * 设置任务执行间隔
	 * 
	 * @param period
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	public long getPeriod() {
		return period;
	}
}
