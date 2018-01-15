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
 * eNB��Ԫģ��
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class Enb extends Mo {

	private Long enbId;

	// eNB����(Ĭ����XW7000��վ,���վ)
	private int enbType = EnbTypeDD.XW7400;

	// ��վ˽��IP
	private String privateIp;

	// IPv4�����������������ӵ�IP
	private String ipAddress;

	// ����
	private String netMask;

	// ����
	private String gateway;

	// ����С��״̬

	private Map<Integer, Integer> cellStatusMap;

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

	/**
	 * Э��汾�ţ���λ�汾���е�ǰ��λ��ΪЭ��汾��
	 */
	private String protocolVersion;

	// ����汾
	private String softwareVersion;

	// �Ƿ��ѿ�վ��Ĭ��Ϊδ��վ
	private boolean isActive = false;

	// ����ҵ��(�������á�������)���ñ�ʶ.
	private AtomicBoolean isFullTableOperation = new AtomicBoolean(false);

	// ����Ԫ���ڽ��е�ҵ������
	private String bizName;

	public static final int SYNC_DIRECTION_EMS_TO_NE = 0;

	public static final int SYNC_DIRECTION_NE_TO_EMS = 1;

	// ����ͬ������,Ĭ��Ϊ���ܵ���վ�� 0���ܵ���վ��1��վ������
	private int syncDirection = 0;

	public static final int MONITOR_STATE_STOP = 0;

	public static final int MONITOR_STATE_START = 1;

	// ���״̬��Ĭ��Ϊֹͣ
	private int monitorState = MONITOR_STATE_STOP;

	// ��վ����Map(Key:������, Value:����ֵ)
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
	 * ��ȡ16���ƵĻ�վID������8λǰ�油0
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
	 * �жϻ�վ�Ƿ��ǿ����õ�
	 * 
	 * @return
	 */
	public boolean isConfigurable() {
		// ֻ�����߹���״̬�²ſɽ����������ݵ���Ԫ(20140610)
		// return isOnlineManage();
		return (isOnlineManage() && isConnected());
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

	@Override
	public Enb clone() {
		try {
			return (Enb) super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * �жϸ���Ԫ�Ƿ�����ִ������ҵ��(�������á�������)
	 * 
	 * @return
	 */
	public boolean getFullTableOperation() {
		return isFullTableOperation.get();
	}

	/**
	 * ��������ҵ��(�������á�������)״̬ ���ؽ����true��ʾ���óɹ�,false��ʾ����ʧ��
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

	/**
	 * ����Ϊ�ѿ�վ
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
	 * ���ü��״̬
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
	 * �Ƿ����ڱ����
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
