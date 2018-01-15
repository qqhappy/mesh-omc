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
 * �����վģ��
 * 
 * @author chenjunhua
 * 
 */

public class McBts extends Mo {

	// �������� 0 - ȫ������
	public static final int ANTENNA_TYPE_OMNI = 0;

	// �������� 1 - ��������
	public static final int ANTENNA_TYPE_PANEL = 1;

	// ��վID
	private Long btsId;

	// ��վ����: V5��΢���ѡ�������Զ
	private int btsType;

	// ��վƵ������: 1.8G��400M��340M
	private int btsFreqType;

	// ��վ�������� 0 - ȫ������ 1 - ��������
	private int antennaType;

	// ��վ���߽Ƕ�
	private int antennaAngle;

	// ��վ˽��IP��ַ���������룩
	private String btsIp = "0.0.0.0";

	// ��վ��SAGͨ�ŵ�IP��ַ(SAG VLan)
	private String sagBtsIp = "192.168.1.100";

	// ��iM3000�������õĻ�վIP,���·�����վ.
	private String btsConfigIp;

	// ��վ����ֱָͨʾ
	private int voiceDirectConnFlag;

	public static final int DCONN_FLAG_NO = 0;

	public static final int DCONN_FLAG_YES = 1;

	// λ����
	private long locationAreaId;

	// ����ID: 0~1023
	private int networkId;

	// ����ģʽ: 0-��վģʽ; 1-����ģʽ
	private int workMode;

	public static final int WORK_MODE_SINGLE = 0;

	public static final int WORK_MODE_NETWORK = 1;

	// ����Դ: 0-BTS; 1-EMS
	private int bootSource;

	public static final int BOOT_SOURCE_BTS = 0;

	public static final int BOOT_SOURCE_EMS = 1;

	// NAT AP Key (1~31)
	private int natAPKey;

	// SAG VLan ʹ�ñ�ʶ
	private int sagVlanUsedFlag;

	// SAG VLan ID
	private int sagVlanId = 1;

	// �������ͷ���������ʶ: 0-��������; 1-������(��������)
	private int restrictAreaFlag;

	// SAG�����豸��
	private long sagDeviceId;

	// SAG����IP
	private String sagVoiceIp;

	// SAG����IP
	private String sagSignalIp;

	// SAGĬ������?
	private String sagDefaultGateway = "192.168.1.1";

	// SAG��������?
	private String sagSubNetMask = "255.255.255.0";

	// ��վý��˿�
	private int btsMediaPort;

	// SAGý��˿�
	private int sagMediaPort;

	// ��վ����˿�
	private int btsSignalPort;

	// SAG����˿�
	private int sagSignalPort;

	// ��վ��������
	private int btsSignalPointCode;

	// SAG��������
	private int sagSignalPointCode;

	// EMS������IP(��������)
	private String emsIp;

	// ****************************************
	//
	// ���²���Ϊ��վע��״̬��ز���
	//
	// ****************************************
	// ע��״̬(Ĭ����δ����)
	private int registerState = DISCONNECTED;

	private static final int DISCONNECTED = 0;

	private static final int REGISTERING = 10;

	private static final int CONNECTED = 20;

	private static final int DELETED = 30;

	// ****************************************
	//
	// ���²���Ϊ��վע��֪ͨʱ�����ϱ�
	//
	// ****************************************
	// ����IP
	private String publicIp;

	// �����˿�
	private int publicPort;

	// ����汾
	private String softwareVersion;

	// Ӳ���汾
	private String hardwareVersion;

	// ****************************************
	//
	// ����
	//
	// ****************************************
	// ��վƵ��
	private int btsFreq;

	// �ڴ湤��״̬
	private int workingStatus;

	// һ�㹤��״̬
	private static final int NORMAL = 0;

	// ��������Ƶ����״̬
	private static final int ANTIJAMMING = 1;

	// ʹ�õ�ģ��ID,ֻ����������վ��ʱ��Ż��������ֵ
	private long templateId;

	// ����ID
	private long districtId;

	// ��վ����Map(Key:������, Value:����ֵ)
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
	 * �Ƿ�δ����
	 * 
	 * @return
	 */
	public boolean isDisconnected() {
		synchronized (this) {
			return registerState == DISCONNECTED;
		}
	}

	/**
	 * ��վ�Ƿ���ɾ��
	 * 
	 * @return
	 */
	public boolean isDeleted() {
		synchronized (this) {
			return registerState == DELETED;
		}
	}

	/**
	 * ���û�վΪ��ɾ��״̬
	 */
	public void setDeleted() {
		synchronized (this) {
			registerState = DELETED;
			// clearRealtimeData();
		}
	}

	/**
	 * �Ƿ�ע����
	 * 
	 * @return
	 */
	public boolean isRegistering() {
		synchronized (this) {
			return registerState == REGISTERING;
		}
	}

	/**
	 * �Ƿ�������
	 * 
	 * @return
	 */
	public boolean isConnected() {
		synchronized (this) {
			return registerState == CONNECTED;
		}
	}

	/**
	 * ����Ϊδ����״̬
	 * 
	 */
	public void setDisconnected() {
		synchronized (this) {
			registerState = DISCONNECTED;
			// clearRealtimeData();
		}
	}

	/**
	 * ����Ϊע����״̬
	 * 
	 */
	public void setRegistering() {
		synchronized (this) {
			registerState = REGISTERING;
		}
	}

	/**
	 * ����Ϊ������״̬
	 * 
	 */
	public void setConnected() {
		synchronized (this) {
			registerState = CONNECTED;
		}
	}

	/**
	 * �Ƿ񿹸�����Ƶ��
	 * 
	 * @return
	 */
	public boolean isAntijamming() {
		synchronized (this) {
			return workingStatus == ANTIJAMMING;
		}
	}

	/**
	 * ����Ϊ��������Ƶ��״̬
	 * 
	 */
	public void setAntijamming() {
		synchronized (this) {
			workingStatus = ANTIJAMMING;
		}
	}

	/**
	 * �жϻ�վ�Ƿ��ǿ����õ�
	 * 
	 * @return
	 */
	public boolean isConfigurable() {
		return (isOnlineManage() && isConnected());
	}

	/**
	 * ��ȡ��վ�澯���� -1: ��վδ����; 0: ����; 1:���ظ澯; 2:��Ҫ �澯; 3:��Ҫ�澯; 4:һ��澯
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
	// ����Ϊgetter/setter
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
	 * ��ȡ16���ƵĻ�վID������8λǰ�油0
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
	 * ���ʵʱ����
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
//		// �Ƚϸ澯״̬
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
	 * ���������վģ�͸�������ģ������
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
