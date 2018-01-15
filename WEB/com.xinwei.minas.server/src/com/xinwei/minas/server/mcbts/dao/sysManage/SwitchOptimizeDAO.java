/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;

/**
 * 
 * 切换优化开关配置DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface SwitchOptimizeDAO {

	/**
	 * 查询全部配置
	 * 
	 * @return
	 */
	public List<SwitchOptimizeConfig> queryAll();

	/**
	 * 更新指定配置
	 * 
	 * @param config
	 */
	public void saveOrUpdate(SwitchOptimizeConfig config);

}
