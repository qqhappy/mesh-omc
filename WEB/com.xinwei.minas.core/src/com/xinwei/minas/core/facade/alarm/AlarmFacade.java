/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-04-07	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.core.facade.alarm;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 告警
 * 
 * @author liuzhongyan
 * 
 */
public interface AlarmFacade extends Remote {
	/**
	 * 根据条件查询当前告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception;

	/**
	 * 根据条件查询历史告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception;

	/**
	 * 根据条件查出所有当前告警
	 * 
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException, Exception;

	/**
	 * 根据条件查出所有历史告警
	 * 
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException, Exception;

	/**
	 * 手工恢复告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	@Loggable
	public void restoreManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception;

	/**
	 * 手工确认告警
	 * 
	 * @param alarmIds
	 *            告警ID列表
	 * @param username
	 *            操作用户名
	 */
	@Loggable
	public void confirmManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception;

	/**
	 * 查询所有没有被确认和恢复的告警信息
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	public List<Alarm> queryActiveAlarm(long moId) throws RemoteException;
}
