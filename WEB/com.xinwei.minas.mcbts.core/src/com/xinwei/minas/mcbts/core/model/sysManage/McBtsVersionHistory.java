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
 * ��վ�汾���ؼ�¼
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsVersionHistory implements java.io.Serializable,
		Comparable<McBtsVersionHistory> {

	// ����
	private Long idx;

	// ��վID
	private Long btsId;

	// ��վ�������
	private Integer btsType;

	// ��վ�����������
	private String btsTypeName;

	// ��վ����汾
	private String version;

	// ��վ�汾���ز���״̬
	private Integer actionResult;

	// ��վ�汾���ؿ�ʼʱ��
	private Date startTime;

	// ��վ�汾���ؽ���ʱ��
	private Date endTime;

	// ���صĽ���,ֻ��С��վ(΢���ѻ�վ)��Ч
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
		// ����ʱ������
		if (this.getEndTime() == null) {
			// ���һ��Ϊ��,һ����Ϊ��,��Ϊ�յ����±�
			if (other.getEndTime() != null)
				return -1;
			else if (other.getEndTime() == null) {
				// ���������Ϊ��,��ʼʱ�俿ǰ�������±�
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
				// �����������Ϊ��,����ʱ�俿ǰ�������±�
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
