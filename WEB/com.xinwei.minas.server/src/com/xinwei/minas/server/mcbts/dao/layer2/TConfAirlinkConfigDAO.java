/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer2;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 空中链路配置信息DAO
 * 
 * @author jiayi
 * 
 */

public interface TConfAirlinkConfigDAO extends GenericDAO<AirlinkConfig, Long> {

	/**
	 * 根据moId查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public AirlinkConfig queryByMoId(Long moId);
	
	
	/**
	 * 根据moId列表查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public List<AirlinkConfig> queryByMoIdList(List<Long> moIds);
}
