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

import com.xinwei.minas.mcbts.core.model.layer1.PSConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfSubRfPsConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 校准类型配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfSubRfPsConfigDAOImpl extends
		GenericHibernateDAO<PSConfigItem, Long> implements
		TConfSubRfPsConfigDAO {

	@Override
	public List<PSConfigItem> queryByMoId(Long moId) {
		List<PSConfigItem> dataList = getHibernateTemplate().find(
				"from PSConfigItem t where t.moId=" + moId);
		return dataList;
	}

	public void saveOrUpdate(List<PSConfigItem> psConfigList) {
		if (psConfigList == null || psConfigList.isEmpty()) {
			return;
		}
		List<PSConfigItem> dataList = queryByMoId(psConfigList.get(0).getMoId());
		for (PSConfigItem psItem : dataList) {
            delete(psItem);
		}
		for (PSConfigItem psItem : psConfigList) {
			saveOrUpdate(psItem);
		}
	}

}
