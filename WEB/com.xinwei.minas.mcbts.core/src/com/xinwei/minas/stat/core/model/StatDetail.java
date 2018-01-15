/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

/**
 * 
 * ͳ����ϸ��Ϣ
 * 
 * @author fanhaoyu
 * 
 */

public class StatDetail {

	public static final int FLAG_SUCCESS = 1;

	public static final int FLAG_FAIL = 0;

	/**
	 * ͳ������
	 */
	private int timeType;

	/**
	 * ͳ�ƽ�ֹʱ��
	 */
	private long time;

	/**
	 * ͳ��ʱ����
	 */
	private long interval;

	/**
	 * ͳ�ƽ�� 0--ʧ�ܣ�1--�ɹ�
	 */
	private int flag;

	/**
	 * ȡ��ͳ�ƽ��
	 * 
	 * @return
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * ����ͳ�ƽ��
	 * 
	 * @param flag
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * ȡ��ͳ������ִ��ʱ��
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * ����ͳ������ִ��ʱ��
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * ȡ��ͳ������
	 * 
	 * @return
	 */
	public int getTimeType() {
		return timeType;
	}

	/**
	 * ����ͳ������
	 * 
	 * @param type
	 */
	public void setTimeType(int type) {
		this.timeType = type;
	}

	/**
	 * ȡ��ͳ������ִ�м��
	 * 
	 * @return
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * ����ͳ������ִ�м��
	 * 
	 * @param interval
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * ��ͳ����ϸ��Ϣת��Ϊ�ַ������
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Stat timeType:" + timeType + "\r\n");
		buf.append("Stat interval:" + interval + "\r\n");
		buf.append("Stat flag:" + (flag == 1 ? "Success" : "Fail") + "\r\n");
		buf.append("Stat time:" + time + "\r\n");
		return buf.toString();
	}
}
