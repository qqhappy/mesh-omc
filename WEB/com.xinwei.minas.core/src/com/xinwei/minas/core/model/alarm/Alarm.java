/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

/**
 * 
 * 告警模型
 * 
 * @author chenjunhua
 * 
 */

public class Alarm implements java.io.Serializable {

	// 记录ID
	private long id;

	// MO编号
	private long moId;

	// MO名称
	private String moName = "";

	// 实体类型
	private String entityType = "";

	// 实体OID
	private String entityOid = "";

	// 告警定义Id
	private long alarmDefId;

	// 告警内容
	private String alarmContent;

	// 告警级别
	private int alarmLevel;

	// 告警状态: 0-恢复; 1-告警
	private int alarmState;

	// 首次告警发生时间(14位长度yyyyMMddhhmmss)
	private long firstAlarmTime;

	// 末次告警发生时间(14位长度yyyyMMddhhmmss)
	private long lastAlarmTime;

	// 告警次数
	private long alarmTimes;

	// 恢复时间
	private long restoredTime;

	// 恢复用户
	private String restoreUser;

	// 恢复标识: 0-系统恢复; 1-手工恢复
	private int restoreFlag;

	// 告警类型: 0-正常告警; 1-闪断告警
	private int alarmType;

	// 确认状态 : 0-未确认; 1-已确认;
	private int confirmState;

	// 确认用户
	private String confirmUser;

	// 确认时间(14位长度yyyyMMddhhmmss)
	private long confirmTime;

	/**
	 * 获取定位信息
	 * 
	 * @return
	 */
	public String getLocation() {
		if (getEntityType() == null || getEntityType().equals("")) {
			return "";
		}
		if (getEntityOid() == null || getEntityOid().equals("")) {
			return "";
		}
		String entityType = getEntityType();
		String entityOid = getEntityOid();
		String[] type = entityType.split("\\.");
		String[] oid = entityOid.split("\\.");
		StringBuffer strBuffer = new StringBuffer();

		int index = 0;
		while (index < type.length) {
			if (type.length > 2 && index == 1) {
				// 把架框槽合为单板
				// 如果第1位是1 即111为BBU
				if (oid[index].equals("1")) {
					// TODO: 单板中文需要做国际化
					strBuffer.append("单板(").append("BBU)").append("-");
				}
				// 如果第1位不是1，即X11形式，改为RRU+（X-1）
				else {
					strBuffer.append("单板(").append("RRU");
					int oidint = Integer.parseInt(oid[index]) - 1;
					strBuffer.append(oidint).append(")").append("-");
				}
				index += 3;
				continue;
			}
			strBuffer.append(type[index]).append("(").append(oid[index])
					.append(")").append("-");
			index++;
		}
		return strBuffer.substring(0, strBuffer.length() - 1);
	}

	/**
	 * 获取微站定位信息
	 * 
	 * @return
	 */
	public String getTinyLocation() {
		if (getEntityType() == null || getEntityType().equals("")) {
			return "";
		}
		if (getEntityOid() == null || getEntityOid().equals("")) {
			return "";
		}
		String entityType = getEntityType();
		String entityOid = getEntityOid();
		String[] type = entityType.split("\\.");
		String[] oid = entityOid.split("\\.");
		StringBuffer strBuffer = new StringBuffer();

		int index = 0;
		while (index < type.length) {
			if (type.length > 2 && index == 1) {
				// 把架框槽合为单板
				// 如果第1位是1 即111为BBU
				if (oid[index].equals("1")) {
					// TODO: 单板中文需要做国际化
					strBuffer.append("MDB").append("-");
				}
				// 如果第1位不是1，即X11形式，改为RRU+（X-1）
				else {
					strBuffer.append("PAU").append("-");
				}
				index += 3;
				continue;
			}
			strBuffer.append(type[index]).append("(").append(oid[index])
					.append(")").append("-");
			index++;
		}
		return strBuffer.substring(0, strBuffer.length() - 1);
	}
	
	
	/**
	 * 判断是否是告警
	 * 
	 * @return
	 */
	public boolean isAlarm() {
		return alarmState == AlarmConstants.ALARM;
	}

	/**
	 * 判断是否是告警恢复
	 * 
	 * @return
	 */
	public boolean isRestored() {
		return alarmState == AlarmConstants.RESTORED;
	}

	/**
	 * 判断告警是否已被确认
	 * 
	 * @return
	 */
	public boolean isConfirmed() {
		return confirmState == AlarmConstants.CONFIRMED;
	}

	/**
	 * 增加告警次数
	 */
	public void increaseAlarmTimes() {
		alarmTimes++;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getMoName() {
		return moName;
	}

	public void setMoName(String moName) {
		this.moName = moName;
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

	public long getFirstAlarmTime() {
		return firstAlarmTime;
	}

	public void setFirstAlarmTime(long firstAlarmTime) {
		this.firstAlarmTime = firstAlarmTime;
	}

	public long getLastAlarmTime() {
		return lastAlarmTime;
	}

	public void setLastAlarmTime(long lastAlarmTime) {
		this.lastAlarmTime = lastAlarmTime;
	}

	public long getAlarmTimes() {
		return alarmTimes;
	}

	public void setAlarmTimes(long alarmTimes) {
		this.alarmTimes = alarmTimes;
	}

	public long getRestoredTime() {
		return restoredTime;
	}

	public void setRestoredTime(long restoredTime) {
		this.restoredTime = restoredTime;
	}

	public int getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

	public int getConfirmState() {
		return confirmState;
	}

	public void setConfirmState(int confirmState) {
		this.confirmState = confirmState;
	}

	public String getConfirmUser() {
		if (confirmUser == null) {
			return "";
		}
		return confirmUser;
	}

	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public int getRestoreFlag() {
		return restoreFlag;
	}

	public void setRestoreFlag(int restoreFlag) {
		this.restoreFlag = restoreFlag;
	}

	public String getRestoreUser() {
		if (restoreUser == null) {
			return "";
		}
		return restoreUser;
	}

	public void setRestoreUser(String restoreUser) {
		this.restoreUser = restoreUser;
	}

}
