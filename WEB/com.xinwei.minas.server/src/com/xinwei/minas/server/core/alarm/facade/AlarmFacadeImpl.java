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
 * �澯ʵ��
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
	 * ����������ѯ��ǰ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception {
		AlarmService alarmservice = getService();
		return alarmservice.queryCurrentAlarm(queryCondition);
	}

	
	/**
	 * ����������ѯ��ʷ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
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
	 * ��������������е�ǰ�澯
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
	 * �����������������ʷ�澯
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
	 * �ֹ��ָ��澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception {
		AlarmService alarmservice = getService();
		alarmservice.restoreManually(alarmIds, username);
	}

	/**
	 * �ֹ�ȷ�ϸ澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	public void confirmManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception {
		AlarmService alarmservice = getService();
		alarmservice.confirmManually(alarmIds, username);
	}

	/**
	 * ��ѯ����û�б�ȷ�Ϻͻָ��ĸ澯��Ϣ
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	@Override
	public List<Alarm> queryActiveAlarm(long moId) throws RemoteException {
		return getService().queryActiveAlarm(moId);
	};
}
