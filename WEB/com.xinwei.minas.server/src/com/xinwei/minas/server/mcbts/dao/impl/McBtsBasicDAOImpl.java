/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.dao.McBtsBasicDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * McBts基本信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBasicDAOImpl extends GenericHibernateDAO<McBts, Long> implements McBtsBasicDAO {
	public McBts queryByMoId(Long moId){
		List<McBts> dataList = getHibernateTemplate().find(
				"from McBts t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
		
	}
	
}
