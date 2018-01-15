/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface ACLDAO extends GenericDAO<McBtsACL, Long> {

	/**
	 * 查询所有实体
	 */
	public List<McBtsACL> queryAll();

	/**
	 * 根据moid查询全部实体
	 * 
	 * @return
	 */
	public List<McBtsACL> queryByMoId(Long moId);

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(List<McBtsACL> mcBtsACLList, Long moId);

	/**
	 * 删除所有ACL记录
	 * 
	 * @param moId
	 */
	public void deleteAll(Long moId);
}
