package com.xinwei.lte.web.enb.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.facade.EnbStatBizFacade;
import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;

/**
 * 统计项缓存
 * 
 * @author zhangqiang
 * 
 */
public class StatItemConfigCache {

	/**
	 * 判断缓存是否被初始化，是：true 否：false
	 */
	private boolean cacheFlag = false;

	private static final StatItemConfigCache instance = new StatItemConfigCache();

	private Map<String, List<CounterItemConfig>> counterMap = new HashMap<String, List<CounterItemConfig>>();

	private Map<String, List<KpiItemConfig>> kpiMap = new HashMap<String, List<KpiItemConfig>>();

	public static StatItemConfigCache getInstance() {
		return instance;
	}

	public synchronized void initialize(EnbStatBizFacade facade)
			throws Exception {
		if (!cacheFlag) {
			List<CounterItemConfig> counterList = facade.queryCounterConfigs();
			for (CounterItemConfig counter : counterList) {
				String key = "COUNTER" + "." + counter.getStatObject() + "."
						+ counter.getReportSystem() + "."
						+ counter.getMeasureType();
				if (counterMap.keySet().contains(key)) {
					counterMap.get(key).add(counter);
				} else {
					List<CounterItemConfig> list = new ArrayList<CounterItemConfig>();
					list.add(counter);
					counterMap.put(key, list);
				}
			}
			List<KpiItemConfig> kpiList = facade.queryKpiConfigs();
			for (KpiItemConfig kpi : kpiList) {
				String key = "KPI" + "." + kpi.getStatObject() + "."
						+ kpi.getPerfType();
				if (kpiMap.keySet().contains(key)) {
					kpiMap.get(key).add(kpi);
				} else {
					List<KpiItemConfig> list = new ArrayList<KpiItemConfig>();
					list.add(kpi);
					kpiMap.put(key, list);
				}
			}
			instance.setCacheFlag(true);
		}
	}

	/**
	 * 根据COUNTERID获取COUNTER统计项
	 * 
	 * @param counterId
	 * @return
	 */
	public synchronized CounterItemConfig getCounterItemConfig(int counterId) {
		for (String key : counterMap.keySet()) {
			List<CounterItemConfig> list = counterMap.get(key);
			for (CounterItemConfig counter : list) {
				if (counterId == counter.getCounterId()) {
					return counter;
				}
			}
		}
		return null;
	}

	/**
	 * 根据KPIID获取KPI统计项
	 * 
	 * @param kpiId
	 * @return
	 */
	public synchronized KpiItemConfig getKpiItemConfig(int kpiId) {
		for (String key : kpiMap.keySet()) {
			List<KpiItemConfig> list = kpiMap.get(key);
			for (KpiItemConfig kpi : list) {
				if (kpiId == kpi.getKpiId()) {
					return kpi;
				}
			}
		}
		return null;
	}

	public Map<String, List<CounterItemConfig>> getCounterMap() {
		return counterMap;
	}

	public void setCounterMap(Map<String, List<CounterItemConfig>> counterMap) {
		this.counterMap = counterMap;
	}

	public Map<String, List<KpiItemConfig>> getKpiMap() {
		return kpiMap;
	}

	public void setKpiMap(Map<String, List<KpiItemConfig>> kpiMap) {
		this.kpiMap = kpiMap;
	}

	public boolean isCacheFlag() {
		return cacheFlag;
	}

	public void setCacheFlag(boolean cacheFlag) {
		this.cacheFlag = cacheFlag;
	}

}
