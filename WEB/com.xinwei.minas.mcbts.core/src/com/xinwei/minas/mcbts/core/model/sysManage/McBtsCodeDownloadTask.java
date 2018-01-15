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
 * ��վ�������ʱʹ�õ�����ģ��
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
	 * ֮ǰ������������δ���
	 */
	public static final int PREVIOUS_TASK_STATUS_UNDONE = 1;
	/**
	 * ���ع����б���
	 */
	public static final int DOWNLOAD_WITH_ERROR = 2;

	private Long id;
	/**
	 * �����Ľ��: δ���, �����, ���س�ʱ����
	 */
	private Integer actionResult;
	/**
	 * ���صĿ�ʼʱ��
	 */
	private Date startTime;
	/**
	 * ���صĽ���ʱ��
	 */
	private Date endTime;
	/**
	 * �ϴλ�ý��ȵ�ʱ��
	 */
	private Date lastTime;
	/**
	 * ������ʷ����,�������ϸ������״��
	 */
	private McBtsVersionHistory mcBtsVersionHistory;

	// ��վ������ȵĳ�ʱʱ�� 30*1000 ��30�룩
	public static final int OVERTIME_TRANSFER_PROGRESS = 30 * 1000;

	public McBtsCodeDownloadTask() {
		this.startTime = new Date();
		this.actionResult = UNDONE; // ��ʼδ���
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

	// ��������
	public void finish(Integer actionResult) {
		this.actionResult = actionResult;
		this.endTime = new Date();
		this.mcBtsVersionHistory.setActionResult(actionResult);
		this.mcBtsVersionHistory.setEndTime(this.endTime);
	}

	/**
	 * ����actionResult�ĸ���״̬
	 * 
	 * @return -2:��ʱ, -1:δ���, 0:�ɹ�, >0:����error
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

	// �ж��Ƿ�ʱ,��ʱ����true
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
