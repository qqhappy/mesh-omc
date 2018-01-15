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
 * �澯�ӿ�ʵ��
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
	 * ����������ѯ��ǰ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		return alarmDAO.queryCurrentAlarm(queryCondition);
	}

	
	/**
	 * ����������ѯ��ʷ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception {
		return alarmDAO.queryHistoryAlarm(queryCondition);
	}

	/**
	 * ��������������е�ǰ�澯
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws Exception {
		return alarmDAO.queryAllCurrentAlarm(queryCondition);
	}
	
	/**
	 * �����������������ʷ�澯
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws Exception{
		return alarmDAO.queryAllHistoryAlarm(queryCondition);
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
			throws Exception {
		alarmDAO.restoreManually(alarmIds, username);
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
			throws Exception {
		alarmDAO.confirmManually(alarmIds, username);
	}

	/**
	 * ��ѯ����û�б�ȷ�Ϻͻָ��ĸ澯��Ϣ
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
