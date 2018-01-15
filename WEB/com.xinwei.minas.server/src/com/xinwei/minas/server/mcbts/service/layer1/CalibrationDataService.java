/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 校准数据配置业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationDataService extends ICustomService {

	/**
	 * 查询校准数据配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo queryCalibrationDataConfigByMoId(Long moId)
			throws Exception;

//	/**
//	 * 配置校准数据配置信息
//	 * 
//	 * @param locationArea
//	 * @throws Exception
//	 */
//	public void config(Long moId, CalibrationDataInfo setting) throws Exception;

	/**
	 * 配置校准数据配置信息
	 * 
	 * @param moId
	 * @param setting
	 * @param isSyncConfig
	 *            是否同步配置邻接表
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo setting,
			boolean isSyncConfig) throws Exception;

	/**
	 * 从网元获得校准数据配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public CalibrationDataInfo queryDeviceCalibrationDataConfigByMoId(Long moId)
			throws Exception;
}
