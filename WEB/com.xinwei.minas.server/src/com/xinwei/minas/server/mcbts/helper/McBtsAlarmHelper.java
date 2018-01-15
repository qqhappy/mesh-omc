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
 * 基站告警助手
 * 
 * @author chenjunhua
 * 
 */

public class McBtsAlarmHelper {

	// 基站自定义告警起始ID
	public static final int BTS_ALARM_BASE_ID = 100000;
	
	// 基站失连告警ID
	public static final int BTS_DISCONNECTED_ALARM_ID = BTS_ALARM_BASE_ID + 1;
	
	// 网归信息导入失败告警
	public static final int COMMON_CHANNEL_IMPORT_FAILED_ALARM_ID = BTS_ALARM_BASE_ID + 2;
	
	//电源信息告警
	public static final int POWER_SUPPLY_ALARM_ID = BTS_ALARM_BASE_ID + 3;

	private AlarmProcessor alarmEventProcessor;

	public McBtsAlarmHelper() {
	}

	/**
	 * 处理基站告警
	 * 
	 * @param btsAlarm
	 *            基站告警模型
	 */
	public void processMcBtsAlarm(McBtsAlarm btsAlarm) {
		// 将基站告警模型转换为通用告警模型
		AlarmEvent alarmEvent = this.transferAlarm(btsAlarm);
		if (alarmEvent != null) {
			// 调用告警事件处理模块
			alarmEventProcessor.process(alarmEvent);
		}
	}

	/**
	 * 触发基站失连告警
	 * 
	 * @param bts
	 */
	public void fireBtsDisconnectedAlarm(McBts bts) {
		McBtsAlarm btsAlarm = this.createBtsDisconnectedAlarm(bts);
		this.processMcBtsAlarm(btsAlarm);
	}
	
	/**
	 * 触发网归导入失败告警信息
	 * @param bts
	 * @param alarmContent
	 */
	public void fireImportCommonChannelFailedAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = this.createImportCommonChannelFailedAlarm(bts, alarmContent);
		this.processMcBtsAlarm(btsAlarm);
	}
	
	/**
	 * 触发电源告警
	 * @param bts
	 * @param alarmContent
	 */
	public void firePowerSupplyAlarm(McBts bts, String alarmContent) {
		McBtsAlarm btsAlarm = this.createPowerSupplyAlarm(bts, alarmContent);
		this.processMcBtsAlarm(btsAlarm);
	}

	/**
	 * 触发基站失连告警恢复
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
	 * 创建基站基站失连告警模型
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
	 * 创建基站基站失连告警模型
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
	 * 网归信息导入失败告警
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
	 * 电源信息告警
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
	 * 转换告警模型
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
		// 设置MoId
		alarmEvent.setMoId(mcBts.getMoId());
		// 设置告警定义Id
		alarmEvent.setAlarmDefId(mcBtsAlarm.getAlarmDefId());
		// 设置告警内容
		alarmEvent.setAlarmContent(mcBtsAlarm.getAlarmContent());
		// 设置告警状态
		alarmEvent.setAlarmState(mcBtsAlarm.getFlag());
		// 设置告警级别
		alarmEvent.setAlarmLevel(mcBtsAlarm.getAlarmLevel());
		// 设置告警实体
		String entityType = this.generateEntityType(mcBtsAlarm);
		alarmEvent.setEntityType(entityType);
		// 设置告警Oid
		String entityOid = this.generateEntityOid(mcBtsAlarm);
		alarmEvent.setEntityOid(entityOid);
		// 设置告警时间(14位长度yyyyMMddhhmmss)
		long eventTime = DateUtils.getBriefTimeFromMillisecondTime(System
				.currentTimeMillis());
		alarmEvent.setEventTime(eventTime);
		return alarmEvent;
	}

	/**
	 * 根据基站告警模型生成告警的实体Oid
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
	 * 根据基站告警模型生成告警的实体类型
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
