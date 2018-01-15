/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.server.enb.model.EnbNeighbourRelation;

/**
 * 
 * eNB邻区关系DAO
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbNeighbourRelationDAO {

	/**
	 * 按条件查询邻区关系记录
	 * 
	 * @param moId
	 * @param srvCellId
	 * @param enbId
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	public List<EnbNeighbourRelation> queryRelation(long moId,
			Integer srvCellId, Long enbId, Integer cellId) throws Exception;

	/**
	 * 添加邻区关系
	 * 
	 * @param relation
	 * @throws Exception
	 */
	public void addRelation(EnbNeighbourRelation relation) throws Exception;

	/**
	 * 修改邻区关系
	 * 
	 * @param relation
	 * @throws Exception
	 */
	public void updateRelation(EnbNeighbourRelation relation) throws Exception;

	/**
	 * 删除邻区关系
	 * 
	 * @param moId
	 * @param srvCellId
	 * @param enbId
	 * @param cellId
	 * @throws Exception
	 */
	public void deleteRelation(long moId, Integer srvCellId, Long enbId,
			Integer cellId) throws Exception;

}
