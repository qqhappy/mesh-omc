/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xidea.el.Expression;
import org.xidea.el.impl.ExpressionImpl;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.enb.core.utils.EnbStatConstants;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.xstat.EnbStatModule;
import com.xinwei.minas.server.enb.xstat.util.EnbStatUtil;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatConstants;
import com.xinwei.minas.xstat.core.model.StatItem;
import com.xinwei.minas.xstat.core.util.StatItemUtil;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.CalculateUtil;

/**
 * 
 * eNB话务统计数据分析器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatAnalyzer {

	/**
	 * 计算Counter统计项的预统计值
	 * 
	 * @param dataMap
	 *            格式entityOid - Map[itemId, List StatItem]
	 * @param statTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> calCounterItem(
			Map<String, Map<Integer, List<StatItem>>> dataMap, long statTime)
			throws Exception {
		// 获取counter配置
		Map<Integer, CounterItemConfig> counterConfigMap = EnbStatModule
				.getInstance().getCounterConfigMap();
		List<PreStatItem> preStatItems = new ArrayList<PreStatItem>();
		for (String key : dataMap.keySet()) {
			Map<Integer, List<StatItem>> itemMap = dataMap.get(key);
			for (Integer itemId : itemMap.keySet()) {
				List<StatItem> itemList = itemMap.get(itemId);
				if (itemList.isEmpty())
					continue;
				CounterItemConfig config = counterConfigMap.get(itemId);
				StatItem firstItem = itemList.get(0);
				PreStatItem preStatItem = StatItemUtil
						.convertToPreStatItem(firstItem);
				// 时间统计方式为求和
				if (config.getTimeType().equals(StatConstants.COLLECT_TYPE_ADD)) {
					double sum = getSum(itemList);
					preStatItem.setStatValue(sum);
				} else {
					// 时间统计方式为求平均
					double average = getAverage(itemList);
					preStatItem.setStatValue(average);
				}
				// 设置统计时间
				preStatItem.setStatTime(statTime);
				preStatItems.add(preStatItem);
			}
		}
		return preStatItems;
	}

	/**
	 * 计算KPI统计项的预统计值
	 * 
	 * @param enb
	 * @param dataMap
	 *            格式entityOid - Map[itemId , List StatItem]
	 * @param statTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> calKpiItem(Enb enb,
			Map<String, Map<Integer, List<StatItem>>> dataMap, long statTime)
			throws Exception {
		List<PreStatItem> preStatItems = new ArrayList<PreStatItem>();
		// 查询当前eNB的小区ID列表
		List<Integer> cellIdList = queryCellByMoId(enb.getMoId());
		if (cellIdList == null || cellIdList.isEmpty())
			return preStatItems;
		// 获取所有KPI统计项的配置
		Map<Integer, KpiItemConfig> kpiConfigMap = EnbStatModule.getInstance()
				.getKpiConfigMap();
		// 根据统计项间依赖关系计算出KPI统计项的计算顺序
		List<Integer> kpiIdList = EnbStatUtil.getKpiList(kpiConfigMap);
		for (Integer kpiId : kpiIdList) {
			KpiItemConfig config = kpiConfigMap.get(kpiId);
			String kpiExp = config.getExp();
			if (config.isCellStatObject()) {
				// 小区级统计项
				for (Integer cellId : cellIdList) {
					String key = enb.getHexEnbId() + StatConstants.POINT
							+ cellId;
					Map<Integer, List<StatItem>> counterItemMap = dataMap
							.get(key);
					if (counterItemMap == null)
						continue;
					// 按时间分组的统计项Map
					Map<Long, Map<Integer, StatItem>> counterTimeMap = convert(counterItemMap);
					// 获取按时间分组的参数Map
					Map<Long, Map<String, Double>> paramTimeMap = getParamTimeMap(
							kpiExp, counterTimeMap);
					// 计算统计项的值
					Double statValue = calKpiStatValue(kpiExp, counterTimeMap,
							paramTimeMap, config.getTimeType());
					String valueStr = statValue.toString();
					// 如果是异常值，则统计值为默认值
					if (valueStr.equals("Infinity") || valueStr.equals("NaN"))
						statValue = config.getDefaultValue();
					PreStatItem item = EnbStatUtil.createCellItem(enb, cellId,
							kpiId, statValue, statTime,
							EnbStatConstants.ITEM_TYPE_KPI);
					preStatItems.add(item);
				}
			} else if (config.isBtsStatObject()) {
				// 基站级统计项
				String key = enb.getHexEnbId();
				Map<Integer, List<StatItem>> counterItemMap = dataMap.get(key);
				if (counterItemMap == null)
					continue;
				// 按时间分组
				Map<Long, Map<Integer, StatItem>> counterTimeMap = convert(counterItemMap);
				// 获取按时间分组的参数Map
				Map<Long, Map<String, Double>> paramTimeMap = getParamTimeMap(
						kpiExp, counterTimeMap);
				// 计算统计项的值
				Double statValue = calKpiStatValue(kpiExp, counterTimeMap,
						paramTimeMap, config.getTimeType());
				String valueStr = statValue.toString();
				// 如果是异常值，则统计值为默认值
				if (valueStr.equals("Infinity") || valueStr.equals("NaN"))
					statValue = config.getDefaultValue();
				PreStatItem item = EnbStatUtil.createBtsItem(enb, kpiId,
						statValue, statTime, EnbStatConstants.ITEM_TYPE_KPI);
				preStatItems.add(item);
			}
		}
		return preStatItems;
	}

	/**
	 * 计算KPI统计项的预统计值
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            格式time - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            格式time - Map[param, value]
	 * @param timeType
	 * @return
	 */
	private double calKpiStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap, String timeType) {
		double statValue = 0;
		// 如果是基站级的统计项
		if (timeType.equals(StatConstants.COLLECT_TYPE_AVERAGE)) {
			// 时间统计方式为平均
			statValue = calKpiAverageStatValue(expString, counterTimeMap,
					paramTimeMap);
		} else if (timeType.equals(StatConstants.COLLECT_TYPE_ADD)) {
			// 时间统计方式为加
			statValue = calKpiSumStatValue(expString, counterTimeMap,
					paramTimeMap);
		}
		return statValue;
	}

	/**
	 * 计算KPI统计项的预统计值-统计方式求和
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            格式time - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            格式time - Map[param, value]
	 * @return
	 */
	private double calKpiSumStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap) {
		// 如果公式中无除号，则结果=各个时间点统计值之和/时间点个数
		double sum = 0;
		for (Long time : counterTimeMap.keySet()) {
			// 计算各个时间点统计值之和
			Map<String, Double> paramMap = paramTimeMap.get(time);
			sum += calExpressionValue(expString, paramMap);
		}
		return sum;

	}

	/**
	 * 计算KPI统计项的预统计值-统计方式求平均值
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            格式time - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            格式time - Map[param, value]
	 * @return
	 */
	private double calKpiAverageStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap) {
		if (expString.contains(CalculateUtil.SIGN_DIVIDE)) {
			// 如果公式中包含除号，则需将分子和分母分开计算，结果=分子之和/分母之和
			String[] temp = expString.split(CalculateUtil.SIGN_DIVIDE);
			String sonExp = temp[0];
			String motherExp = temp[1];
			double son = 0;
			double mother = 0;
			for (Long time : counterTimeMap.keySet()) {
				// 计算分子的值
				Map<String, Double> paramMap = paramTimeMap.get(time);
				son += calExpressionValue(sonExp, paramMap);
				mother += calExpressionValue(motherExp, paramMap);
			}
			return son / mother;
		} else {
			// 如果公式中无除号，则结果=各个时间点统计值之和/时间点个数
			double sum = calKpiSumStatValue(expString, counterTimeMap,
					paramTimeMap);
			return sum / counterTimeMap.keySet().size();
		}

	}

	/**
	 * 获取公式按时间分组的参数Map
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            格式time - Map[itemId, StatItem]
	 * @return
	 */
	private Map<Long, Map<String, Double>> getParamTimeMap(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap) {
		Map<Long, Map<String, Double>> paramTimeMap = new HashMap<Long, Map<String, Double>>();
		for (Long time : counterTimeMap.keySet()) {
			Map<Integer, StatItem> counterMap = counterTimeMap.get(time);
			Map<String, Double> paramMap = getParamMap(expString, counterMap);
			paramTimeMap.put(time, paramMap);
		}
		return paramTimeMap;
	}

	/**
	 * 获取公式的参数Map
	 * 
	 * @param expString
	 * @param counterMap
	 *            格式Map[itemId, StatItem]
	 * @return
	 */
	public static Map<String, Double> getParamMap(String expString,
			Map<Integer, StatItem> counterMap) {
		Map<String, Double> paramMap = new HashMap<String, Double>();
		List<String> paramList = getParams(expString);
		for (String param : paramList) {
			if (param.startsWith("C")) {
				// 如果参数是counter统计项，直接从map中取
				int counterId = Integer.valueOf(param.substring(1));
				try {
					paramMap.put(param, counterMap.get(counterId)
							.getStatValue());
				} catch (Exception e) {
					System.err.println("counterId=" + counterId
							+ e.getLocalizedMessage());
					paramMap.put(param, 0d);
				}
			} else if (param.startsWith("K")) {
				// 如果参数是kpi统计项，需要先对该统计项的值进行计算
				int kpiId = Integer.valueOf(param.substring(1));
				KpiItemConfig config = EnbStatModule.getInstance()
						.getKpiConfigMap().get(kpiId);
				Map<String, Double> subParamMap = getParamMap(config.getExp(),
						counterMap);
				double paramValue = calExpressionValue(config.getExp(),
						subParamMap);
				paramMap.put(param, paramValue);
			} else {
				// 特殊值的处理
				if (param.equals("SEC_OF_ONE_GRAIN")) {
					paramMap.put(param, 900d);
				}
			}
		}
		return paramMap;
	}

	/**
	 * 计算表达式的值
	 * 
	 * @param expString
	 * @param counterMap
	 *            格式Map[itemId, StatItem]
	 * @return
	 */
	public static double calExpressionValue(String expString,
			Map<String, Double> paramMap) {
		try {
			Expression expression = new ExpressionImpl(expString);
			Object value = expression.evaluate(paramMap);
			return Double.valueOf(value.toString());
		} catch (Exception e) {
			System.err.println("exp=" + expString + e.getLocalizedMessage());
			return 0d;
		}
	}

	/**
	 * 将itemId-List[StatItem]转化成time-Map[itemId, StatItem]
	 * 
	 * @param itemMap
	 * @return
	 */
	private Map<Long, Map<Integer, StatItem>> convert(
			Map<Integer, List<StatItem>> itemMap) {
		Map<Long, Map<Integer, StatItem>> timeMap = new HashMap<Long, Map<Integer, StatItem>>();
		for (Integer itemId : itemMap.keySet()) {
			List<StatItem> itemList = itemMap.get(itemId);
			for (StatItem statItem : itemList) {
				long time = statItem.getStartTime();
				Map<Integer, StatItem> idMap = timeMap.get(time);
				if (idMap == null) {
					idMap = new HashMap<Integer, StatItem>();
					timeMap.put(time, idMap);
				}
				idMap.put(itemId, statItem);
			}
		}
		return timeMap;
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
	 * 查询某个基站的小区ID列表
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private List<Integer> queryCellByMoId(long moId) throws Exception {
		EnbBizConfigService service = AppContext.getCtx().getBean(
				EnbBizConfigService.class);
		XBizTable xBizTable = service.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null, false);
		List<Integer> idList = new ArrayList<Integer>();
		List<XBizRecord> recordList = xBizTable.getRecords();
		if (recordList != null && !recordList.isEmpty()) {
			for (XBizRecord xBizRecord : recordList) {
				XBizField field = xBizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
				idList.add(Integer.valueOf(field.getValue().toString()));
			}
		}
		return idList;
	}

	private double getSum(List<StatItem> itemList) {
		double sum = 0;
		for (StatItem statItem : itemList) {
			sum += statItem.getStatValue();
		}
		return sum;
	}

	private double getAverage(List<StatItem> itemList) {
		double sum = getSum(itemList);
		return sum / (double) (itemList.size());
	}
}
