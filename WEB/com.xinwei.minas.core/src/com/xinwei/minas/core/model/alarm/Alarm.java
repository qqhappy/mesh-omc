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
 * �澯ģ��
 * 
 * @author chenjunhua
 * 
 */

public class Alarm implements java.io.Serializable {

	// ��¼ID
	private long id;

	// MO���
	private long moId;

	// MO����
	private String moName = "";

	// ʵ������
	private String entityType = "";

	// ʵ��OID
	private String entityOid = "";

	// �澯����Id
	private long alarmDefId;

	// �澯����
	private String alarmContent;

	// �澯����
	private int alarmLevel;

	// �澯״̬: 0-�ָ�; 1-�澯
	private int alarmState;

	// �״θ澯����ʱ��(14λ����yyyyMMddhhmmss)
	private long firstAlarmTime;

	// ĩ�θ澯����ʱ��(14λ����yyyyMMddhhmmss)
	private long lastAlarmTime;

	// �澯����
	private long alarmTimes;

	// �ָ�ʱ��
	private long restoredTime;

	// �ָ��û�
	private String restoreUser;

	// �ָ���ʶ: 0-ϵͳ�ָ�; 1-�ֹ��ָ�
	private int restoreFlag;

	// �澯����: 0-�����澯; 1-���ϸ澯
	private int alarmType;

	// ȷ��״̬ : 0-δȷ��; 1-��ȷ��;
	private int confirmState;

	// ȷ���û�
	private String confirmUser;

	// ȷ��ʱ��(14λ����yyyyMMddhhmmss)
	private long confirmTime;

	/**
	 * ��ȡ��λ��Ϣ
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
				// �Ѽܿ�ۺ�Ϊ����
				// �����1λ��1 ��111ΪBBU
				if (oid[index].equals("1")) {
					// TODO: ����������Ҫ�����ʻ�
					strBuffer.append("����(").append("BBU)").append("-");
				}
				// �����1λ����1����X11��ʽ����ΪRRU+��X-1��
				else {
					strBuffer.append("����(").append("RRU");
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
	 * ��ȡ΢վ��λ��Ϣ
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
				// �Ѽܿ�ۺ�Ϊ����
				// �����1λ��1 ��111ΪBBU
				if (oid[index].equals("1")) {
					// TODO: ����������Ҫ�����ʻ�
					strBuffer.append("MDB").append("-");
				}
				// �����1λ����1����X11��ʽ����ΪRRU+��X-1��
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
	 * �ж��Ƿ��Ǹ澯
	 * 
	 * @return
	 */
	public boolean isAlarm() {
		return alarmState == AlarmConstants.ALARM;
	}

	/**
	 * �ж��Ƿ��Ǹ澯�ָ�
	 * 
	 * @return
	 */
	public boolean isRestored() {
		return alarmState == AlarmConstants.RESTORED;
	}

	/**
	 * �жϸ澯�Ƿ��ѱ�ȷ��
	 * 
	 * @return
	 */
	public boolean isConfirmed() {
		return confirmState == AlarmConstants.CONFIRMED;
	}

	/**
	 * ���Ӹ澯����
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
