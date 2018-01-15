package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * 同播基站配置DAO接口
 * 
 * @author yinbinqiang
 *
 */
public interface TConfMBMSBtsDAO extends GenericDAO<TConfMBMSBts, Long> {
	/**
	 * 查询同播基站配置
	 * @param moid
	 * @return
	 */
	public TConfMBMSBts queryByMoId(Long moId);
}
