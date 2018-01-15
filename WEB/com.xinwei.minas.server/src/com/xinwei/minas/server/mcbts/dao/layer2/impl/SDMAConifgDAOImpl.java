package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.mcbts.dao.layer2.SDMAConfigDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * SDMA基本信息DAO实现
 * 
 * @author fangping
 * 
 */
public class SDMAConifgDAOImpl extends GenericHibernateDAO<SDMAConfig, Long>
		implements SDMAConfigDAO {

	@Override
	public SDMAConfig queryByMoId(Long moId) {
		List<SDMAConfig> list = getHibernateTemplate().find(
				"from SDMAConfig t where t.moId=" + moId);
		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
	}
}
