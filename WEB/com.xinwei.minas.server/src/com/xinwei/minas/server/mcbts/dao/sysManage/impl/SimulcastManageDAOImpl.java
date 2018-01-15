/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.mcbts.dao.sysManage.SimulcastManageDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 同播资源的持久化类
 * 
 * 
 * @author tiance
 * 
 */

public class SimulcastManageDAOImpl extends
		GenericHibernateDAO<Simulcast, Long> implements SimulcastManageDAO {
	@SuppressWarnings("unchecked")
	@Override
	public List<Simulcast> queryByDistrictId(long districtId) {
		return getHibernateTemplate().find(
				"from Simulcast where districtId = " + districtId);
	}
}
