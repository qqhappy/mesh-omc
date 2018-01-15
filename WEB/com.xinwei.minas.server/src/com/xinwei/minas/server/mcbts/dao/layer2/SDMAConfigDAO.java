package com.xinwei.minas.server.mcbts.dao.layer2;

import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 *SDMA基本信息DAO
 * @author fangping
 * 
 *
 */
public interface SDMAConfigDAO extends GenericDAO<SDMAConfig, Long> {
	/**
	 * 根据moId查询负载均衡基本信息
	 * @param moId
	 */
	public SDMAConfig queryByMoId(Long moId);
}
