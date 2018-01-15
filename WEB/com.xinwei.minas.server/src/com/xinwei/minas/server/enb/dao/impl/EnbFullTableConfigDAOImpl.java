/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.server.enb.dao.EnbFullTableConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 整表配置业务DAO层
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableConfigDAOImpl extends
		GenericHibernateDAO<FullTableConfigInfo, Long> implements
		EnbFullTableConfigDAO {

	@Override
	public FullTableConfigInfo queryByMoId(Long moId) {
		@SuppressWarnings("unchecked")
		List<FullTableConfigInfo> result = getHibernateTemplate().find(
				"from FullTableConfigInfo where moId=" + moId);
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<FullTableConfigInfo> queryByStatus(int status) {
		@SuppressWarnings("unchecked")
		List<FullTableConfigInfo> result = getHibernateTemplate().find(
				"from FullTableConfigInfo where status=" + status);
		if (result == null) {
			return new ArrayList<FullTableConfigInfo>();
		}
		return result;
	}

	
}
