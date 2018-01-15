package com.xinwei.lte.web.lte.model;

public class PdtConfigInfo {

	//对端PDT标识
	private int pdtId;
	
	private String pdtIdStr;
	
	//对端PDT的IP地址
	private String pdtRemoteIp;
	
	//对端PDT的SIP端口号
	private int pdtRemotePort;
	
	//TCN1000与PDT的连接状态
	private int pdtLinkState;
	
	//描述信息
	private String comment;

	public int getPdtId() {
		return pdtId;
	}

	public void setPdtId(int pdtId) {
		this.pdtId = pdtId;
	}

	public String getPdtRemoteIp() {
		return pdtRemoteIp;
	}

	public void setPdtRemoteIp(String pdtRemoteIp) {
		this.pdtRemoteIp = pdtRemoteIp;
	}

	public int getPdtRemotePort() {
		return pdtRemotePort;
	}

	public void setPdtRemotePort(int pdtRemotePort) {
		this.pdtRemotePort = pdtRemotePort;
	}

	public int getPdtLinkState() {
		return pdtLinkState;
	}

	public void setPdtLinkState(int pdtLinkState) {
		this.pdtLinkState = pdtLinkState;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPdtIdStr() {
		return pdtIdStr;
	}

	public void setPdtIdStr(String pdtIdStr) {
		this.pdtIdStr = pdtIdStr;
	}
}
