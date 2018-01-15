/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.alarm.Alarm;

/**
 * 
 * 告警缓存区
 * 
 * @author chenjunhua
 * 
 */

public class AlarmBuffer {

	// 告警缓存
	private Map<String, List<Alarm>> cache = new ConcurrentHashMap();

	/**
	 * 增加告警
	 * 
	 * @param alarm
	 */
	public void add(Alarm alarm) {
		String key = createKey(alarm);
		alarm.getAlarmDefId();
		if (!cache.containsKey(key)) {
			cache.put(key, new LinkedList());
		}
		List<Alarm> alarmList = cache.get(key);
		if (alarmList.isEmpty()) {
			alarmList.add(alarm);
		}
		else {
			Alarm lastAlarm = alarmList.get(alarmList.size()-1);
			if (lastAlarm.getAlarmState() == alarm.getAlarmState()) {
				// 如果本次告警的状态和上次告警的状态一致，则更新上次告警的信息
				lastAlarm.setLastAlarmTime(alarm.getLastAlarmTime());
				lastAlarm.increaseAlarmTimes();
			}
			else {
				// 如果本次告警的状态和上次告警的状态一致，则追加告警
				alarmList.add(alarm);
			}
		}
		
	}



	/**
	 * 获取缓冲
	 * 
	 * @return
	 */
	public Map<String, List<Alarm>> getCache() {
		return cache;
	}

	/**
	 * 清除告警缓存
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * 创建告警缓存的Key
	 * @param alarm
	 * @return
	 */
	private String createKey(Alarm alarm) {
		String key = alarm.getMoId() + "/" + alarm.getEntityType() + "/"
				+ alarm.getEntityOid() + "/" + alarm.getAlarmDefId();

		return key;
	}

}
