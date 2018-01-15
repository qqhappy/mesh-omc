/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.basic;

import java.io.Serializable;

/**
 * 
 * NK���ݱ���������
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class ZkBackupTask implements Serializable {

	public static final int INTVAL_30MIN = 0;

	public static final int INTVAL_PERHOUR = 1;

	public static final int INTVAL_PER6HOUR = 2;

	public static final int STATE_CLOSED = 0;

	public static final int STATE_OPEN = 1;

	// ����ID
	private int id;

	// ����ִ�м��
	private String interval;

	// �����ϴ�ִ�е�ʱ��
	private String lastTime;

	// ����״̬���ѿ������ѹر�
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}