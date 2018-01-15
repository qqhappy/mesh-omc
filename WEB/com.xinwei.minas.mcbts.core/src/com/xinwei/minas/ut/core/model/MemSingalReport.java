/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * MEM��Ϣ�ϱ�ʵ��
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class MemSingalReport implements Serializable{
	
	//���عر�
	public static final int SWITCH_CLOSE = 0;
	
	//���ش�
	public static final int SWITCH_OPEN = 1;
	
	private Long idx;
	
	//�ն�PID
	private long pid;
	
	//�ϱ�����
	
	private int switchFlag;
	
	//�ϱ�ʱ����
	private long distance;
	
	//��ʼ�ϱ�ʱ��
	private Date lastStartTime;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(int switchFlag) {
		this.switchFlag = switchFlag;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public Date getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	
	
}
