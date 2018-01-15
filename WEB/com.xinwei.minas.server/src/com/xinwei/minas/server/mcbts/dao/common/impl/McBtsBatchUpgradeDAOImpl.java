/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.server.mcbts.dao.common.McBtsBatchUpgradeDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 基站批量升级持久化的类
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeDAOImpl extends
		GenericHibernateDAO<UpgradeInfo, Long> implements McBtsBatchUpgradeDAO {

	public McBtsBatchUpgradeDAOImpl() {
		super();
	}

	@Override
	public void saveAll(List<UpgradeInfo> list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	@Override
	public UpgradeInfo queryByMoId(long moId) {
		@SuppressWarnings("unchecked")
		List<UpgradeInfo> list = getHibernateTemplate().find(
				"from UpgradeInfo u where moId = " + moId
						+ " order by u.idx desc");

		if (list == null || list.size() == 0)
			return null;

		return list.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public UpgradeInfo queryFreeUpgradeInfo() {
		List<UpgradeInfo> list = getHibernateTemplate()
				.find("from UpgradeInfo u where u.status = 0 and u.scheduledTime < now() order by u.scheduledTime, u.idx");

		if (list == null || list.size() == 0)
			return null;

		return list.get(0);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UpgradeInfo> queryToArchive() {
		return getHibernateTemplate().find(
				"from UpgradeInfo u where u.status not in(0, 10, 40)");
	}

	@Override
	public void deleteArchive(List<UpgradeInfo> listToArchive) {
		getHibernateTemplate().deleteAll(listToArchive);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version) {
		return getHibernateTemplate().find(
				"from UpgradeInfo u where u.downloadVersion = '" + version
						+ "' and u.status = 0");
	}
}
