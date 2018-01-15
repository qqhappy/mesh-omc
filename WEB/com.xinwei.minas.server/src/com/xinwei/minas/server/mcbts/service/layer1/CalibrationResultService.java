/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.io.File;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;

/**
 * 
 * 校准结果服务接口
 * 
 * 
 * @author zhaolingling
 * 
 */

public interface CalibrationResultService {

	/**
	 * 提供给校准数据用于展现天线发送和接收的校准参数的方法
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationResult getCalibrationResult(Long moId) throws Exception;

	/**
	 * 查询校准结果信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public Object[] queryByMoId(Long moId) throws Exception;

	/**
	 * 更新数据库
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void updateCalibGenericConfig(Long moId, File file) throws Exception;
}