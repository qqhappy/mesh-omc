/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.xstat.core.model.PreStatItem;

/**
 * 
 * 预统计数据持久化接口
 * 
 * @author fanhaoyu
 * 
 */

public interface PreStatDataDAO {

	/**
	 * 保存预统计数据
	 * 
	 * @param item
	 * @param preStatType
	 *            预统计类型，参考StatConstants中的预统计类型定义
	 * @throws Exception
	 */
	public void savePreStatItem(PreStatItem item, int preStatType)
			throws Exception;

	/**
	 * 批量保存预统计数据
	 * 
	 * @param itemList
	 * @param preStatType
	 *            预统计类型，参考StatConstants中的预统计类型定义
	 * @throws Exception
	 */
	public void savePreStatItem(List<PreStatItem> itemList, int preStatType)
			throws Exception;

	/**
	 * 根据条件查询小时预统计数据
	 * 
	 * @param entityMap
	 *            格式:key-moId,value=List[entityType#entityOid]
	 * @param itemList
	 *            统计项列表，格式:itemType#itemId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryHourPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception;

	/**
	 * 根据条件查询天预统计数据
	 * 
	 * @param entityMap
	 *            格式:key-moId,value=List[entityType#entityOid]
	 * @param itemList
	 *            统计项列表，格式:itemType#itemId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryDayPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception;

}
