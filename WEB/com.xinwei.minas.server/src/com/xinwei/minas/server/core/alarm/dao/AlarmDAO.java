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
 * �澯DAO�ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface AlarmDAO {

	/**
	 * ��ָ��������ѯ��ǰ�澯
	 * 
	 * @param moId
	 *            MO���
	 * @param entityType
	 *            ʵ������
	 * @param entityOid
	 *            ʵ��OID
	 * @param alarmDefId
	 *            �澯����Id
	 * @return
	 */
	public Alarm queryCurrentBy(long moId, String entityType, String entityOid,
			long alarmDefId) throws Exception;

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
	 * ��ѯָ����Ԫ�����е�ǰ�澯
	 * 
	 * @param moId
	 *            ��ԪID
	 * @return
	 */
	public List<Alarm> queryCurrentAlarm(long moId) throws Exception;

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
	 * �ڵ�ǰ�澯������µļ�¼.
	 * 
	 * @param alarm
	 *            ���洢�ĸ澯
	 */
	public void add(Alarm alarm) throws Exception;

	/**
	 * ���µ�ǰ�澯
	 * 
	 * @param alarm
	 */
	public void update(Alarm alarm) throws Exception;

	/**
	 * ���澯Ǩ�Ƶ���ʷ����
	 * 
	 * @param alarm
	 */
	public void move2History(Alarm alarm) throws Exception;

	/**
	 * �ֹ��ָ��澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
	 */
	public void restoreManually(List<Long> alarmIds, String username)
			throws Exception;

	/**
	 * �ֹ�ȷ�ϸ澯
	 * 
	 * @param alarmIds
	 *            �澯ID�б�
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
	 * ���ݴ����MoId�б���ѯMoId��Ӧ����߼���澯�����û�и澯���򷵻ؿա�
	 * 
	 * @param moIds
	 *            MoId�б�
	 * @return MoId������߼���澯��ӳ������û�и澯���򲻷���
	 */
	public Map<Long, Integer> queryMaxLevelMapping(List<Long> moIds);

	/**
	 * ɾ����ʷ�澯
	 * 
	 * @param alarmReservedDay
	 *            �澯��������
	 */
	public void deleteHistoryAlarm(int alarmReservedDay);

	/**
	 * ����moIdɾ����ǰ�����ʷ��ĸ澯
	 * 
	 * @param moId
	 */
	public void deleteAlarmByMoId(long moId);

	/**
	 * ��ѯ���еĸ澯����
	 * 
	 * @return
	 */
	public List<AlarmDef> queryAllAlarmDef();
	
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
}
