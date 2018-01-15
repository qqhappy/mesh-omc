package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

public interface ResManagementDAO extends GenericDAO<TConfResmanagement, Long> {

	public TConfResmanagement queryByMoId(Long moId);

	public void saveOrUpdate(TConfResmanagement entity);
}
