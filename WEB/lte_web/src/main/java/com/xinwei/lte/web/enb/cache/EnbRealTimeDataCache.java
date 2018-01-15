/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.cache;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * eNB实时性能数据缓存
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealTimeDataCache {

	private Log log = LogFactory.getLog(EnbRealTimeDataCache.class);

	/**
	 * 最新数据的时间
	 */
	private long latestDataTime = 0;

	// 数据过期时间默认60s
	private int dataOvertime = 60;

	private static EnbRealTimeDataCache instance = new EnbRealTimeDataCache();

	private ScheduledExecutorService executorService;

	// key:moId, value:[key:time, value:dataList],时间格式yyyymmddhhmmss
	private Map<Long, Map<Long, List<EnbRealtimeItemData>>> dataMap = new HashMap<Long, Map<Long, List<EnbRealtimeItemData>>>();

	private EnbRealTimeDataCache() {
		// 过期数据删除任务1分钟执行一次
		executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(new RemoveOverTimeData(), 60, 60,
				TimeUnit.SECONDS);
	}

	public static EnbRealTimeDataCache getInstance() {
		return instance;
	}

	/**
	 * 将同一时刻的一组数据加入缓存
	 * 
	 * @param moId
	 * @param itemDataList
	 */
	public void addData(long moId, List<EnbRealtimeItemData> itemDataList) {
		if (itemDataList == null || itemDataList.isEmpty())
			return;
		long dataTime = itemDataList.get(0).getEndTime();
		// 记录最新数据的时间
		this.latestDataTime = dataTime;
		Map<Long, List<EnbRealtimeItemData>> itemDataMap = dataMap.get(moId);
		if (itemDataMap == null) {
			itemDataMap = new HashMap<Long, List<EnbRealtimeItemData>>();
			dataMap.put(moId, itemDataMap);
		}
		List<EnbRealtimeItemData> dataList = itemDataMap.get(dataTime);
		if (dataList == null) {
			dataList = new LinkedList<EnbRealtimeItemData>();
			itemDataMap.put(dataTime, dataList);
		}
		dataList.addAll(itemDataList);
	}

	/**
	 * 按eNB ID和统计项ID查询
	 * 
	 * @param moId
	 * @param itemId
	 * @return
	 */
	public List<EnbRealtimeItemData> queryDataBy(long moId, int itemId) {
		Map<Long, List<EnbRealtimeItemData>> itemMap = dataMap.get(moId);
		if (itemMap == null)
			return Collections.emptyList();
		List<EnbRealtimeItemData> resultList = new LinkedList<EnbRealtimeItemData>();
		// 筛选
		for (Long time : itemMap.keySet()) {
			List<EnbRealtimeItemData> dataList = itemMap.get(time);
			for (EnbRealtimeItemData dataItem : dataList) {
				int id = dataItem.getItemId();
				if (itemId == id) {
					resultList.add(dataItem);
				}
			}
		}
		return resultList;
	}

	/**
	 * 查询指定统计项一定时间范围内的数据
	 * 
	 * @param moId
	 * @param itemId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<EnbRealtimeItemData> queryDataBy(long moId, int itemId,
			long startTime, long endTime) {
		Map<Long, List<EnbRealtimeItemData>> itemMap = dataMap.get(moId);
		if (itemMap == null)
			return Collections.emptyList();
		List<EnbRealtimeItemData> resultList = new LinkedList<EnbRealtimeItemData>();
		for (Long dataTime : itemMap.keySet()) {
			// 按时间筛选
			if (dataTime >= startTime && dataTime <= endTime) {
				List<EnbRealtimeItemData> dataList = itemMap.get(dataTime);
				// 按itemId筛选
				for (EnbRealtimeItemData dataItem : dataList) {
					int id = dataItem.getItemId();
					if (itemId == id) {
						resultList.add(dataItem);
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 以当前缓存中的最新数据的时间为基准，查询最新时间以前secondeOffset秒的数据
	 * 
	 * @param moId
	 * @param itemId
	 * @param secondOffset
	 * @return
	 */
	public List<EnbRealtimeItemData> queryLatestData(long moId, int itemId,
			int secondOffset) {
		Map<Long, List<EnbRealtimeItemData>> itemMap = dataMap.get(moId);
		if (itemMap == null)
			return Collections.emptyList();
		long endTime = this.latestDataTime;
		long startTime = decreateTime(endTime, secondOffset);
		return queryDataBy(moId, itemId, startTime, endTime);
	}

	/**
	 * 删除缓存中beforeTime以前的所有数据
	 * 
	 * @param beforeTime
	 * @return 删除的数据个数
	 */
	private long removeData(long beforeTime) {
		Set<Long> enbIdSet = new HashSet<Long>(dataMap.keySet());
		long removeCount = 0;
		for (Long enbId : enbIdSet) {
			Map<Long, List<EnbRealtimeItemData>> itemMap = dataMap.get(enbId);
			Set<Long> timeSet = new HashSet<Long>(itemMap.keySet());
			for (Long time : timeSet) {
				if (time < beforeTime) {
					List<EnbRealtimeItemData> deleteList = itemMap.remove(time);
					removeCount += deleteList.size();
				}
			}
			// 如果某moId的数据为空，则将该key移除
			if (itemMap.keySet().isEmpty()) {
				dataMap.remove(enbId);
			}
		}
		return removeCount;
	}

	/**
	 * 在time的基础上减去secondCount秒
	 * 
	 * @param time
	 * @param secondCount
	 * @return
	 */
	private long decreateTime(long time, int secondCount) {
		long millSecond = DateUtils.getMillisecondTimeFromBriefTime(time);
		// 最新时间减去secondeOffset
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(millSecond));
		calendar.add(Calendar.SECOND, 0 - secondCount);
		return DateUtils.getBriefTimeFromMillisecondTime(calendar
				.getTimeInMillis());
	}

	/**
	 * 设置数据超时时间，单位秒
	 * 
	 * @param dataOvertime
	 */
	public void setDataOvertime(int dataOvertime) {
		this.dataOvertime = dataOvertime;
	}

	public int getDataOvertime() {
		return dataOvertime;
	}

	class RemoveOverTimeData implements Runnable {

		@Override
		public void run() {
			try {
				if (latestDataTime == 0)
					return;
				long beforeTime = decreateTime(latestDataTime, dataOvertime);
				log.info("remove overtime realtime data. latestDataTime="
						+ latestDataTime + ", beforeTime=" + beforeTime);
				// 删除过期数据
				long removeCount = removeData(beforeTime);
				log.info("remove overtime realtime data count=" + removeCount);
			} catch (Throwable e) {
				log.warn("remove overtime data with error.", e);
			}
		}

	}

}
