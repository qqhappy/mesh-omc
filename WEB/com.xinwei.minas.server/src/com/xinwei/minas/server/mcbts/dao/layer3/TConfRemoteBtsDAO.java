package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * Զ�����վ����DAo�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfRemoteBtsDAO extends GenericDAO<TConfRemoteBts, Long> {
	/**
	 * ��ѯԶ�����վ����
	 * @param moid
	 * @return
	 */
	public TConfRemoteBts queryByMoId(Long moId);
}
