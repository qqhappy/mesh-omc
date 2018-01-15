package com.xinwei.minas.mcbts.core.model.simplenms;

import java.io.Serializable;

public class SAGData implements Serializable {
	private long sagid;// SAG设备号

	private String strSagMediaIP;// sag语音IP

	private String strSagSignalIP;// sag信令IP

	private int lSagTcpEID;// SAG信令编码

	private String strSagGateWay;// sag网关

	private String strSagMask;// sag子网掩码

	public int getLSagTcpEID() {
		return lSagTcpEID;
	}

	public void setLSagTcpEID(int sagTcpEID) {
		lSagTcpEID = sagTcpEID;
	}

	public long getSagid() {
		return sagid;
	}

	public void setSagid(long sagid) {
		this.sagid = sagid;
	}

	public String getStrSagGateWay() {
		return strSagGateWay;
	}

	public void setStrSagGateWay(String strSagGateWay) {
		this.strSagGateWay = strSagGateWay;
	}

	public String getStrSagMask() {
		return strSagMask;
	}

	public void setStrSagMask(String strSagMask) {
		this.strSagMask = strSagMask;
	}

	public String getStrSagMediaIP() {
		return strSagMediaIP;
	}

	public void setStrSagMediaIP(String strSagMediaIP) {
		this.strSagMediaIP = strSagMediaIP;
	}

	public String getStrSagSignalIP() {
		return strSagSignalIP;
	}

	public void setStrSagSignalIP(String strSagSignalIP) {
		this.strSagSignalIP = strSagSignalIP;
	}
}
