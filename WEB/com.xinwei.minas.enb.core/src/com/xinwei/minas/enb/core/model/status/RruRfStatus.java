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
 * RRU��Ƶ״̬
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class RruRfStatus implements Serializable {

	private int rackNo;

	private int shelfNo;

	private int slotNo;
	// ѡ��ͨ�� : ͨ��1 ͨ��2 ͨ��3 ͨ��4 ȫ��
	private int channelNo;
	// ��������״̬
	private int dlAntStatus;
	// ��������״̬
	private int ulAntStatus;
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

	public int getRackNo() {
		return rackNo;
	}

	public void setRackNo(int rackNo) {
		this.rackNo = rackNo;
	}

	public int getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(int shelfNo) {
		this.shelfNo = shelfNo;
	}

	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	public int getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(int channelNo) {
		this.channelNo = channelNo;
	}

	public int getDlAntStatus() {
		return dlAntStatus;
	}

	public void setDlAntStatus(int dlAntStatus) {
		this.dlAntStatus = dlAntStatus;
	}

	public int getUlAntStatus() {
		return ulAntStatus;
	}

	public void setUlAntStatus(int ulAntStatus) {
		this.ulAntStatus = ulAntStatus;
	}

	public int getChannelTemperature() {
		return channelTemperature;
	}

	public void setChannelTemperature(int channelTemperature) {
		this.channelTemperature = channelTemperature;
	}

	/**
	 * @return the sendPower
	 */
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
