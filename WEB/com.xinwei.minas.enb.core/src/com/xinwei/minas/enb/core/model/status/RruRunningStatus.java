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
 * RRU运行状态
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class RruRunningStatus implements Serializable {

	private int rackNo;

	private int shelfNo;

	private int slotNo;
	// 射频本振频率(单位:10KHz)
	private int rfLocalFreq;
	// 射频本振状态
	private int rfLocalStatus;
	// 时钟状态
	private int clockStatus;
	// Ir接口工作模式
	private int irInfWorkMode;
	// RRU运行状态
	private int runningStatus;
	// 主板温度(℃)
	private int mainBoardTemp;
	// 从板温度(℃)
	private int slaveBoardTemp;
	// DPD训练结果
	private int dpdTrainResult;

	private int channelNum;

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

	public int getRfLocalFreq() {
		return rfLocalFreq;
	}

	public void setRfLocalFreq(int rfLocalFreq) {
		this.rfLocalFreq = rfLocalFreq;
	}

	public int getRfLocalStatus() {
		return rfLocalStatus;
	}

	public void setRfLocalStatus(int rfLocalStatus) {
		this.rfLocalStatus = rfLocalStatus;
	}

	public int getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(int clockStatus) {
		this.clockStatus = clockStatus;
	}

	public int getIrInfWorkMode() {
		return irInfWorkMode;
	}

	public void setIrInfWorkMode(int irInfWorkMode) {
		this.irInfWorkMode = irInfWorkMode;
	}

	public int getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(int runningStatus) {
		this.runningStatus = runningStatus;
	}

	public int getMainBoardTemp() {
		return mainBoardTemp;
	}

	public void setMainBoardTemp(int mainBoardTemp) {
		this.mainBoardTemp = mainBoardTemp;
	}

	public int getSlaveBoardTemp() {
		return slaveBoardTemp;
	}

	public void setSlaveBoardTemp(int slaveBoardTemp) {
		this.slaveBoardTemp = slaveBoardTemp;
	}

	public int getDpdTrainResult() {
		return dpdTrainResult;
	}

	public void setDpdTrainResult(int dpdTrainResult) {
		this.dpdTrainResult = dpdTrainResult;
	}

	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public int getChannelNum() {
		return channelNum;
	}

}
