/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsAppendNeighbor;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * �����ڽӱ����ݿ���ʽӿ�
 * @author zhuxiaozhan
 *
 */
public interface AppendNeighborDAO extends GenericDAO<McBtsAppendNeighbor, Long>{
	
	
	/**
	 * ��ѯ�����ڽӱ�
	 * @param moId
	 * @return
	 */
	public List<McBtsAppendNeighbor> queryNeighbour(Long moId);

	
	/**
	 * ɾ���ɵ��ڽӹ�ϵ
	 * @param moId
	 */
	public void deleteOld(Long moId);
	
	/**
	 * �ж�������վ�Ƿ�����ڽӹ�ϵ
	 * @param basicMoId
	 * @param appendNeighbourMoId
	 * @return
	 */
	public boolean isAppendNeighborRelation(Long basicMoId, Long appendNeighborMoId);
	
	/**
	 * ɾ��ָ�����ڽӹ�ϵ
	 * @param basicMoId
	 * @param appendNeighborMoId
	 */
	public void delAppendNbRelationship(Long basicMoId, Long appendNeighborMoId);
}
