/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.helper;

import com.xinwei.minas.core.model.alarm.AlarmLevel;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.core.alarm.model.AlarmEvent;
import com.xinwei.minas.server.core.alarm.processor.AlarmProcessor;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.model.message.McBtsAlarm;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ�澯����
 * 
 * @author chenjunhua
 * 
 */

public class McBtsAlarmHelper {

	// ��վ�Զ���澯��ʼID
	public static final int BTS_ALARM_BASE_ID = 100000;
	
	// ��վʧ���澯ID
	public static final int BTS_DISCONNECTED_ALARM_ID = BTS_ALARM_BASE_ID + 1;
	
	// ������Ϣ����ʧ�ܸ澯
	public static final int COMMON_CHANNEL_IMPORT_FAILED_ALARM_ID = BTS_ALARM_BASE_ID + 2;
	
	//��Դ��Ϣ�澯
	public static final int POWER_SUPPLY_ALARM_ID = BTS_ALARM_BASE_ID + 3;

	private AlarmProcessor alarmEventProcessor;

	public McBtsAlarmHelper() {
	}

	/**
	 * �����վ�澯
	 * 
	 * @param btsAlarm
	 *            ��վ�澯ģ��
	 */
	public void processMcBtsAlarm(McBtsAlarm btsAlarm) {
		// ����վ�澯ģ��ת��Ϊͨ�ø澯ģ��
		AlarmEvent alarmEvent = this.transferAlarm(btsAlarm);
		if (alarmEvent != null) {
			// ���ø澯�¼�����ģ��
			alarmEventProcessor.process(alarmEvent);
		}
	}

	/**
	 * ������վʧ���澯
	 * 
	 * @param bts
	 */
	public void fireBtsDisconnectedAlarm(McBts bts) {
		McBtsAlarm btsAlarm = this.createBtsDisconnectedAlarm(bts);
		this.processMcBtsAlarm(btsAlarm);
	}
	
	/**
	 * �������鵼��ʧ�ܸ澯��Ϣ
	 * @param bts
	 * @param alarmContent
	 */
	public void fireImportCommonChannelFailedAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = this.createImportCommonChannelFailedAlarm(bts, alarmContent);
		this.processMcBtsAlarm(btsAlarm);
	}
	
	/**
	 * ������Դ�澯
	 * @param bts
	 * @param alarmContent
	 */
	public void firePowerSupplyAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = this.createPowerSupplyAlarm(bts, alarmContent);
		this.processMcBtsAlarm(btsAlarm);
	}

	/**
	 * ������վʧ���澯�ָ�
	 * 
	 * @param bts
	 */
	public void fireBtsDisconnectedAlarmRestored(McBts bts) {
		McBtsAlarm btsAlarm = this.createBtsDisconnectedAlarmRestored(bts);
		this.processMcBtsAlarm(btsAlarm);
	}

	public void setAlarmEventProcessor(AlarmProcessor alarmEventProcessor) {
		this.alarmEventProcessor = alarmEventProcessor;
	}

	/**
	 * ������վ��վʧ���澯ģ��
	 * 
	 * @param bts
	 * @return
	 */
	private McBtsAlarm createBtsDisconnectedAlarm(McBts bts) {
		McBtsAlarm btsAlarm = new McBtsAlarm(bts.getBtsId());
		btsAlarm.setFlag(McBtsAlarm.FLAG_ALARM);
		btsAlarm.setType(McBtsAlarm.TYPE_L3);
		btsAlarm.setIndex(0);
		btsAlarm.setAlarmDefId(BTS_DISCONNECTED_ALARM_ID);
		btsAlarm.setAlarmLevel(AlarmLevel.STATE_CRITICAL);
		String alarmContent = OmpAppContext.getMessage("mcbts_alarm_disconnect_with_ems");
		btsAlarm.setAlarmContent(alarmContent);

		return btsAlarm;
	}

	/**
	 * ������վ��վʧ���澯ģ��
	 * 
	 * @param bts
	 * @return
	 */
	private McBtsAlarm createBtsDisconnectedAlarmRestored(McBts bts) {
		McBtsAlarm btsAlarm = createBtsDisconnectedAlarm(bts);
		btsAlarm.setFlag(McBtsAlarm.FLAG_RESTORED);
		return btsAlarm;
	}
	
	/**
	 * ������Ϣ����ʧ�ܸ澯
	 * @param bts
	 * @param alarmContent
	 * @return
	 */
	private McBtsAlarm createImportCommonChannelFailedAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = new McBtsAlarm(bts.getBtsId());
		btsAlarm.setFlag(McBtsAlarm.FLAG_ALARM);
		btsAlarm.setType(McBtsAlarm.TYPE_L3);
		btsAlarm.setIndex(0);
		btsAlarm.setAlarmDefId(COMMON_CHANNEL_IMPORT_FAILED_ALARM_ID);
		btsAlarm.setAlarmLevel(AlarmLevel.STATE_CRITICAL);
		btsAlarm.setAlarmContent(alarmContent);
		return btsAlarm;
	}
	
	
	/**
	 * ��Դ��Ϣ�澯
	 * @param bts
	 * @param alarmContent
	 * @return
	 */
	private McBtsAlarm createPowerSupplyAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = new McBtsAlarm(bts.getBtsId());
		btsAlarm.setFlag(McBtsAlarm.FLAG_ALARM);
		btsAlarm.setType(McBtsAlarm.TYPE_L3);
		btsAlarm.setIndex(0);
		btsAlarm.setAlarmDefId(POWER_SUPPLY_ALARM_ID);
		btsAlarm.setAlarmLevel(AlarmLevel.STATE_CRITICAL);
		btsAlarm.setAlarmContent(alarmContent);
		return btsAlarm;
	}
	

	/**
	 * ת���澯ģ��
	 * 
	 * @param mcBtsAlarm
	 * @return
	 */
	private AlarmEvent transferAlarm(McBtsAlarm mcBtsAlarm) {
		AlarmEvent alarmEvent = new AlarmEvent();
		Long btsId = mcBtsAlarm.getBtsId();
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			return null;
		}
		// ����MoId
		alarmEvent.setMoId(mcBts.getMoId());
		// ���ø澯����Id
		alarmEvent.setAlarmDefId(mcBtsAlarm.getAlarmDefId());
		// ���ø澯����
		alarmEvent.setAlarmContent(mcBtsAlarm.getAlarmContent());
		// ���ø澯״̬
		alarmEvent.setAlarmState(mcBtsAlarm.getFlag());
		// ���ø澯����
		alarmEvent.setAlarmLevel(mcBtsAlarm.getAlarmLevel());
		// ���ø澯ʵ��
		String entityType = this.generateEntityType(mcBtsAlarm);
		alarmEvent.setEntityType(entityType);
		// ���ø澯Oid
		String entityOid = this.generateEntityOid(mcBtsAlarm);
		alarmEvent.setEntityOid(entityOid);
		// ���ø澯ʱ��(14λ����yyyyMMddhhmmss)
		long eventTime = DateUtils.getBriefTimeFromMillisecondTime(System
				.currentTimeMillis());
		alarmEvent.setEventTime(eventTime);
		return alarmEvent;
	}

	/**
	 * ���ݻ�վ�澯ģ�����ɸ澯��ʵ��Oid
	 * 
	 * @param mcBtsAlarm
	 * @return
	 */
	private String generateEntityOid(McBtsAlarm mcBtsAlarm) {
		StringBuilder entityOid = new StringBuilder();
		Long btsId = mcBtsAlarm.getBtsId();
		String hexBtsId = Long.toHexString(btsId).toLowerCase();
		StringBuffer strBuffer = new StringBuffer();
		if (8 - hexBtsId.length() > 0) {
			int makeIndex = 8 - hexBtsId.length();
			for (int index = 0; index < makeIndex; index++) {
				strBuffer.append("0");
			}
		}
		strBuffer.append(hexBtsId);
		entityOid.append(strBuffer.toString()).append(McBtsConstants.DOT)
				.append(mcBtsAlarm.getIndex());
		return entityOid.toString();
	}

	/**
	 * ���ݻ�վ�澯ģ�����ɸ澯��ʵ������
	 * 
	 * @param mcBtsAlarm
	 * @return
	 */
	private String generateEntityType(McBtsAlarm mcBtsAlarm) {
		String entityType = "";
		int type = mcBtsAlarm.getType();
		switch (type) {
		case McBtsAlarm.TYPE_L3:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_L3;
			break;
		case McBtsAlarm.TYPE_L2:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_L2;
			break;
		case McBtsAlarm.TYPE_MCP:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_MCP;
			break;
		case McBtsAlarm.TYPE_AUX:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_AUX;
			break;
		case McBtsAlarm.TYPE_ENV:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_ENV;
			break;
		case McBtsAlarm.TYPE_PLL:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_PLL;
			break;
		case McBtsAlarm.TYPE_RF:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_RF;
			break;
		case McBtsAlarm.TYPE_GPS:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_GPS;
			break;
		case McBtsAlarm.TYPE_L1:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_L1;
			break;
		case McBtsAlarm.TYPE_FPGA:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_FPGA;
			break;
		case McBtsAlarm.TYPE_FEP:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_FEP;
			break;
		case McBtsAlarm.TYPE_CORE9:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_CORE9;
			break;
		case McBtsAlarm.TYPE_RRU:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_RRU;
			break;
		case McBtsAlarm.TYPE_AIF:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_AIF;
			break;
		case McBtsAlarm.TYPE_BATTERY:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS_BATTERY;
			break;
		default:
			entityType = McBtsConstants.ENTITY_TYPE_MCBTS + ".Type" + type;
			break;
		}
		return entityType;
	}
}
