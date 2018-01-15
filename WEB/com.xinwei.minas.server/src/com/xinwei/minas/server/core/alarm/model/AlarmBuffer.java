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
 * �澯������
 * 
 * @author chenjunhua
 * 
 */

public class AlarmBuffer {

	// �澯����
	private Map<String, List<Alarm>> cache = new ConcurrentHashMap();

	/**
	 * ���Ӹ澯
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
				// ������θ澯��״̬���ϴθ澯��״̬һ�£�������ϴθ澯����Ϣ
				lastAlarm.setLastAlarmTime(alarm.getLastAlarmTime());
				lastAlarm.increaseAlarmTimes();
			}
			else {
				// ������θ澯��״̬���ϴθ澯��״̬һ�£���׷�Ӹ澯
				alarmList.add(alarm);
			}
		}
		
	}



	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public Map<String, List<Alarm>> getCache() {
		return cache;
	}

	/**
	 * ����澯����
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * �����澯�����Key
	 * @param alarm
	 * @return
	 */
	private String createKey(Alarm alarm) {
		String key = alarm.getMoId() + "/" + alarm.getEntityType() + "/"
				+ alarm.getEntityOid() + "/" + alarm.getAlarmDefId();

		return key;
	}

}
