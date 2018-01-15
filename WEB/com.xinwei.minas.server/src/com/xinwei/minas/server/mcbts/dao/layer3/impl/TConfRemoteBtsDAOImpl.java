package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRemoteBtsDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * 远距离基站配置DAo接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfRemoteBtsDAOImpl extends GenericHibernateDAO<TConfRemoteBts, Long> implements TConfRemoteBtsDAO {

	@Override
	public TConfRemoteBts queryByMoId(Long moId) {
		List list = getHibernateTemplate().find("from TConfRemoteBts t where t.moId = " + moId);
		if(list != null && list.size() != 0) {
			return (TConfRemoteBts) list.get(0);
		}
		return null;
	}
}
