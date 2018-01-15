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
 * 终端实体类
 * 
 * @author tiance
 * 
 */

public class UserTerminal implements Serializable {
	// 关机
	public static final int STATUS_OFF = 0x00;
	// 活动态
	public static final int STATUS_ACTIVE = 0x01;
	// 停机
	public static final int STATUS_HALT = 0x02;
	// 盗机
	public static final int STATUS_STEAL = 0x09;
	// 未开户
	public static final int STATUS_NO_OPEN = 0x0a;

	// 开机注册
	public static final int REG_TYPE_ON_OPEN = 0x00;
	// 周期注册
	public static final int REG_TYPE_PERIOD = 0x01;

	// PID
	private String pid;
	// UID
	private String uid;
	// 终端状态
	private int status;
	// 终端别名
	private String alias;
	// 基站ID
	private long btsId;
	// SAG ID
	private long sagId;
	// 注册类型 00:开机注册 01:周期注册 02位置更新注册 03:切换
	private int regType;
	// 注册时间
	private Date regTime;
	// 硬件类型
	private String hwType;
	// 软件类型
	private int swType;
	// Rsv 保留字段
	private int rsv;
	// 软件当前版本
	private String activeSwVersion;
	// 软件备份版本
	private String standbySwVersion;
	// 硬件版本
	private String hwVersion;
	// 终端组别
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

	//返回16进制
	public String getHwType() {
		return hwType;
	}
	
	//返回十进制
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
