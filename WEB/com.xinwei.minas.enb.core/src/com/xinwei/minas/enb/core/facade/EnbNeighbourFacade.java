/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB邻区关系配置门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbNeighbourFacade extends Remote {

	/**
	 * 查询指定网元所有邻区记录
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<EnbNeighbourRecord> queryNeighbourRecords(long moId)
			throws Exception;

	/**
	 * 按条件查询邻区记录
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public EnbNeighbourRecord queryNeighbourRecord(long moId,
			XBizRecord condition) throws Exception;

	/**
	 * 添加指定网元的邻区关系表记录，如果记录中邻小区与服务小区互为邻区，则将记录添加至该网元邻区关系表后，
	 * 还需要在邻区所在eNB的邻区关系表中添加对应记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void addNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

	/**
	 * 更新指定网元的邻区记录，如果之前互为邻区，改为非邻区，需要将对应eNB邻区关系表中的相应记录删除；如果之前非邻区，改为互为邻区，
	 * 需要在对应eNB邻区关系表中添加相应记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void updateNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

	/**
	 * 删除指定网元的邻区记录，如果互为邻区，则需要将对应eNB邻区关系表中的相应记录删除
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void deleteNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

}
