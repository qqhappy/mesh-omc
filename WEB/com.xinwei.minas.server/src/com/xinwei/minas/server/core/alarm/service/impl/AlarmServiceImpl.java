/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-04-07	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.server.core.alarm.service.impl;

import java.util.List;

import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.core.alarm.service.AlarmService;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 告警接口实现
 * 
 * @author liuzhongyan
 * 
 */
public class AlarmServiceImpl implements AlarmService {

	private AlarmDAO alarmDAO;

	public void setAlarmDAO(AlarmDAO alarmDAO) {
		this.alarmDAO = alarmDAO;
	}

	/**
	 * 根据条件查询当前告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		return alarmDAO.queryCurrentAlarm(queryCondition);
	}

	
	/**
	 * 根据条件查询历史告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		return alarmDAO.queryHistoryAlarm(queryCondition);
	}

	/**
	 * 根据条件查出所有当前告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws Exception {
		return alarmDAO.queryAllCurrentAlarm(queryCondition);
	}
	
	/**
	 * 根据条件查出所有历史告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws Exception{
		return alarmDAO.queryAllHistoryAlarm(queryCondition);
	}
	/**
	 * 手工恢复告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception {
		alarmDAO.restoreManually(alarmIds, username);
	}

	/**
	 * 手工确认告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	public void confirmManually(List<Long> alarmIds, String username)
			throws Exception {
		alarmDAO.confirmManually(alarmIds, username);
	}

	/**
	 * 查询所有没有被确认和恢复的告警信息
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	@Override
	public List<Alarm> queryActiveAlarm(long moId) {
		return alarmDAO.queryActiveAlarm(moId);
	}

	@Override
	public void move2History(long moId) throws Exception {
		List<Alarm> alarmList = alarmDAO.queryCurrentAlarm(moId);
		for (Alarm alarm : alarmList) {
			alarmDAO.move2History(alarm);
		}
	}

}
