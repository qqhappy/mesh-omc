/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service;

import java.util.List;

import com.xinwei.minas.zk.core.basic.ZkCluster;


/**
 * 
 * ZK基本服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface ZkBasicService {
	
	/**
	 * 初始化
	 */
	public void initialize();
	
	/**
	 * 查询ZooKeeper集群列表
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters();
	
	/**
	 * 根据ID查询集群信息
	 * @param zkClusterId 集群ID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId);

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
