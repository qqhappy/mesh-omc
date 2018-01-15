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
 * ��վ�澯����
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarmHelper {

	public static final Log log = LogFactory.getLog(EnbAlarmHelper.class);

	// ��վ�Զ���澯��ʼID
	public static final int ENB_ALARM_BASE_ID = 0x0f000000;

	// ��վʧ���澯ID
	public static final int BTS_DISCONNECTED_ALARM_ID = ENB_ALARM_BASE_ID + 1;

	public static final int ENB_FULLTABLE_REVERSE_ALARM_ID = ENB_ALARM_BASE_ID + 2;
	// eNB����ͬ��ʧ�ܸ澯
	public static final int ENB_DATA_SYNC_FAILED_ALARM_ID = ENB_ALARM_BASE_ID + 3;
	// eNB��ѧϰʧ�ܸ澯
	public static final int ENB_STUDY_FAILED_ALARM_ID = ENB_ALARM_BASE_ID + 4;
	// eNB�汾�����ݸ澯
	public static final int ENB_VERSION_INCOMPATIBLE_ALARM_ID = ENB_ALARM_BASE_ID + 5;

	private AlarmProcessor alarmEventProcessor;

	private int alarmTimethreshold = 1;

	public EnbAlarmHelper() {
	}

	/**
	 * �����վ�澯
	 * 
	 * @param enbAlarm
	 *            ��վ�澯ģ��
	 */
	public void processEnbAlarm(EnbAlarm enbAlarm) {
		// ��վ�ϱ��澯ʱ�䲻�Ϸ����������ܷ�����ʱ��Ϊ׼
		if (!alarmTimeLegal(enbAlarm.getEventTime())) {
			enbAlarm.setEventTime(DateUtils
					.getBriefTimeFromMillisecondTime(System.currentTimeMillis()));
		}
		// ����վ�澯ģ��ת��Ϊͨ�ø澯ģ��
		AlarmEvent alarmEvent = this.transferAlarm(enbAlarm);
		if (alarmEvent != null) {
			// ���ø澯�¼�����ģ��
			alarmEventProcessor.process(alarmEvent);
		}
	}

	/**
	 * ������վʧ���澯
	 * 
	 * @param enb
	 */
	public void fireBtsDisconnectedAlarm(Enb enb) {
		EnbAlarm alarm = this.createAlarm(enb, BTS_DISCONNECTED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ������վʧ���澯�ָ�
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
	 * �����������澯
	 * 
	 * @param bts
	 */
	public void fireFullTableReverseAlarm(Enb bts, String alarmDesc) {
		EnbAlarm alarm = this.createFullTableReverseAlarm(bts, alarmDesc);
		this.processEnbAlarm(alarm);
	}

	/**
	 * �����������澯�ָ�
	 * 
	 * @param bts
	 */
	public void fireFullTableReverseAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createFullTableReverseAlarmRestored(bts);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ��������ͬ��ʧ�ܸ澯
	 * 
	 * @param bts
	 */
	public void fireDataSyncFailedAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts, ENB_DATA_SYNC_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ��������ͬ��ʧ�ܸ澯�ָ�
	 * 
	 * @param bts
	 */
	public void fireDataSyncFailedAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_DATA_SYNC_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ������ѧϰʧ�ܸ澯
	 * 
	 * @param bts
	 */
	public void fireStudyFailedAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts, ENB_STUDY_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ������ѧϰʧ�ܸ澯�ָ�
	 * 
	 * @param bts
	 */
	public void fireStudyFailedAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_STUDY_FAILED_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ������վ�汾�����ݸ澯
	 * 
	 * @param bts
	 */
	public void fireEnbVersionIncompatibleAlarm(Enb bts) {
		EnbAlarm alarm = this.createAlarm(bts,
				ENB_VERSION_INCOMPATIBLE_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * ������վ�汾�����ݸ澯�ָ�
	 * 
	 * @param bts
	 */
	public void fireEnbVersionIncompatibleAlarmRestored(Enb bts) {
		EnbAlarm alarm = this.createAlarmRestored(bts,
				ENB_VERSION_INCOMPATIBLE_ALARM_ID);
		this.processEnbAlarm(alarm);
	}

	/**
	 * �����������澯
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
	 * �����������ָ��澯
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
	 * ���ݸ澯�봴���Զ���澯ģ��
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
	 * ���ݸ澯�봴���Զ���澯�ָ�ģ��
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
	 * ����澯����ID
	 * 
	 * @param enbAlarm
	 * @return
	 */
	public long calculateAlarmDefId(EnbAlarm enbAlarm) {
		long alarmDefId = 0;
		long enbType = MoTypeDD.ENODEB;
		long alarmCode = enbAlarm.getAlarmCode();
		long alarmSubCode = enbAlarm.getAlarmSubCode();
		// 2���ֽ��豸����+4�ֽڸ澯��+2�ֽڸ澯����
		alarmDefId = (enbType << 48) + (alarmCode << 16) + alarmSubCode;
		return alarmDefId;
	}

	/**
	 * enB�澯ͬ��
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
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(req);
				// ����Ӧ����
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
	 * �ж��Ƿ��������Զ���澯
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
	 * ת���澯ģ��
	 * 
	 * @param mcBtsAlarm
	 * @return
	 */
	private AlarmEvent transferAlarm(EnbAlarm enbAlarm) {
		AlarmEvent alarmEvent = new AlarmEvent();
		// ����澯����ID
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
		// �澯������ʾ�Ļ�վIDΪ16������
		// String entityOid = String.valueOf(enbId);
		String entityOid = enb.getHexEnbId();
		// ���ʻ�
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
	 * ��entityType���й��ʻ�
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
	 * �жϻ�վ�ϱ��澯��ʱ���Ƿ����<br/>
	 * ���ʱ�䳬�����ܷ�������ǰʱ������alarmTimethreshold��ķ�Χ����Ϊ������
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
	 * ����eNB��վ�澯�ϱ�����ʱ�䷶Χ����λ�죬Ĭ��1��
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
