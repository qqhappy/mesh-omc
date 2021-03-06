/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 校准数据RF配置信息DAO
 * 
 * @author chenjunhua
 * 
 */

public interface TConfSubCalibGenConfigDAO extends
		GenericDAO<CalibGenConfigItem, Long> {

	/**
	 * 根据moId查询校准数据RF配置基本信息
	 * 
	 * @param moId
	 */
	public List<CalibGenConfigItem> queryByMoId(Long moId);

	/**
	 * 保存配置基本信息
	 * 
	 * @param moId
	 */
	public void saveOrUpdate(List<CalibGenConfigItem> psConfigList);
}
