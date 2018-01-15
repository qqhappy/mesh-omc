/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 校准数据RF配置信息DAO
 * 
 * @author chenjunhua
 * 
 */

public interface TConfRfConfigDAO extends
		GenericDAO<RFConfig, Long> {

	/**
	 * 根据moId查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public RFConfig queryByMoId(Long moId);
}
