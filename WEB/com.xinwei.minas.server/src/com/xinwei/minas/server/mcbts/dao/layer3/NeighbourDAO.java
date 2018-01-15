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
	 * ��ѯ��վ���ڽӱ�
	 * @param moId
	 * @return
	 */
	public List<McBtsNeighbour> queryNeighbour(Long moId);

	/**
	 * ɾ���ɵ��ڽӱ�
	 * @param moId
	 */
	public void deleteOld(Long moId);
	
	/**
	 * �ж�������վ�Ƿ�����ڽӹ�ϵ
	 * @param basicMoId
	 * @param neighbourMoId
	 * @return
	 */
	public boolean isNeighborRelation(Long basicMoId, Long neighbourMoId);
	
	/**
	 * ɾ��ָ���ڽӹ�ϵ
	 * @param basicMoId
	 * @param neighbourMoId
	 */
	public void delNbRelationship(Long basicMoId, Long neighbourMoId);
}
