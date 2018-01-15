/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * SAG�ƻ�ҵ��ӿ�ʵ��
 * 
 * @author liuzhongyan
 * 
 */

public interface SagClusterBizFacade extends Remote {
	/**
	 * ��ѯָ����ȺID��SAGRoot
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws Exception
	 */
	public ZkNode querySagRoot(Long zkClusterId) throws RemoteException,
			Exception;

	/**
	 * ��ѯָ����ȺID��ZkNode·��
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public ZkNode queryNodePath(Long zkClusterId, String nodePath)
			throws RemoteException, Exception;

	/**
	 * ����һ��SAG Group�ڵ���Ϣ
	 * 
	 * @param zkClusterId
	 * @param sagGroup
	 * @throws Exception
	 */
	@Loggable
	public void addOneSagGroup(Long zkClusterId, ZkNode sagGroup)
			throws RemoteException, Exception;

	/**
	 * ��SAG�޸�Ϊ��Ⱥ����ģʽ
	 * 
	 * @param zkClusterId
	 * @param sagId
	 *            SAGID
	 * @throws Exception
	 */
	@Loggable
	public void changeSagToClusterMode(Long zkClusterId, Long sagId)
			throws RemoteException, Exception;

	/**
	 * ��SAG�޸�Ϊ��������ģʽ
	 * 
	 * @param zkClusterId
	 * @param sagId
	 *            SAGID
	 * @throws Exception
	 */
	/*
	 * ���ڹ����е�SAG�˳���Ⱥ��������SAG�����ߣ�����SAG����û�з���BTS�顣���SAG���ڷ���״̬��
	 * �����ͨ���Ƚ����ֹ��л��ķ�ʽ��SAG��BTSȺ���л�������SAG��
	 * �����ĳ��SAG���ڹ���״̬������ӹܵ�BTS��ᱻ�Զ��л�������SAG�¡��л���ɺ󣬿���ͨ��NKCLI���ø�SAG�˳���Ⱥ��
	 * NKCLIͨ������SAGData�ڵ��������ʵ��֪ͨSAG�˳�Ⱥ��
	 * �����SAG������ɣ��򱣳ֵ�ǰ���Ŷ��Լ���BTS����·���ӣ�����˳��ɹ�����SAGɾ�������Ŷӽڵ㣬������֪ͨ����Ⱥ״̬��
	 * NKCLI�ڶ�SAG�����˳�Ⱥ��ʧ�ܺ�
	 * �����������⴦���ɲ���Ա���ݵ�ǰ״̬�����ֹ��л�BTS��Ĳ���������ֱ�Ӷ�SAG�µ�(��Ҫȷ��SAG�û��Ѿ����л�������SAG)��
	 */
	@Loggable
	public void changeSagToSingleMode(Long zkClusterId, Long sagId)
			throws RemoteException, Exception;

	/**
	 * ����BTS�ڵ�
	 * 
	 * @param zkClusterId
	 * @param btsGroupId
	 * @param btsNode
	 * @throws Exception
	 */
	@Loggable
	public void addBts(Long zkClusterId, String path, Long btsGroupId,
			ZkNode btsNode) throws Exception;

	/**
	 * ɾ��BTS�ڵ�
	 * 
	 * @param zkClusterId
	 * @param btsId
	 *            ��վID
	 * @throws Exception
	 */
	@Loggable
	public void deleteBts(Long zkClusterId, String path, Long btsId)
			throws RemoteException, Exception;

	/**
	 * ��ѯZooKeeper��Ⱥ�б�
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters() throws Exception;

	/**
	 * �޸�ָ��cluster��ָ��zkNode����Ϣ
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * 
	 * @throws Exception
	 */
	@Loggable
	public void modifyZkNode(Long zkClusterId, ZkNode zkNode) throws Exception;

	/**
	 * ��ѯָ����ȺID��ָ�����͵�ZkNode�б�
	 * 
	 * @param zkClusterId
	 * @param nodeType
	 * @return
	 * @throws Exception
	 */
	public List<ZkNode> queryZkNodes(Long zkClusterId, String path, int nodeType)
			throws Exception;

	/**
	 * ��ָ��·�������ZkNode
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public void addZkNode(Long zkClusterId, ZkNode zkNode, int createMode)
			throws Exception;

	/**
	 * ɾ��ָ��·����ZkNode
	 * 
	 * @param zkClusterId
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public void deleteZkNode(Long zkClusterId, String path) throws Exception;

	/**
	 * �ݹ�����ZK�ڵ�
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * @param createMode
	 * @throws Exception
	 */
	@Loggable
	public void addZkNodeRecursive(Long zkClusterId, ZkNode zkNode,
			int createMode) throws Exception;

	/**
	 * �ݹ�ɾ��ZK�ڵ�
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * @throws Exception
	 */
	@Loggable
	public void deleteZkNodeRecursive(Long zkClusterId, ZkNode zkNode)
			throws Exception;

	/**
	 * ����SagRoot�ڵ�����
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	@Loggable
	public void updateSagRoot(long zkClusterId) throws Exception;
}
