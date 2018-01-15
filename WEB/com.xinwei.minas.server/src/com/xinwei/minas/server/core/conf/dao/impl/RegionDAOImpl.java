/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao.impl;

import com.xinwei.minas.core.model.Region;
import com.xinwei.minas.server.core.conf.dao.RegionDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 地区服务DAO
 * 
 * @author chenjunhua
 * 
 */

public class RegionDAOImpl extends GenericHibernateDAO<Region, Long> implements
		RegionDAO {
	
	public Region findByName(String name) {
		return null;
	}
}
