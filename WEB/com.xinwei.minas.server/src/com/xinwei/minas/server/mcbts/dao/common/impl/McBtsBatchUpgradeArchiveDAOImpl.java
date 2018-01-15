/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.server.mcbts.dao.common.McBtsBatchUpgradeArchiveDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 基站批量升级归档持久层类
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeArchiveDAOImpl extends
		GenericHibernateDAO<UpgradeInfoArchive, Long> implements
		McBtsBatchUpgradeArchiveDAO {

	@Override
	public void saveAll(List<UpgradeInfoArchive> list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UpgradeInfoArchive> queryLatestArchive() {
		return getHibernateTemplate().find(
				"from UpgradeInfoArchive u order by u.moId, u.scheduledTime");
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId) {
		try {
			return getHibernateTemplate().find(
					"FROM UpgradeInfoArchive u where u.moId = " + moId
							+ " order by scheduledTime desc");
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
