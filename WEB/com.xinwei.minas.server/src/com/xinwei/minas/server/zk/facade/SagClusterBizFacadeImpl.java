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
 * SAG云化业务接口实现
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
	 * 查询指定集群ID的SAG Group列表
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
	 * 查询指定集群ID的ZkNode路径
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
	 * 增加一个SAG Group节点信息
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
	 * 将SAG修改为集群工作模式
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
	 * 将SAG修改为单独工作模式
	 * 
	 * @param zkClusterId
	 * @param sagId
	 *            SAGID
	 * @throws Exception
	 */
	/*
	 * 正在工作中的SAG退出集群的条件是SAG不在线，或者SAG下面没有服务BTS组。如果SAG正在服务状态，
	 * 则可以通过先进行手工切换的方式将SAG下BTS群组切换到其他SAG下
	 * 。如果某个SAG处于故障状态，则其接管的BTS组会被自动切换到其他SAG下。切换完成后，可以通过NKCLI设置该SAG退出集群。
	 * NKCLI通过设置SAGData节点的数据来实际通知SAG退出群组
	 * 。如果SAG不能完成，则保持当前的排队以及与BTS的链路连接；如果退出成功，则SAG删除自身排队节点，向网管通知自身集群状态。
	 * NKCLI在对SAG进行退出群组失败后
	 * ，不进行特殊处理，由操作员根据当前状态进行手工切换BTS组的操作，或者直接对SAG下电(需要确认SAG用户已经被切换至其他SAG)。
	 */
	public void changeSagToSingleMode(Long zkClusterId, Long sagId)
			throws RemoteException, Exception {
		getService().changeSagToSingleMode(zkClusterId, sagId);
	}

	/**
	 * 增加BTS节点
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
	 * 删除BTS节点
	 * 
	 * @param zkClusterId
	 * @param btsId
	 *            基站ID
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
