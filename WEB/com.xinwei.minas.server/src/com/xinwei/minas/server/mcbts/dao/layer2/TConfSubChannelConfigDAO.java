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

import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 空中链路通道选择配置信息DAO
 * 
 * @author jiayi
 * 
 */

public interface TConfSubChannelConfigDAO extends
		GenericDAO<SCGChannelConfigItem, Long> {

	/**
	 * 根据moId查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public List<SCGChannelConfigItem> queryByMoId(Long moId);

	/**
	 * 保存配置基本信息
	 * 
	 * @param moId
	 * @param configList
	 */
	public void saveOrUpdate(Long moId, List<SCGChannelConfigItem> configList);
}
