/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface RepeaterDAO extends GenericDAO<McBtsRepeater, Long> {

	/**
	 * 查询所有记录
	 */
	public List<McBtsRepeater> queryByMoId(Long moId);

	/**
	 * 保存实体
	 */
	public void saveOrUpdate(List<McBtsRepeater> mcBtsRepeaterList, Long moId);
}
