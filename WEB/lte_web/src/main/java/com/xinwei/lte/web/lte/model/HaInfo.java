package com.xinwei.lte.web.lte.model;

public class HaInfo {

	//TCN1000类型
	private int haTcn1000Type;
	
	//是否配置HA
	private int haEnable;
	
	//SDC的HA IP
	private String haSdcIp;
	
	//SDC的HA 端口
	private int haSdcPort;
	
	//XGW的HA IP
	private String haXgwIp;
	
	//XGW的HA 端口
	private int haXgwPort;

	//远端SDC的HA IP
	private String haRemoteSdcIp;
	
	//远端SDC的HA 端口
	private int haRemoteSdcPort;
	
	//远端XGW的HA IP
	private String haRemoteXgwIp;
	
	//远端XGW的HA 端口
	private int haRemoteXgwPort;

	public int getHaTcn1000Type() {
		return haTcn1000Type;
	}

	public void setHaTcn1000Type(int haTcn1000Type) {
		this.haTcn1000Type = haTcn1000Type;
	}

	public int getHaEnable() {
		return haEnable;
	}

	public void setHaEnable(int haEnable) {
		this.haEnable = haEnable;
	}

	public String getHaSdcIp() {
		return haSdcIp;
	}

	public void setHaSdcIp(String haSdcIp) {
		this.haSdcIp = haSdcIp;
	}

	public int getHaSdcPort() {
		return haSdcPort;
	}

	public void setHaSdcPort(int haSdcPort) {
		this.haSdcPort = haSdcPort;
	}

	public String getHaXgwIp() {
		return haXgwIp;
	}

	public void setHaXgwIp(String haXgwIp) {
		this.haXgwIp = haXgwIp;
	}

	public int getHaXgwPort() {
		return haXgwPort;
	}

	public void setHaXgwPort(int haXgwPort) {
		this.haXgwPort = haXgwPort;
	}

	public String getHaRemoteSdcIp() {
		return haRemoteSdcIp;
	}

	public void setHaRemoteSdcIp(String haRemoteSdcIp) {
		this.haRemoteSdcIp = haRemoteSdcIp;
	}

	public int getHaRemoteSdcPort() {
		return haRemoteSdcPort;
	}

	public void setHaRemoteSdcPort(int haRemoteSdcPort) {
		this.haRemoteSdcPort = haRemoteSdcPort;
	}

	public String getHaRemoteXgwIp() {
		return haRemoteXgwIp;
	}

	public void setHaRemoteXgwIp(String haRemoteXgwIp) {
		this.haRemoteXgwIp = haRemoteXgwIp;
	}

	public int getHaRemoteXgwPort() {
		return haRemoteXgwPort;
	}

	public void setHaRemoteXgwPort(int haRemoteXgwPort) {
		this.haRemoteXgwPort = haRemoteXgwPort;
	}
}
