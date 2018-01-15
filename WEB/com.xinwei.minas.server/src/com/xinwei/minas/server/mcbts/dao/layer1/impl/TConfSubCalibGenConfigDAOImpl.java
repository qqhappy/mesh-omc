/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfSubCalibGenConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 校准类型配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfSubCalibGenConfigDAOImpl extends
		GenericHibernateDAO<CalibGenConfigItem, Long> implements
		TConfSubCalibGenConfigDAO {

	@Override
	public List<CalibGenConfigItem> queryByMoId(Long moId) {
		List<CalibGenConfigItem> dataList = getHibernateTemplate().find(
				"from CalibGenConfigItem t where t.moId=" + moId);
		return dataList;
	}

	public void saveOrUpdate(List<CalibGenConfigItem> configList) {
		if (configList == null || configList.isEmpty()) {
			return;
		}
		List<CalibGenConfigItem> dataList = queryByMoId(configList.get(0).getMoId());
		for (CalibGenConfigItem item : dataList) {
            delete(item);
		}
		for (CalibGenConfigItem item : configList) {
			saveOrUpdate(item);
		}
	}

}
