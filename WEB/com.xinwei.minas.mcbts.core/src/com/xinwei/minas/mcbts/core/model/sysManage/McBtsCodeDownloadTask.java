/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 基站软件下载时使用的任务模型
 * 
 * @author tiance
 * 
 */

public class McBtsCodeDownloadTask implements Serializable {
	// actionResult
	public static final int OVERTIME = -1;
	public static final int UNDONE = -2;
	public static final int DONE = 0;

	/**
	 * 之前的下载任务尚未完成
	 */
	public static final int PREVIOUS_TASK_STATUS_UNDONE = 1;
	/**
	 * 下载过程中报错
	 */
	public static final int DOWNLOAD_WITH_ERROR = 2;

	private Long id;
	/**
	 * 操作的结果: 未完成, 已完成, 下载超时三种
	 */
	private Integer actionResult;
	/**
	 * 下载的开始时间
	 */
	private Date startTime;
	/**
	 * 下载的结束时间
	 */
	private Date endTime;
	/**
	 * 上次获得进度的时间
	 */
	private Date lastTime;
	/**
	 * 下载历史对象,保存更详细的下载状况
	 */
	private McBtsVersionHistory mcBtsVersionHistory;

	// 基站传输进度的超时时间 30*1000 （30秒）
	public static final int OVERTIME_TRANSFER_PROGRESS = 30 * 1000;

	public McBtsCodeDownloadTask() {
		this.startTime = new Date();
		this.actionResult = UNDONE; // 初始未完成
	}

	public McBtsCodeDownloadTask(McBtsVersionHistory mcBtsVersionHistory) {
		this();
		this.mcBtsVersionHistory = mcBtsVersionHistory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActionResult() {
		return actionResult;
	}

	public void setActionResult(Integer actionResult) {
		this.actionResult = actionResult;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public McBtsVersionHistory getMcBtsVersionHistory() {
		return mcBtsVersionHistory;
	}

	public void setMcBtsVersionHistory(McBtsVersionHistory mcBtsVersionHistory) {
		this.mcBtsVersionHistory = mcBtsVersionHistory;
	}

	public boolean isFinished() {
		return actionResult != UNDONE;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	// 结束任务
	public void finish(Integer actionResult) {
		this.actionResult = actionResult;
		this.endTime = new Date();
		this.mcBtsVersionHistory.setActionResult(actionResult);
		this.mcBtsVersionHistory.setEndTime(this.endTime);
	}

	/**
	 * 返回actionResult的各种状态
	 * 
	 * @return -2:超时, -1:未完成, 0:成功, >0:各种error
	 * 
	 */
	public int getStatus() {
		int ar = actionResult.intValue();
		if (ar == UNDONE) {
			if (isOverTime(System.currentTimeMillis())) {
				this.actionResult = OVERTIME;
				return OVERTIME;
			} else {
				return UNDONE;
			}
		} else if (ar == OVERTIME) {
			return OVERTIME;
		}
		return DONE;
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
