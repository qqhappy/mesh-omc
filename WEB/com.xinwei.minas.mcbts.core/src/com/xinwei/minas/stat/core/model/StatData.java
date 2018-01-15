/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类简要描述
 * 
 * @author fanhaoyu
 * 
 */

public class StatData implements Copyable<StatData> {

	private long btsId;

	private long collectTime;

	private long interval;

	private int timeType;

	private Map<Integer, Double> itemMap;

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public Map<Integer, Double> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<Integer, Double> itemMap) {
		this.itemMap = itemMap;
	}

	/**
	 * 获取统计数据包含的所有统计项数据的列表
	 * 
	 * @return
	 */
	public List<SingleStatItemData> getItemDatas() {
		List<SingleStatItemData> itemDataList = new ArrayList<SingleStatItemData>();
		if (itemMap != null) {
			for (Integer itemId : itemMap.keySet()) {
				SingleStatItemData itemData = new SingleStatItemData();
				itemData.setBtsId(btsId);
				itemData.setCollectTime(collectTime);
				itemData.setInterval(interval);
				itemData.setTimeType(timeType);
				itemData.setItemId(itemId);
				itemData.setValue(itemMap.get(itemId));
				itemDataList.add(itemData);
			}
		}
		return itemDataList;
	}

	public SingleStatItemData getItemData(int itemId) {
		if (itemMap == null)
			return null;
		if (itemMap.get(itemId) == null) {
			return null;
		}
		SingleStatItemData itemData = new SingleStatItemData();
		itemData.setBtsId(btsId);
		itemData.setCollectTime(collectTime);
		itemData.setInterval(interval);
		itemData.setTimeType(timeType);
		itemData.setItemId(itemId);
		itemData.setValue(itemMap.get(itemId));
		return itemData;
	}

	public void addItemData(int itemId, double itemData) {
		if (itemMap == null)
			itemMap = new HashMap<Integer, Double>();
		itemMap.put(itemId, itemData);
	}

	@Override
	public StatData copy() {
		StatData statData = new StatData();
		statData.setBtsId(btsId);
		statData.setCollectTime(collectTime);
		statData.setInterval(interval);
		statData.setTimeType(timeType);
		Map<Integer, Double> itemDataMap = new HashMap<Integer, Double>();
		for (Integer itemId : itemMap.keySet()) {
			itemDataMap.put(itemId, itemMap.get(itemId));
		}
		statData.setItemMap(itemDataMap);
		return statData;
	}

	@Override
	public String toString() {
		return "StatData [btsId=" + btsId + ", collectTime=" + collectTime
				+ ", interval=" + interval + ", timeType=" + timeType
				+ ", itemMap=" + itemMap + "]";
	}

}
