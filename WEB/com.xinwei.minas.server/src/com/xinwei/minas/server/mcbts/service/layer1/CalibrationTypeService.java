/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 校准类型配置业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationTypeService extends ICustomService{

	/**
	 * 查询校准类型配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationType queryByMoId(Long moId) throws Exception;

	/**
	 * 配置校准类型配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(CalibrationType setting) throws Exception;

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationType queryFromNE(Long moId) throws Exception;
}
