/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfAirlinkConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 空中链路配置信息DAO实现
 * 
 * @author jiayi
 * 
 */

public class TConfAirlinkConfigDAOImpl extends
		GenericHibernateDAO<AirlinkConfig, Long> implements
		TConfAirlinkConfigDAO {

	@Override
	public AirlinkConfig queryByMoId(Long moId) {
		List<AirlinkConfig> dataList = getHibernateTemplate().find(
				"from AirlinkConfig t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}

	@Override
	public List<AirlinkConfig> queryByMoIdList(List<Long> moIds) {
		if (moIds == null || moIds.isEmpty()) {
			return new LinkedList(); 
		}
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where 1=2 ");
		for (Long moId : moIds) {
			whereClause.append(" or t.moId=").append(moId);
		}
		List<AirlinkConfig> dataList = getHibernateTemplate().find(
				"from AirlinkConfig t" + whereClause.toString());
		
		if (dataList == null) {
			return new LinkedList();
		}
		
		return dataList;
	}

	
}
