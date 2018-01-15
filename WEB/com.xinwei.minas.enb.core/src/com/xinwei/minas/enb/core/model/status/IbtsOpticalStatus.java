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

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * 
 * Ibts光口状态
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class IbtsOpticalStatus implements EnbDynamicInfo {
	// 光模块
	private int moduleNo;
	// "收功率 单位0.1uW
	private int receivePower;
	// 发功率 单位0.1 uW
	private int sendPower;
	// 在位信息 , 1：在位；0：不在位
	private int inPlaceFlag;
	// 光模块厂商 ASCII
	private String manufacture;
	// 光模块传输bit速率 单位 Mbit/s
	private int transBitRate;
	// 温度 单位 C摄氏度
	private int temperature;
	// 电压 单位 mV
	private int voltage;
	// 电流 单位 mA"
	private int current;


	public int getModuleNo() {
		return moduleNo;
	}

	public void setModuleNo(int moduleNo) {
		this.moduleNo = moduleNo;
	}

	public int getReceivePower() {
		return receivePower;
	}

	public void setReceivePower(int receivePower) {
		this.receivePower = receivePower;
	}

	public int getSendPower() {
		return sendPower;
	}

	public void setSendPower(int sendPower) {
		this.sendPower = sendPower;
	}

	public int getInPlaceFlag() {
		return inPlaceFlag;
	}

	public void setInPlaceFlag(int inPlaceFlag) {
		this.inPlaceFlag = inPlaceFlag;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public int getTransBitRate() {
		return transBitRate;
	}

	public void setTransBitRate(int transBitRate) {
		this.transBitRate = transBitRate;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

}
