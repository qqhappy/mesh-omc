/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.dao;

import java.util.Collection;
import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;

/**
 * 
 * 统计项配置持久化接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatItemConfigDAO {

	/**
	 * 查询所有Counter配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CounterItemConfig> queryCounterConfigs() throws Exception;

	/**
	 * 查询所有KPI配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<KpiItemConfig> queryKpiConfigs() throws Exception;

	/**
	 * 保存Counter统计项配置
	 * 
	 * @param configs
	 * @throws Exception
	 */
	public void saveCounterConfig(Collection<CounterItemConfig> configs)
			throws Exception;

	/**
	 * 保存KPI统计项配置
	 * 
	 * @param configs
	 * @throws Exception
	 */
	public void saveKpiConfig(Collection<KpiItemConfig> configs)
			throws Exception;

}
