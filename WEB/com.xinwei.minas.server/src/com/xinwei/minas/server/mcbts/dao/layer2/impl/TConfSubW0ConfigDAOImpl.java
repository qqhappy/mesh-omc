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
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer2.TConfSubW0ConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 空中链路Scale配置信息DAO实现
 * 
 * @author jiayi
 * 
 */

public class TConfSubW0ConfigDAOImpl extends
		GenericHibernateDAO<W0ConfigItem, Long> implements TConfSubW0ConfigDAO {

	@Override
	public List<W0ConfigItem> queryByMoId(Long moId) {
		List<W0ConfigItem> dataList = getHibernateTemplate().find(
				"from W0ConfigItem t where t.moId=" + moId);
		return dataList;
	}

	public void saveOrUpdate(List<W0ConfigItem> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		List<W0ConfigItem> dataList = queryByMoId(configList.get(0).getMoId());
		for (W0ConfigItem dataItem : dataList) {
            delete(dataItem);
		}
		for (W0ConfigItem item : configList) {
			saveOrUpdate(item);
		}
	}

}
