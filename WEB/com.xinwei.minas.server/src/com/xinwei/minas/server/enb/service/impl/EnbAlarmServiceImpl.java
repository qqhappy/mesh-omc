/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.core.alarm.dao.AlarmDAO;
import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.service.EnbAlarmService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB�澯����ӿ�ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class EnbAlarmServiceImpl implements EnbAlarmService {

	// ϵͳ�ָ���ȷ���û�
	private String systemUser = "system";

	@Override
	public void syncAlarm(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ��Ԫ������״̬�Ž��и澯ͬ��
		if (enb.isConfigurable()) {
			// ͬ���·��澯ͬ������
			EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
					EnbAlarmHelper.class);
			enbAlarmHelper.syncEnbAlarm(enb.getEnbId());
			AlarmDAO alarmDAO = OmpAppContext.getCtx().getBean(AlarmDAO.class);
			// �յ��澯ͬ��Ӧ��ɹ��󣬽����Զ���澯����Ϊ�ѻָ�����������ʷ�澯
			List<Alarm> alarmList = alarmDAO.queryActiveAlarm(moId);
			// ��ȡ���Զ���澯
			List<Alarm> listToMove = new LinkedList<Alarm>();
			for (Alarm alarm : alarmList) {
				long alarmDefId = alarm.getAlarmDefId();
				if (!enbAlarmHelper.isCustomizedAlarm(alarmDefId)) {
					listToMove.add(alarm);
				}
			}

			List<Long> alarmIds = new LinkedList<Long>();
			for (Alarm alarm : listToMove) {
				alarmIds.add(alarm.getId());
			}
			// �����Զ���ĸ澯����Ϊ�ѻָ���Ȼ��������ʷ�澯
			alarmDAO.restoreManually(alarmIds, systemUser);
			for (Alarm alarm : listToMove) {
				alarmDAO.move2History(alarm);
			}

		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

}
