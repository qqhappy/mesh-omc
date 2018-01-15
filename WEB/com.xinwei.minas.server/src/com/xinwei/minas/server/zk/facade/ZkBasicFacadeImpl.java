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
	 * ��ʼ��
	 */
	public void initialize() throws RemoteException, Exception {

	}

	/**
	 * ��ѯZooKeeper��Ⱥ�б�
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
	 * ����ID��ѯ��Ⱥ��Ϣ
	 * 
	 * @param zkClusterId
	 *            ��ȺID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId)
			throws RemoteException, Exception {
		return getService().queryZkClusterById(zkClusterId);
	}

	/**
	 * ����ZooKeeper��Ⱥ(���ӵ�ͬʱ��Ҫ������ZK������)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void addZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception {
		getService().addZkCluster(zkCluster);
	}

	/**
	 * �޸�ZooKeeper��Ⱥ(�޸�ǰ��Ҫ�Ͽ���ZK������)
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void modifyZkCluster(ZkCluster zkCluster) throws RemoteException,
			Exception {
		getService().modifyZkCluster(zkCluster);

	}

	/**
	 * ɾ��ZooKeeper��Ⱥ(��Ҫ�Ͽ���ZK������)
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	public void deleteZkCluster(Long zkClusterId) throws RemoteException,
			Exception {
		getService().deleteZkCluster(zkClusterId);
	}

}
