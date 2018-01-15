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
 * 微站状态模型
 * 
 * @author chenjunhua
 * 
 */

public class IbtsStatus implements EnbDynamicInfo{

	// 基站时间
	private String enbTime;
	
	// 时钟类型
	private int clockType;
	
	// 时钟状态
	private int clockStatus;
	
	// 基站温度(℃)
	private int temperature;
	
	// 可视卫星数量
	private int visibleSatelliteNum;
	
	// 追踪卫星数量
	private int trackSatelliteNum;
	
	// 运行时间
	private String runningTime;
	
	// 运行状态
	private int state;
	
	//射频本振状态 
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
