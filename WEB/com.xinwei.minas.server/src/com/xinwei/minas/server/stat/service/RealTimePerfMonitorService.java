/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.service;

import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * 实时性能监控服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface RealTimePerfMonitorService {

	/**
	 * 启动对某监视项的监视
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void start(String sessionId, MonitorItem item) throws Exception;

	/**
	 * 停止对某监视项的监视
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void stop(String sessionId, MonitorItem item) throws Exception;

	/**
	 * 客户端与服务器间的握手
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void handshake(String sessionId, MonitorItem item) throws Exception;

}
