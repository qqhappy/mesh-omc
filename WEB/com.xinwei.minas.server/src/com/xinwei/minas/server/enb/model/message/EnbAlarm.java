/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.model.message;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * eNB�澯ģ��
 * 
 * @author chenjunhua
 * 
 */

public class EnbAlarm {

	// �ָ���
	public static final String DOT = ".";

	// �澯
	public static final int FLAG_ALARM = 1;
	// �ָ�
	public static final int FLAG_RESTORED = 0;

	// ����
	public static final int LOC_TYPE_RACK = 0;
	// ����
	public static final int LOC_TYPE_SHELF = 1;
	// ��λ
	public static final int LOC_TYPE_BOARD = 2;
	// С��
	public static final int LOC_TYPE_CELL = 3;
	// ����
	public static final int LOC_TYPE_ASSO = 4;
	// ������
	public static final int LOC_TYPE_PHY_SPF_PORT = 5;
	// RRUͨ��
	public static final int LOC_TYPE_RRU_CHAN = 6;
	// RRU�忨
	public static final int LOC_TYPE_RRU_S_BOARD = 7;
	// оƬ��
	public static final int LOC_TYPE_CHIP = 8;

	// eNB ID
	private long enbId;

	// �澯��
	private long alarmCode;

	// �澯����
	private int alarmSubCode;

	// �澯��ʾ: 0-�澯�ָ�; 1-�澯����
	private int flag;

	// ʵ������(��.�ָ�)
	private String entityType = "";

	// ʵ��OID(��.�ָ�)
	private String entityOid = "";

	// �¼�ʱ��(14λ����yyyyMMddhhmmss)
	private long eventTime;

	// �澯��������(���Ǹ澯����)
	private String alarmDesc = "";

	public EnbAlarm(Long enbId) {
		this.enbId = enbId;
		eventTime = DateUtils.getBriefTimeFromMillisecondTime(System
				.currentTimeMillis());
	}

	public EnbAlarm(EnbAppMessage message) {
		this(message.getEnbId());
		this.alarmCode = message.getLongValue(TagConst.ALARM_DEF_CODE);
		this.alarmSubCode = message.getIntValue(TagConst.ALARM_DEF_SUB_CODE);
		this.flag = message.getIntValue(TagConst.ALARM_FLAG);
		// ������λ��Ϣ
		byte[] locTypeBytes = message.getByteValue(TagConst.ALARM_LOC_TYPE);
		byte[] locIdBytes = message.getByteValue(TagConst.ALARM_LOC_ID);
		this.parseLocationType(locTypeBytes);
		this.parseLocationId(locIdBytes);
		this.eventTime = message.getDateTimeValue(TagConst.ALARM_TIME);
	}

	/**
	 * ����λ������
	 * 
	 * @param buf
	 */
	private void parseLocationType(byte[] buf) {
		if (buf == null) {
			return;
		}
		// �������ͳ���
		int len = 2;
		int size = buf.length / len;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			int locType = ByteUtils.toInt(buf, offset, len);
			switch (locType) {
			case LOC_TYPE_RACK:
				entityType += "RACK";
				break;
			case LOC_TYPE_SHELF:
				entityType += "SHELF";
				break;
			case LOC_TYPE_BOARD:
				entityType += "BOARD";
				break;
			case LOC_TYPE_CELL:
				entityType += "CELL";
				break;
			case LOC_TYPE_ASSO:
				entityType += "ASSO_ID";
				break;
			case LOC_TYPE_PHY_SPF_PORT:
				entityType += "PHY_SPF_PORT";
				break;
			case LOC_TYPE_RRU_CHAN:
				entityType += "RRU_CHAN";
				break;
			case LOC_TYPE_RRU_S_BOARD:
				entityType += "RRU_S_BOARD";
				break;
			case LOC_TYPE_CHIP:
				entityType += "CHIP";
				break;
			default:
				entityType += "Unknown";
				break;
			}
			if (i != (size - 1)) {
				entityType += DOT;
			}
			offset += len;
		}
	}

	/**
	 * ����λ��ID
	 * 
	 * @param buf
	 */
	private void parseLocationId(byte[] buf) {
		if (buf == null) {
			return;
		}
		int len = 2;
		int size = buf.length / len;
		int offset = 0;
		for (int i = 0; i < size; i++) {
			int locId = ByteUtils.toInt(buf, offset, len);
			entityOid += locId;
			if (i != (size - 1)) {
				entityOid += DOT;
			}
			offset += len;
		}
	}

	/**
	 * �ж��Ƿ��Ǹ澯��Ϣ
	 * 
	 * @return
	 */
	public boolean isAlarm() {
		return flag > 0;
	}

	/**
	 * �ж��Ƿ��ǻָ���Ϣ
	 * 
	 * @return
	 */
	public boolean isRestored() {
		return flag == 0;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public long getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(long alarmCode) {
		this.alarmCode = alarmCode;
	}

	public int getAlarmSubCode() {
		return alarmSubCode;
	}

	public void setAlarmSubCode(int alarmSubCode) {
		this.alarmSubCode = alarmSubCode;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getEntityOid() {
		if (entityOid == null) {
			return "";
		}
		return entityOid;
	}

	public void setEntityOid(String entityOid) {
		this.entityOid = entityOid;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public String getEntityType() {
		if (entityType == null) {
			return "";
		}
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getAlarmDesc() {
		if (alarmDesc == null) {
			return "";
		}
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

}
