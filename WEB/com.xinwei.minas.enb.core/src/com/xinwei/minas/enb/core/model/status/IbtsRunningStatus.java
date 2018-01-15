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
 * ΢վ����״̬ģ��
 * 
 * @author chenjunhua
 * 
 */

public class IbtsRunningStatus implements EnbDynamicInfo {

	// ��Ƶ����Ƶ��(��λ:10KHz)
	private int rfLocalFreq;

	// ��Ƶ����״̬
	private int rfLocalStatus;

	// �����¶�(��)
	private int mainBoardTemp;

	// DPDѵ�����
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
