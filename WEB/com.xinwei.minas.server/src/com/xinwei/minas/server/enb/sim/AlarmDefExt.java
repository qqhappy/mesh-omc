package com.xinwei.minas.server.enb.sim;

import com.xinwei.minas.core.model.alarm.AlarmDef;

public class AlarmDefExt {

	// ��Ԫ����
	private String moType;
	
	// �澯����Id
	private String alarmDefId;

	// �澯����
	private String alarmLevel;

	// ���ĸ澯����
	private String alarmNameZh;
	
	// Ӣ�ĸ澯����
	private String alarmNameEn;

	public String getMoType() {
		return moType;
	}

	public void setMoType(String moType) {
		this.moType = moType;
	}

	public String getAlarmDefId() {
		return alarmDefId;
	}

	public void setAlarmDefId(String alarmDefId) {
		this.alarmDefId = alarmDefId;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmNameZh() {
		return alarmNameZh;
	}

	public void setAlarmNameZh(String alarmNameZh) {
		this.alarmNameZh = alarmNameZh;
	}

	public String getAlarmNameEn() {
		return alarmNameEn;
	}

	public void setAlarmNameEn(String alarmNameEn) {
		this.alarmNameEn = alarmNameEn;
	}

	
	
}
