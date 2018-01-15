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
 * �澯����
 * 
 * @author chenjunhua
 * 
 */

public class AlarmTask {
	
	private static final Log log = LogFactory.getLog(AlarmTask.class);
	
	// �澯��������
	private int alarmReservedDay = 7;
		
	private AlarmDAO alarmDAO;

	/**
	 * ִ������
	 */
	public void doWork() {
		try {
			deleteHistoryAlarm();
		} catch (Exception e) {
			log.error("failed to delete history alarm.", e);
		}
	}
	
	/**
	 * ɾ����ʷ�澯
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
