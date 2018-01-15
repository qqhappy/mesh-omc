/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.zk.core.basic.ZkCluster;

/**
 * 
 * 
 * @author liuzhongyan
 * 
 */

public interface ZkBasicFacade extends Remote {

	/**
	 * 初始化
	 */
	public void initialize() throws RemoteException, Exception;

	/**
	 * 查询ZooKeeper集群列表
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters() throws RemoteException, Exception;

	/**
	 * 根据ID查询集群信息
	 * 
	 * @param zkClusterId
	 *            集群ID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId)
			throws RemoteException, Exception;

	/**
	 * 增加ZooKeeper集群(增加的同时需要建立与ZK的连接)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	@Loggable
	public void addZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception;

	/**
	 * 修改ZooKeeper集群(修改前需要断开与ZK的连接)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	@Loggable
	public void modifyZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception;

	/**
	 * 删除ZooKeeper集群(需要断开与ZK的连接)
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	@Loggable
	public void deleteZkCluster(Long zkClusterId) throws RemoteException,
			Exception;

}
