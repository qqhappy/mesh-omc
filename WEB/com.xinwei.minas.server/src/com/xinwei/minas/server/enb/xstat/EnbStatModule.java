/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-24	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.server.enb.xstat.dao.EnbStatItemConfigDAO;
import com.xinwei.minas.server.xstat.task.ScheduledTask;

/**
 * 
 * eNB话务统计模块
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatModule {

	private Map<Integer, CounterItemConfig> counterConfigMap = new HashMap<Integer, CounterItemConfig>();

	private Map<Integer, KpiItemConfig> kpiConfigMap = new HashMap<Integer, KpiItemConfig>();

	private static final EnbStatModule instance = new EnbStatModule();

	private ScheduledExecutorService service;

	private EnbStatItemConfigDAO enbStatItemConfigDAO;

	private EnbStatFileProcessTask enbStatFileProcessTask;

	private EnbStatModule() {
	}

	public static EnbStatModule getInstance() {
		return instance;
	}

	public void initialize() throws Exception {
		// 初始化COUNTER配置
		initCounterConfig();
		// 初始化KPI配置
		initKpiConfig();
		// 启动性能文件处理任务
		startStatFileProcessTask();
	}

	private void initCounterConfig() throws Exception {
		List<CounterItemConfig> counterList = enbStatItemConfigDAO
				.queryCounterConfigs();
		for (CounterItemConfig counterItemConfig : counterList) {
			counterConfigMap.put(counterItemConfig.getCounterId(),
					counterItemConfig);
		}
	}

	private void initKpiConfig() throws Exception {
		List<KpiItemConfig> kpiList = enbStatItemConfigDAO.queryKpiConfigs();
		for (KpiItemConfig kpi : kpiList) {
			kpiConfigMap.put(kpi.getKpiId(), kpi);
		}
	}

	private void startStatFileProcessTask() {
		ScheduledTask task = enbStatFileProcessTask;
		long currentTime = System.currentTimeMillis();
		long fifteenMin = 15 * 60 * 1000;
		// 下一个整15分钟开始执行
		long count = currentTime / fifteenMin;
		long startTime = fifteenMin * (count + 1);
		long delay = (startTime - currentTime) / 1000 + task.getDelay();
		// 开始定时执行可调度的任务
		service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(task, delay, task.getPeriod(),
				TimeUnit.SECONDS);
	}

	public void setCounterConfigMap(
			Map<Integer, CounterItemConfig> counterConfigMap) {
		this.counterConfigMap = counterConfigMap;
	}

	public Map<Integer, CounterItemConfig> getCounterConfigMap() {
		return counterConfigMap;
	}

	public void setKpiConfigMap(Map<Integer, KpiItemConfig> kpiConfigMap) {
		this.kpiConfigMap = kpiConfigMap;
	}

	public Map<Integer, KpiItemConfig> getKpiConfigMap() {
		return kpiConfigMap;
	}

	public void setEnbStatItemConfigDAO(
			EnbStatItemConfigDAO enbStatItemConfigDAO) {
		this.enbStatItemConfigDAO = enbStatItemConfigDAO;
	}

	public void setEnbStatFileProcessTask(
			EnbStatFileProcessTask enbStatFileProcessTask) {
		this.enbStatFileProcessTask = enbStatFileProcessTask;
	}

	public void dispose() {
		this.service.shutdown();
	}

}
