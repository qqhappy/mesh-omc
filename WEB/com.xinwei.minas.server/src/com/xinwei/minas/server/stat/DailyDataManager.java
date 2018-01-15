/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.stat.dao.StatDataDAO;
import com.xinwei.minas.server.stat.net.ClientNotifier;
import com.xinwei.minas.stat.core.StatUtil;
import com.xinwei.minas.stat.core.model.MonitorItem;
import com.xinwei.minas.stat.core.model.SingleStatItemData;
import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * 日数据管理器
 * 
 * @author fanhaoyu
 * 
 */

public class DailyDataManager {

	private Log log = LogFactory.getLog(DailyDataManager.class);

	private static DailyDataManager instance = new DailyDataManager();

	/**
	 * btsId - List<StatData>
	 */
	private Map<Long, List<StatData>> dailyDataCache = new HashMap<Long, List<StatData>>();

	private StatDataDAO statDataDAO;

	private DailyDataManager() {
	}

	public static DailyDataManager getInstance() {
		return instance;
	}

	/**
	 * 初始化日数据管理器
	 * 
	 * @param statDataDAO
	 *            采集数据DAO
	 * @throws Exception
	 */
	public void initialize(StatDataDAO statDataDAO) throws Exception {
		this.statDataDAO = statDataDAO;
		// 系统启动后,首先对DayDataManager进行初始化,从数据库中装载1天的数据到缓存
		long endtime = System.currentTimeMillis();
		long begintime = endtime - 24 * 60 * 60 * 1000;

		int type = StatUtil.COLLECT_TYPE_DAILY;
		List<StatData> statDatas = statDataDAO
				.getData(begintime, endtime, type);

		if (statDatas == null || statDatas.isEmpty())
			return;

		for (int j = 0; j < statDatas.size(); j++) {
			StatData statData = statDatas.get(j);
			this.addData2Cache(statData, true);
		}

	}

	/**
	 * 监控数据主动上报
	 * 
	 * @param data
	 */
	private void notifyDaliyData(SingleStatItemData data) {
		long btsId = data.getBtsId();
		// 如果该数据的统计项正在被监视
		if (MonitorManager.getInstance().isMonitoring(
				new MonitorItem(btsId, data.getItemId()))) {
			// 将数据上报分析系统
			try {
				ClientNotifier.getInstance().sendData(data);
			} catch (Exception e) {
				log.error("DailyDataManager send StatData to client error.", e);
			}
		}
	}

	/**
	 * 增加日数据
	 * 
	 * @param statData
	 */
	public void add(StatData statData) {
		int timeType = statData.getTimeType();
		if (timeType != StatUtil.COLLECT_TYPE_DAILY) {// 过滤非天数据
			return;
		}
		// 监控数据主动上报
		for (SingleStatItemData itemData : statData.getItemDatas()) {
			notifyDaliyData(itemData);
		}
		// 将数据加入缓存
		this.addData2Cache(statData, false);
		// 将数据持久化
		try {
			statDataDAO.saveData(statData);
		} catch (Exception e) {
			log.error("DailyDataManager save StatData to DB error.", e);
		}

	}

	/**
	 * 将数据加入缓存
	 * 
	 * @param statData
	 */
	private void addData2Cache(StatData statData, boolean isInit) {
		long btsId = statData.getBtsId();
		List<StatData> dataList = dailyDataCache.get(btsId);
		if (dataList == null) {
			dataList = new LinkedList<StatData>();
			dailyDataCache.put(btsId, dataList);
		}
		dataList.add(statData);

		// 非初始化过程，删除过期的数据
		if (!isInit) {
			this.removeOverTimeData(dataList);
		}

	}

	/**
	 * 删除数据列表中的过期数据
	 * 
	 * @param dataList
	 *            数据列表
	 */
	private void removeOverTimeData(List<StatData> dataList) {
		// 删除超过一天的数据
		int count = 0;
		for (int i = 0; i < dataList.size(); i++) {
			StatData data = dataList.get(i);
			if (dataOverOneDay(data.getCollectTime())) {
				count++;
				dataList.remove(i);
				i--;
			}
			data = null;
		}
		log.debug("Remove overtime DailyData in cache. number=" + count);
	}

	/**
	 * 判断采集数据是否超过一天
	 * 
	 * @param collectedData
	 *            采集数据
	 * @return true if data is over one day, false if not
	 */
	private boolean dataOverOneDay(long collectTime) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - collectTime > 24 * 60 * 60 * 1000) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回指定基站和统计项的最近一天的统计数据
	 * 
	 * @return 指定基站和统计项的最近一天的统计数据列表
	 */
	public List<SingleStatItemData> getDataOfRecentOneDay(long btsId, int itemId) {
		List<SingleStatItemData> oneDayDataList = new ArrayList<SingleStatItemData>();
		List<StatData> dataList = dailyDataCache.get(btsId);
		if (dataList == null) {
			dataList = new LinkedList<StatData>();
			dailyDataCache.put(btsId, dataList);
		}
		for (int i = 0; i < dataList.size(); i++) {
			StatData statData = dataList.get(i);
			if (!dataOverOneDay(statData.getCollectTime())) {
				SingleStatItemData itemData = statData.getItemData(itemId);
				if (itemData != null) {
					oneDayDataList.add(itemData);
				}
			}
		}
		return oneDayDataList;
	}
}
