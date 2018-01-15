package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.server.mcbts.dao.layer3.DataPackageFilterDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class DataPackageFilterDAOImpl extends
		GenericHibernateDAO<DataPackageFilter, Long> implements
		DataPackageFilterDAO {
	private Log log = LogFactory.getLog(DataPackageFilterDAOImpl.class);

	@Override
	public void saveOrUpdate(List<DataPackageFilter> entity) {
		List<DataPackageFilter> list = queryAll();
		getHibernateTemplate().deleteAll(list);

		getHibernateTemplate().saveOrUpdateAll(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataPackageFilter> queryAll() {
		return (List<DataPackageFilter>)getHibernateTemplate().find("from DataPackageFilter d order by d.idx asc");
	}
	
}
