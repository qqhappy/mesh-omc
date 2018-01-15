/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.alarm.model;


/**
 * 
 * �澯�¼�ģ��
 * 
 */
public class AlarmEvent {
	
	// MO���
	private long moId;

	// ʵ������
	private String entityType;

	// ʵ��OID
	private String entityOid;

	// �澯����Id
	private long alarmDefId;

	// �澯����
	private String alarmContent;

	// �澯����
	private int alarmLevel;

	// �澯״̬ (0-�ָ�; 1-�澯)
	private int alarmState;

	// �澯�¼�����ʱ��(14λ����yyyyMMddhhmmss)
	private long eventTime;

	
	/**
	 * �ж��Ƿ��Ǹ澯�ָ��¼�
	 * @return
	 */
	public boolean isRestored() {
		return alarmState == 0;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityOid() {
		return entityOid;
	}

	public void setEntityOid(String entityOid) {
		this.entityOid = entityOid;
	}

	public long getAlarmDefId() {
		return alarmDefId;
	}

	public void setAlarmDefId(long alarmDefId) {
		this.alarmDefId = alarmDefId;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public int getAlarmState() {
		return alarmState;
	}

	public void setAlarmState(int alarmState) {
		this.alarmState = alarmState;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	
	
}
