/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighbour;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * @author chenshaohua
 * 
 */
public interface NeighbourDAO extends GenericDAO<McBtsNeighbour, Long> {

	/**
	 * 查询基站的邻接表
	 * @param moId
	 * @return
	 */
	public List<McBtsNeighbour> queryNeighbour(Long moId);

	/**
	 * 删除旧的邻接表
	 * @param moId
	 */
	public void deleteOld(Long moId);
	
	/**
	 * 判断两个基站是否具有邻接关系
	 * @param basicMoId
	 * @param neighbourMoId
	 * @return
	 */
	public boolean isNeighborRelation(Long basicMoId, Long neighbourMoId);
	
	/**
	 * 删除指定邻接关系
	 * @param basicMoId
	 * @param neighbourMoId
	 */
	public void delNbRelationship(Long basicMoId, Long neighbourMoId);
}
