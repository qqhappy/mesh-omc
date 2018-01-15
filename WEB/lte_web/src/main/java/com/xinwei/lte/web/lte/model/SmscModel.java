package com.xinwei.lte.web.lte.model;

public class SmscModel {

	private String smscUserName;
	
	private String smscPassWord;
	
	private String smscIp;
	
	private int smscPort;
	
	private String smscComment;
	
    private int smscStatus;
    
    
	public int getSmscStatus() {
		return smscStatus;
	}

	public void setSmscStatus(int smscStatus) {
		this.smscStatus = smscStatus;
	}

	public String getSmscUserName() {
		return smscUserName;
	}

	public void setSmscUserName(String smscUserName) {
		this.smscUserName = smscUserName;
	}

	public String getSmscPassWord() {
		return smscPassWord;
	}

	public void setSmscPassWord(String smscPassWord) {
		this.smscPassWord = smscPassWord;
	}

	public String getSmscIp() {
		return smscIp;
	}

	public void setSmscIp(String smscIp) {
		this.smscIp = smscIp;
	}

	public int getSmscPort() {
		return smscPort;
	}

	public void setSmscPort(int smscPort) {
		this.smscPort = smscPort;
	}

	public String getSmscComment() {
		return smscComment;
	}

	public void setSmscComment(String smscComment) {
		this.smscComment = smscComment;
	}
	
	
}
