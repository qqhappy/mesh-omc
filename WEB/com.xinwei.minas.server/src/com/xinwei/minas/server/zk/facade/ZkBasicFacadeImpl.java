/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.zk.service.ZkBasicService;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.minas.zk.core.facade.ZkBasicFacade;

/**
 * 
 * 
 * @author liuzhongyan
 * 
 */

public class ZkBasicFacadeImpl extends UnicastRemoteObject implements
		ZkBasicFacade {
	private  Log log = LogFactory.getLog(ZkBasicFacadeImpl.class);

	protected ZkBasicFacadeImpl() throws RemoteException {
		super();
	}

	
	private ZkBasicService getService() {
		return AppContext.getCtx().getBean(ZkBasicService.class);
	}
	/**
	 * 初始化
	 */
	public void initialize() throws RemoteException, Exception {

	}

	/**
	 * 查询ZooKeeper集群列表
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters() throws RemoteException, Exception {
		try {
			return getService().queryZkClusters();
		} catch (Exception e) {
			log.error(e);
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 根据ID查询集群信息
	 * 
	 * @param zkClusterId
	 *            集群ID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId)
			throws RemoteException, Exception {
		return getService().queryZkClusterById(zkClusterId);
	}

	/**
	 * 增加ZooKeeper集群(增加的同时需要建立与ZK的连接)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void addZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception {
		getService().addZkCluster(zkCluster);
	}

	/**
	 * 修改ZooKeeper集群(修改前需要断开与ZK的连接)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void modifyZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception {
		getService().modifyZkCluster(zkCluster);

	}

	/**
	 * 删除ZooKeeper集群(需要断开与ZK的连接)
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	public void deleteZkCluster(Long zkClusterId) throws RemoteException,
			Exception {
		getService().deleteZkCluster(zkClusterId);
	}

}
