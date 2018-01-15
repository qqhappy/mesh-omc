/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-12	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.Date;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * 基站批量升级模型
 * 
 * 
 * @author tiance
 * 
 */

public class UpgradeInfo implements Serializable {

	public static final int START_IMMEDIATELY = 1;
	public static final int START_SCHEDULED = 0;

	// 尚未开始
	public static final int STATUS_NOT_START = 0;
	// 正在下载
	public static final int STATUS_DOWNLOADING = 10;
	// 下载失败
	public static final int STATUS_DOWNLOAD_FAIL = 20;
	// 下载超时
	public static final int STATUS_DOWNLOAD_TIMEOUT = 30;
	// 下载完成
	public static final int STATUS_DOWNLOAD_FINISH = 40;
	// 升级命令已下发
	public static final int STATUS_UPGRADE = 50;
	// 升级失败
	public static final int STATUS_UPGRADE_FAIL = 60;
	// 基站未连接
	public static final int STATUS_MCBTS_DISCONNECTED = 70;
	// 基站离线管理
	public static final int STATUS_MCBTS_OFFLINE_MANAGE = 80;
	// 基站不存在
	public static final int STATUS_MCBTS_NOT_EXISTS = 90;
	// 升级被终止
	public static final int STATUS_TERMINATED = 100;

	// 升级目标
	public static final int TARGET_BBU = 0;
	public static final int TARGET_RRU = 1;
	public static final int TARGET_BBU_RRU = 2;

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
	// 预约执行时间
	private Date ScheduledTime;
	// 开始标识(立即执行/预约执行)
	private int startSign;
	// 开始下载升级时间
	private Date startTime;
	// 结束时间
	private Date endTime;

	private int errorCode;
	
	//消息序列号
	private int transactionId;

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

	public Date getScheduledTime() {
		return ScheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		ScheduledTime = scheduledTime;
	}

	public int getStartSign() {
		return startSign;
	}

	public void setStartSign(int startSign) {
		this.startSign = startSign;
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

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionId() {
		return transactionId;
	}

}
