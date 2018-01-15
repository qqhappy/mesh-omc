/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * �ն�ʵ����
 * 
 * @author tiance
 * 
 */

public class UserTerminal implements Serializable {
	// �ػ�
	public static final int STATUS_OFF = 0x00;
	// �̬
	public static final int STATUS_ACTIVE = 0x01;
	// ͣ��
	public static final int STATUS_HALT = 0x02;
	// ����
	public static final int STATUS_STEAL = 0x09;
	// δ����
	public static final int STATUS_NO_OPEN = 0x0a;

	// ����ע��
	public static final int REG_TYPE_ON_OPEN = 0x00;
	// ����ע��
	public static final int REG_TYPE_PERIOD = 0x01;

	// PID
	private String pid;
	// UID
	private String uid;
	// �ն�״̬
	private int status;
	// �ն˱���
	private String alias;
	// ��վID
	private long btsId;
	// SAG ID
	private long sagId;
	// ע������ 00:����ע�� 01:����ע�� 02λ�ø���ע�� 03:�л�
	private int regType;
	// ע��ʱ��
	private Date regTime;
	// Ӳ������
	private String hwType;
	// �������
	private int swType;
	// Rsv �����ֶ�
	private int rsv;
	// �����ǰ�汾
	private String activeSwVersion;
	// ������ݰ汾
	private String standbySwVersion;
	// Ӳ���汾
	private String hwVersion;
	// �ն����
	private String group;

	private List<UserTerminalNetwork> utNetwork;

	public UserTerminal() {

	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public long getSagId() {
		return sagId;
	}

	public void setSagId(long sagId) {
		this.sagId = sagId;
	}

	public int getRegType() {
		return regType;
	}

	public void setRegType(int regType) {
		this.regType = regType;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	//����16����
	public String getHwType() {
		return hwType;
	}
	
	//����ʮ����
	public String getDecimalHwType() {
		return Integer.valueOf(hwType, 16).toString();
	}
	
	public void setHwType(String hwType) {
		this.hwType = hwType;
	}

	public int getSwType() {
		return swType;
	}

	public void setSwType(int swType) {
		this.swType = swType;
	}

	public int getRsv() {
		return rsv;
	}

	public void setRsv(int rsv) {
		this.rsv = rsv;
	}

	public String getActiveSwVersion() {
		return activeSwVersion;
	}

	public void setActiveSwVersion(String activeSwVersion) {
		this.activeSwVersion = activeSwVersion;
	}

	public String getStandbySwVersion() {
		return standbySwVersion;
	}

	public void setStandbySwVersion(String standbySwVersion) {
		this.standbySwVersion = standbySwVersion;
	}

	public String getHwVersion() {
		return this.hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<UserTerminalNetwork> getUtNetwork() {
		return utNetwork;
	}

	public void setUtNetwork(List<UserTerminalNetwork> utNetwork) {
		this.utNetwork = utNetwork;
	}

	public String getHexBtsID() {
		StringBuilder hexBtsId = new StringBuilder(Long.toHexString(btsId));
		while (hexBtsId.length() < 8) {
			hexBtsId.insert(0, "0");
		}
		return hexBtsId.toString();
	}
}
