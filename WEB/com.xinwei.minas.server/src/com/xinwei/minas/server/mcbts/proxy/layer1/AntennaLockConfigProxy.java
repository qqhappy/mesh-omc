/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-8-7	| fanhaoyu		 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;

/**
 * 天线闭锁配置业务服务Proxy
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigProxy {

	/**
	 * 配置网元业务数据
	 * 
	 * @param lockConfig
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws Exception;

}