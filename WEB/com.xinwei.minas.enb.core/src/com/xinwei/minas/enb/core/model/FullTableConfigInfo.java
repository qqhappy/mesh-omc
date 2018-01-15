package com.xinwei.minas.enb.core.model;

import java.io.Serializable;
import java.util.Date;

public class FullTableConfigInfo implements Serializable{
	
	//配置中
	public static final int CONFIGING = 0;
	
	//配置失败
	public static final int CONFIG_FAIL = 1;
	
	//配置成功
	public static final int CONFIG_SUCCESS = 2;
	
	//未开始配置
	public static final int CONFIG_UNSTART = -1;
	
	//数据库标识
	private Long idx;
	
	//Enb标识
	private long moId;
	
	//整表配置状态
	private int configStatus;
	
	//开始时间
	private Date startConfigTime;
	
	//错误信息
	private String errorMessage;
	
	public Long getIdx() {
		return idx;
	}
	public void setIdx(Long idx) {
		this.idx = idx;
	}
	public long getMoId() {
		return moId;
	}
	public void setMoId(long moId) {
		this.moId = moId;
	}
	
	public Date getStartConfigTime() {
		return startConfigTime;
	}
	public void setStartConfigTime(Date startConfigTime) {
		this.startConfigTime = startConfigTime;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getConfigStatus() {
		return configStatus;
	}
	public void setConfigStatus(int configStatus) {
		this.configStatus = configStatus;
	}
}
