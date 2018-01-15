/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.status;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * 
 * ΢վ״̬ģ��
 * 
 * @author chenjunhua
 * 
 */

public class IbtsStatus implements EnbDynamicInfo{

	// ��վʱ��
	private String enbTime;
	
	// ʱ������
	private int clockType;
	
	// ʱ��״̬
	private int clockStatus;
	
	// ��վ�¶�(��)
	private int temperature;
	
	// ������������
	private int visibleSatelliteNum;
	
	// ׷����������
	private int trackSatelliteNum;
	
	// ����ʱ��
	private String runningTime;
	
	// ����״̬
	private int state;
	
	//��Ƶ����״̬ 
    private int rflofreqState;
    
	public int getRflofreqState() {
		return rflofreqState;
	}

	public void setRflofreqState(int rflofreqState) {
		this.rflofreqState = rflofreqState;
	}

	public String getEnbTime() {
		return enbTime;
	}

	public void setEnbTime(String enbTime) {
		this.enbTime = enbTime;
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

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
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

	public String getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "IbtsStatus [enbTime=" + enbTime + ", clockType=" + clockType
				+ ", clockStatus=" + clockStatus + ", temperature="
				+ temperature + ", visibleSatelliteNum=" + visibleSatelliteNum
				+ ", trackSatelliteNum=" + trackSatelliteNum + ", runningTime="
				+ runningTime + ", state=" + state + "]";
	}
	
	

}
