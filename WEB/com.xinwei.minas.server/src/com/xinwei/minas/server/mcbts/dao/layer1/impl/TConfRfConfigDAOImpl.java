/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfRfConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 校准类型配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfRfConfigDAOImpl extends
		GenericHibernateDAO<RFConfig, Long> implements
		TConfRfConfigDAO {

	@Override
	public RFConfig queryByMoId(Long moId) {
		List<RFConfig> dataList = getHibernateTemplate().find(
				"from RFConfig t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}

}
