package com.xinwei.lte.web.enb.model;

import java.util.Map;

import com.xinwei.minas.core.model.Mo;

/**
 * 异步查询enb列表所需的enb模型，为了解决json keys must be strings 异常
 * @author zhangqiang
 *
 */
public class AsyncEnbModel extends Mo{
	/**
	 * 16进制基站ID
	 */
	private String enbId;

	/**
	 * 基站告警级别
	 */
	private int alarmLevel;
	
	/**
	 * 基站类型
	 */
	private int enbType;
	
	/**
	 * 私网IP
	 */
	private String privateIp;
	
	/**
	 * 注册状态
	 */
	private int registerState;
	
	/**
	 * 公网IP
	 */
	private String publicIp;
	
	/**
	 * 公网端口
	 */
	private int publicPort;
	
	/**
	 * 软件版本
	 */
	private String softwareVersion;
	
	/**
	 * 协议版本
	 */
	private String protocolVersion;
	
	/**
	 * 是否已激活  0：否    1：是
	 */
	private int isActive = 1;
	
	/**
	 * 小区状态
	 */
	private Map<String, Integer> cellStatus;	

	public String getEnbId() {
		return enbId;
	}

	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public int getEnbType() {
		return enbType;
	}

	public void setEnbType(int enbType) {
		this.enbType = enbType;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public int getRegisterState() {
		return registerState;
	}

	public void setRegisterState(int registerState) {
		this.registerState = registerState;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public Map<String, Integer> getCellStatus() {
		return cellStatus;
	}

	public void setCellStatus(Map<String, Integer> cellStatus) {
		this.cellStatus = cellStatus;
	}
	
	
	
}
