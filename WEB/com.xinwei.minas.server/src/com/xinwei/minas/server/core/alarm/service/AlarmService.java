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
 * �澯��ѯ����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface AlarmService {

	/**
	 * ����������ѯ��ǰ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryCurrentAlarm(
			AlarmQueryCondition queryCondition) throws Exception;

	/**
	 * ����������ѯ��ʷ�澯
	 * 
	 * @param queryCondition
	 *            ��ѯ����
	 * @return
	 */
	public PagingData<Alarm> queryHistoryAlarm(
			AlarmQueryCondition queryCondition) throws Exception;

	/**
	 * ��������������е�ǰ�澯
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllCurrentAlarm(AlarmQueryCondition queryCondition)
			throws Exception;
	/**
	 * �����������������ʷ�澯
	 * @param queryCondition
	 * @return
	 * @throws Exception
	 */
	public List<Alarm> queryAllHistoryAlarm(AlarmQueryCondition queryCondition)
			throws Exception;
	
	/**
	 * �ֹ��ָ��澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * �ֹ�ȷ�ϸ澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 * @param username
	 *            �����û���
	 */
	public void confirmManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * ��ѯ����û�б�ȷ�Ϻͻָ��ĸ澯��Ϣ
	 * 
	 * @param moId
	 * @return List<Alarm>
	 */
	public List<Alarm> queryActiveAlarm(long moId);

	/**
	 * ��ָ����Ԫ�ĵ�ǰ�澯Ǩ�Ƶ���ʷ����
	 * 
	 * @param moId
	 */
	public void move2History(long moId) throws Exception;

}
