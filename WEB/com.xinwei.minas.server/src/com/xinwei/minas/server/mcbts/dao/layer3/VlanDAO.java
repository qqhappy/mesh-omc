/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface VlanDAO extends GenericDAO<McBtsVlan, Long> {

	/**
	 * 查询全部记录
	 */
	public List<McBtsVlan> queryAllByMoId(Long moId);

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 */
	public void persistent(long moId, List<McBtsVlan> mcBtsVlanList);

	public McBtsVlanAttach queryAttachByMoId(long moId);

	public void saveAttach(McBtsVlanAttach attach);
}
