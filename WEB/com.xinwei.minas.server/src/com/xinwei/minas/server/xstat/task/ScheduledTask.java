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
 * �ɵ��ȵ����������
 * 
 * @author fanhaoyu
 * 
 */

public abstract class ScheduledTask implements Runnable {

	private Log log = LogFactory.getLog(ScheduledTask.class);

	// Ĭ���ӳ�10s��ʼִ��
	private long delay = 10;

	// Ĭ��60��ִ��һ��
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
	 * ���������״�ִ��ʱ����ʱ
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
	 * ��������ִ�м��
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
