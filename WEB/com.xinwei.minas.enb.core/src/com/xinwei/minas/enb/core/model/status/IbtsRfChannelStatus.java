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
 * IBTS��Ƶͨ��״̬
 * 
 * @author chenjunhua
 * 
 */

public class IbtsRfChannelStatus implements EnbDynamicInfo {
	
	// ͨ����
	private int channelNo;
	
	// ��������״̬
	private int ulAntStatus;

	// ��������״̬
	private int dlAntStatus;

	// ͨ���¶� (��)
	private int channelTemperature;

	// ���书��(��λ: 0.1dBm)
	private int sendPower;

	// ��������(0.5dB)
	private int sendGain;

	// ��������(0.5dB)
	private int receiveGain;
	
	// ���չ���(��λ: 0.1dBm)
	private int receivePower;
	
	// ͨ��פ����(��λ: 0.1)
	private int vswr;
	
	// ���й��ʶ�ȡ���
	private int dlPowerReadResult;
	
	// ���й��ʶ�ȡ���
	private int ulPowerReadResult;
	
	// פ���ȼ�����
	private int vswrCalResult;
	
	//����У׼���
	private int powerResult;
	
	//DPDѵ�����
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
