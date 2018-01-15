/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * L1配置业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface L1GeneralSettingService extends ICustomService {

	/**
	 * 查询L1配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws Exception;

	/**
	 * 配置L1配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(L1GeneralSetting setting, boolean isSyncConfig)
			throws Exception;

	/**
	 * 从网元查询L1配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return GenericBizData
	 */
	public L1GeneralSetting queryFromNE(Long moId) throws Exception;
}
