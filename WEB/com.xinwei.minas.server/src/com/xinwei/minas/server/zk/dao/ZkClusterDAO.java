/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.dao;

import java.util.List;

import com.xinwei.minas.zk.core.basic.ZkCluster;

/**
 * 
 * ZK集群DAO接口
 * 
 * @author chenjunhua
 * 
 */

public interface ZkClusterDAO {
	/**
	 * 查询ZooKeeper集群列表
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters()throws Exception;
	
	/**
	 * 根据ID查询集群信息
	 * @param zkClusterId 集群ID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId) throws Exception;

	/**
	 * 增加ZooKeeper集群
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void addZkCluster(ZkCluster zkCluster) throws Exception;

	/**
	 * 修改ZooKeeper集群
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void modifyZkCluster(ZkCluster zkCluster) throws Exception;

	/**
	 * 删除ZooKeeper集群
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	public void deleteZkCluster(Long zkClusterId) throws Exception;

}
