/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute.Key;

/**
 * 
 * 宽带基站模型
 * 
 * @author chenjunhua
 * 
 */

public class McBts extends Mo {

	// 天线类型 0 - 全向天线
	public static final int ANTENNA_TYPE_OMNI = 0;

	// 天线类型 1 - 定向天线
	public static final int ANTENNA_TYPE_PANEL = 1;

	// 基站ID
	private Long btsId;

	// 基站类型: V5、微蜂窝、光纤拉远
	private int btsType;

	// 基站频段类型: 1.8G、400M、340M
	private int btsFreqType;

	// 基站天线类型 0 - 全向天线 1 - 定向天线
	private int antennaType;

	// 基站天线角度
	private int antennaAngle;

	// 基站私网IP地址（不用输入）
	private String btsIp = "0.0.0.0";

	// 基站与SAG通信的IP地址(SAG VLan)
	private String sagBtsIp = "192.168.1.100";

	// 给iM3000进行配置的基站IP,不下发到基站.
	private String btsConfigIp;

	// 基站语音直通指示
	private int voiceDirectConnFlag;

	public static final int DCONN_FLAG_NO = 0;

	public static final int DCONN_FLAG_YES = 1;

	// 位置区
	private long locationAreaId;

	// 网络ID: 0~1023
	private int networkId;

	// 工作模式: 0-单站模式; 1-联网模式
	private int workMode;

	public static final int WORK_MODE_SINGLE = 0;

	public static final int WORK_MODE_NETWORK = 1;

	// 启动源: 0-BTS; 1-EMS
	private int bootSource;

	public static final int BOOT_SOURCE_BTS = 0;

	public static final int BOOT_SOURCE_EMS = 1;

	// NAT AP Key (1~31)
	private int natAPKey;

	// SAG VLan 使用标识
	private int sagVlanUsedFlag;

	// SAG VLan ID
	private int sagVlanId = 1;

	// 限制区和非限制区标识: 0-非限制区; 1-限制区(不用输入)
	private int restrictAreaFlag;

	// SAG本地设备号
	private long sagDeviceId;

	// SAG语音IP
	private String sagVoiceIp;

	// SAG信令IP
	private String sagSignalIp;

	// SAG默认网关?
	private String sagDefaultGateway = "192.168.1.1";

	// SAG子网掩码?
	private String sagSubNetMask = "255.255.255.0";

	// 基站媒体端口
	private int btsMediaPort;

	// SAG媒体端口
	private int sagMediaPort;

	// 基站信令端口
	private int btsSignalPort;

	// SAG信令端口
	private int sagSignalPort;

	// 基站信令点编码
	private int btsSignalPointCode;

	// SAG信令点编码
	private int sagSignalPointCode;

	// EMS服务器IP(不用输入)
	private String emsIp;

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

	// 软件版本
	private String softwareVersion;

	// 硬件版本
	private String hardwareVersion;

	// ****************************************
	//
	// 其它
	//
	// ****************************************
	// 基站频点
	private int btsFreq;

	// 内存工作状态
	private int workingStatus;

	// 一般工作状态
	private static final int NORMAL = 0;

	// 抗干扰跳频工作状态
	private static final int ANTIJAMMING = 1;

	// 使用的模板ID,只有在新增基站的时候才会设置这个值
	private long templateId;

	// 地域ID
	private long districtId;

	// 基站属性Map(Key:属性名, Value:属性值)
	private HashMap<Key, Object> attributes = new HashMap<Key, Object>();

	public McBts() {

	}

	public McBts(long moId, int typeId, String name, String desc,
			int manageStateCode) {
		super(moId, typeId, name, desc, manageStateCode);
	}

	public McBts(Mo mo) {
		this(mo.getMoId(), mo.getTypeId(), mo.getName(), mo.getDescription(),
				mo.getManageStateCode());
	}

	@Override
	public String getDisplayName() {
		return this.getHexBtsId() + "(" + super.getName() + ")";
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

	/**
	 * 是否抗干扰跳频中
	 * 
	 * @return
	 */
	public boolean isAntijamming() {
		synchronized (this) {
			return workingStatus == ANTIJAMMING;
		}
	}

	/**
	 * 设置为抗干扰跳频中状态
	 * 
	 */
	public void setAntijamming() {
		synchronized (this) {
			workingStatus = ANTIJAMMING;
		}
	}

	/**
	 * 判断基站是否是可配置的
	 * 
	 * @return
	 */
	public boolean isConfigurable() {
		return (isOnlineManage() && isConnected());
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

	// ****************************************
	//
	// 以下为getter/setter
	//
	// ****************************************

	public int getBootSource() {
		return bootSource;
	}

	public int getWorkMode() {
		return workMode;
	}

	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}

	public void setBootSource(int bootSource) {
		this.bootSource = bootSource;
	}

	public HashMap<Key, Object> getAttributes() {
		return attributes;
	}


	/**
	 * 获取16机制的基站ID，不足8位前面补0
	 * 
	 * @return
	 */
	public String getHexBtsId() {
		try {
			String sBtsId = Long.toHexString(btsId);
			int fillLen = 8 - sBtsId.length();
			for (int i = 0; i < fillLen; i++) {
				sBtsId = "0" + sBtsId;
			}
			return sBtsId;
		} catch (Exception e) {
			return "";
		}
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public int getBtsFreqType() {
		return btsFreqType;
	}

	public void setBtsFreqType(int btsFreqType) {
		this.btsFreqType = btsFreqType;
	}

	public int getBtsType() {
		return btsType;
	}

	public void setBtsType(int btsType) {
		this.btsType = btsType;
	}

	public String getBtsIp() {
		return btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public int getBtsMediaPort() {
		return btsMediaPort;
	}

	public void setBtsMediaPort(int btsMediaPort) {
		this.btsMediaPort = btsMediaPort;
	}

	public int getBtsSignalPointCode() {
		return btsSignalPointCode;
	}

	public void setBtsSignalPointCode(int btsSignalPointCode) {
		this.btsSignalPointCode = btsSignalPointCode;
	}

	public int getBtsSignalPort() {
		return btsSignalPort;
	}

	public void setBtsSignalPort(int btsSignalPort) {
		this.btsSignalPort = btsSignalPort;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public long getLocationAreaId() {
		return locationAreaId;
	}

	public void setLocationAreaId(long locationAreaId) {
		this.locationAreaId = locationAreaId;
	}

	public int getNatAPKey() {
		return natAPKey;
	}

	public void setNatAPKey(int natAPKey) {
		this.natAPKey = natAPKey;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
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

	public int getRegisterState() {
		return registerState;
	}

	public void setRegisterState(int registerState) {
		this.registerState = registerState;
	}

	public int getRestrictAreaFlag() {
		return restrictAreaFlag;
	}

	public void setRestrictAreaFlag(int restrictAreaFlag) {
		this.restrictAreaFlag = restrictAreaFlag;
	}

	public String getSagDefaultGateway() {
		return sagDefaultGateway;
	}

	public void setSagDefaultGateway(String sagDefaultGateway) {
		this.sagDefaultGateway = sagDefaultGateway;
	}

	public long getSagDeviceId() {
		return sagDeviceId;
	}

	public void setSagDeviceId(long sagDeviceId) {
		this.sagDeviceId = sagDeviceId;
	}

	public int getSagMediaPort() {
		return sagMediaPort;
	}

	public void setSagMediaPort(int sagMediaPort) {
		this.sagMediaPort = sagMediaPort;
	}

	public String getSagSignalIp() {
		return sagSignalIp;
	}

	public void setSagSignalIp(String sagSignalIp) {
		this.sagSignalIp = sagSignalIp;
	}

	public int getSagSignalPointCode() {
		return sagSignalPointCode;
	}

	public void setSagSignalPointCode(int sagSignalPointCode) {
		this.sagSignalPointCode = sagSignalPointCode;
	}

	public int getSagSignalPort() {
		return sagSignalPort;
	}

	public void setSagSignalPort(int sagSignalPort) {
		this.sagSignalPort = sagSignalPort;
	}

	public String getSagSubNetMask() {
		return sagSubNetMask;
	}

	public void setSagSubNetMask(String sagSubNetMask) {
		this.sagSubNetMask = sagSubNetMask;
	}

	public int getSagVlanId() {
		return sagVlanId;
	}

	public void setSagVlanId(int sagVlanId) {
		this.sagVlanId = sagVlanId;
	}

	public int getSagVlanUsedFlag() {
		return sagVlanUsedFlag;
	}

	public void setSagVlanUsedFlag(int sagVlanUsedFlag) {
		this.sagVlanUsedFlag = sagVlanUsedFlag;
	}

	public String getSagVoiceIp() {
		return sagVoiceIp;
	}

	public void setSagVoiceIp(String sagVoiceIp) {
		this.sagVoiceIp = sagVoiceIp;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getEmsIp() {
		return emsIp;
	}

	public void setEmsIp(String emsIp) {
		this.emsIp = emsIp;
	}

	public int getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(int antennaType) {
		this.antennaType = antennaType;
	}

	public int getAntennaAngle() {
		return antennaAngle;
	}

	public void setAntennaAngle(int antennaAngle) {
		this.antennaAngle = antennaAngle;
	}

	public int getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(int workingStatus) {
		this.workingStatus = workingStatus;
	}

	public int getBtsFreq() {
		return btsFreq;
	}

	public void setBtsFreq(int btsFreq) {
		this.btsFreq = btsFreq;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}
	
	public void setAttributes(HashMap<Key, Object> attributes) {
		this.attributes = (HashMap)attributes.clone();
	}
	
	public void addAttribute(Key name, Object value) {
		attributes.put(name, value);
	}

	public void removeAttribute(Key name) {
		attributes.remove(name);
	}

	public Object getAttribute(Key name) {
		return attributes.get(name);
	}

	public String getSagBtsIp() {
		return sagBtsIp;
	}

	public void setSagBtsIp(String sagBtsIp) {
		this.sagBtsIp = sagBtsIp;
	}

	public String getBtsConfigIp() {
		return btsConfigIp;
	}

	public void setBtsConfigIp(String btsConfigIp) {
		this.btsConfigIp = btsConfigIp;
	}

	public int getVoiceDirectConnFlag() {
		return voiceDirectConnFlag;
	}

	public void setVoiceDirectConnFlag(int voiceDirectConnFlag) {
		this.voiceDirectConnFlag = voiceDirectConnFlag;
	}

	/**
	 * 清除实时数据
	 */
	public void clearRealtimeData() {
		this.setBtsIp("0.0.0.0");
		this.setSoftwareVersion("");
		this.setHardwareVersion("");
		this.setPublicIp("0.0.0.0");
		this.setPublicPort(0);
		attributes.remove(McBtsAttribute.Key.FPGA_VERSION);
		attributes.remove(McBtsAttribute.Key.MCU_VERSION);
	}

	@Override
	public McBts clone() {
		try {
			McBts bts = (McBts)super.clone();
			bts.setAttributes(this.getAttributes());
			return bts;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((btsId == null) ? 0 : btsId.hashCode());
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
		McBts other = (McBts) obj;
		if (btsId == null) {
			if (other.btsId != null)
				return false;
		} else if (!btsId.equals(other.btsId))
			return false;
		return true;
	}

//	public boolean equals2(McBts obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		McBts other = (McBts) obj;
//		if (antennaAngle != other.antennaAngle)
//			return false;
//		if (antennaType != other.antennaType)
//			return false;
//		if (attributes == null) {
//			if (other.attributes != null)
//				return false;
//		} else if (!attributes.equals(other.attributes))
//			return false;
//		if (bootSource != other.bootSource)
//			return false;
//		if (btsFreq != other.btsFreq)
//			return false;
//		if (btsFreqType != other.btsFreqType)
//			return false;
//		if (btsId == null) {
//			if (other.btsId != null)
//				return false;
//		} else if (!btsId.equals(other.btsId))
//			return false;
//		if (btsIp == null) {
//			if (other.btsIp != null)
//				return false;
//		} else if (!btsIp.equals(other.btsIp))
//			return false;
//		if (btsMediaPort != other.btsMediaPort)
//			return false;
//		if (btsSignalPointCode != other.btsSignalPointCode)
//			return false;
//		if (btsSignalPort != other.btsSignalPort)
//			return false;
//		if (btsType != other.btsType)
//			return false;
//		if (emsIp == null) {
//			if (other.emsIp != null)
//				return false;
//		} else if (!emsIp.equals(other.emsIp))
//			return false;
//		if (hardwareVersion == null) {
//			if (other.hardwareVersion != null)
//				return false;
//		} else if (!hardwareVersion.equals(other.hardwareVersion))
//			return false;
//		if (locationAreaId != other.locationAreaId)
//			return false;
//		if (natAPKey != other.natAPKey)
//			return false;
//		if (networkId != other.networkId)
//			return false;
//		if (publicIp == null) {
//			if (other.publicIp != null)
//				return false;
//		} else if (!publicIp.equals(other.publicIp))
//			return false;
//		if (publicPort != other.publicPort)
//			return false;
//		if (registerState != other.registerState)
//			return false;
//		if (restrictAreaFlag != other.restrictAreaFlag)
//			return false;
//		if (sagDefaultGateway == null) {
//			if (other.sagDefaultGateway != null)
//				return false;
//		} else if (!sagDefaultGateway.equals(other.sagDefaultGateway))
//			return false;
//		if (sagDeviceId != other.sagDeviceId)
//			return false;
//		if (sagMediaPort != other.sagMediaPort)
//			return false;
//		if (sagSignalIp == null) {
//			if (other.sagSignalIp != null)
//				return false;
//		} else if (!sagSignalIp.equals(other.sagSignalIp))
//			return false;
//		if (sagSignalPointCode != other.sagSignalPointCode)
//			return false;
//		if (sagSignalPort != other.sagSignalPort)
//			return false;
//		if (sagSubNetMask == null) {
//			if (other.sagSubNetMask != null)
//				return false;
//		} else if (!sagSubNetMask.equals(other.sagSubNetMask))
//			return false;
//		if (sagVlanId != other.sagVlanId)
//			return false;
//		if (sagVlanUsedFlag != other.sagVlanUsedFlag)
//			return false;
//		if (sagVoiceIp == null) {
//			if (other.sagVoiceIp != null)
//				return false;
//		} else if (!sagVoiceIp.equals(other.sagVoiceIp))
//			return false;
//		if (softwareVersion == null) {
//			if (other.softwareVersion != null)
//				return false;
//		} else if (!softwareVersion.equals(other.softwareVersion))
//			return false;
//		if (templateId != other.templateId)
//			return false;
//		if (districtId != other.districtId)
//			return false;
//		if (workMode != other.workMode)
//			return false;
//		if (workingStatus != other.workingStatus)
//			return false;
//		// 比较告警状态
//		if (this.getAlarmLevel() != other.getAlarmLevel()) {
//			return false;
//		}
//		return true;
//	}

	public boolean equals2(McBts obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		McBts other = (McBts) obj;
		if (antennaAngle != other.antennaAngle)
			return false;
		if (antennaType != other.antennaType)
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (bootSource != other.bootSource)
			return false;
		if (btsConfigIp == null) {
			if (other.btsConfigIp != null)
				return false;
		} else if (!btsConfigIp.equals(other.btsConfigIp))
			return false;
		if (btsFreq != other.btsFreq)
			return false;
		if (btsFreqType != other.btsFreqType)
			return false;
		if (btsId == null) {
			if (other.btsId != null)
				return false;
		} else if (!btsId.equals(other.btsId))
			return false;
		if (btsIp == null) {
			if (other.btsIp != null)
				return false;
		} else if (!btsIp.equals(other.btsIp))
			return false;
		if (btsMediaPort != other.btsMediaPort)
			return false;
		if (btsSignalPointCode != other.btsSignalPointCode)
			return false;
		if (btsSignalPort != other.btsSignalPort)
			return false;
		if (btsType != other.btsType)
			return false;
		if (districtId != other.districtId)
			return false;
		if (emsIp == null) {
			if (other.emsIp != null)
				return false;
		} else if (!emsIp.equals(other.emsIp))
			return false;
		if (hardwareVersion == null) {
			if (other.hardwareVersion != null)
				return false;
		} else if (!hardwareVersion.equals(other.hardwareVersion))
			return false;
		if (locationAreaId != other.locationAreaId)
			return false;
		if (natAPKey != other.natAPKey)
			return false;
		if (networkId != other.networkId)
			return false;
		if (publicIp == null) {
			if (other.publicIp != null)
				return false;
		} else if (!publicIp.equals(other.publicIp))
			return false;
		if (publicPort != other.publicPort)
			return false;
		if (registerState != other.registerState)
			return false;
		if (restrictAreaFlag != other.restrictAreaFlag)
			return false;
		if (sagBtsIp == null) {
			if (other.sagBtsIp != null)
				return false;
		} else if (!sagBtsIp.equals(other.sagBtsIp))
			return false;
		if (sagDefaultGateway == null) {
			if (other.sagDefaultGateway != null)
				return false;
		} else if (!sagDefaultGateway.equals(other.sagDefaultGateway))
			return false;
		if (sagDeviceId != other.sagDeviceId)
			return false;
		if (sagMediaPort != other.sagMediaPort)
			return false;
		if (sagSignalIp == null) {
			if (other.sagSignalIp != null)
				return false;
		} else if (!sagSignalIp.equals(other.sagSignalIp))
			return false;
		if (sagSignalPointCode != other.sagSignalPointCode)
			return false;
		if (sagSignalPort != other.sagSignalPort)
			return false;
		if (sagSubNetMask == null) {
			if (other.sagSubNetMask != null)
				return false;
		} else if (!sagSubNetMask.equals(other.sagSubNetMask))
			return false;
		if (sagVlanId != other.sagVlanId)
			return false;
		if (sagVlanUsedFlag != other.sagVlanUsedFlag)
			return false;
		if (sagVoiceIp == null) {
			if (other.sagVoiceIp != null)
				return false;
		} else if (!sagVoiceIp.equals(other.sagVoiceIp))
			return false;
		if (softwareVersion == null) {
			if (other.softwareVersion != null)
				return false;
		} else if (!softwareVersion.equals(other.softwareVersion))
			return false;
		if (templateId != other.templateId)
			return false;
		if (voiceDirectConnFlag != other.voiceDirectConnFlag)
			return false;
		if (workMode != other.workMode)
			return false;
		if (workingStatus != other.workingStatus)
			return false;
		return true;
	}

	/**
	 * 根据输入基站模型更新自身模型数据
	 * 
	 * @param input
	 */
	public void update(McBts input) {
		if (input == null || input.isDeleted()) {
			this.setDeleted();
			return;
		}
		this.setAntennaType(input.getAntennaType());
		this.setAntennaAngle(input.getAntennaAngle());
		this.setWorkMode(input.getWorkMode());
		this.setBootSource(input.getBootSource());
		this.setBtsFreq(input.getBtsFreq());
		this.setBtsFreqType(input.getBtsFreqType());
		this.setBtsId(input.getBtsId());
		this.setBtsIp(input.getBtsIp());
		this.setSagBtsIp(input.getSagBtsIp());
		this.setBtsMediaPort(input.getBtsMediaPort());
		this.setBtsSignalPointCode(input.getBtsSignalPointCode());
		this.setBtsSignalPort(input.getBtsSignalPort());
		this.setBtsType(input.getBtsType());
		this.setDescription(input.getDescription());
		this.setEmsIp(input.getEmsIp());
		this.setHardwareVersion(input.getHardwareVersion());
		this.setLocationAreaId(input.getLocationAreaId());
		this.setManageStateCode(input.getManageStateCode());
		this.setMoId(input.getMoId());
		this.setName(input.getName());
		this.setNatAPKey(input.getNatAPKey());
		this.setNetworkId(input.getNetworkId());
		this.setPublicIp(input.getPublicIp());
		this.setPublicPort(input.getPublicPort());
		this.setRegisterState(input.getRegisterState());
		this.setRestrictAreaFlag(input.getRestrictAreaFlag());
		this.setSagDefaultGateway(input.getSagDefaultGateway());
		this.setSagDeviceId(input.getSagDeviceId());
		this.setSagMediaPort(input.getSagMediaPort());
		this.setSagSignalIp(input.getSagSignalIp());
		this.setSagSignalPointCode(input.getSagSignalPointCode());
		this.setSagSignalPort(input.getSagSignalPort());
		this.setSagSubNetMask(input.getSagSubNetMask());
		this.setSagVlanId(input.getSagVlanId());
		this.setSagVlanUsedFlag(input.getSagVlanUsedFlag());
		this.setSagVoiceIp(input.getSagVoiceIp());
		this.setSoftwareVersion(input.getSoftwareVersion());
		this.setTypeId(input.getTypeId());
		this.setWorkingStatus(input.getWorkingStatus());
		this.setTemplateId(input.getTemplateId());
		this.setDistrictId(input.getDistrictId());
		this.setAttributes(input.getAttributes());
	}

}
