/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.proxy;

/**
 * 
 * eNB实时性能监控代理接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeMonitorProxy {

	/**
	 * 开始上报实时性能数据
	 * 
	 * @param enbId
	 * @param intervalFlag
	 * @throws Exception
	 */
	public void start(long enbId, int intervalFlag) throws Exception;

	/**
	 * 停止上报实时性能数据
	 * 
	 * @param enbId
	 * @throws Exception
	 */
	public void stop(long enbId) throws Exception;

}
