/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-04-07	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.facade.alarm.AlarmFacade;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.minas.server.core.alarm.service.AlarmService;
import com.xinwei.minas.server.core.conf.service.MoBasicService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 告警实现
 * 
 * @author liuzhongyan
 * 
 */
public class AlarmFacadeImpl extends UnicastRemoteObject implements AlarmFacade {
	protected AlarmFacadeImpl() throws RemoteException {
		super();
	}

	private AlarmService getService() {
		return AppContext.getCtx().getBean(AlarmService.class);
	}

	/**
	 * 根据条件查询当前告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception {
		AlarmService alarmservice = getService();
		return alarmservice.queryCurrentAlarm(queryCondition);
	}

	
	/**
	 * 根据条件查询历史告警
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception {
		AlarmService alarmservice = getService();
		return alarmservice.queryHistoryAlarm(queryCondition);
	}
	
	@Override
	/**
	 * 根据条件查出所有当前告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException,Exception{
		AlarmService alarmservice = getService();
		return alarmservice.queryAllCurrentAlarm(queryCondition);
	}
	
	@Override
	/**
	 * 根据条件查出所有历史告警
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException,Exception{
		AlarmService alarmservice = getService();
		return alarmservice.queryAllHistoryAlarm(queryCondition);
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
			throws RemoteException, Exception {
		AlarmService alarmservice = getService();
		alarmservice.restoreManually(alarmIds, username);
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
			throws RemoteException, Exception {
		AlarmService alarmservice = getService();
		alarmservice.confirmManually(alarmIds, username);
	}

	/**
	 * 查询所有没有被确认和恢复的告警信息
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	@Override
	public List<Alarm> queryActiveAlarm(long moId) throws RemoteException {
		return getService().queryActiveAlarm(moId);
	};
}
