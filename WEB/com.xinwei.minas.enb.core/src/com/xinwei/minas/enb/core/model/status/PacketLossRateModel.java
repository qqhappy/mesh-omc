package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;
/**
 * ��S1�û��涪���ʼ��
 * @author sunzhangbin
 *
 */
public class PacketLossRateModel implements Serializable{

	//��60��S1�û��涪����
	private String localpacketLossRate;
	private String dstpacketLossRate;
	private int result;
	
	private String errorMsg;
	
	

	

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getLocalpacketLossRate() {
		return localpacketLossRate;
	}

	public void setLocalpacketLossRate(String localpacketLossRate) {
		this.localpacketLossRate = localpacketLossRate;
	}

	public String getDstpacketLossRate() {
		return dstpacketLossRate;
	}

	public void setDstpacketLossRate(String dstpacketLossRate) {
		this.dstpacketLossRate = dstpacketLossRate;
	}


	
}
