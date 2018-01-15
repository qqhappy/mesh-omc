package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItemPK;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRCPEItemDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * rcpe uid信息DAO类实现
 * 
 * @author yinbinqiang
 * 
 */
public class TConfRCPEItemDAOImpl extends
		GenericHibernateDAO<TConfRCPEItem, TConfRCPEItemPK> implements
		TConfRCPEItemDAO {

	@Override
	public List<TConfRCPEItem> queryByMoId(Long moId) throws Exception {
		return getHibernateTemplate().find(
				"from TConfRCPEItem t where t.id.parentId=" + moId);
	}

	@Override
	public void removeAll(Long moId) throws Exception {
		List<TConfRCPEItem> all = queryByMoId(moId);

		getHibernateTemplate().deleteAll(all);
	}

	@Override
	public void saveRCPEItems(List<TConfRCPEItem> rcpeItems) throws Exception {
		getHibernateTemplate().saveOrUpdateAll(rcpeItems);
	}

}
