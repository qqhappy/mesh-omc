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
 * ZK��ȺDAO�ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface ZkClusterDAO {
	/**
	 * ��ѯZooKeeper��Ⱥ�б�
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters()throws Exception;
	
	/**
	 * ����ID��ѯ��Ⱥ��Ϣ
	 * @param zkClusterId ��ȺID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId) throws Exception;

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
