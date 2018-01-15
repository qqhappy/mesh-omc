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
 * Ibts���״̬
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class IbtsOpticalStatus implements EnbDynamicInfo {
	// ��ģ��
	private int moduleNo;
	// "�չ��� ��λ0.1uW
	private int receivePower;
	// ������ ��λ0.1 uW
	private int sendPower;
	// ��λ��Ϣ , 1����λ��0������λ
	private int inPlaceFlag;
	// ��ģ�鳧�� ASCII
	private String manufacture;
	// ��ģ�鴫��bit���� ��λ Mbit/s
	private int transBitRate;
	// �¶� ��λ C���϶�
	private int temperature;
	// ��ѹ ��λ mV
	private int voltage;
	// ���� ��λ mA"
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
