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
	 * ��ʼ��
	 */
	public void initialize() throws RemoteException, Exception;

	/**
	 * ��ѯZooKeeper��Ⱥ�б�
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters() throws RemoteException, Exception;

	/**
	 * ����ID��ѯ��Ⱥ��Ϣ
	 * 
	 * @param zkClusterId
	 *            ��ȺID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId)
			throws RemoteException, Exception;

	/**
	 * ����ZooKeeper��Ⱥ(���ӵ�ͬʱ��Ҫ������ZK������)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	@Loggable
	public void addZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception;

	/**
	 * �޸�ZooKeeper��Ⱥ(�޸�ǰ��Ҫ�Ͽ���ZK������)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	@Loggable
	public void modifyZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception;

	/**
	 * ɾ��ZooKeeper��Ⱥ(��Ҫ�Ͽ���ZK������)
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	@Loggable
	public void deleteZkCluster(Long zkClusterId) throws RemoteException,
			Exception;

}
