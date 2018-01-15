/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 统计数据查询条件
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class StatDataQueryCondition implements Serializable {

	/**
	 * 统计间隔，15分钟
	 */
	public static final int INTERVAL_FIFTEEN_MINUTES = 1;

	/**
	 * 统计间隔，1小时
	 */
	public static final int INTERVAL_ONE_HOUR = 2;

	/**
	 * 统计间隔，1天
	 */
	public static final int INTERVAL_ONE_DAY = 3;

	private long startTime;

	private long endTime;

	private int interval;

	private List<String> itemList;

	private Map<Long, List<String>> entityMap = new HashMap<Long, List<String>>();

	public long getStartTime() {
		return startTime;
	}

	/**
	 * 设置数据开始时间，格式YYYYMMDDhhmmss
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	/**
	 * 设置数据结束时间，格式YYYYMMDDhhmmss
	 * 
	 * @param endTime
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getInterval() {
		return interval;
	}

	/**
	 * 设置统计间隔
	 * 
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * 设置要查询的统计项列表，格式：统计项类型#统计项ID
	 * 
	 * @param itemList
	 */
	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	public List<String> getItemList() {
		return itemList;
	}

	/**
	 * 添加要查询的实体列表，格式：entityType#entityOid
	 * 
	 * @param entityList
	 */
	public void addEntity(long moId, String entityType, String entityOid) {
		List<String> entityList = entityMap.get(moId);
		if (entityList == null) {
			entityList = new ArrayList<String>();
			entityMap.put(moId, entityList);
		}
		entityList.add(entityType + StatConstants.DEFAULT_SPLIT + entityOid);
	}

	public Map<Long, List<String>> getEntityMap() {
		return entityMap;
	}

}
