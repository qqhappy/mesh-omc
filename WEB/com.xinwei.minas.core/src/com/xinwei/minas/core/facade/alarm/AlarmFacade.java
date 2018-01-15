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
 * �澯
 * 
 * @author liuzhongyan
 * 
 */
public interface AlarmFacade extends Remote {
	/**
	 * ����������ѯ��ǰ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception;

	/**
	 * ����������ѯ��ʷ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws RemoteException,
			Exception;

	/**
	 * ��������������е�ǰ�澯
	 * 
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException, Exception;

	/**
	 * �����������������ʷ�澯
	 * 
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws RemoteException, Exception;

	/**
	 * �ֹ��ָ��澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	@Loggable
	public void restoreManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception;

	/**
	 * �ֹ�ȷ�ϸ澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	@Loggable
	public void confirmManually(List<Long> alarmIds, String username)
			throws RemoteException, Exception;

	/**
	 * ��ѯ����û�б�ȷ�Ϻͻָ��ĸ澯��Ϣ
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	public List<Alarm> queryActiveAlarm(long moId) throws RemoteException;
}
