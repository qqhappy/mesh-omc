/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;

/**
 * 空中链路配置业务服务Proxy
 * 
 * @author jiayi
 * 
 */
public interface AirlinkProxy {

	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @param comparableMode
	 * @return
	 * @throws Exception
	 */
	public AirlinkConfig query(Long moId, ChannelComparableMode comparableMode)
			throws Exception;

	/**
	 * 配置网元业务数据
	 * 
	 * @param moId
	 * @param setting
	 * @param comparableMode
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig setting,
			ChannelComparableMode comparableMode) throws Exception;
}
