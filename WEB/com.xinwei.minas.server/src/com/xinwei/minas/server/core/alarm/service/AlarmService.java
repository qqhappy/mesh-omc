/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.service;

import java.util.List;

import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * 告警查询服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface AlarmService {

	/**
	 * 根据条件查询当前告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws Exception;

	/**
	 * 根据条件查询历史告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception;

	/**
	 * 根据条件查出所有当前告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws Exception;
	/**
	 * 根据条件查出所有历史告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws Exception;
	
	/**
	 * 手工恢复告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * 手工确认告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	public void confirmManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * 查询所有没有被确认和恢复的告警信息
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	public List<Alarm> queryActiveAlarm(long moId);

	/**
	 * 将指定网元的当前告警迁移到历史表中
	 * 
	 * @param moId
	 */
	public void move2History(long moId) throws Exception;

}
