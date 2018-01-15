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
 * 告警事件模型
 * 
 */
public class AlarmEvent {
	
	// MO编号
	private long moId;

	// 实体类型
	private String entityType;

	// 实体OID
	private String entityOid;

	// 告警定义Id
	private long alarmDefId;

	// 告警内容
	private String alarmContent;

	// 告警级别
	private int alarmLevel;

	// 告警状态 (0-恢复; 1-告警)
	private int alarmState;

	// 告警事件发生时间(14位长度yyyyMMddhhmmss)
	private long eventTime;

	
	/**
	 * 判断是否是告警恢复事件
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
