package com.xinwei.lte.web.enb.model;

import com.xinwei.minas.core.model.alarm.Alarm;

/**
 * 告警模型，用于页面传参
 * 
 * @author zhangqiang
 * 
 */
public class AlarmModel {

	/**
	 * 后台告警模型
	 */
	private Alarm alarm;

	/**
	 * 网元名称
	 */
	private String enbName;

	/**
	 * 告警发生时间
	 */
	private String firstAlarmTimeString;

	/**
	 * 告警恢复时间
	 */
	private String restoredTimeString;

	/**
	 * 告警确认时间
	 */
	private String confirmTimeString;

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public String getEnbName() {
		return enbName;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	public String getFirstAlarmTimeString() {
		return firstAlarmTimeString;
	}

	public void setFirstAlarmTimeString(String firstAlarmTimeString) {
		this.firstAlarmTimeString = firstAlarmTimeString;
	}

	public String getRestoredTimeString() {
		return restoredTimeString;
	}

	public void setRestoredTimeString(String restoredTimeString) {
		this.restoredTimeString = restoredTimeString;
	}

	public String getConfirmTimeString() {
		return confirmTimeString;
	}

	public void setConfirmTimeString(String confirmTimeString) {
		this.confirmTimeString = confirmTimeString;
	}
}
