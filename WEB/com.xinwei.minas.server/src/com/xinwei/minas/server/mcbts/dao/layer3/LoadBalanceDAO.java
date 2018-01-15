package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 负载均衡基本信息DAO
 * @author yinbinqiang
 *
 */
public interface LoadBalanceDAO extends GenericDAO<TConfLoadBalance, Long> {
	/**
	 * 根据moId查询负载均衡基本信息
	 * @param moId
	 */
	public TConfLoadBalance queryByMoId(Long moId);
}
