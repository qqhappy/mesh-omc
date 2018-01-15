package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfMBMSBtsDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * 同播基站配置DAO接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfMBMSBtsDAOImpl extends
		GenericHibernateDAO<TConfMBMSBts, Long> implements TConfMBMSBtsDAO {

	@Override
	public TConfMBMSBts queryByMoId(Long moId) {
		List list = getHibernateTemplate().find("from TConfMBMSBts t where t.moId=" + moId);
		if(list != null && list.size() != 0) {
			return (TConfMBMSBts) list.get(0);
		}
		return null;
	}
}
