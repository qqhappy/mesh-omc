package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * ͬ����վ����DAO�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfMBMSBtsDAO extends GenericDAO<TConfMBMSBts, Long> {
	/**
	 * ��ѯͬ����վ����
	 * @param moid
	 * @return
	 */
	public TConfMBMSBts queryByMoId(Long moId);
}
