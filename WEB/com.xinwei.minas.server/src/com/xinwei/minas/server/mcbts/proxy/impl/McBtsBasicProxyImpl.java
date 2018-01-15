/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.impl;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.proxy.McBtsBasicProxy;

/**
 * 
 * McBts基本业务服务Proxy 
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBasicProxyImpl implements McBtsBasicProxy {

	/**
	 * 查询McBts基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws Exception {
		// TODO: 
		return null;
	}

	/**
	 * 配置McBts基本信息
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void config(McBts mcBts) throws Exception {

	}

}
