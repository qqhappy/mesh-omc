/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-12-2	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service.impl;



/**
 * 
 * 基站健康检查管理类
 * 
 * 
 * @author chenlong
 * 
 */

public class EnbCheckTaskManager {

	private static EnbCheckTaskManager instance = new EnbCheckTaskManager();
	
	public static EnbCheckTaskManager getInstance() {
		if(null == instance) {
			return new EnbCheckTaskManager();
		}
		return instance;
	}
	
	private final int FREE = 0;
	
	private final int QUERYING = 1;
	
	private int queryStatus = FREE;
	
	private long queryingTime = 0;
	
	private final long OUT_TIME = 1000 * 60 * 5;
	
	/**
	 * 查询任务是否空闲
	 * @return
	 */
	public boolean isFree() {
		if(FREE == queryStatus) {
			return true;
		}
		if(QUERYING == queryStatus) {
			long currentTime = System.currentTimeMillis();
			if(currentTime - queryingTime > OUT_TIME) {
				setFree();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置查询状态为空闲
	 */
	public void setFree() {
		queryStatus = FREE;
	}
	
	/**
	 * 设置查询状态为查询中
	 */
	public void setQuerying() {
		queryStatus = QUERYING;
		queryingTime = System.currentTimeMillis();
	}
}
