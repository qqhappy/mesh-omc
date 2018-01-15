/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.mcbts.core.model.layer1.PSConfigItem;
import com.xinwei.minas.server.mcbts.dao.common.GPSDataDao;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * GPS管理消息Dao
 * 
 * 
 * @author tiance
 * 
 */

public class GPSDataDaoImpl extends GenericHibernateDAO<GPSData, Long>
		implements GPSDataDao {

	@Override
	public GPSData queryByMoId(Long moId) {
		
		@SuppressWarnings("unchecked")
		List<GPSData> list = getHibernateTemplate().find(
				"from GPSData where moId =? ", moId);
		if (list == null || list.size() == 0)
			return null;

		return list.get(0);

	}

}
