/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;

/**
 * 
 * 告警任务
 * 
 * @author chenjunhua
 * 
 */

public class AlarmTask {
	
	private static final Log log = LogFactory.getLog(AlarmTask.class);
	
	// 告警保留天数
	private int alarmReservedDay = 7;
		
	private AlarmDAO alarmDAO;

	/**
	 * 执行任务
	 */
	public void doWork() {
		try {
			deleteHistoryAlarm();
		} catch (Exception e) {
			log.error("failed to delete history alarm.", e);
		}
	}
	
	/**
	 * 删除历史告警
	 */
	private void deleteHistoryAlarm() {
		if (alarmDAO != null) {
			alarmDAO.deleteHistoryAlarm(alarmReservedDay);
		}
	}

	public void setAlarmReservedDay(int alarmReservedDay) {
		this.alarmReservedDay = alarmReservedDay;
	}

	public void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}
	
	
	
	
}
