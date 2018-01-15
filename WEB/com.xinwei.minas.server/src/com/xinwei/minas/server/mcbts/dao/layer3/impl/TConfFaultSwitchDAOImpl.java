package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfFaultSwitchDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class TConfFaultSwitchDAOImpl extends
		GenericHibernateDAO<TConfFaultSwitch, Long> implements
		TConfFaultSwitchDAO {

	@Override
	public TConfFaultSwitch queryByMoid(Long moId) {
		List list = getHibernateTemplate().find("from TConfFaultSwitch t where t.moId="+moId);
		if(list.size() != 0 && list != null) {
			return (TConfFaultSwitch) list.get(0);
		}
		return null;
	}
}
