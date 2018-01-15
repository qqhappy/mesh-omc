/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;
import com.xinwei.minas.server.mcbts.proxy.layer1.CalibResultProxy;

/**
 * 
 * 校准结果业务服务Proxy
 * 
 * @author jiayi
 * 
 */

public class CalibResultProxyImpl implements CalibResultProxy {

	public CalibResultProxyImpl() {
	}

	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationResult query(Long moId) throws Exception {
		// TODO: 查询最新校准结果实现
		return null;
	}

	/**
	 * 查询校准结果历史数据
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public List<CalibrationResult> queryHistory(Long moId) throws Exception {
		// TODO: 查询最新校准结果实现
		List<CalibrationResult> result = new ArrayList<CalibrationResult>();
		return result;
	}
}
