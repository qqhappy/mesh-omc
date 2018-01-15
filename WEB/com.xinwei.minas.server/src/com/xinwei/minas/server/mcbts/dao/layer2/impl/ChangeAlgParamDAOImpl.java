/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer2.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.server.mcbts.dao.layer2.ChangeAlgParamDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class ChangeAlgParamDAOImpl extends
		GenericHibernateDAO<TConfChangeAlgParam, Long> implements
		ChangeAlgParamDAO {

	public TConfChangeAlgParam queryByMoId(Long moId) {
		List<TConfChangeAlgParam> params = getHibernateTemplate().find(
				"from TConfChangeAlgParam t where t.moId=" + moId);
		if (params.size() != 0) {
			return params.get(0);
		} else {
			return null;
		}
	}
}
