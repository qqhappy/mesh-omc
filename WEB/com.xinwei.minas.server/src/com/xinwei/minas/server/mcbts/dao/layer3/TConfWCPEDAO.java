package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * WCPE配置基本DAO接口
 * 
 * @author yinbinqiang
 *
 */
public interface TConfWCPEDAO extends GenericDAO<TConfWCPE, Long> {
	/**
	 * 查询WCPE配置信息
	 * @param moId
	 * @return
	 */
	public TConfWCPE queryByMoId(Long moId) ;
}
