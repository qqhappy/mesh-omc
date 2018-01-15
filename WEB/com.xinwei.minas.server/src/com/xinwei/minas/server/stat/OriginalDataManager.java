/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.analyze.DataAnalyzer;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * 原始数据管理器
 * 
 * @author fanhaoyu
 * 
 */

public class OriginalDataManager {

	private Log log = LogFactory.getLog(OriginalDataManager.class);

	private static OriginalDataManager instance = new OriginalDataManager();

	/**
	 * 日统计图的统计间隔
	 */
	private long dailyStatInterval;

	/**
	 * 统计数据缓存, key-btsId, value-指定设备的原始统计数值序列Map
	 */
	private Map<Long, Map<Long, List<StatData>>> statDataCache = new HashMap<Long, Map<Long, List<StatData>>>();

	/**
	 * 数据分析器
	 */
	private DataAnalyzer dataAnalyzer;

	/**
	 * 数据处理线程池
	 */
	private ThreadPoolExecutor dataProcessThreadPool = null;

	private OriginalDataManager() {
		int threadNum = SystemContext.getInstance().getMaxThreadNum();
		dataProcessThreadPool = new ThreadPoolExecutor(1, threadNum, 15,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static OriginalDataManager getInstance() {
		return instance;
	}

	/**
	 * 初始化原始数据管理器
	 * 
	 * @param analysisAgent
	 *            分析系统Agent
	 * @param dataAnalyzer
	 *            数据分析器
	 * @param dailyStatInterval
	 *            日数据统计间隔,单位:秒
	 * @throws Exception
	 */
	public void initialize(DataAnalyzer dataAnalyzer, long dailyStatInterval)
			throws Exception {
		this.dataAnalyzer = dataAnalyzer;
		this.dailyStatInterval = dailyStatInterval;
		if (SystemContext.getInstance().saveRealTimeData()) {
			// 系统启动后,首先对OriginalDataManager进行初始化,从数据库中装载1小时的实时数据到缓存
			loadData2Cache();
		}
	}

	/**
	 * 装载一小时数据到缓存
	 * 
	 * @throws Exception
	 */
	private void loadData2Cache() throws Exception {
		long currentTimeSeq = calcTimeSeqBy(System.currentTimeMillis());

		long endTime = calcStartTimeBy(currentTimeSeq);
		long startTime = endTime - 1000 * 60 * 60;

		List<StatData> statDatas = SystemContext.getInstance().getStatDataDAO()
				.getData(startTime, endTime, StatUtil.COLLECT_TYPE_REALTIME);

		if (statDatas == null || statDatas.isEmpty())
			return;

		for (StatData statData : statDatas) {
			long btsId = statData.getBtsId();
			if (!statDataCache.containsKey(btsId)) {
				Map<Long, List<StatData>> map = new HashMap<Long, List<StatData>>();
				statDataCache.put(btsId, map);
			}
			// 计算当前数据所属时间片
			long timeSeq = calcTimeSeqBy(statData.getCollectTime());
			Map<Long, List<StatData>> oneTimeSeqDataMap = statDataCache
					.get(btsId);
			if (!oneTimeSeqDataMap.containsKey(timeSeq)) {
				oneTimeSeqDataMap.put(timeSeq, new ArrayList<StatData>());
			}
			List<StatData> itemDataList = oneTimeSeqDataMap.get(timeSeq);
			// 增加采集数据
			itemDataList.add(statData);
		}
	}

	/**
	 * 监控数据实时上报
	 * 
	 * @param itemData
	 */
	private void notifyRealTimeData(SingleStatItemData itemData) {
		long btsId = itemData.getBtsId();
		int itemId = itemData.getItemId();
		// 如果该统计项正在被监视
		if (MonitorManager.getInstance().isMonitoring(
				new MonitorItem(btsId, itemId))) {
			// 将数据上报分析系统
			try {
				ClientNotifier.getInstance().sendData(itemData);
			} catch (Exception e) {
				log.error(
						"OriginalDataManager send SingleStatItemData to client error.",
						e);
			}
		}
	}

	/**
	 * 增加原始采集数据
	 * 
	 * @param statData
	 *            原始采集数据
	 */
	public void add(StatData statData) {
		// 监控数据实时上报
		for (SingleStatItemData itemData : statData.getItemDatas()) {
			notifyRealTimeData(itemData);
		}
		// 将数据加入缓存
		addData2Cache(statData);

	}

	private void addData2Cache(StatData statData) {
		long collectTime = statData.getCollectTime();
		long btsId = statData.getBtsId();
		if (!statDataCache.containsKey(btsId)) {
			Map<Long, List<StatData>> map = new HashMap<Long, List<StatData>>(
					32);
			statDataCache.put(btsId, map);
		}
		Map<Long, List<StatData>> oneTimeSeqDataMap = statDataCache.get(btsId);
		// 根据统计时间计算时间片
		long timeSeq = calcTimeSeqBy(collectTime);
		if (!oneTimeSeqDataMap.containsKey(timeSeq)) {
			List<StatData> dataList = new ArrayList<StatData>(16);
			oneTimeSeqDataMap.put(timeSeq, dataList);
		}
		List<StatData> dataList = oneTimeSeqDataMap.get(timeSeq);
		// 增加采集数据
		dataList.add(statData);

		// 如果满足归并条件,则归并上个时间片内的原始数据
		if (shouldMergeData(dataList)) {
			long lastTimeSeq = timeSeq - 1;
			List<StatData> lastDataList = oneTimeSeqDataMap.get(lastTimeSeq);
			if (lastDataList == null) {
				return;
			}
			// 数据处理：待归并数据存库、数据归并
			dataProcessThreadPool.execute(new DataProcessTask(lastTimeSeq,
					lastDataList));

			// 删除超过一个小时的数据
			Iterator<Long> keyItr = oneTimeSeqDataMap.keySet().iterator();
			List<Long> removeList = new ArrayList<Long>();

			while (keyItr.hasNext()) {
				long _timeSeq = keyItr.next();
				if (!isTimeSeqInOneHour(_timeSeq, timeSeq)) {
					removeList.add(_timeSeq);
				}
			}
			log.debug("Remove overtime RealTimeData in cache. number="
					+ removeList.size());
			for (int i = 0; i < removeList.size(); i++) {
				long key = removeList.get(i);
				oneTimeSeqDataMap.remove(key);
			}
			removeList = null;
		}
	}

	/**
	 * 根据当前时间片内统计数据个数，判断是否该归并上个时间片的数据
	 * 
	 * @param currentDataList
	 *            当前时间片内的数据列表
	 * @return 是否该归并上个时间片的数据
	 */
	private boolean shouldMergeData(List<StatData> currentDataList) {
		return (currentDataList != null && currentDataList.size() == 2);
	}

	/**
	 * 判断两个时间片序列是否在一个小时范围内
	 * 
	 * @param timeSeq1
	 *            时间片序列
	 * @param timeSeq2
	 *            时间片序列
	 * @return true-两个时间片序列在一个小时范围内, false-两个时间片序列不在一个小时范围内
	 */
	public boolean isTimeSeqInOneHour(long timeSeq1, long timeSeq2) {
		long timeSeqDiff = Math.abs(timeSeq1 - timeSeq2);
		if (timeSeqDiff <= 60 * 60 / dailyStatInterval) {
			return true;
		}
		return false;
	}

	/**
	 * 返回指定基站和统计项最近一小时的原始数据
	 * 
	 * @return 指定基站和统计项最近一小时的原始数据列表
	 */
	public List<SingleStatItemData> getDataOfRecentOneHour(long btsId,
			int itemId) {
		Map<Long, List<StatData>> map = statDataCache.get(btsId);
		List<SingleStatItemData> itemDataList = new ArrayList<SingleStatItemData>();
		if (map == null) {
			return itemDataList;
		}
		long currentTime = System.currentTimeMillis();
		long currentTimeSeq = calcTimeSeqBy(currentTime);
		Iterator<Long> keyItr = map.keySet().iterator();
		while (keyItr.hasNext()) {
			long timeSeq = keyItr.next();
			if (isTimeSeqInOneHour(timeSeq, currentTimeSeq)) {
				for (StatData statData : map.get(timeSeq)) {
					SingleStatItemData itemData = statData.getItemData(itemId);
					if (itemData != null) {
						itemDataList.add(itemData);
					}
				}
			}
		}
		return itemDataList;
	}

	/**
	 * 根据时间计算时间片序列
	 * 
	 * @param time
	 *            时间
	 * @return 时间片序列
	 */
	public long calcTimeSeqBy(long time) {
		return time / 1000 / dailyStatInterval;
	}

	/**
	 * 根据时间片序列计算起始时间
	 * 
	 * @param timeSeq
	 *            时间片序列
	 * @return 起始时间
	 */
	public long calcStartTimeBy(long timeSeq) {
		return timeSeq * 1000 * dailyStatInterval;
	}

	/**
	 * 数据处理任务
	 * 
	 * @author chenjunhua
	 * 
	 */
	class DataProcessTask implements Runnable {

		private long timeSeq;

		private List<StatData> dataList;

		public DataProcessTask(long timeSeq, List<StatData> dataList) {
			this.timeSeq = timeSeq;
			this.dataList = dataList;
		}

		public void run() {
			// 如果配置为保存实时数据
			if (SystemContext.getInstance().saveRealTimeData()) {
				saveStatDataToDB();
			}
			mergeData();
		}

		private void saveStatDataToDB() {
			try {
				long startTime = System.currentTimeMillis();
				SystemContext.getInstance().getStatDataDAO().saveData(dataList);
				long finishTime = System.currentTimeMillis();
				long costTime = finishTime - startTime;
				log.debug("Save realTimeData to DB. startTime=" + startTime
						+ " , finishTime=" + finishTime + ", costTime="
						+ costTime + "ms");
			} catch (Exception e) {
				log.error("Save realTimeData to DB error.", e);
			}
		}

		/*
		 * 数据归并
		 */
		private void mergeData() {
			// 数据归并
			long startTime = calcStartTimeBy(timeSeq);
			List<StatData> mergedDataList = dataAnalyzer.analyze(dataList,
					StatUtil.COLLECT_TYPE_DAILY, dailyStatInterval, startTime);
			// 将归并后的数据增加到日数据管理器中
			for (int i = 0; i < mergedDataList.size(); i++) {
				StatData data = mergedDataList.get(i);
				DailyDataManager.getInstance().add(data);
			}
		}
	}

}
