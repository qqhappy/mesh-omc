/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.EnbStatEntity;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.minas.xstat.core.model.StatItem;
import com.xinwei.omp.core.utils.CalculateUtil;
import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * eNB话务统计助手
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatUtil {

	/**
	 * 根据拓扑排序发计算KPI统计项计算顺序
	 * 
	 * @param kpiConfigMap
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getKpiList(
			Map<Integer, KpiItemConfig> kpiConfigMap) throws Exception {
		Map<Integer, Set<Integer>> dependentMap = new HashMap<Integer, Set<Integer>>();
		for (Integer kpiId : kpiConfigMap.keySet()) {
			KpiItemConfig item = kpiConfigMap.get(kpiId);
			Set<Integer> set = new HashSet<Integer>();
			// 找到计算参数中包含KPI参数的KPI项
			if (hasKpiParam(item.getExp())) {
				set.addAll(getKpiParamIds(item.getExp()));
			}
			dependentMap.put(kpiId, set);
		}
		// 计算每一项的入度
		Map<Integer, Integer> inDegrees = computeInDegrees(dependentMap);
		// TOPO排序
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		Iterator<Integer> keyItr = dependentMap.keySet().iterator();
		// 无其他点依赖的点入队列
		while (keyItr.hasNext()) {
			Integer kpiId = keyItr.next();
			int inDegree = inDegrees.get(kpiId);
			if (inDegree == 0) {
				queue.offer(kpiId);
			}
		}

		List<Integer> topoKpiIds = new LinkedList<Integer>();
		while (queue.size() > 0) {
			Integer kpiId = queue.take();
			if (!topoKpiIds.contains(kpiId)) {
				topoKpiIds.add(kpiId);
			}
			Set<Integer> dependentTables = dependentMap.get(kpiId);
			for (Integer dependentTable : dependentTables) {
				int inDegree = inDegrees.get(dependentTable) - 1;
				inDegrees.put(dependentTable, inDegree);
				if (inDegree == 0) {
					queue.offer(dependentTable);
				}
			}
		}
		Collections.reverse(topoKpiIds);

		return topoKpiIds;
	}

	/**
	 * 根据KPI依赖关系计算KPI的入度
	 * 
	 * @param dependentMap
	 * @return
	 */
	private static Map<Integer, Integer> computeInDegrees(
			Map<Integer, Set<Integer>> dependentMap) {
		Map<Integer, Integer> inDegrees = new LinkedHashMap<Integer, Integer>();
		Iterator<Integer> keyItr = dependentMap.keySet().iterator();
		while (keyItr.hasNext()) {
			Integer kpiId = keyItr.next();
			if (!inDegrees.containsKey(kpiId)) {
				inDegrees.put(kpiId, 0);
			}
			Set<Integer> dependentKpis = dependentMap.get(kpiId);
			for (Integer dependKpiId : dependentKpis) {
				if (!inDegrees.containsKey(dependKpiId)) {
					inDegrees.put(dependKpiId, 0);
				}
				inDegrees.put(dependKpiId, inDegrees.get(dependKpiId) + 1);
			}
		}

		return inDegrees;
	}

	/**
	 * 获取公式中的所有参数
	 * 
	 * @param exp
	 * @return
	 */
	public static List<String> getParams(String exp) {
		exp = exp.replaceAll("\\" + CalculateUtil.SIGN_LEFT_BRACKET, "");
		exp = exp.replaceAll("\\" + CalculateUtil.SIGN_RIGHT_BRACKET, "");
		return CalculateUtil.getNumbers(exp);
	}

	/**
	 * 公式中是否存在KPI类型的参数
	 * 
	 * @return
	 */
	public static boolean hasKpiParam(String exp) {
		List<String> params = getParams(exp);
		if (params == null || params.isEmpty())
			return false;
		for (String param : params) {
			if (param.startsWith("K")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取KPI类型的参数的id
	 * 
	 * @return
	 */
	public static List<Integer> getKpiParamIds(String exp) {
		List<String> params = getParams(exp);
		if (params == null || params.isEmpty())
			return null;
		List<Integer> idList = new ArrayList<Integer>();
		for (String param : params) {
			if (param.startsWith("K")) {
				String temp = param.substring(1);
				idList.add(Integer.valueOf(temp));
			}
		}
		return idList;
	}

	/**
	 * 创建基站KPI统计项数据
	 * 
	 * @param moId
	 * @param itemId
	 * @param value
	 * @param entity
	 * @return
	 */
	public static StatItem createBtsKpiItem(long moId, int itemId,
			Double value, EnbStatEntity entity) {
		StatItem item = new StatItem();
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB);
		item.setEntityOid(String.valueOf(entity.getEnbId()));
		item.setItemType(EnbStatConstants.ITEM_TYPE_KPI);
		item.setItemId(itemId);
		item.setStatValue(value);
		item.setMoId(moId);
		item.setStartTime(entity.getStartStatTime());
		item.setEndTime(entity.getEndStatTime());
		item.setVersion(entity.getVersion());
		return item;
	}

	/**
	 * 创建小区KPI统计项数据
	 * 
	 * @param moId
	 * @param cellId
	 * @param itemId
	 * @param value
	 * @param entity
	 * @return
	 */
	public static StatItem createCellKpiItem(long moId, int cellId, int itemId,
			Double value, EnbStatEntity entity) {
		StatItem item = new StatItem();
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB
				+ StatConstants.POINT + EnbStatConstants.STAT_OBJECT_CELL);
		item.setEntityOid(entity.getEnbId() + StatConstants.POINT + cellId);
		item.setItemType(EnbStatConstants.ITEM_TYPE_KPI);
		item.setItemId(itemId);
		item.setStatValue(value);
		item.setMoId(moId);
		item.setStartTime(entity.getStartStatTime());
		item.setEndTime(entity.getEndStatTime());
		item.setVersion(entity.getVersion());
		return item;
	}

	/**
	 * 创建基站COUNTER统计项数据
	 * 
	 * @param moId
	 * @param itemId
	 * @param value
	 * @param entity
	 * @return
	 */
	public static StatItem createBtsCounterItem(long moId, int itemId,
			Double value, EnbStatEntity entity) {
		StatItem item = new StatItem();
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB);
		item.setEntityOid(StringUtils.to8HexString(entity.getEnbId()));
		item.setItemType(EnbStatConstants.ITEM_TYPE_COUNTER);
		item.setItemId(itemId);
		item.setStatValue(value);
		item.setMoId(moId);
		item.setStartTime(entity.getStartStatTime());
		item.setEndTime(entity.getEndStatTime());
		item.setVersion(entity.getVersion());
		return item;
	}

	/**
	 * 创建小区COUNTER统计项数据
	 * 
	 * @param moId
	 * @param cellId
	 * @param itemId
	 * @param value
	 * @param entity
	 * @return
	 */
	public static StatItem createAreaCounterItem(long moId, int cellId,
			int itemId, Double value, EnbStatEntity entity) {
		StatItem item = new StatItem();
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB
				+ StatConstants.POINT + EnbStatConstants.STAT_OBJECT_CELL);
		item.setEntityOid(StringUtils.to8HexString(entity.getEnbId())
				+ StatConstants.POINT + cellId);
		item.setItemType(EnbStatConstants.ITEM_TYPE_COUNTER);
		item.setItemId(itemId);
		item.setStatValue(value);
		item.setMoId(moId);
		item.setStartTime(entity.getStartStatTime());
		item.setEndTime(entity.getEndStatTime());
		item.setVersion(entity.getVersion());
		return item;
	}

	public static PreStatItem createCellItem(Enb enb, int cellId, int itemId,
			double statValue, long time, String itemType) {
		PreStatItem item = new PreStatItem();
		item.setMoId(enb.getMoId());
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB
				+ StatConstants.POINT + EnbStatConstants.STAT_OBJECT_CELL);
		item.setEntityOid(enb.getHexEnbId() + StatConstants.POINT + cellId);
		item.setItemType(itemType);
		item.setStatTime(time);
		item.setStatValue(statValue);
		item.setItemId(itemId);
		return item;
	}

	public static PreStatItem createBtsItem(Enb enb, int itemId,
			double statValue, long time, String itemType) {
		PreStatItem item = new PreStatItem();
		item.setMoId(enb.getMoId());
		item.setEntityType(EnbStatConstants.STAT_OBJECT_ENB);
		item.setEntityOid(enb.getHexEnbId());
		item.setItemType(itemType);
		item.setStatTime(time);
		item.setStatValue(statValue);
		item.setItemId(itemId);
		return item;
	}

	/**
	 * 将通用统计项列表转换为List<StatItem>转换为map: 格式entityOid - Map[itemId , List
	 * StatItem]
	 * 
	 * @param itemList
	 * @return map
	 */
	public static Map<String, Map<Integer, List<StatItem>>> converseStatItemListToMap(
			List<StatItem> itemList) {
		Map<String, Map<Integer, List<StatItem>>> map = new HashMap<String, Map<Integer, List<StatItem>>>();
		for (StatItem statItem : itemList) {
			String key = statItem.getEntityOid();
			Map<Integer, List<StatItem>> StatItemMap = map.get(key);
			int itemId = statItem.getItemId();
			if (StatItemMap == null) {
				StatItemMap = new HashMap<Integer, List<StatItem>>();
				map.put(key, StatItemMap);
			}
			List<StatItem> StatItemList = StatItemMap.get(itemId);
			if (StatItemList == null) {
				StatItemList = new ArrayList<StatItem>();
				StatItemMap.put(itemId, StatItemList);
			}
			StatItemList.add(statItem);
		}
		return map;

	}

	/**
	 * 将List<PreStatItem>转化为List<StatItem>
	 * 
	 * @param entity
	 * @param kpiItemList
	 * @return List<StatItem>
	 */
	public static List<StatItem> conversePreStatItemToStatItem(
			EnbStatEntity entity, List<PreStatItem> kpiItemList) {
		List<StatItem> statItem = new ArrayList<StatItem>();
		for (PreStatItem kpiItem : kpiItemList) {
			StatItem item = new StatItem();
			item.setMoId(kpiItem.getMoId());
			item.setEntityType(kpiItem.getEntityType());
			item.setEntityOid(kpiItem.getEntityOid());
			item.setItemType(kpiItem.getItemType());
			item.setItemId(kpiItem.getItemId());
			item.setStartTime(entity.getStartStatTime());
			item.setEndTime(entity.getEndStatTime());
			item.setStatValue(kpiItem.getStatValue());
			item.setVersion(entity.getVersion());
			statItem.add(item);
		}
		return statItem;
	}
}
