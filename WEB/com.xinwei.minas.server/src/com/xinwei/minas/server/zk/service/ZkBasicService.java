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
 * ZK��������ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface ZkBasicService {
	
	/**
	 * ��ʼ��
	 */
	public void initialize();
	
	/**
	 * ��ѯZooKeeper��Ⱥ�б�
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters();
	
	/**
	 * ����ID��ѯ��Ⱥ��Ϣ
	 * @param zkClusterId ��ȺID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId);

	/**
	 * ����ZooKeeper��Ⱥ
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void addZkCluster(ZkCluster zkCluster) throws Exception;

	/**
	 * �޸�ZooKeeper��Ⱥ
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void modifyZkCluster(ZkCluster zkCluster) throws Exception;

	/**
	 * ɾ��ZooKeeper��Ⱥ
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	public void deleteZkCluster(Long zkClusterId) throws Exception;





}
