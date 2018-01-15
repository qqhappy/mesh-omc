/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service;

/**
 * 
 * 定时数据同步服务
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkDataSyncService {

	/*
	 * 获取最新数据
	 */
	public void updateData() throws Exception;

}