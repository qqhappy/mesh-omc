/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.dao;

import java.util.Collection;
import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;

/**
 * 
 * ͳ�������ó־û��ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatItemConfigDAO {

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
	 * ����Counterͳ��������
	 * 
	 * @param configs
	 * @throws Exception
	 */
	public void saveCounterConfig(Collection<CounterItemConfig> configs)
			throws Exception;

	/**
	 * ����KPIͳ��������
	 * 
	 * @param configs
	 * @throws Exception
	 */
	public void saveKpiConfig(Collection<KpiItemConfig> configs)
			throws Exception;

}
