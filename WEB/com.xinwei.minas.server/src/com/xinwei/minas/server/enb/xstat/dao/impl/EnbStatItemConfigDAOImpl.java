/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.server.enb.xstat.dao.EnbStatItemConfigDAO;

/**
 * 
 * 统计项配置持久化接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatItemConfigDAOImpl extends HibernateDaoSupport implements
		EnbStatItemConfigDAO {

	@Override
	public List<CounterItemConfig> queryCounterConfigs() throws Exception {
		return getHibernateTemplate().loadAll(CounterItemConfig.class);
	}

	@Override
	public List<KpiItemConfig> queryKpiConfigs() throws Exception {
		return getHibernateTemplate().loadAll(KpiItemConfig.class);
	}

	@Override
	public void saveCounterConfig(Collection<CounterItemConfig> configs)
			throws Exception {
		for (CounterItemConfig config : configs) {
			getHibernateTemplate().save(config);
		}
	}

	@Override
	public void saveKpiConfig(Collection<KpiItemConfig> configs)
			throws Exception {
		for (KpiItemConfig config : configs) {
			getHibernateTemplate().save(config);
		}
	}

}
