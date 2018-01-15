package com.xinwei.lte.web.lte.model;

/**
 * license模型
 * @author sunzhangbin
 *
 */
public class LicenseModel {
	
	/*
	 *设备类型
	 */
	private int deviceType;
	
	/*
	 * 设备硬件标识
	 */
	private String  hardwareSignature;
	
	/*
	 * 版本类型
	 */
	private int  versionType;
	
	/*
	 * 当前用户数
	 */
	private String  curUserNum;
	
	/*
	 * License限制的最大用户数
	 */
	private String  maxUserNum;
	
	
	/*
	 * 到期时间
	 */
	private String  expire;
	
	/*
	 * 授权码
	 */
	private String  authCode;
	
	/*
	 * 授权描述信息（格式UTF8）
	 */
	private String  descrition;

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getHardwareSignature() {
		return hardwareSignature;
	}

	public void setHardwareSignature(String hardwareSignature) {
		this.hardwareSignature = hardwareSignature;
	}

	

	public int getVersionType() {
		return versionType;
	}

	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}

	public String getCurUserNum() {
		return curUserNum;
	}

	public void setCurUserNum(String curUserNum) {
		this.curUserNum = curUserNum;
	}

	public String getMaxUserNum() {
		return maxUserNum;
	}

	public void setMaxUserNum(String maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getDescrition() {
		return descrition;
	}

	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}
	

}
