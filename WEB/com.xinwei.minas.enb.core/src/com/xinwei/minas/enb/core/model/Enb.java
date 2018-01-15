/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.enb.core.model.EnbAttribute.Key;

/**
 * 
 * eNB网元模型
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class Enb extends Mo {

	private Long enbId;

	// eNB类型(默认是XW7000基站,宏基站)
	private int enbType = EnbTypeDD.XW7400;

	// 基站私网IP
	private String privateIp;

	// IPv4表中用于与网管连接的IP
	private String ipAddress;

	// 掩码
	private String netMask;

	// 网关
	private String gateway;

	// 新增小区状态

	private Map<Integer, Integer> cellStatusMap;

	// ****************************************
	//
	// 以下参数为基站注册状态相关参数
	//
	// ****************************************
	// 注册状态(默认是未连接)
	private int registerState = DISCONNECTED;

	private static final int DISCONNECTED = 0;

	private static final int REGISTERING = 10;

	private static final int CONNECTED = 20;

	private static final int DELETED = 30;

	// ****************************************
	//
	// 以下参数为基站注册通知时主动上报
	//
	// ****************************************
	// 公网IP
	private String publicIp;

	// 公网端口
	private int publicPort;

	/**
	 * 协议版本号，四位版本号中的前三位作为协议版本号
	 */
	private String protocolVersion;

	// 软件版本
	private String softwareVersion;

	// 是否已开站，默认为未开站
	private boolean isActive = false;

	// 整表业务(整表配置、整表反构)配置标识.
	private AtomicBoolean isFullTableOperation = new AtomicBoolean(false);

	// 该网元正在进行的业务类型
	private String bizName;

	public static final int SYNC_DIRECTION_EMS_TO_NE = 0;

	public static final int SYNC_DIRECTION_NE_TO_EMS = 1;

	// 数据同步方向,默认为网管到基站， 0网管到基站，1基站到网管
	private int syncDirection = 0;

	public static final int MONITOR_STATE_STOP = 0;

	public static final int MONITOR_STATE_START = 1;

	// 监控状态，默认为停止
	private int monitorState = MONITOR_STATE_STOP;

	// 基站属性Map(Key:属性名, Value:属性值)
	private HashMap<Key, Object> attributes = new HashMap<Key, Object>();

	public Long getEnbId() {
		return enbId;
	}

	public void setEnbId(Long enbId) {
		this.enbId = enbId;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}

	public String getNetMask() {
		return netMask;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getGateway() {
		return gateway;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public int getPublicPort() {
		return publicPort;
	}

	/**
	 * @param protocolVersion
	 *            the protocolVersion to set
	 */
	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * @return the protocolVersion
	 */
	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setRegisterState(int registerState) {
		this.registerState = registerState;
	}

	public int getRegisterState() {
		return registerState;
	}

	public void setEnbType(int enbType) {
		this.enbType = enbType;
	}

	public int getEnbType() {
		return enbType;
	}

	/**
	 * 获取16机制的基站ID，不足8位前面补0
	 * 
	 * @return
	 */
	public String getHexEnbId() {
		try {
			String sBtsId = Long.toHexString(enbId);
			int fillLen = 8 - sBtsId.length();
			for (int i = 0; i < fillLen; i++) {
				sBtsId = "0" + sBtsId;
			}
			return sBtsId;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断基站是否是可配置的
	 * 
	 * @return
	 */
	public boolean isConfigurable() {
		// 只有在线管理状态下才可进行配置数据到网元(20140610)
		// return isOnlineManage();
		return (isOnlineManage() && isConnected());
	}

	/**
	 * 是否未连接
	 * 
	 * @return
	 */
	public boolean isDisconnected() {
		synchronized (this) {
			return registerState == DISCONNECTED;
		}
	}

	/**
	 * 基站是否已删除
	 * 
	 * @return
	 */
	public boolean isDeleted() {
		synchronized (this) {
			return registerState == DELETED;
		}
	}

	/**
	 * 设置基站为已删除状态
	 */
	public void setDeleted() {
		synchronized (this) {
			registerState = DELETED;
			// clearRealtimeData();
		}
	}

	/**
	 * 是否注册中
	 * 
	 * @return
	 */
	public boolean isRegistering() {
		synchronized (this) {
			return registerState == REGISTERING;
		}
	}

	/**
	 * 是否已连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		synchronized (this) {
			return registerState == CONNECTED;
		}
	}

	/**
	 * 设置为未连接状态
	 * 
	 */
	public void setDisconnected() {
		synchronized (this) {
			registerState = DISCONNECTED;
			// clearRealtimeData();
		}
	}

	/**
	 * 设置为注册中状态
	 * 
	 */
	public void setRegistering() {
		synchronized (this) {
			registerState = REGISTERING;
		}
	}

	/**
	 * 设置为已连接状态
	 * 
	 */
	public void setConnected() {
		synchronized (this) {
			registerState = CONNECTED;
		}
	}

	@Override
	public Enb clone() {
		try {
			return (Enb) super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断该网元是否正在执行整表业务(整表配置、整表反构)
	 * 
	 * @return
	 */
	public boolean getFullTableOperation() {
		return isFullTableOperation.get();
	}

	/**
	 * 设置整表业务(整表配置、整表反构)状态 返回结果：true表示设置成功,false表示设置失败
	 * 
	 * @param flag
	 * @return
	 */
	public boolean setFullTableOperation(boolean flag) {
		return isFullTableOperation.compareAndSet(!flag, flag);
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public HashMap<Key, Object> getAttributes() {
		return attributes;
	}

	@SuppressWarnings({ "unchecked" })
	public void setAttributes(HashMap<Key, Object> attributes) {
		this.attributes = (HashMap<Key, Object>) attributes.clone();
	}

	public void addAttribute(Key name, Object value) {
		attributes.put(name, value);
	}

	public void removeAttribute(
			com.xinwei.minas.enb.core.model.EnbAttribute.Key name) {
		attributes.remove(name);
	}

	public Object getAttribute(Key name) {
		return attributes.get(name);
	}

	/**
	 * 获取基站告警级别 -1: 基站未连接; 0: 正常; 1:严重告警; 2:重要 告警; 3:次要告警; 4:一般告警
	 * 
	 * @return
	 */
	public int getAlarmLevel() {
		Integer alarmLevel = (Integer) getAttribute(Key.ALARM_LEVEL);
		if (alarmLevel == null || isDisconnected() || isDeleted()) {
			alarmLevel = -1;
		}
		return alarmLevel;
	}

	/**
	 * 设置为已开站
	 */
	public synchronized void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setSyncDirection(int syncDirection) {
		this.syncDirection = syncDirection;
	}

	public int getSyncDirection() {
		return syncDirection;
	}

	/**
	 * 设置监控状态
	 * 
	 * @param monitorState
	 */
	public void setMonitorState(int monitorState) {
		this.monitorState = monitorState;
	}

	public int getMonitorState() {
		return monitorState;
	}

	/**
	 * 是否正在被监控
	 * 
	 * @return
	 */
	public boolean isMonitoring() {
		return monitorState == MONITOR_STATE_START;
	}

	public void setCellStatusMap(Map<Integer, Integer> cellStatusMap) {
		this.cellStatusMap = cellStatusMap;
	}

	public Map<Integer, Integer> getCellStatusMap() {
		return cellStatusMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((enbId == null) ? 0 : enbId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enb other = (Enb) obj;
		if (enbId == null) {
			if (other.enbId != null)
				return false;
		} else if (!enbId.equals(other.enbId))
			return false;
		return true;
	}

}
