/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 终端软件下载时使用的任务模型
 * 
 * 
 * @author tiance
 * 
 */

public class UTCodeDownloadTask implements Comparable, Serializable {
	public static final int OVERTIME = -1;
	public static final int UNDONE = -2;
	public static final int DONE = 0;

	public static final int PREVIOUS_TASK_STATUS_UNDONE = 1;

	private Long id;
	private Integer actionResult;
	private Date startTime;
	private Date endTime;
	private TerminalVersion terminalVersion;

	public UTCodeDownloadTask() {
		this.startTime = new Date();
		this.actionResult = UNDONE; // 初始未完成
	}

	public UTCodeDownloadTask(TerminalVersion terminalVersion) {
		this();
		this.terminalVersion = terminalVersion;
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

	public TerminalVersion getTerminalVersion() {
		return terminalVersion;
	}

	public void setTerminalVersion(TerminalVersion terminalVersion) {
		this.terminalVersion = terminalVersion;
	}

	public boolean isFinished() {
		return actionResult != UNDONE;
	}

	// 结束任务
	public void finish(Integer actionResult) {
		this.actionResult = actionResult;
		this.endTime = new Date();
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

	// 判断是否超时
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

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
