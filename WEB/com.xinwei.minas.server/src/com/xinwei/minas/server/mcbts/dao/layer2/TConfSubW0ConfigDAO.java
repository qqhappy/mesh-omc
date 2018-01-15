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

import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 空中链路W0配置信息DAO
 * 
 * @author jiayi
 * 
 */

public interface TConfSubW0ConfigDAO extends GenericDAO<W0ConfigItem, Long> {

	/**
	 * 根据moId查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public List<W0ConfigItem> queryByMoId(Long moId);

	/**
	 * 保存配置基本信息
	 * 
	 * @param moId
	 */
	public void saveOrUpdate(List<W0ConfigItem> configList);
}
