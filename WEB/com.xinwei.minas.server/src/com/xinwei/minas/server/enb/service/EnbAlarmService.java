/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

/**
 * eNB告警服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbAlarmService {

	/**
	 * 对指定网元进行告警同步操作
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void syncAlarm(long moId) throws Exception;

}
