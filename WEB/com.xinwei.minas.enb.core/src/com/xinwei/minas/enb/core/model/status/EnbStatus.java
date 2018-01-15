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
 * eNB状态信息
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbStatus implements Serializable {

	// 基站状态
	private int enbStatus;
	// 基站时间
	private String enbTime;
	// 运行时间
	private String runningTime;
	// 时钟类型
	private int clockType;
	// 时钟状态
	private int clockStatus;
	// 可视卫星数量
	private int visibleSatelliteNum;
	// 追踪卫星数量
	private int trackSatelliteNum;
	// 基站温度(℃)
	private int temperature;
	// 基站即时功率
	private int power;
	// 风扇转速 (RPM)
	private int[] fanSpeeds;
	// 端口实际工作模式 光电模式 0：电口； 1：光口
	private Integer portWorkMode;
	// 速率类型
	private Integer portRate;
	// 双工模式 0：半双工；1：全双工
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
