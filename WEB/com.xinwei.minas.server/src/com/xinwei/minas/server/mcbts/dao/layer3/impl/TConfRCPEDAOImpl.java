package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRCPEDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class TConfRCPEDAOImpl extends GenericHibernateDAO<TConfRCPE, Long> implements TConfRCPEDAO {

	@Override
	public TConfRCPE queryByMoId(Long moId) {
		List list = getHibernateTemplate().find("from TConfRCPE t where t.moId=" + moId);
		if(list.size() != 0) {
			return (TConfRCPE) list.get(0);
		}
		return null;
	}
}
