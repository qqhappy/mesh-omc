/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubChannelConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 空中链路通道配置信息DAO实现
 * 
 * @author jiayi
 * 
 */

public class TConfSubChannelConfigDAOImpl extends
		GenericHibernateDAO<SCGChannelConfigItem, Long> implements
		TConfSubChannelConfigDAO {

	@Override
	public List<SCGChannelConfigItem> queryByMoId(Long moId) {
		List<SCGChannelConfigItem> dataList = getHibernateTemplate().find(
				"from SCGChannelConfigItem t where t.moId=" + moId);
		return dataList;
	}

	public void saveOrUpdate(Long moId, List<SCGChannelConfigItem> configList) {
		if (configList == null) {
			return;
		}
		List<SCGChannelConfigItem> dataList = queryByMoId(moId);
		for (SCGChannelConfigItem dataItem : dataList) {
            delete(dataItem);
		}
		for (SCGChannelConfigItem item : configList) {
			saveOrUpdate(item);
		}
	}

}
