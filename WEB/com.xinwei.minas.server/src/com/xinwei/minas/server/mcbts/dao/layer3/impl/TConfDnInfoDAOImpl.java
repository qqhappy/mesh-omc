package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfoPK;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfDnInfoDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * 故障弱化中配置号码信息DAO接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfDnInfoDAOImpl extends
		GenericHibernateDAO<TConfDnInfo, TConfDnInfoPK> implements
		TConfDnInfoDAO {

	@Override
	public List<TConfDnInfo> queryByMoid(Long moid) {
		return getHibernateTemplate().find("from TConfDnInfo t where t.id.parentid = " + moid);
	}

	@Override
	public void saveDnInfoList(List<TConfDnInfo> dnInfos) {
		getHibernateTemplate().saveOrUpdateAll(dnInfos);
	}
}
