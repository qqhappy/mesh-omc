package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.mcbts.dao.layer3.LoadBalanceDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 负载均衡基本信息DAO实现
 * 
 * @author yinbinqiang
 * 
 */
public class LoadBalanceDAOImpl extends
		GenericHibernateDAO<TConfLoadBalance, Long> implements LoadBalanceDAO {

	@Override
	public TConfLoadBalance queryByMoId(Long moId) {
		List<TConfLoadBalance> list = getHibernateTemplate().find(
				"from TConfLoadBalance t where t.moId=" + moId);
		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
	}
}
