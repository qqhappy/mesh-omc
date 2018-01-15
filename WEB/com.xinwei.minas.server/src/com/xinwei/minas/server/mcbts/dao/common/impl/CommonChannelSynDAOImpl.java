/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;
import com.xinwei.minas.server.mcbts.dao.common.CommonChannelSynDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 *网归信息同步访问类
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class CommonChannelSynDAOImpl extends
		GenericHibernateDAO<CommonChannelSynInfo, Long> implements
		CommonChannelSynDAO {

	@Override
	public CommonChannelSynInfo queryByMoId(long moId) {
		@SuppressWarnings("unchecked")
		List<CommonChannelSynInfo> synInfos = getHibernateTemplate().find("from CommonChannelSynInfo m where m.moId=" + moId);
		if (synInfos == null || synInfos.size() == 0) {
			return null;
		}
		return synInfos.get(0);
	}

}
