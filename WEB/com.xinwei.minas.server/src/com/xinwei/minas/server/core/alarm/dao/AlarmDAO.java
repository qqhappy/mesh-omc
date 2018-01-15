/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-5	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmDef;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * 告警DAO接口
 * 
 * @author chenjunhua
 * 
 */

public interface AlarmDAO {

	/**
	 * 按指定条件查询当前告警
	 * 
	 * @param moId
	 *            MO编号
	 * @param entityType
	 *            实体类型
	 * @param entityOid
	 *            实体OID
	 * @param alarmDefId
	 *            告警定义Id
	 * @return
	 */
	public Alarm queryCurrentBy(long moId, String entityType, String entityOid,
			long alarmDefId) throws Exception;

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
	 * 查询指定网元的所有当前告警
	 * 
	 * @param moId
	 *            网元ID
	 * @return
	 */
	public List<Alarm> queryCurrentAlarm(long moId) throws Exception;

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
	 * 在当前告警表插入新的记录.
	 * 
	 * @param alarm
	 *            待存储的告警
	 */
	public void add(Alarm alarm) throws Exception;

	/**
	 * 更新当前告警
	 * 
	 * @param alarm
	 */
	public void update(Alarm alarm) throws Exception;

	/**
	 * 将告警迁移到历史表中
	 * 
	 * @param alarm
	 */
	public void move2History(Alarm alarm) throws Exception;

	/**
	 * 手工恢复告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * 手工确认告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
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
	 * 根据传入的MoId列表，查询MoId对应的最高级别告警，如果没有告警，则返回空。
	 * 
	 * @param moIds
	 *            MoId列表
	 * @return MoId及其最高级别告警的映射表，如果没有告警，则不返回
	 */
	public Map<Long, Integer> queryMaxLevelMapping(List<Long> moIds);

	/**
	 * 删除历史告警
	 * 
	 * @param alarmReservedDay
	 *            告警保留天数
	 */
	public void deleteHistoryAlarm(int alarmReservedDay);

	/**
	 * 基于moId删除当前表和历史表的告警
	 * 
	 * @param moId
	 */
	public void deleteAlarmByMoId(long moId);

	/**
	 * 查询所有的告警定义
	 * 
	 * @return
	 */
	public List<AlarmDef> queryAllAlarmDef();
	
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
}
