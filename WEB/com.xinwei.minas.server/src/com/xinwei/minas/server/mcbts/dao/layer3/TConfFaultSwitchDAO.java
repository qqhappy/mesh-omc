package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

public interface TConfFaultSwitchDAO extends GenericDAO<TConfFaultSwitch, Long> {
	public TConfFaultSwitch queryByMoid(Long moId);
}
