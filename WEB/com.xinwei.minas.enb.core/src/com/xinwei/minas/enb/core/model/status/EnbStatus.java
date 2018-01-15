/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

/**
 * 
 * eNB״̬��Ϣ
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatus implements Serializable {

	// ��վ״̬
	private int enbStatus;
	// ��վʱ��
	private String enbTime;
	// ����ʱ��
	private String runningTime;
	// ʱ������
	private int clockType;
	// ʱ��״̬
	private int clockStatus;
	// ������������
	private int visibleSatelliteNum;
	// ׷����������
	private int trackSatelliteNum;
	// ��վ�¶�(��)
	private int temperature;
	// ��վ��ʱ����
	private int power;
	// ����ת�� (RPM)
	private int[] fanSpeeds;
	// �˿�ʵ�ʹ���ģʽ ���ģʽ 0����ڣ� 1�����
	private Integer portWorkMode;
	// ��������
	private Integer portRate;
	// ˫��ģʽ 0����˫����1��ȫ˫��
	private Integer portDuplexMode;

	public int getEnbStatus() {
		return enbStatus;
	}

	public void setEnbStatus(int enbStatus) {
		this.enbStatus = enbStatus;
	}

	public String getEnbTime() {
		return enbTime;
	}

	public void setEnbTime(String enbTime) {
		this.enbTime = enbTime;
	}

	public String getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public int getClockType() {
		return clockType;
	}

	public void setClockType(int clockType) {
		this.clockType = clockType;
	}

	public int getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(int clockStatus) {
		this.clockStatus = clockStatus;
	}

	public int getVisibleSatelliteNum() {
		return visibleSatelliteNum;
	}

	public void setVisibleSatelliteNum(int visibleSatelliteNum) {
		this.visibleSatelliteNum = visibleSatelliteNum;
	}

	public int getTrackSatelliteNum() {
		return trackSatelliteNum;
	}

	public void setTrackSatelliteNum(int trackSatelliteNum) {
		this.trackSatelliteNum = trackSatelliteNum;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPower() {
		return power;
	}

	public int[] getFanSpeeds() {
		return fanSpeeds;
	}

	public void setFanSpeeds(int[] fanSpeeds) {
		this.fanSpeeds = fanSpeeds;
	}

	public Integer getPortWorkMode() {
		return portWorkMode;
	}

	public void setPortWorkMode(Integer portWorkMode) {
		this.portWorkMode = portWorkMode;
	}

	public Integer getPortRate() {
		return portRate;
	}

	public void setPortRate(Integer portRate) {
		this.portRate = portRate;
	}

	public Integer getPortDuplexMode() {
		return portDuplexMode;
	}

	public void setPortDuplexMode(Integer portDuplexMode) {
		this.portDuplexMode = portDuplexMode;
	}

}
