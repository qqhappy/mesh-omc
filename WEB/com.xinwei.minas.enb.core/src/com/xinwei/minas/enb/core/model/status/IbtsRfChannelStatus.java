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
 * IBTS射频通道状态
 * 
 * @author chenjunhua
 * 
 */

public class IbtsRfChannelStatus implements EnbDynamicInfo {
	
	// 通道号
	private int channelNo;
	
	// 上行天线状态
	private int ulAntStatus;

	// 下行天线状态
	private int dlAntStatus;

	// 通道温度 (℃)
	private int channelTemperature;

	// 发射功率(单位: 0.1dBm)
	private int sendPower;

	// 发射增益(0.5dB)
	private int sendGain;

	// 接收增益(0.5dB)
	private int receiveGain;
	
	// 接收功率(单位: 0.1dBm)
	private int receivePower;
	
	// 通道驻波比(单位: 0.1)
	private int vswr;
	
	// 下行功率读取结果
	private int dlPowerReadResult;
	
	// 上行功率读取结果
	private int ulPowerReadResult;
	
	// 驻波比计算结果
	private int vswrCalResult;
	
	//功率校准结果
	private int powerResult;
	
	//DPD训练结果
	private int  dpdTrainingResult;

	public int getPowerResult() {
		return powerResult;
	}

	public void setPowerResult(int powerResult) {
		this.powerResult = powerResult;
	}

	public int getDpdTrainingResult() {
		return dpdTrainingResult;
	}

	public void setDpdTrainingResult(int dpdTrainingResult) {
		this.dpdTrainingResult = dpdTrainingResult;
	}

	public int getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(int channelNo) {
		this.channelNo = channelNo;
	}

	public int getUlAntStatus() {
		return ulAntStatus;
	}

	public void setUlAntStatus(int ulAntStatus) {
		this.ulAntStatus = ulAntStatus;
	}

	public int getDlAntStatus() {
		return dlAntStatus;
	}

	public void setDlAntStatus(int dlAntStatus) {
		this.dlAntStatus = dlAntStatus;
	}

	public int getChannelTemperature() {
		return channelTemperature;
	}

	public void setChannelTemperature(int channelTemperature) {
		this.channelTemperature = channelTemperature;
	}

	public int getSendPower() {
		return sendPower;
	}

	public void setSendPower(int sendPower) {
		this.sendPower = sendPower;
	}

	public int getSendGain() {
		return sendGain;
	}

	public void setSendGain(int sendGain) {
		this.sendGain = sendGain;
	}

	public int getReceiveGain() {
		return receiveGain;
	}

	public void setReceiveGain(int receiveGain) {
		this.receiveGain = receiveGain;
	}

	public int getReceivePower() {
		return receivePower;
	}

	public void setReceivePower(int receivePower) {
		this.receivePower = receivePower;
	}

	public int getVswr() {
		return vswr;
	}

	public void setVswr(int vswr) {
		this.vswr = vswr;
	}

	public int getDlPowerReadResult() {
		return dlPowerReadResult;
	}

	public void setDlPowerReadResult(int dlPowerReadResult) {
		this.dlPowerReadResult = dlPowerReadResult;
	}

	public int getUlPowerReadResult() {
		return ulPowerReadResult;
	}

	public void setUlPowerReadResult(int ulPowerReadResult) {
		this.ulPowerReadResult = ulPowerReadResult;
	}

	public int getVswrCalResult() {
		return vswrCalResult;
	}

	public void setVswrCalResult(int vswrCalResult) {
		this.vswrCalResult = vswrCalResult;
	}
	
	
	
}
