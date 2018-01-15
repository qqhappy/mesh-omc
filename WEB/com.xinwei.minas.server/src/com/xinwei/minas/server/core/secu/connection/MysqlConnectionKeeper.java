/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.connection;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Mysql���ӱ��֣���ֹ���ӳ�ʱ
 * 
 * @author fanhaoyu
 * 
 */

public class MysqlConnectionKeeper {

	private Log log = LogFactory.getLog(MysqlConnectionKeeper.class);

	private Timer timer;

	private int delay;

	private int period;

	private TimerTask task;

	public void start() {
		log.info("MysqlConnectionKeeper start.");
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(task, delay * 1000, period * 1000);
		}
	}

	public void restart() {
		this.stop();
		this.start();
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * ���������״�ִ���ӳ٣���λ��
	 * 
	 * @param delay
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * ��������ִ�м������λ��
	 * 
	 * @param period
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * ��ʱ����
	 * 
	 * @param task
	 */
	public void setTask(TimerTask task) {
		this.task = task;
	}

}
