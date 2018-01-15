/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.server.enb.stat.dao.EnbRealtimeItemConfigDAO;

/**
 * 
 * eNB实时性能统计项DAO接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeItemConfigDAOImpl extends HibernateDaoSupport implements
		EnbRealtimeItemConfigDAO {

	@Override
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception {
		return getHibernateTemplate().loadAll(EnbRealtimeItemConfig.class);
	}

	@Override
	public void saveItemConfig(List<EnbRealtimeItemConfig> configList)
			throws Exception {
		getHibernateTemplate().saveOrUpdateAll(configList);
	}

}
