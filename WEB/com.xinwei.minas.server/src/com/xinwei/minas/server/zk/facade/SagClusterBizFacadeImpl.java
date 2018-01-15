/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.zk.service.SagClusterBizService;
import com.xinwei.minas.server.zk.service.ZkBasicService;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.minas.zk.core.facade.SagClusterBizFacade;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * SAG�ƻ�ҵ��ӿ�ʵ��
 * 
 * @author liuzhongyan
 * 
 */

@SuppressWarnings("serial")
public class SagClusterBizFacadeImpl extends UnicastRemoteObject implements
		SagClusterBizFacade {

	protected SagClusterBizFacadeImpl() throws RemoteException {
		super();
	}

	private SagClusterBizService getService() {

		return AppContext.getCtx().getBean(SagClusterBizService.class);
	}

	private ZkBasicService getBasicService() {
		return AppContext.getCtx().getBean(ZkBasicService.class);
	}

	/**
	 * ��ѯָ����ȺID��SAG Group�б�
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws Exception
	 */
	public ZkNode querySagRoot(Long zkClusterId) throws RemoteException,
			Exception {
		return getService().querySagRoot(zkClusterId);
	}

	/**
	 * ��ѯָ����ȺID��ZkNode·��
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public ZkNode queryNodePath(Long zkClusterId, String nodePath)
			throws RemoteException, Exception{
		return getService().queryNodePath(zkClusterId , nodePath);
	}

	
	/**
	 * ����һ��SAG Group�ڵ���Ϣ
	 * 
	 * @param zkClusterId
	 * @param sagGroup
	 * @throws Exception
	 */
	public void addOneSagGroup(Long zkClusterId, ZkNode sagGroup)
			throws RemoteException, Exception {
		getService().addOneSagGroup(zkClusterId, sagGroup);
	}

	/**
	 * ��SAG�޸�Ϊ��Ⱥ����ģʽ
	 * 
	 * @param zkClusterId
	 * @param sagId
	 *            SAGID
	 * @throws Exception
	 */
	public void changeSagToClusterMode(Long zkClusterId, Long sagId)
			throws RemoteException, Exception {
		getService().changeSagToClusterMode(zkClusterId, sagId);
	}

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
	public void changeSagToSingleMode(Long zkClusterId, Long sagId)
			throws RemoteException, Exception {
		getService().changeSagToSingleMode(zkClusterId, sagId);
	}

	/**
	 * ����BTS�ڵ�
	 * 
	 * @param zkClusterId
	 * @param zkBts
	 * @throws Exception
	 */
	@Override
	public void addBts(Long zkClusterId, String path, Long btsGroupId, ZkNode btsNode)
			throws Exception {
		getService().addBts(zkClusterId, path, btsGroupId, btsNode);
	}

	/**
	 * ɾ��BTS�ڵ�
	 * 
	 * @param zkClusterId
	 * @param btsId
	 *            ��վID
	 * @throws Exception
	 */
	public void deleteBts(Long zkClusterId, String path, Long btsId) throws RemoteException,
			Exception {
		getService().deleteBts(zkClusterId, path, btsId);
	}

	@Override
	public List<ZkCluster> queryZkClusters() throws Exception {
		return getBasicService().queryZkClusters();
	}

	@Override
	public void modifyZkNode(Long zkClusterId, ZkNode zkNode) throws Exception {
		getService().modifyZkNode(zkClusterId, zkNode);
	}

	@Override
	public List<ZkNode> queryZkNodes(Long zkClusterId, String path, int nodeType)
			throws Exception {
		return getService().queryZkNodes(zkClusterId, path,  nodeType);
	}

	
	@Override
	public void addZkNode(Long zkClusterId, ZkNode zkNode, int createMode)
			throws Exception {
		getService().addZkNode(zkClusterId, zkNode, createMode);
	}
	
	@Override
	public void deleteZkNode(Long zkClusterId, String path)
			throws Exception {
		getService().deleteZkNode(zkClusterId, path);
	}

	@Override
	public void addZkNodeRecursive(Long zkClusterId, ZkNode zkNode,
			int createMode) throws Exception {
		getService().addZkNodeRecursive(zkClusterId, zkNode, createMode);
	}

	@Override
	public void deleteZkNodeRecursive(Long zkClusterId, ZkNode zkNode)
			throws Exception {
		getService().deleteZkNodeRecursive(zkClusterId, zkNode);
	}

	@Override
	public void updateSagRoot(long zkClusterId) throws Exception {
		getService().updateSagRoot(zkClusterId);
	}
}
