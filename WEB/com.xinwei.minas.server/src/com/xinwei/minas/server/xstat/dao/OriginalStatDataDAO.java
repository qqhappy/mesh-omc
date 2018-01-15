/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * 话务统计实体持久化接口
 * 
 * @author fanhaoyu
 * 
 */

public interface OriginalStatDataDAO {

	/**
	 * 批量保存统计数据实体
	 * 
	 * @param statItemList
	 * @throws Exception
	 */
	public void save(List<StatItem> statItemList) throws Exception;

	/**
	 * 按moId和起始结束时间查询
	 * 
	 * @param moId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<StatItem> queryBy(long moId, long startTime, long endTime)
			throws Exception;

	/**
	 * 删除指定时间段内的所有统计数据
	 * 
	 * @param startTime
	 * @param endTime
	 * @throws Exception
	 */
	public void delete(long startTime, long endTime) throws Exception;

	/**
	 * 根据条件查询原始数据
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
	public List<StatItem> queryBy(Map<Long, List<String>> entityMap,
			List<String> itemList, long startTime, long endTime)
			throws Exception;

}
