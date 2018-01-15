/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.util.Date;

/**
 * 
 * 基站版本下载记录
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsVersionHistory implements java.io.Serializable,
		Comparable<McBtsVersionHistory> {

	// 索引
	private Long idx;

	// 基站ID
	private Long btsId;

	// 基站软件类型
	private Integer btsType;

	// 基站软件类型名称
	private String btsTypeName;

	// 基站软件版本
	private String version;

	// 基站版本下载操作状态
	private Integer actionResult;

	// 基站版本下载开始时间
	private Date startTime;

	// 基站版本下载结束时间
	private Date endTime;

	// 下载的进度,只对小基站(微蜂窝基站)有效
	private Integer downloadProgress;

	public McBtsVersionHistory() {
	}

	public McBtsVersionHistory(Long btsId, Integer btsType, String version,
			Integer actionResult, Date startTime) {
		super();
		this.btsId = btsId;
		this.btsType = btsType;
		this.version = version;
		this.actionResult = actionResult;
		this.startTime = startTime;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public Integer getBtsType() {
		return btsType;
	}

	public void setBtsType(Integer btsType) {
		this.btsType = btsType;
	}

	public String getBtsTypeName() {
		return btsTypeName;
	}

	public void setBtsTypeName(String btsTypeName) {
		this.btsTypeName = btsTypeName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getActionResult() {
		return actionResult;
	}

	public void setActionResult(Integer actionResult) {
		this.actionResult = actionResult;
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

	public Integer getDownloadProgress() {
		if (downloadProgress == null)
			return 0;
		return downloadProgress;
	}

	private void setDownloadProgress(Integer downloadProgress) {
		this.downloadProgress = downloadProgress;
	}

	public void updateDownloadProgress(Integer progress) {
		setDownloadProgress(progress);
	}

	@Override
	public int compareTo(McBtsVersionHistory other) {
		// 根据时间排序
		if (this.getEndTime() == null) {
			// 如果一个为空,一个不为空,不为空的在下边
			if (other.getEndTime() != null)
				return -1;
			else if (other.getEndTime() == null) {
				// 如果两个都为空,开始时间靠前的排在下边
				if (this.getStartTime().getTime() > other.getStartTime()
						.getTime())
					return -1;
				else if (this.getStartTime().getTime() < other.getStartTime()
						.getTime())
					return 1;
				else
					return 0;
			}
		} else if (this.getEndTime() != null) {
			if (other.getEndTime() == null)
				return 1;
			else if (other.getEndTime() != null) {
				// 如果两个都不为空,结束时间靠前的排在下边
				if (this.getEndTime().getTime() > other.getEndTime().getTime())
					return -1;
				else if (this.getEndTime().getTime() < other.getEndTime()
						.getTime())
					return 1;
				else
					return 0;
			}
		}
		return 0;
	}

	// 判断是否超时,超时返回true
	public boolean isOverTime(Long DEAD_TIME) {
		if (this.endTime != null) {
			return false;
		}
		long timeDiff = System.currentTimeMillis() - this.startTime.getTime();
		if (timeDiff > DEAD_TIME) {
			return true;
		} else {
			return false;
		}
	}
}
