package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfBackupSagDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
/**
 * 备份SAG参数配置DAO接口实现
 * 
 * @author yinbinqiang
 *
 */
public class TConfBackupSagDAOImpl extends
		GenericHibernateDAO<TConfBackupSag, Long> implements TConfBackupSagDAO {

	@Override
	public TConfBackupSag queryByMoId(Long moid) {
		List list = getHibernateTemplate().find(
				"from TConfBackupSag t where t.moId =" + moid);
		if(list.size() != 0 && list != null) {
			return (TConfBackupSag) list.get(0);
		}
		return null;
	}
}
