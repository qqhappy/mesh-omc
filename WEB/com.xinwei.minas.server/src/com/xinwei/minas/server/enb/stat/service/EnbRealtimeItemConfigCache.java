/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.server.enb.stat.dao.EnbRealtimeItemConfigDAO;

/**
 * 
 * eNB实时性能统计项配置缓存
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeItemConfigCache {

	private static EnbRealtimeItemConfigCache instance = new EnbRealtimeItemConfigCache();

	private Map<Integer, EnbRealtimeItemConfig> itemMap;

	// tagId , itemIdList
	private Map<Integer, List<Integer>> tagMap;

	private EnbRealtimeItemConfigCache() {
	}

	public static EnbRealtimeItemConfigCache getInstance() {
		return instance;
	}

	public synchronized void initialize(
			EnbRealtimeItemConfigDAO enbRealtimeItemConfigDAO) throws Exception {
		if (itemMap == null) {
			itemMap = new LinkedHashMap<Integer, EnbRealtimeItemConfig>();
		}
		if (tagMap == null) {
			tagMap = new LinkedHashMap<Integer, List<Integer>>();
		}
		List<EnbRealtimeItemConfig> configList = enbRealtimeItemConfigDAO
				.queryItemConfig();
		if (configList != null) {
			for (EnbRealtimeItemConfig config : configList) {
				int itemId = config.getItemId();
				itemMap.put(itemId, config);

				int tagId = config.getTagId();
				List<Integer> itemIdList = tagMap.get(tagId);
				if (itemIdList == null) {
					itemIdList = new LinkedList<Integer>();
					tagMap.put(tagId, itemIdList);
				}
				itemIdList.add(itemId);
			}
		}
	}

	/**
	 * 获取所有统计项配置
	 * 
	 * @return
	 */
	public Map<Integer, EnbRealtimeItemConfig> getAllItemConfigs() {
		return itemMap;
	}

	/**
	 * 获取tag中包含的itemId列表
	 * 
	 * @param tagId
	 * @return
	 */
	public List<Integer> getTagInnerItems(int tagId) {
		if (tagMap == null)
			return null;
		return tagMap.get(tagId);
	}

	/**
	 * 获取tag列表
	 * 
	 * @return
	 */
	public List<Integer> getTagList() {
		if (tagMap == null)
			return null;
		return new LinkedList<Integer>(tagMap.keySet());
	}

	public EnbRealtimeItemConfig getConfig(int itemId) {
		if (itemMap == null)
			return null;
		return itemMap.get(itemId);
	}

}
