/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;

/**
 * 
 * eNB话务统计业务服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatBizService {

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
	 * 按条件查询统计数据
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryStatData(StatDataQueryCondition condition)
			throws Exception;

	/**
	 * 删除指定时间以前的所有统计数据，包括原始数据和预统计数据，
	 * 
	 * @param endTime
	 * @throws Exception
	 */
	public void deleteStatData(long startTime, long endTime) throws Exception;

}
