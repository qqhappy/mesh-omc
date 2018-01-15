/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.mcbts.dao.layer1.McbtsAntennaLockDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 天线闭锁配置DAO实现类
 * 
 * @author fanhaoyu
 * 
 */

public class McbtsAntennaLockDAOImpl extends
		GenericHibernateDAO<McBtsAntennaLock, Long> implements
		McbtsAntennaLockDAO {

	@SuppressWarnings("unchecked")
	@Override
	public McBtsAntennaLock queryByMoId(Long moId) {
		List<McBtsAntennaLock> dataList = getHibernateTemplate().find(
				"from McbtsAntennaLock t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}
}