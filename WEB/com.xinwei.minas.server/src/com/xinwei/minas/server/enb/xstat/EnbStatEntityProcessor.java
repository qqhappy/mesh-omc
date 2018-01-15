/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.EnbStatEntity;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.xstat.analyze.EnbStatAnalyzer;
import com.xinwei.minas.server.enb.xstat.util.EnbStatUtil;
import com.xinwei.minas.server.xstat.service.StatEntityProcessor;
import com.xinwei.minas.server.xstat.task.StatItemProcessTask;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * eNB统计实体处理器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatEntityProcessor implements
		StatEntityProcessor<EnbStatEntity> {
	private EnbStatAnalyzer analyzer;

	// 处理数据的线程池大小
	private int poolSize = 5;
	// 处理线程池
	private ExecutorService service;

	public EnbStatEntityProcessor(int poolSize) {
		this.poolSize = poolSize;
		service = Executors.newFixedThreadPool(this.poolSize);
	}

	@Override
	public void process(List<EnbStatEntity> entities) throws Exception {
		for (EnbStatEntity entity : entities) {
			// 转换为通用的统计项列表
			List<StatItem> itemList = new ArrayList<StatItem>();
			// 基站级参数Map: ItemId -- StatItem
			Map<Integer, StatItem> btsStatItemMap = new HashMap<Integer, StatItem>();
			// 小区级参数Map: CellId -- ItemId -- StatItem
			Map<Integer, Map<Integer, StatItem>> cellMap = new HashMap<Integer, Map<Integer, StatItem>>();
			Enb enb = EnbCache.getInstance().queryByEnbId(entity.getEnbId());
			Map<String, Double> itemMap = entity.getItemMap();
			for (String key : itemMap.keySet()) {
				StatItem item = null;
				if (key.contains(".")) {
					// 小区级参数
					String[] temp = key.split("\\.");
					int cellId = Integer.valueOf(temp[0]);
					int itemId = Integer.valueOf(temp[1]);

					item = EnbStatUtil.createAreaCounterItem(enb.getMoId(),
							cellId, itemId, itemMap.get(key), entity);
					itemList.add(item);

					Map<Integer, StatItem> counterMap = cellMap.get(cellId);
					if (counterMap == null) {
						counterMap = new HashMap<Integer, StatItem>();
						cellMap.put(cellId, counterMap);
					}
					counterMap.put(itemId, item);
				} else {
					// 基站级参数
					int itemId = Integer.valueOf(key);
					item = EnbStatUtil.createBtsCounterItem(enb.getMoId(),
							itemId, itemMap.get(key), entity);
					itemList.add(item);

					btsStatItemMap.put(itemId, item);
				}
			}
			// 计算特殊的counter
			Map<Integer, CounterItemConfig> counterConfigMap = EnbStatModule
					.getInstance().getCounterConfigMap();
			for (int itemId : counterConfigMap.keySet()) {
				CounterItemConfig config = counterConfigMap.get(itemId);
				String exp = config.getExp();
				if (exp == null || exp.equals(""))
					continue;
				if (config.getStatObject().equals(
						EnbStatConstants.STAT_OBJECT_CELL)) {
					int cellId = -1;
					Iterator iter = cellMap.keySet().iterator();
					while (iter.hasNext()) {
						cellId = (Integer) iter.next();
						Map<String, Double> paramMap = EnbStatAnalyzer
								.getParamMap(exp, cellMap.get(cellId));
						double value = EnbStatAnalyzer.calExpressionValue(exp,
								paramMap);
						StatItem statItem = EnbStatUtil.createAreaCounterItem(
								enb.getMoId(), cellId, itemId, value, entity);
						itemList.add(statItem);
					}

				} else {
					Map<String, Double> paramMap = EnbStatAnalyzer.getParamMap(
							exp, btsStatItemMap);
					double value = EnbStatAnalyzer.calExpressionValue(exp,
							paramMap);
					StatItem statItem = EnbStatUtil.createBtsCounterItem(
							enb.getMoId(), itemId, value, entity);
					itemList.add(statItem);
				}
			}
			// for (StatItem statItem : itemList) {
			// CounterItemConfig config = counterConfigMap.get(statItem
			// .getItemId());
			// String exp = config.getExp();
			// if (exp == null || exp.equals(""))
			// continue;
			// if (config.getStatObject().equals(
			// EnbStatConstants.STAT_OBJECT_CELL)) {
			// int cellId = Integer.valueOf(statItem.getEntityOid().split(
			// "\\.")[1]);
			// Map<String, Double> paramMap = EnbStatAnalyzer.getParamMap(
			// exp, cellMap.get(cellId));
			// double value = EnbStatAnalyzer.calExpressionValue(exp,
			// paramMap);
			// statItem.setStatValue(value);
			// } else {
			// Map<String, Double> paramMap = EnbStatAnalyzer.getParamMap(
			// exp, btsStatItemMap);
			// double value = EnbStatAnalyzer.calExpressionValue(exp,
			// paramMap);
			// statItem.setStatValue(value);
			// }
			// }
			// 增加KPI统计项的计算，并增加到itemList
			// 将上述的通用的统计项列表List<StatItem>转换为map: 格式entityOid - Map[itemId , List
			// StatItem]
			Map<String, Map<Integer, List<StatItem>>> map = EnbStatUtil
					.converseStatItemListToMap(itemList);

			// 计算KPI
			List<PreStatItem> kpiItemList = analyzer.calKpiItem(enb, map,
					entity.getStartStatTime());

			// 将15分钟的预统计项转换为通用统计项的格式
			List<StatItem> statItemitemList = EnbStatUtil
					.conversePreStatItemToStatItem(entity, kpiItemList);
			itemList.addAll(statItemitemList);
			service.execute(new StatItemProcessTask(itemList));
		}
	}

	public EnbStatAnalyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(EnbStatAnalyzer analyzer) {
		this.analyzer = analyzer;
	}
}
