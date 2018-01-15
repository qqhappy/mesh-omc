/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;

/**
 * 
 * eNB实时性能监控服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeMonitorService {

	/**
	 * 查询实时性能统计项配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception;

	/**
	 * 开始监控某eNB
	 * 
	 * @param sessionId
	 * @param moId
	 * @throws Exception
	 */
	public void startMonitor(String sessionId, long moId) throws Exception;

	/**
	 * 停止监控某eNB
	 * 
	 * @param sessionId
	 * @param moId
	 * @throws Exception
	 */
	public void stopMonitor(String sessionId, long moId) throws Exception;

}
