/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;

/**
 * 校准结果业务服务Proxy
 * 
 * @author jiayi
 * 
 */
public interface CalibResultProxy {

	/**
	 * 查询配置结果数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationResult query(Long moId) throws Exception;

	/**
	 * 查询配置结果数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<CalibrationResult> queryHistory(Long moId) throws Exception;
}
