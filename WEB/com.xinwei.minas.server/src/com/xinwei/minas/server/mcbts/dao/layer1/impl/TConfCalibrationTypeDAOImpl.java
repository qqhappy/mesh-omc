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

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibrationTypeDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 校准类型配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfCalibrationTypeDAOImpl extends
		GenericHibernateDAO<CalibrationType, Long> implements
		TConfCalibrationTypeDAO {

	@Override
	public CalibrationType queryByMoId(Long moId) {
		List<CalibrationType> dataList = getHibernateTemplate().find(
				"from CalibrationType t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}

}
