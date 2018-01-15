/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.server.enb.xstat.dao.EnbStatItemConfigDAO;
import com.xinwei.minas.server.enb.xstat.service.EnbStatBizService;
import com.xinwei.minas.server.xstat.dao.OriginalStatDataDAO;
import com.xinwei.minas.server.xstat.dao.PreStatDataDAO;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;
import com.xinwei.minas.xstat.core.model.StatItem;
import com.xinwei.minas.xstat.core.util.StatItemUtil;

/**
 * 
 * eNB话务统计业务服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatBizServiceImpl implements EnbStatBizService {

	private OriginalStatDataDAO originalStatDataDAO;

	private PreStatDataDAO preStatDataDAO;

	private EnbStatItemConfigDAO enbStatItemConfigDAO;

	@Override
	public List<CounterItemConfig> queryCounterConfigs() throws Exception {
		return enbStatItemConfigDAO.queryCounterConfigs();
	}

	@Override
	public List<KpiItemConfig> queryKpiConfigs() throws Exception {
		return enbStatItemConfigDAO.queryKpiConfigs();
	}

	@Override
	public List<PreStatItem> queryStatData(StatDataQueryCondition condition)
			throws Exception {

		if (condition.getInterval() == StatDataQueryCondition.INTERVAL_FIFTEEN_MINUTES) {
			List<StatItem> itemList = originalStatDataDAO.queryBy(
					condition.getEntityMap(), condition.getItemList(),
					condition.getStartTime(), condition.getEndTime());
			List<PreStatItem> list = new ArrayList<PreStatItem>();
			for (StatItem statItem : itemList) {
				PreStatItem item = StatItemUtil.convertToPreStatItem(statItem);
				list.add(item);
			}
			return list;
		} else if (condition.getInterval() == StatDataQueryCondition.INTERVAL_ONE_HOUR) {
			return preStatDataDAO.queryHourPreStatData(
					condition.getEntityMap(), condition.getItemList(),
					condition.getStartTime(), condition.getEndTime());
		} else if (condition.getInterval() == StatDataQueryCondition.INTERVAL_ONE_DAY) {
			return preStatDataDAO.queryDayPreStatData(condition.getEntityMap(),
					condition.getItemList(), condition.getStartTime(),
					condition.getEndTime());
		}
		return null;
	}

	@Override
	public void deleteStatData(long startTime, long endTime) throws Exception {
		originalStatDataDAO.delete(startTime, endTime);
	}

	public void setOriginalStatDataDAO(OriginalStatDataDAO originalStatDataDAO) {
		this.originalStatDataDAO = originalStatDataDAO;
	}

	public OriginalStatDataDAO getOriginalStatDataDAO() {
		return originalStatDataDAO;
	}

	public void setPreStatDataDAO(PreStatDataDAO preStatDataDAO) {
		this.preStatDataDAO = preStatDataDAO;
	}

	public PreStatDataDAO getPreStatDataDAO() {
		return preStatDataDAO;
	}

	public void setEnbStatItemConfigDAO(
			EnbStatItemConfigDAO enbStatItemConfigDAO) {
		this.enbStatItemConfigDAO = enbStatItemConfigDAO;
	}

	public EnbStatItemConfigDAO getEnbStatItemConfigDAO() {
		return enbStatItemConfigDAO;
	}

}
