/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.alarm.Alarm;

/**
 * 
 * �澯����Ĭ��ʵ�� 
 * 
 * @author chenjunhua
 * 
 */
@Deprecated
public class AlarmCacheDefaultImpl {

	// �澯����
	private Map<Long, Map<String, Alarm>> cacheByMoId = new ConcurrentHashMap();
	
	/**
	 * ��ʼ���澯���棬�����ݿ��еĵ�ǰ�澯װ�ص�����
	 * @param alarms
	 */
	public void initialize(List<Alarm> alarms) {
		// TODO:
	}
	
	
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
	public Alarm queryBy(long moId, String entityType, String entityOid,
			long alarmDefId) throws Exception {
		return null;
	}
}
