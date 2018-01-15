/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.core.model.alarm.AlarmDef;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.core.alarm.cache.AlarmDefCache;
import com.xinwei.minas.server.core.alarm.model.AlarmEvent;
import com.xinwei.minas.server.core.alarm.processor.AlarmProcessor;
import com.xinwei.minas.server.enb.model.message.EnbAlarm;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站告警助手
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarmHelper {

	public static final Log log = LogFactory.getLog(EnbAlarmHelper.class);

	// 基站自定义告警起始ID
	public static final int ENB_ALARM_BASE_ID = 0x0f000000;

	// 基站失连告警ID
	public static final int BTS_DISCONNECTED_ALARM_ID = ENB_ALARM_BASE_ID + 1;

	public static final int ENB_FULLTABLE_REVERSE_ALARM_ID = ENB_ALARM_BASE_ID + 2;
	// eNB数据同步失败告警
	public static final int ENB_DATA_SYNC_FAILED_ALARM_ID = ENB_ALARM_BASE_ID + 3;
	// eNB自学习失败告警
	public static final int ENB_STUDY_FAILED_ALARM_ID = ENB_ALARM_BASE_ID + 4;
	// eNB版本不兼容告警
	public static final int ENB_VERSION_INCOMPATIBLE_ALARM_ID = ENB_ALARM_BASE_ID + 5;

	private AlarmProcessor alarmEventProcessor;

	private int alarmTimethreshold = 1;

	public EnbAlarmHelper() {
	}

	/**
	 * 处理基站告警
	 * 
	 * @param enbAlarm
	 *            基站告警模型
	 */
	public void processEnbAlarm(EnbAlarm enbAlarm) {
		// 基站上报告警时间不合法，则以网管服务器时间为准
		if (!alarmTimeLegal(enbAlarm.getEventTime())) {
			enbAlarm.setEventTime(DateUtils
					.getBriefTimeFromMillisecondTime(System.currentTimeMillis()));
		}
		// 将基站告警模型转换为通用告警模型
		AlarmEvent alarmEvent = this.transferAlarm(enbAlarm);
		if (alarmEvent != null) {
			// 调用告警事件处理模块
			alarmEventProcessor.process(alarmEvent);
		}
	}

	/**
	 * 触发基站失连告警
	 * 
	 * @param enb
	 */
	public void fireBtsDisconnectedAlarm(Enb enb) {
		EnbAlarm alarm = this.createAlarm(enb, BTS_DISCONNECTED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发基站失连告警恢复
	 * 
	 * @param enb
	 */
	public void fireBtsDisconnectedAlarmRestored(Enb enb) {
		EnbAlarm alarm = this.createAlarmRestored(enb,
				BTS_DISCONNECTED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	public void setAlarmEventProcessor(AlarmProcessor alarmEventProcessor) {
		this.alarmEventProcessor = alarmEventProcessor;
	}

	/**
	 * 触发整表反构告警
	 * 
	 * @param bts
	 */
	public void fireFullTableReverseAlarm(Enb bts, String alarmDesc) {
		EnbAlarm alarm = this.createFullTableReverseAlarm(bts, alarmDesc);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发整表反构告警恢复
	 * 
	 * @param bts
	 */
	public void fireFullTableReverseAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createFullTableReverseAlarmRestored(bts);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发数据同步失败告警
	 * 
	 * @param bts
	 */
	public void fireDataSyncFailedAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts, ENB_DATA_SYNC_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发数据同步失败告警恢复
	 * 
	 * @param bts
	 */
	public void fireDataSyncFailedAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_DATA_SYNC_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发自学习失败告警
	 * 
	 * @param bts
	 */
	public void fireStudyFailedAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts, ENB_STUDY_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发自学习失败告警恢复
	 * 
	 * @param bts
	 */
	public void fireStudyFailedAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_STUDY_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发基站版本不兼容告警
	 * 
	 * @param bts
	 */
	public void fireEnbVersionIncompatibleAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts,
				ENB_VERSION_INCOMPATIBLE_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 触发基站版本不兼容告警恢复
	 * 
	 * @param bts
	 */
	public void fireEnbVersionIncompatibleAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_VERSION_INCOMPATIBLE_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * 创建整表反构告警
	 * 
	 * @param enb
	 * @param alarmDesc
	 * @return
	 */
	private EnbAlarm createFullTableReverseAlarm(Enb enb, String alarmDesc) {
		EnbAlarm enbAlarm = new EnbAlarm(enb.getEnbId());
		enbAlarm.setAlarmCode(ENB_FULLTABLE_REVERSE_ALARM_ID);
		enbAlarm.setAlarmSubCode(0);
		enbAlarm.setFlag(EnbAlarm.FLAG_ALARM);
		enbAlarm.setEntityType("");
		enbAlarm.setEntityOid("");
		enbAlarm.setAlarmDesc(alarmDesc);
		return enbAlarm;
	}

	/**
	 * 创建整表反构恢复告警
	 * 
	 * @param bts
	 * @return
	 */
	private EnbAlarm createFullTableReverseAlarmRestored(Enb enb) {
		EnbAlarm enbAlarm = createFullTableReverseAlarm(enb, "");
		enbAlarm.setFlag(EnbAlarm.FLAG_RESTORED);
		return enbAlarm;
	}

	/**
	 * 根据告警码创建自定义告警模型
	 * 
	 * @param enb
	 * @param alarmCode
	 * @return
	 */
	private EnbAlarm createAlarm(Enb enb, int alarmCode) {
		EnbAlarm enbAlarm = new EnbAlarm(enb.getEnbId());
		enbAlarm.setAlarmCode(alarmCode);
		enbAlarm.setAlarmSubCode(0);
		enbAlarm.setFlag(EnbAlarm.FLAG_ALARM);
		enbAlarm.setEntityType("");
		enbAlarm.setEntityOid("");
		return enbAlarm;
	}

	/**
	 * 根据告警码创建自定义告警恢复模型
	 * 
	 * @param enb
	 * @param alarmCode
	 * @return
	 */
	private EnbAlarm createAlarmRestored(Enb enb, int alarmCode) {
		EnbAlarm enbAlarm = createAlarm(enb, alarmCode);
		enbAlarm.setFlag(EnbAlarm.FLAG_RESTORED);
		return enbAlarm;
	}

	/**
	 * 计算告警定义ID
	 * 
	 * @param enbAlarm
	 * @return
	 */
	public long calculateAlarmDefId(EnbAlarm enbAlarm) {
		long alarmDefId = 0;
		long enbType = MoTypeDD.ENODEB;
		long alarmCode = enbAlarm.getAlarmCode();
		long alarmSubCode = enbAlarm.getAlarmSubCode();
		// 2个字节设备类型+4字节告警码+2字节告警子码
		alarmDefId = (enbType << 48) + (alarmCode << 16) + alarmSubCode;
		return alarmDefId;
	}

	/**
	 * enB告警同步
	 * 
	 * @param enbId
	 * @throws Exception
	 */
	public void syncEnbAlarm(Long enbId) throws Exception {
		EnbAppMessage req = new EnbAppMessage();
		req.setEnbId(enbId);
		req.setMa(EnbMessageConstants.MA_ALARM);
		req.setMoc(EnbMessageConstants.MOC_ALARM_SYNC);
		req.setActionType(EnbMessageConstants.ACTION_OTHERS);
		req.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		EnbConnector enbConnector = getEnbConnector();
		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(req);
				// 解析应答结果
				EnbMessageHelper.parseResponse(resp);
			}
		} catch (TimeoutException e) {
			log.error("send eNB alarm sync request timeout.");
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("send eNB alarm sync request failed.", e);
			throw new Exception(OmpAppContext.getMessage("sync_alarm_failed")
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 判断是否是网管自定义告警
	 * 
	 * @param alarmDefId
	 * @return
	 */
	public boolean isCustomizedAlarm(long alarmDefId) {
		long alarmCode = (alarmDefId - (((long) MoTypeDD.ENODEB) << 48)) >> 16;
		if (alarmCode / ENB_ALARM_BASE_ID == 1)
			return true;
		return false;
	}

	private EnbConnector getEnbConnector() {
		return OmpAppContext.getCtx().getBean(EnbConnector.class);
	}

	/**
	 * 转换告警模型
	 * 
	 * @param mcBtsAlarm
	 * @return
	 */
	private AlarmEvent transferAlarm(EnbAlarm enbAlarm) {
		AlarmEvent alarmEvent = new AlarmEvent();
		// 计算告警定义ID
		long alarmDefId = calculateAlarmDefId(enbAlarm);
		AlarmDef alarmDef = AlarmDefCache.getInstance().get(alarmDefId);
		if (alarmDef == null) {
			log.error("unknown enb alarm, alarmDefId=" + alarmDefId);
			return null;
		}
		Long enbId = enbAlarm.getEnbId();
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb == null) {
			log.error("enb does not exist, enbId=0x"
					+ StringUtils.to8HexString(enbId));
			return null;
		}
		alarmEvent.setMoId(enb.getMoId());
		alarmEvent.setAlarmDefId(alarmDefId);
		alarmEvent.setEventTime(enbAlarm.getEventTime());
		String alarmContent = alarmDef.getAlarmName();
		if (!"".equals(enbAlarm.getAlarmDesc())) {
			alarmContent += ":" + enbAlarm.getAlarmDesc();
		}
		alarmEvent.setAlarmContent(alarmContent);
		alarmEvent.setAlarmLevel(alarmDef.getAlarmLevel());
		alarmEvent.setAlarmState(enbAlarm.getFlag());
		String entityType = "eNB";
		// 告警定义显示的基站ID为16进制数
		// String entityOid = String.valueOf(enbId);
		String entityOid = enb.getHexEnbId();
		// 国际化
		String type = convertEntityType(enbAlarm.getEntityType());
		if (!type.equals("")) {
			entityType += EnbAlarm.DOT + type;
		}
		if (!enbAlarm.getEntityOid().equals("")) {
			entityOid += EnbAlarm.DOT + enbAlarm.getEntityOid();
		}
		alarmEvent.setEntityType(entityType);
		alarmEvent.setEntityOid(entityOid);

		return alarmEvent;
	}

	/**
	 * 对entityType进行国际化
	 * 
	 * @param entityType
	 * @return
	 */
	private String convertEntityType(String entityType) {
		if (entityType.equals("")) {
			return entityType;
		}
		String str = "";
		String[] typeArray = entityType.split("\\" + EnbAlarm.DOT);
		for (int i = 0; i < typeArray.length; i++) {
			str += OmpAppContext.getMessage("LOC_TYPE_" + typeArray[i]);
			if (i != (typeArray.length - 1)) {
				str += EnbAlarm.DOT;
			}
		}
		return str;
	}

	/**
	 * 判断基站上报告警的时间是否合理<br/>
	 * 如果时间超过网管服务器当前时间左右alarmTimethreshold天的范围，则为不合理
	 * 
	 * @param alarmTime
	 * @return
	 */
	private boolean alarmTimeLegal(long alarmTime) {
		alarmTime = DateUtils.getMillisecondTimeFromBriefTime(alarmTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(alarmTime);
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(System.currentTimeMillis());
		current.add(Calendar.DAY_OF_MONTH, -alarmTimethreshold);
		if (calendar.compareTo(current) < 0)
			return false;
		current.add(Calendar.DAY_OF_MONTH, 2 * alarmTimethreshold);
		if (calendar.compareTo(current) > 0)
			return false;
		return true;
	}

	/**
	 * 设置eNB基站告警上报合理时间范围，单位天，默认1天
	 * 
	 * @param alarmTimethreshold
	 */
	public void setAlarmTimethreshold(int alarmTimethreshold) {
		this.alarmTimethreshold = alarmTimethreshold;
	}

	public int getAlarmTimethreshold() {
		return alarmTimethreshold;
	}

	public static void main(String[] args) {
		long alarmDefId = 0;
		long enbType = 2000;
		long alarmCode = 11;
		long alarmSubCode = 0;
		long a = enbType << 48;
		long b = alarmCode << 16;
		alarmDefId = a + b + alarmSubCode;
		System.out.println(alarmDefId);
	}

}
