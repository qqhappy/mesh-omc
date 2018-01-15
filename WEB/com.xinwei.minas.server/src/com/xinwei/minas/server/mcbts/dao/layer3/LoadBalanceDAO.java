package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * ���ؾ��������ϢDAO
 * @author yinbinqiang
 *
 */
public interface LoadBalanceDAO extends GenericDAO<TConfLoadBalance, Long> {
	/**
	 * ����moId��ѯ���ؾ��������Ϣ
	 * @param moId
	 */
	public TConfLoadBalance queryByMoId(Long moId);
}
