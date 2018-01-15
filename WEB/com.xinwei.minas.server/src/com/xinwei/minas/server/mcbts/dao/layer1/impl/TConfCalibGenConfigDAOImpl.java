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

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationGeneralConfig;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibGenConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 校准数据发送接收增益配置信息DAO实现
 * 
 * @author chenjunhua
 * 
 */

public class TConfCalibGenConfigDAOImpl extends
		GenericHibernateDAO<CalibrationGeneralConfig, Long> implements
		TConfCalibGenConfigDAO {

	@Override
	public CalibrationGeneralConfig queryByMoId(Long moId) {
		List<CalibrationGeneralConfig> dataList = getHibernateTemplate().find(
				"from CalibrationGeneralConfig t where t.moId=" + moId);
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		return dataList.get(0);
	}

}
