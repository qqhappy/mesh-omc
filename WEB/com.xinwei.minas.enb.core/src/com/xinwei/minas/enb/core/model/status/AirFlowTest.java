package com.xinwei.minas.enb.core.model.status;

import java.io.Serializable;

public class AirFlowTest implements Serializable {
	
	//UE的IP地址
	private String ipAddress;
	
	//P报文字节长度，单位：字节
	private int packetLength;
	
	//IP报文流量，单位：Mbit/s
	private int flowRate;
	
	//开始返回结果
	private int beginResult;
	
	//错误信息
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
