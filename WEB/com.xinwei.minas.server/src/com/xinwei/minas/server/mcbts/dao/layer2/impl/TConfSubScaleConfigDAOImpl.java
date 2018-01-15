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
import com.xinwei.minas.mcbts.core.model.layer2.SCGScaleConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubScaleConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 空中链路Scale配置信息DAO实现
 * 
 * @author jiayi
 * 
 */

public class TConfSubScaleConfigDAOImpl extends
		GenericHibernateDAO<SCGScaleConfigItem, Long> implements
		TConfSubScaleConfigDAO {

	@Override
	public List<SCGScaleConfigItem> queryByMoId(Long moId) {
		List<SCGScaleConfigItem> dataList = getHibernateTemplate().find(
				"from SCGScaleConfigItem t where t.moId=" + moId);
		return dataList;
	}

	public void saveOrUpdate(List<SCGScaleConfigItem> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		List<SCGScaleConfigItem> dataList = queryByMoId(configList.get(0).getMoId());
		for (SCGScaleConfigItem dataItem : dataList) {
            delete(dataItem);
		}
		for (SCGScaleConfigItem item : configList) {
			saveOrUpdate(item);
		}
	}

}
