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
 * 统计明细信息
 * 
 * @author fanhaoyu
 * 
 */

public class StatDetail {

	public static final int FLAG_SUCCESS = 1;

	public static final int FLAG_FAIL = 0;

	/**
	 * 统计类型
	 */
	private int timeType;

	/**
	 * 统计截止时间
	 */
	private long time;

	/**
	 * 统计时间间隔
	 */
	private long interval;

	/**
	 * 统计结果 0--失败，1--成功
	 */
	private int flag;

	/**
	 * 取得统计结果
	 * 
	 * @return
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * 设置统计结果
	 * 
	 * @param flag
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * 取得统计任务执行时间
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * 设置统计任务执行时间
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * 取得统计类型
	 * 
	 * @return
	 */
	public int getTimeType() {
		return timeType;
	}

	/**
	 * 设置统计类型
	 * 
	 * @param type
	 */
	public void setTimeType(int type) {
		this.timeType = type;
	}

	/**
	 * 取得统计任务执行间隔
	 * 
	 * @return
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * 设置统计任务执行间隔
	 * 
	 * @param interval
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * 将统计明细信息转换为字符串输出
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
