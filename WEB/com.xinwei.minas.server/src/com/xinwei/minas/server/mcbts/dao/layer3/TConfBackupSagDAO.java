package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 备份SAG参数配置DAO接口
 * 
 * @author yinbinqiang
 *
 */
public interface TConfBackupSagDAO extends GenericDAO<TConfBackupSag, Long> {
	/**
	 * 得到备份SAG参数基本信息
	 * @param moid
	 * @return
	 */
	public TConfBackupSag queryByMoId(Long moid);
}
