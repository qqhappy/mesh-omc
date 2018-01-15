/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.xstat.dao.OriginalStatDataDAO;
import com.xinwei.minas.server.xstat.dao.PreStatDataDAO;
import com.xinwei.minas.server.xstat.task.ScheduledTask;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.minas.xstat.core.model.StatItem;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * eNB预统计任务
 * 
 * @author fanhaoyu
 * 
 */

public class EnbPreStatTask extends ScheduledTask {

	private Log log = LogFactory.getLog(EnbPreStatTask.class);

	private long startTime = 0;

	private long endTime = 0;

	private int preStatType;

	private EnbStatAnalyzer analyzer;

	/**
	 * 构造方法
	 * 
	 * @param preStatType
	 *            预统计类型，按小时1或按天2
	 */
	public EnbPreStatTask(int preStatType, EnbStatAnalyzer analyzer) {
		this.preStatType = preStatType;
		this.analyzer = analyzer;
	}

	@Override
	public void doWork() throws Exception {

		// 计算要查询数据的开始时间和结束时间
		calStartEndTime();

		log.debug("EnbPreStatTask start. startTime=" + this.startTime
				+ ", endTime=" + this.endTime);

		// 获取eNB基站列表
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		for (Enb enb : enbList) {
			Map<String, Map<Integer, List<StatItem>>> itemMap = queryStatData(enb);
			try {
				// 计算counter
				List<PreStatItem> couterItemList = analyzer.calCounterItem(
						itemMap, this.startTime);
				// counter预统计数据存库
				savePreStatItem(couterItemList);
			} catch (Exception e) {
				log.error("EnbPreStatTask execute counter with error. enbId"
						+ enb.getHexEnbId() + ", startTime=" + this.startTime
						+ ", endTime=" + this.endTime, e);
				continue;
			}
			try {
				// 计算KPI
				List<PreStatItem> kpiItemList = analyzer.calKpiItem(enb,
						itemMap, this.startTime);
				// kpi预统计数据存库
				savePreStatItem(kpiItemList);
			} catch (Exception e) {
				log.error(
						"EnbPreStatTask execute kpi with error. enbId"
								+ enb.getHexEnbId() + ", startTime="
								+ this.startTime + ", endTime=" + this.endTime,
						e);
			}
		}
		log.debug("EnbPreStatTask finished. startTime=" + this.startTime
				+ ", endTime=" + this.endTime);
	}

	private void calStartEndTime() {
		long currentTime = System.currentTimeMillis();
		if (this.preStatType == StatConstants.PRE_STAT_TYPE_HOUR) {
			long oneHourMillis = 1000 * 60 * 60;
			// 计算上一小时的计数值
			long currentHourCount = currentTime / oneHourMillis;
			// 上一小时的起始时间
			this.startTime = (currentHourCount - 1) * oneHourMillis;
			// 上一小时的结束时间
			this.endTime = currentHourCount * oneHourMillis;
		} else {
			long oneDayMillis = 1000 * 60 * 60 * 24;
			// 计算上一天的计数值
			long currentDayCount = currentTime / oneDayMillis;
			// 上一天的起始时间
			this.startTime = (currentDayCount - 1) * oneDayMillis;
			// 上一天的结束时间
			this.endTime = currentDayCount * oneDayMillis;
		}
		this.startTime = DateUtils
				.getBriefTimeFromMillisecondTime(this.startTime);
		this.endTime = DateUtils.getBriefTimeFromMillisecondTime(this.endTime);
	}

	/**
	 * 查询指定时间内
	 * 
	 * @param enb
	 * @return
	 * @throws Exception
	 */
	private Map<String, Map<Integer, List<StatItem>>> queryStatData(Enb enb)
			throws Exception {
		OriginalStatDataDAO statDataDAO = AppContext.getCtx().getBean(
				OriginalStatDataDAO.class);
		List<StatItem> items = statDataDAO.queryBy(enb.getMoId(),
				this.startTime, this.endTime);
		// key = entityOid, value=Map<itemId, List<StatItem>>
		Map<String, Map<Integer, List<StatItem>>> map = new HashMap<String, Map<Integer, List<StatItem>>>();
		for (StatItem statItem : items) {
			String key = statItem.getEntityOid();
			Map<Integer, List<StatItem>> itemMap = map.get(key);
			int itemId = statItem.getItemId();
			if (itemMap == null) {
				itemMap = new HashMap<Integer, List<StatItem>>();
				map.put(key, itemMap);
			}
			List<StatItem> itemList = itemMap.get(itemId);
			if (itemList == null) {
				itemList = new ArrayList<StatItem>();
				itemMap.put(itemId, itemList);
			}
			itemList.add(statItem);
		}
		return map;
	}

	private void savePreStatItem(List<PreStatItem> preStatItems)
			throws Exception {
		PreStatDataDAO statDataDAO = AppContext.getCtx().getBean(
				PreStatDataDAO.class);
		statDataDAO.savePreStatItem(preStatItems, preStatType);
	}

}
