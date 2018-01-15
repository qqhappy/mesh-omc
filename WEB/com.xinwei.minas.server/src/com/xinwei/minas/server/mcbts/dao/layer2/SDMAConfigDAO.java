package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 *SDMA������ϢDAO
 * @author fangping
 * 
 *
 */
public interface SDMAConfigDAO extends GenericDAO<SDMAConfig, Long> {
	/**
	 * ����moId��ѯ���ؾ��������Ϣ
	 * @param moId
	 */
	public SDMAConfig queryByMoId(Long moId);
}
