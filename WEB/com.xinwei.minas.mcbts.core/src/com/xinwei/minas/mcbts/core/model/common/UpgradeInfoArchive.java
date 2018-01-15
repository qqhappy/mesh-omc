/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * UpgradeInfo的归档对象
 * 
 * @author tiance
 * 
 */

public class UpgradeInfoArchive implements Serializable {
	// 数据库存储的ID
	private long idx;
	// 基站moId
	private long moId;
	// 基站ID(16进制,即McBts中的getHexBtsId())
	private String btsId;
	// 基站名称
	private String name;
	// 基站类型
	private int btsType;
	// 状态
	private int status;
	// 下载的版本
	private String downloadVersion;
	// 当前版本,光纤拉远基站显示BBU版本
	private String version;
	// mcu版本
	private String mcuVersion;
	// fpga版本
	private String fpgaVersion;
	// 光纤拉远的升级目标
	private int target;
	// 开始标识(立即执行/预约执行)
	private int startSign;
	// 预约执行时间
	private Date ScheduledTime;
	// 开始下载升级时间
	private Date startTime;
	// 结束时间
	private Date endTime;

	private int errorCode;

	public UpgradeInfoArchive() {
		super();
	}

	public UpgradeInfoArchive(UpgradeInfo upgradeInfo) {
		super();

		parse(upgradeInfo);
	}

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getBtsId() {
		return btsId;
	}

	public void setBtsId(String btsId) {
		this.btsId = btsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBtsType() {
		return btsType;
	}

	public void setBtsType(int btsType) {
		this.btsType = btsType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDownloadVersion() {
		return downloadVersion;
	}

	public void setDownloadVersion(String downloadVersion) {
		this.downloadVersion = downloadVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMcuVersion() {
		return mcuVersion;
	}

	public void setMcuVersion(String mcuVersion) {
		this.mcuVersion = mcuVersion;
	}

	public String getFpgaVersion() {
		return fpgaVersion;
	}

	public void setFpgaVersion(String fpgaVersion) {
		this.fpgaVersion = fpgaVersion;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getStartSign() {
		return startSign;
	}

	public void setStartSign(int startSign) {
		this.startSign = startSign;
	}

	public Date getScheduledTime() {
		return ScheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		ScheduledTime = scheduledTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 转换UpgradeInfo模型为UpgradeInfoArchive模型
	 * 
	 * @param info
	 */
	private void parse(UpgradeInfo info) {
		this.idx = info.getIdx();
		this.moId = info.getMoId();
		this.btsId = info.getBtsId();
		this.btsType = info.getBtsType();
		this.status = info.getStatus();
		this.downloadVersion = info.getDownloadVersion();
		this.version = info.getVersion();
		this.mcuVersion = info.getMcuVersion();
		this.fpgaVersion = info.getFpgaVersion();
		this.target = info.getTarget();
		this.startSign = info.getStartSign();
		this.ScheduledTime = info.getScheduledTime();
		this.startTime = info.getStartTime();
		this.endTime = info.getEndTime();
		this.errorCode = info.getErrorCode();
	}

}
