/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * SAG云化业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface SagClusterBizService {

	/**
	 * 查询指定集群ID的SAGRoot
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws Exception
	 */
	public ZkNode querySagRoot(Long zkClusterId) throws Exception;

	/**
	 * 查询指定集群ID的ZkNode路径
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public ZkNode queryNodePath(Long zkClusterId, String nodePath)
			throws Exception;

	/**
	 * 增加一个SAG Group节点信息
	 * 
	 * @param zkClusterId
	 * @param sagGroup
	 * @throws Exception
	 */
	public void addOneSagGroup(Long zkClusterId, ZkNode sagGroup)
			throws Exception;

	/**
	 * 将SAG修改为集群工作模式
	 * 
	 * @param zkClusterId
	 * @param sagId
	 *            SAGID
	 * @throws Exception
	 */
	public void changeSagToClusterMode(Long zkClusterId, Long sagId)
			throws Exception;

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
			throws Exception;

	/**
	 * 增加BTS节点
	 * 
	 * @param zkClusterId
	 * @param btsGroupId
	 * @param btsNode
	 * @throws Exception
	 */
	public void addBts(Long zkClusterId, String path, Long btsGroupId, ZkNode btsNode)
			throws Exception;

	/**
	 * 删除BTS节点
	 * 
	 * @param zkClusterId
	 * @param btsId
	 *            基站ID
	 * @throws Exception
	 */
	public void deleteBts(Long zkClusterId, String path, Long btsId)
			throws Exception;

	/**
	 * 修改指定cluster中指定zkNode的信息
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * 
	 * @throws Exception
	 */
	public void modifyZkNode(Long zkClusterId, ZkNode zkNode) throws Exception;

	/**
	 * 查询指定集群和路径下, 指定类型的ZkNode列表
	 * 
	 * @param zkClusterId
	 * @param nodeType
	 * @return
	 * @throws Exception
	 */
	public List<ZkNode> queryZkNodes(Long zkClusterId, String path, int nodeType)
			throws Exception;

	/**
	 * 在指定路径下添加ZkNode
	 * 
	 * @param zkClusterId
	 * @return
	 * @throws Exception
	 */
	public void addZkNode(Long zkClusterId, ZkNode zkNode, int createMode)
			throws Exception;

	/**
	 * 删除指定路径的ZkNode
	 * 
	 * @param zkClusterId
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public void deleteZkNode(Long zkClusterId, String path) throws Exception;

	/**
	 * 递归增加ZK节点
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * @param createMode
	 * @throws Exception
	 */
	public void addZkNodeRecursive(Long zkClusterId, ZkNode zkNode,
			int createMode) throws Exception;

	/**
	 * 递归删除ZK节点
	 * 
	 * @param zkClusterId
	 * @param zkNode
	 * @throws Exception
	 */
	public void deleteZkNodeRecursive(Long zkClusterId, ZkNode zkNode)
			throws Exception;

	/**
	 * 更新SagRoot节点数据
	 * 
	 * @param zkClusterId	 * 
	 */
	public void updateSagRoot(long zkClusterId) throws Exception;
}
