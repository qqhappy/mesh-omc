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
 * 附加邻接表数据库访问接口
 * @author zhuxiaozhan
 *
 */
public interface AppendNeighborDAO extends GenericDAO<McBtsAppendNeighbor, Long>{
	
	
	/**
	 * 查询附加邻接表
	 * @param moId
	 * @return
	 */
	public List<McBtsAppendNeighbor> queryNeighbour(Long moId);

	
	/**
	 * 删除旧的邻接关系
	 * @param moId
	 */
	public void deleteOld(Long moId);
	
	/**
	 * 判断两个基站是否具有邻接关系
	 * @param basicMoId
	 * @param appendNeighbourMoId
	 * @return
	 */
	public boolean isAppendNeighborRelation(Long basicMoId, Long appendNeighborMoId);
	
	/**
	 * 删除指定的邻接关系
	 * @param basicMoId
	 * @param appendNeighborMoId
	 */
	public void delAppendNbRelationship(Long basicMoId, Long appendNeighborMoId);
}
