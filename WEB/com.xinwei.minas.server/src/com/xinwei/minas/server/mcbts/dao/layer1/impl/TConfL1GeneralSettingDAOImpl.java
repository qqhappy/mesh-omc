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

import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfL1GeneralSettingDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * L1配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfL1GeneralSettingDAOImpl extends
		GenericHibernateDAO<L1GeneralSetting, Long> implements
		TConfL1GeneralSettingDAO {

	@Override
	public L1GeneralSetting queryByMoId(Long moId) {
		List<L1GeneralSetting> dataList = getHibernateTemplate().find(
				"from L1GeneralSetting t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}

}
