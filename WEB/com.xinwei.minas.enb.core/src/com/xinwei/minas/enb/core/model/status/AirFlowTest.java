package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

public class AirFlowTest implements Serializable {
	
	//UE��IP��ַ
	private String ipAddress;
	
	//P�����ֽڳ��ȣ���λ���ֽ�
	private int packetLength;
	
	//IP������������λ��Mbit/s
	private int flowRate;
	
	//��ʼ���ؽ��
	private int beginResult;
	
	//������Ϣ
	private String errorStr;

	
	public int getBeginResult() {
		return beginResult;
	}

	public void setBeginResult(int beginResult) {
		this.beginResult = beginResult;
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public int getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(int flowRate) {
		this.flowRate = flowRate;
	}
	
	

}
