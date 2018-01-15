/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.server.mcbts.dao.layer2.AntijammingParamDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class AntijammingParamDAOImpl extends
		GenericHibernateDAO<TConfAntijammingParam, Long> implements
		AntijammingParamDAO {
	private  Log log = LogFactory.getLog(AntijammingParamDAOImpl.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.mcbts.dao.layer2.AntijammingParamDAO#queryByMoId(java.lang.Long)
	 */
	public TConfAntijammingParam queryByMoId(Long moId) {
		// TODO Auto-generated method stub
		// return (TConfAntijammingParam)

		try {
			TConfAntijammingParam tmp = (TConfAntijammingParam) getHibernateTemplate()
					.find("from TConfAntijammingParam t where t.moId=" + moId)
					.get(0);
			return tmp;
		} catch (Exception e) {
			log.error(e);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.mcbts.dao.layer2.AntijammingParamDAO#saveOrUpdate(com.xinwei.minas.mcbts.core.model.layer2.TConfAntijamming)
	 */
	public void saveOrUpdate(TConfAntijammingParam entity) {
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
	public void delete(TConfAntijammingParam entity) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xinwei.minas.server.platform.generic.dao.GenericDAO#queryById(java.io.Serializable)
	 */
	public TConfAntijammingParam queryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
