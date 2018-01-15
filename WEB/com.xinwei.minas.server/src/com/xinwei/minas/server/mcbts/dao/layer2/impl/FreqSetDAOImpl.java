/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.minas.server.mcbts.dao.layer2.FreqSetDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class FreqSetDAOImpl extends GenericHibernateDAO<TConfFreqSet, Long>
		implements FreqSetDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.mcbts.dao.layer2.FreqSetDAO#queryByMoId(java.lang.Long)
	 */
	public TConfFreqSet queryByMoId(Long moId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.mcbts.dao.layer2.FreqSetDAO#saveOrUpdate(com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet)
	 */
	public void saveOrUpdate(TConfFreqSet entity) {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.platform.generic.dao.GenericDAO#delete(java.io.Serializable)
	 */
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.platform.generic.dao.GenericDAO#delete(java.lang.Object)
	 */
	public void delete(TConfFreqSet entity) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.platform.generic.dao.GenericDAO#queryById(java.io.Serializable)
	 */
	public TConfFreqSet queryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
