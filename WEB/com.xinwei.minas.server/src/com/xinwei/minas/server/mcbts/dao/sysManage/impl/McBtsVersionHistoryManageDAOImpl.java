/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionHistoryManageDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionManageDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsVersionHistoryManageDAOImpl extends
		GenericHibernateDAO<McBtsVersionHistory, Long> implements
		McBtsVersionHistoryManageDAO {

	@Override
	public List<McBtsVersionHistory> queryHistroy(Long btsId) {
		List<McBtsVersionHistory> list = getHibernateTemplate().find(
				"from McBtsVersionHistory where btsId = ?", btsId);

		if (list == null || list.size() == 0)
			return null;

		return list;
	}

	@Override
	public McBtsVersionHistory queryHistoryByKey(TDLHistoryKey key) {
		return queryByBtsIdAndVersion(key.getBtsId(), key.getVersion());
	}

	@Override
	public McBtsVersionHistory queryByBtsIdAndVersion(Long btsId, String version) {

		List<McBtsVersionHistory> list = getHibernateTemplate().find(
				"from McBtsVersionHistory where btsId = ? and version = ?",
				btsId, version);

		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public int deleteAllHistory(Long btsId) {
		List<McBtsVersionHistory> list = queryHistroy(btsId);
		if (list == null || list.size() == 0)
			return 0;

		getHibernateTemplate().deleteAll(list);

		return list.size();
	}
}
