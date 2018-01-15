/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;

/**
 * 
 * eNB����ͳ��ҵ�����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatBizService {

	/**
	 * ��ѯ����Counter����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CounterItemConfig> queryCounterConfigs() throws Exception;

	/**
	 * ��ѯ����KPI����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<KpiItemConfig> queryKpiConfigs() throws Exception;

	/**
	 * ��������ѯͳ������
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryStatData(StatDataQueryCondition condition)
			throws Exception;

	/**
	 * ɾ��ָ��ʱ����ǰ������ͳ�����ݣ�����ԭʼ���ݺ�Ԥͳ�����ݣ�
	 * 
	 * @param endTime
	 * @throws Exception
	 */
	public void deleteStatData(long startTime, long endTime) throws Exception;

}
