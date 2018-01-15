package com.xinwei.lte.web.enb.model;

import java.util.Map;

import com.xinwei.minas.enb.core.model.Enb;

/**
 * enb模型，用于action
 * 
 * @author zhangqiang
 * 
 */
public class EnbModel {

	/**
	 * 16进制基站ID
	 */
	private String enbId;

	/**
	 * 是否已激活 0：否 1：是
	 */
	private int isActive;

	/**
	 * 基站告警级别
	 */
	private int alarmLevel;

	/**
	 * 小区状态
	 */
	private Map<String, Integer> cellStatus;

	/**
	 * enb模型
	 */
	private Enb enb = new Enb();

	public String getEnbId() {
		return enbId;
	}

	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	public Enb getEnb() {
		return enb;
	}

	public void setEnb(Enb enb) {
		this.enb = enb;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Map<String, Integer> getCellStatus() {
		return cellStatus;
	}

	public void setCellStatus(Map<String, Integer> cellStatus) {
		this.cellStatus = cellStatus;
	}
}
