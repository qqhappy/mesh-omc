package com.xinwei.minas.mcbts.core.model.simplenms;

import java.io.Serializable;

public class SAGData implements Serializable {
	private long sagid;// SAG�豸��

	private String strSagMediaIP;// sag����IP

	private String strSagSignalIP;// sag����IP

	private int lSagTcpEID;// SAG�������

	private String strSagGateWay;// sag����

	private String strSagMask;// sag��������

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
