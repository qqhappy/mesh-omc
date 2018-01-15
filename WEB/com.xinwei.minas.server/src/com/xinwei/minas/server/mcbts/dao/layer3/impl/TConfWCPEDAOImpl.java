package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfWCPEDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * WCPE配置基本DAO接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfWCPEDAOImpl extends GenericHibernateDAO<TConfWCPE, Long>
		implements TConfWCPEDAO {

	@Override
	public TConfWCPE queryByMoId(Long moId) {
		List list = getHibernateTemplate().find("from TConfWCPE t where t.moId =" + moId);
		if(list.size() != 0 && list != null) {
			return (TConfWCPE) list.get(0);
		}
		return null;
	}
}
