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
 * 微站运行状态模型
 * 
 * @author chenjunhua
 * 
 */

public class IbtsRunningStatus implements EnbDynamicInfo {

	// 射频本振频率(单位:10KHz)
	private int rfLocalFreq;

	// 射频本振状态
	private int rfLocalStatus;

	// 主板温度(℃)
	private int mainBoardTemp;

	// DPD训练结果
	private int dpdTrainResult;

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

	public int getMainBoardTemp() {
		return mainBoardTemp;
	}

	public void setMainBoardTemp(int mainBoardTemp) {
		this.mainBoardTemp = mainBoardTemp;
	}

	public int getDpdTrainResult() {
		return dpdTrainResult;
	}

	public void setDpdTrainResult(int dpdTrainResult) {
		this.dpdTrainResult = dpdTrainResult;
	}

	@Override
	public String toString() {
		return "IbtsRunningStatus [rfLocalFreq=" + rfLocalFreq
				+ ", rfLocalStatus=" + rfLocalStatus + ", mainBoardTemp="
				+ mainBoardTemp + ", dpdTrainResult=" + dpdTrainResult + "]";
	}

}
