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
 * eNB告警服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbAlarmServiceImpl implements EnbAlarmService {

	// 系统恢复和确认用户
	private String systemUser = "system";

	@Override
	public void syncAlarm(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 网元可配置状态才进行告警同步
		if (enb.isConfigurable()) {
			// 同步下发告警同步请求
			EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
					EnbAlarmHelper.class);
			enbAlarmHelper.syncEnbAlarm(enb.getEnbId());
			AlarmDAO alarmDAO = OmpAppContext.getCtx().getBean(AlarmDAO.class);
			// 收到告警同步应答成功后，将非自定义告警设置为已恢复，并移入历史告警
			List<Alarm> alarmList = alarmDAO.queryActiveAlarm(moId);
			// 获取非自定义告警
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
			// 将非自定义的告警设置为已恢复，然后移入历史告警
			alarmDAO.restoreManually(alarmIds, systemUser);
			for (Alarm alarm : listToMove) {
				alarmDAO.move2History(alarm);
			}

		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

}
