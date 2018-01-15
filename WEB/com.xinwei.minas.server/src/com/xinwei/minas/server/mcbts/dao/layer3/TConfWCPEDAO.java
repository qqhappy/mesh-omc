package com.xinwei.minas.server.mcbts.dao.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
/**
 * WCPE���û���DAO�ӿ�
 * 
 * @author yinbinqiang
 *
 */
public interface TConfWCPEDAO extends GenericDAO<TConfWCPE, Long> {
	/**
	 * ��ѯWCPE������Ϣ
	 * @param moId
	 * @return
	 */
	public TConfWCPE queryByMoId(Long moId) ;
}
