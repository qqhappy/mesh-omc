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
 * eNB����ͳ�����ݷ�����
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatAnalyzer {

	/**
	 * ����Counterͳ�����Ԥͳ��ֵ
	 * 
	 * @param dataMap
	 *            ��ʽentityOid - Map[itemId, List StatItem]
	 * @param statTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> calCounterItem(
			Map<String, Map<Integer, List<StatItem>>> dataMap, long statTime)
			throws Exception {
		// ��ȡcounter����
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
				// ʱ��ͳ�Ʒ�ʽΪ���
				if (config.getTimeType().equals(StatConstants.COLLECT_TYPE_ADD)) {
					double sum = getSum(itemList);
					preStatItem.setStatValue(sum);
				} else {
					// ʱ��ͳ�Ʒ�ʽΪ��ƽ��
					double average = getAverage(itemList);
					preStatItem.setStatValue(average);
				}
				// ����ͳ��ʱ��
				preStatItem.setStatTime(statTime);
				preStatItems.add(preStatItem);
			}
		}
		return preStatItems;
	}

	/**
	 * ����KPIͳ�����Ԥͳ��ֵ
	 * 
	 * @param enb
	 * @param dataMap
	 *            ��ʽentityOid - Map[itemId , List StatItem]
	 * @param statTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> calKpiItem(Enb enb,
			Map<String, Map<Integer, List<StatItem>>> dataMap, long statTime)
			throws Exception {
		List<PreStatItem> preStatItems = new ArrayList<PreStatItem>();
		// ��ѯ��ǰeNB��С��ID�б�
		List<Integer> cellIdList = queryCellByMoId(enb.getMoId());
		if (cellIdList == null || cellIdList.isEmpty())
			return preStatItems;
		// ��ȡ����KPIͳ���������
		Map<Integer, KpiItemConfig> kpiConfigMap = EnbStatModule.getInstance()
				.getKpiConfigMap();
		// ����ͳ�����������ϵ�����KPIͳ����ļ���˳��
		List<Integer> kpiIdList = EnbStatUtil.getKpiList(kpiConfigMap);
		for (Integer kpiId : kpiIdList) {
			KpiItemConfig config = kpiConfigMap.get(kpiId);
			String kpiExp = config.getExp();
			if (config.isCellStatObject()) {
				// С����ͳ����
				for (Integer cellId : cellIdList) {
					String key = enb.getHexEnbId() + StatConstants.POINT
							+ cellId;
					Map<Integer, List<StatItem>> counterItemMap = dataMap
							.get(key);
					if (counterItemMap == null)
						continue;
					// ��ʱ������ͳ����Map
					Map<Long, Map<Integer, StatItem>> counterTimeMap = convert(counterItemMap);
					// ��ȡ��ʱ�����Ĳ���Map
					Map<Long, Map<String, Double>> paramTimeMap = getParamTimeMap(
							kpiExp, counterTimeMap);
					// ����ͳ�����ֵ
					Double statValue = calKpiStatValue(kpiExp, counterTimeMap,
							paramTimeMap, config.getTimeType());
					String valueStr = statValue.toString();
					// ������쳣ֵ����ͳ��ֵΪĬ��ֵ
					if (valueStr.equals("Infinity") || valueStr.equals("NaN"))
						statValue = config.getDefaultValue();
					PreStatItem item = EnbStatUtil.createCellItem(enb, cellId,
							kpiId, statValue, statTime,
							EnbStatConstants.ITEM_TYPE_KPI);
					preStatItems.add(item);
				}
			} else if (config.isBtsStatObject()) {
				// ��վ��ͳ����
				String key = enb.getHexEnbId();
				Map<Integer, List<StatItem>> counterItemMap = dataMap.get(key);
				if (counterItemMap == null)
					continue;
				// ��ʱ�����
				Map<Long, Map<Integer, StatItem>> counterTimeMap = convert(counterItemMap);
				// ��ȡ��ʱ�����Ĳ���Map
				Map<Long, Map<String, Double>> paramTimeMap = getParamTimeMap(
						kpiExp, counterTimeMap);
				// ����ͳ�����ֵ
				Double statValue = calKpiStatValue(kpiExp, counterTimeMap,
						paramTimeMap, config.getTimeType());
				String valueStr = statValue.toString();
				// ������쳣ֵ����ͳ��ֵΪĬ��ֵ
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
	 * ����KPIͳ�����Ԥͳ��ֵ
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            ��ʽtime - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            ��ʽtime - Map[param, value]
	 * @param timeType
	 * @return
	 */
	private double calKpiStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap, String timeType) {
		double statValue = 0;
		// ����ǻ�վ����ͳ����
		if (timeType.equals(StatConstants.COLLECT_TYPE_AVERAGE)) {
			// ʱ��ͳ�Ʒ�ʽΪƽ��
			statValue = calKpiAverageStatValue(expString, counterTimeMap,
					paramTimeMap);
		} else if (timeType.equals(StatConstants.COLLECT_TYPE_ADD)) {
			// ʱ��ͳ�Ʒ�ʽΪ��
			statValue = calKpiSumStatValue(expString, counterTimeMap,
					paramTimeMap);
		}
		return statValue;
	}

	/**
	 * ����KPIͳ�����Ԥͳ��ֵ-ͳ�Ʒ�ʽ���
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            ��ʽtime - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            ��ʽtime - Map[param, value]
	 * @return
	 */
	private double calKpiSumStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap) {
		// �����ʽ���޳��ţ�����=����ʱ���ͳ��ֵ֮��/ʱ������
		double sum = 0;
		for (Long time : counterTimeMap.keySet()) {
			// �������ʱ���ͳ��ֵ֮��
			Map<String, Double> paramMap = paramTimeMap.get(time);
			sum += calExpressionValue(expString, paramMap);
		}
		return sum;

	}

	/**
	 * ����KPIͳ�����Ԥͳ��ֵ-ͳ�Ʒ�ʽ��ƽ��ֵ
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            ��ʽtime - Map[itemId, StatItem]
	 * @param paramTimeMap
	 *            ��ʽtime - Map[param, value]
	 * @return
	 */
	private double calKpiAverageStatValue(String expString,
			Map<Long, Map<Integer, StatItem>> counterTimeMap,
			Map<Long, Map<String, Double>> paramTimeMap) {
		if (expString.contains(CalculateUtil.SIGN_DIVIDE)) {
			// �����ʽ�а������ţ����轫���Ӻͷ�ĸ�ֿ����㣬���=����֮��/��ĸ֮��
			String[] temp = expString.split(CalculateUtil.SIGN_DIVIDE);
			String sonExp = temp[0];
			String motherExp = temp[1];
			double son = 0;
			double mother = 0;
			for (Long time : counterTimeMap.keySet()) {
				// ������ӵ�ֵ
				Map<String, Double> paramMap = paramTimeMap.get(time);
				son += calExpressionValue(sonExp, paramMap);
				mother += calExpressionValue(motherExp, paramMap);
			}
			return son / mother;
		} else {
			// �����ʽ���޳��ţ�����=����ʱ���ͳ��ֵ֮��/ʱ������
			double sum = calKpiSumStatValue(expString, counterTimeMap,
					paramTimeMap);
			return sum / counterTimeMap.keySet().size();
		}

	}

	/**
	 * ��ȡ��ʽ��ʱ�����Ĳ���Map
	 * 
	 * @param expString
	 * @param counterTimeMap
	 *            ��ʽtime - Map[itemId, StatItem]
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
	 * ��ȡ��ʽ�Ĳ���Map
	 * 
	 * @param expString
	 * @param counterMap
	 *            ��ʽMap[itemId, StatItem]
	 * @return
	 */
	public static Map<String, Double> getParamMap(String expString,
			Map<Integer, StatItem> counterMap) {
		Map<String, Double> paramMap = new HashMap<String, Double>();
		List<String> paramList = getParams(expString);
		for (String param : paramList) {
			if (param.startsWith("C")) {
				// ���������counterͳ���ֱ�Ӵ�map��ȡ
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
				// ���������kpiͳ�����Ҫ�ȶԸ�ͳ�����ֵ���м���
				int kpiId = Integer.valueOf(param.substring(1));
				KpiItemConfig config = EnbStatModule.getInstance()
						.getKpiConfigMap().get(kpiId);
				Map<String, Double> subParamMap = getParamMap(config.getExp(),
						counterMap);
				double paramValue = calExpressionValue(config.getExp(),
						subParamMap);
				paramMap.put(param, paramValue);
			} else {
				// ����ֵ�Ĵ���
				if (param.equals("SEC_OF_ONE_GRAIN")) {
					paramMap.put(param, 900d);
				}
			}
		}
		return paramMap;
	}

	/**
	 * ������ʽ��ֵ
	 * 
	 * @param expString
	 * @param counterMap
	 *            ��ʽMap[itemId, StatItem]
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
	 * ��itemId-List[StatItem]ת����time-Map[itemId, StatItem]
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
	 * ��ȡ��ʽ�е����в���
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
	 * ��ѯĳ����վ��С��ID�б�
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
