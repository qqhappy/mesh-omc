package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.mcbts.dao.layer2.ResManagementDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class ResManagementDAOImpl extends
		GenericHibernateDAO<TConfResmanagement, Long> implements
		ResManagementDAO {

	public TConfResmanagement queryByMoId(Long moId) {

		List<TConfResmanagement> resmanagements = getHibernateTemplate().find(
				"from TConfResmanagement t where t.moId=" + moId);
		if (resmanagements.size() != 0) {
			return resmanagements.get(0);
		} else {
			return null;
		}
	}

}
