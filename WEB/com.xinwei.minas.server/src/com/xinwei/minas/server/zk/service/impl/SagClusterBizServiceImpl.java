/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service.impl;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.server.zk.net.ZkClusterConnectorManager;
import com.xinwei.minas.server.zk.net.ZkNodeHelper;
import com.xinwei.minas.server.zk.net.impl.ZkClusterConnectorZkClientImpl;
import com.xinwei.minas.server.zk.service.SagClusterBizService;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;
import com.xinwei.minas.zk.core.facade.ZkCallbackFacade;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeHeader;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsStatusVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagNodeLockVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagVO;
import com.xinwei.omp.server.OmpAppContext;

/**
 * SAG云化业务服务接口实现
 * 
 * @author chenjunhua
 */

public class SagClusterBizServiceImpl implements SagClusterBizService {

	private static final Log log = LogFactory
			.getLog(SagClusterBizServiceImpl.class);

	private ZkClusterConnectorManager connectorManager;

	@Override
	public ZkNode querySagRoot(Long zkClusterId) throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		return connector.getSagRoot();
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
			throws Exception {
		ZkNode zkNode = querySagRoot(zkClusterId);
		ZkNode zkNodeByPath = zkNode.findByPath(nodePath);
		return zkNodeByPath;
	}

	@Override
	public void addOneSagGroup(Long zkClusterId, final ZkNode sagGroup)
			throws Exception {
		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) this
				.getConnectorBy(zkClusterId);
		Lock lock = connector.getLock();
		if (!lock.tryLock()) {
			throw new Exception(
					OmpAppContext.getMessage("connection_is_occupied"));
		}
		try {
			ZkNode sagRoot = connector.getSagRoot();
			// 校验基站ID是否与其他GROUP下的基站ID重复
			checkInputSagGroup(sagRoot, sagGroup);
			// 查找与新的sagGroup名称相同的节点
			Set<ZkNode> childSet = sagRoot.getChildren();
			ZkNode sameNode = null;
			for (ZkNode child : childSet) {
				// 存在
				if (child.getName().equals(sagGroup.getName())) {
					sameNode = child;
					break;
				}
			}
			// 如果存在name相同的sagGroup节点
			if (sameNode != null) {
				ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sameNode.getZkNodeVO();
				// 处于初始化或已停用状态
				if (sagGroupVO.isInitialized() || sagGroupVO.isStopped()) {
					this.deleteSagGroup(zkClusterId, sameNode, true);
				} else if (sagGroupVO.isStarted()) {
					String message = OmpAppContext
							.getMessage("sagGroup_has_been_started_up_cannot_be_deleted");
					throw new Exception(MessageFormat.format(message,
							String.valueOf(sameNode.getName())));
				}
			}
			// 递归增加SAG Group节点
			this.addSagGroup(zkClusterId, sagGroup, true);
		} finally {
			lock.unlock();
		}
		notifyZkClientRefresh();
	}

	/**
	 * 校验输入的SAG Group数据
	 * 
	 * @param sagRoot
	 * @param sagGroup
	 * @throws Exception
	 */
	private void checkInputSagGroup(ZkNode sagRoot, ZkNode sagGroup)
			throws Exception {
		Set<ZkNode> children = sagRoot.getChildren();
		// 获取其他Sag Group
		Set<ZkNode> otherSagGroups = new LinkedHashSet<ZkNode>();
		for (ZkNode child : children) {
			if (child.getName().equalsIgnoreCase(sagGroup.getName())) {
				continue;
			}
			otherSagGroups.add(child);
		}
		// 校验基站ID是否重复
		this.checkDuplicatedBtsId(sagGroup, otherSagGroups);

		// 校验SAG ID是否重复
		this.checkDuplicatedSagId(sagGroup, otherSagGroups);
	}

	/**
	 * 校验基站ID是否重复
	 * 
	 * @param sagGroup
	 * @param otherSagGroups
	 * @throws Exception
	 */
	private void checkDuplicatedBtsId(ZkNode sagGroup,
			Set<ZkNode> otherSagGroups) throws Exception {
		List<ZkNode> newBtsNodes = this.getNodesByType(sagGroup,
				ZkNodeConstant.NODE_TYPE_BTS);
		for (ZkNode child : otherSagGroups) {
			List<ZkNode> btsNodes = this.getNodesByType(child,
					ZkNodeConstant.NODE_TYPE_BTS);
			for (ZkNode newBtsNode : newBtsNodes) {
				Long newBtsId = ((ZkBtsVO) newBtsNode.getZkNodeVO()).getBtsId();
				for (ZkNode btsNode : btsNodes) {
					Long btsId = ((ZkBtsVO) btsNode.getZkNodeVO()).getBtsId();
					if ((newBtsId.longValue() & 0xffffffff) == (btsId
							.longValue() & 0xffffffff)) {
						throw new Exception(
								OmpAppContext.getMessage("duplicated_bts_id")
										+ ": 0x" + Long.toHexString(newBtsId));
					}
				}
			}
		}
	}

	/**
	 * 校验SAG ID是否重复
	 * 
	 * @param sagGroup
	 * @param otherSagGroups
	 * @throws Exception
	 */
	private void checkDuplicatedSagId(ZkNode sagGroup,
			Set<ZkNode> otherSagGroups) throws Exception {
		List<ZkNode> newSagNodes = this.getNodesByType(sagGroup,
				ZkNodeConstant.NODE_TYPE_SAG);
		for (ZkNode child : otherSagGroups) {
			List<ZkNode> sagNodes = this.getNodesByType(child,
					ZkNodeConstant.NODE_TYPE_SAG);
			for (ZkNode newSagNode : newSagNodes) {
				Long newSagId = ((ZkSagVO) newSagNode.getZkNodeVO()).getSagId();
				for (ZkNode sagNode : sagNodes) {
					Long sagId = ((ZkSagVO) sagNode.getZkNodeVO()).getSagId();
					if (sagId.longValue() == newSagId.longValue()) {
						throw new Exception(
								OmpAppContext.getMessage("duplicated_sag_id")
										+ ": SAG ID=" + sagId);
					}
				}
			}
		}
	}

	/*
	 * 判断该sag是否存在 如果存在，判断该sag状态 如果为已启用状态，则返回 否则将sag状态更新为启用 注册watch
	 */
	@Override
	public void changeSagToClusterMode(Long zkClusterId, Long sagId)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode sagNode = null;
		// 查找sagNode
		Iterator<ZkNode> iterator = connector.getSagRoot()
				.createLevelOrderIterator();
		while (iterator.hasNext()) {
			ZkNode node = iterator.next();

			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG) {
				ZkSagVO childVo = (ZkSagVO) node.getZkNodeVO();

				if (childVo.getSagId().equals(sagId)) {
					sagNode = node;
					break;
				}
			}
		}

		// 如果存在这样的SAG
		if (sagNode != null) {
			ZkSagVO sagVo = (ZkSagVO) sagNode.getZkNodeVO();
			// 如果已经启用
			if (sagVo.isStarted()) {
				throw new Exception(
						OmpAppContext
								.getMessage("started_SAG_cannot_be_restarted"));
			}
			// 启用
			sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STARTED);
			// 下发
			connector.writeData(sagNode.getPath(), sagNode.encode());

			ZkNode sagGroupNode = sagNode.getParent().getParent();
			ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sagGroupNode.getZkNodeVO();

			// TODO: 如果该SAG所在的组未被启用，将其启用 (需要确认)
			if (!sagGroupVO.isStarted()) {
				sagGroupVO.setGroupStatus(ZkSagGroupVO.SAGGROUP_STATUS_STARTED);
				connector.writeData(sagGroupNode.getPath(),
						sagGroupNode.encode());

			}
		}
	}

	@Override
	public void changeSagToSingleMode(Long zkClusterId, Long sagId)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode sagNode = null;
		// 查找sagNode
		Iterator<ZkNode> iterator = connector.getSagRoot()
				.createLevelOrderIterator();
		while (iterator.hasNext()) {
			ZkNode node = iterator.next();

			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG) {
				ZkSagVO childVo = (ZkSagVO) node.getZkNodeVO();

				if (childVo.getSagId().equals(sagId)) {
					sagNode = node;
					break;
				}
			}
		}

		// 如果存在这样的SAG
		if (sagNode != null) {
			ZkSagVO sagVo = (ZkSagVO) sagNode.getZkNodeVO();
			// 如果该SAG已启用
			if (sagVo.isStarted()) {
				ZkNode sagDataNode = (ZkNode) sagNode.getChildren().toArray()[0];
				ZkNode groupedBts = (ZkNode) sagDataNode.getChildren()
						.toArray()[0];
				Set<ZkNode> childrenSet = groupedBts.getChildren();
				if (childrenSet != null && !childrenSet.isEmpty()) {
					throw new Exception(
							OmpAppContext
									.getMessage("please_switch_BtsGroup_to_other_SAG_before_stop_it"));
				} else {
					// 停用
					sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STOPPED);
					// 下发
					connector.writeData(sagNode.getPath(), sagNode.encode());
				}
			} else {
				// 停用
				sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STOPPED);
				// 下发
				connector.writeData(sagNode.getPath(), sagNode.encode());
			}

			// TODO: 如果所有sag已停用，则停用该sagGroup(待确认)
			ZkNode sagGroup = sagNode.getParent().getParent();
			if (isSagGroupStoped(sagGroup)) {
				ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sagGroup.getZkNodeVO();

				sagGroupVO.setGroupStatus(ZkSagGroupVO.SAGGROUP_STATUS_STOPPED);
				connector.writeData(sagGroup.getPath(), sagGroup.encode());

			}
		}
	}

	/**
	 * 判断一个SAG群组是否已停用 依据为该群组中所有的SAG都已停用，则该SAG群组已停用；否则未停用
	 * 
	 * @param sagGroup
	 * @return
	 * @throws Exception
	 */
	private boolean isSagGroupStoped(ZkNode sagGroup) throws Exception {
		ZkNode sagInfo = null;
		for (ZkNode child : sagGroup.getChildren()) {
			if (child.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG_INFO) {
				sagInfo = child;
				break;
			}
		}
		if (sagInfo != null) {
			ZkSagVO sagVO = null;
			// 如果有sag已启用，则该sagGroup未停用
			for (ZkNode sagNode : sagInfo.getChildren()) {
				sagVO = (ZkSagVO) sagNode.getZkNodeVO();
				if (sagVO.isStarted())
					return false;
			}
		}
		return true;
	}

	@Override
	public void addBts(Long zkClusterId, String path, Long btsGroupId,
			ZkNode btsNode) throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		// 校验基站ID是否重复
		Long btsId = ((ZkBtsVO) btsNode.getZkNodeVO()).getBtsId();
		List<ZkNode> nodeList = queryZkNodes(zkClusterId, path,
				ZkNodeConstant.NODE_TYPE_BTS);
		for (ZkNode node : nodeList) {
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS) {
				ZkBtsVO childVo = (ZkBtsVO) node.getZkNodeVO();
				if (childVo == null) {
					continue;
				}
				if ((childVo.getBtsId().longValue() & 0xfff) == (btsId
						.longValue() & 0xfff)) {
					// 基站ID重复，抛出异常
					throw new Exception(MessageFormat.format(
							OmpAppContext.getMessage("duplicated_bts_id"),
							node.getName()));
				}
			}
		}

		ZkNode btsGroup = null;
		ZkBtsGroupVO btsGroupVO = null;
		// 查找新增基站要添加到的btsGroup
		ZkNode sagGroupNode = connector.getSagRoot().findByPath(path);
		Iterator<ZkNode> iterator = sagGroupNode.createLevelOrderIterator();
		while (iterator.hasNext()) {
			ZkNode node = iterator.next();
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS_GROUP) {
				ZkBtsGroupVO childVo = (ZkBtsGroupVO) node.getZkNodeVO();
				if (childVo.getBtsGroupId().equals(btsGroupId)) {
					btsGroup = node;
					btsGroupVO = childVo;
				}
			}
		}

		// 如果存在这样的btsGroup
		if (btsGroup != null) {
			int currentBtsNum = btsGroup.getChildren().size();
			if (currentBtsNum >= btsGroupVO.getMaxSubNode()) {
				throw new Exception(
						OmpAppContext
								.getMessage("reach_max_bts_num_cannot_add"));
			}
			// 增加基站节点
			this.addZkNode(zkClusterId, btsNode,
					ZkNodeConstant.CREATE_MODE_PERSISTENT);
			// 增加SAGLink节点
			ZkNode sagLink = new ZkNode(btsNode.getPath() + "/" + "SAGLink");
			sagLink.setHeader(new ZkNodeHeader(ZkNodeConstant.NODE_TYPE_SAGLINK));
			ZkBtsSagLinkVO sagLinkVO = new ZkBtsSagLinkVO();
			sagLinkVO.setZkNodeReserve(new ZkNodeReserve());
			sagLinkVO.setSagId(0xFFFFFFFF);
			sagLinkVO.setSagDPID(0);
			sagLinkVO.setSagSignalIp("0.0.0.0");
			sagLinkVO.setSagSignalPort(0);
			sagLinkVO.setSagMediaIP("0.0.0.0");
			sagLinkVO.setSagMediaPort(0);
			sagLink.setZkNodeVO(sagLinkVO);
			this.addZkNode(zkClusterId, sagLink,
					ZkNodeConstant.CREATE_MODE_PERSISTENT);
			// 2013-06-04和wangdaojing确认BtsStatus节点由SAG创建，为临时节点
		}
	}

	@Override
	public void deleteBts(Long zkClusterId, String path, Long btsId)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode btsNode = null;
		// 查找btsNode
		ZkNode nodeRoot = connector.getSagRoot().findByPath(path);
		Iterator<ZkNode> iterator = nodeRoot.createLevelOrderIterator();
		while (iterator.hasNext()) {
			ZkNode node = iterator.next();
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS) {
				ZkBtsVO childVo = (ZkBtsVO) node.getZkNodeVO();
				if (childVo == null) {
					continue;
				}
				if (childVo.getBtsId().equals(btsId)) {
					btsNode = node;
					break;
				}
			}
		}

		// 如果存在这样的BTS
		if (btsNode != null) {
			Set<ZkNode> linkNodeSet = btsNode.getChildren();
			if (linkNodeSet != null && !linkNodeSet.isEmpty()) {
				ZkNode linkNode = (ZkNode) linkNodeSet.toArray()[0];
				Set<ZkNode> statusNodeSet = linkNode.getChildren();

				if (statusNodeSet != null && !statusNodeSet.isEmpty()) {
					ZkNode statusNode = (ZkNode) statusNodeSet.toArray()[0];
					ZkBtsStatusVO statusVO = (ZkBtsStatusVO) statusNode
							.getZkNodeVO();
					// 如果信令和媒体都是断开状态
					if (statusVO.getMediaLinkState() == ZkBtsStatusVO.BTS_MEDIA_LINK_DISCONNECTED
							&& statusVO.getSignalLinkState() == ZkBtsStatusVO.BTS_SIGNAL_LINK_DISCONNECTED) {
						// 删除btsNode及其子节点
						this.deleteZkNodeRecursive(zkClusterId, btsNode);
						// connector.deleteRecursive(btsNode.getPath());
					} else {
						throw new Exception(
								OmpAppContext
										.getMessage("disconn_media_and_signal_link_before_delete_bts"));
					}
				} else {
					// connector.deleteRecursive(btsNode.getPath());
					this.deleteZkNodeRecursive(zkClusterId, btsNode);
				}
			} else {
				// connector.delete(btsNode.getPath());
				this.deleteZkNodeRecursive(zkClusterId, btsNode);
			}
		}
	}

	@Override
	public void modifyZkNode(Long zkClusterId, ZkNode zkNode) throws Exception {

		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		// 查找节点
		if (connector.getSagRoot().findByPath(zkNode.getPath()) == null)
			throw new Exception(
					OmpAppContext.getMessage("node_to_be_modified_not_exist"));
		connector.writeData(zkNode.getPath(), zkNode.encode());
	}

	@Override
	public List<ZkNode> queryZkNodes(Long zkClusterId, String path, int nodeType)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode sagRoot = connector.getSagRoot();
		ZkNode nodeRoot = sagRoot.findByPath(path);
		// 在树上查找指定类型的节点
		List<ZkNode> nodeList = getNodesByType(nodeRoot, nodeType);
		return nodeList;
	}

	@Override
	public void addZkNode(Long zkClusterId, final ZkNode zkNode, int createMode)
			throws Exception {
		this.addZkNode(zkClusterId, zkNode, createMode, true);
	}

	public void addZkNode(Long zkClusterId, final ZkNode zkNode,
			int createMode, boolean registerListener) throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);

		// 查找父节点是否存在
		if (!connector.exists(zkNode.getParentPath())) {
			throw new Exception(OmpAppContext.getMessage("path_not_exist"));
		}
		// 查找本节点是否存在
		if (connector.exists(zkNode.getPath())) {
			throw new Exception(OmpAppContext.getMessage("dupliated_node"));
		}

		ZkNode sagRoot = connector.getSagRoot();
		// 创建节点
		try {
			if (createMode == ZkNodeConstant.CREATE_MODE_PERSISTENT) {
				connector.create(zkNode.getPath(), zkNode.encode(),
						CreateMode.PERSISTENT);
			} else if (createMode == ZkNodeConstant.CREATE_MODE_EPHEMERAL) {
				connector.create(zkNode.getPath(), zkNode.encode(),
						CreateMode.EPHEMERAL);
			}
			// 获取stat信息
			Stat stat = new Stat();
			connector.readData(zkNode.getPath(), stat);
			zkNode.setStat(ZkNodeHelper.buildZkNodeStat(stat));
			// 增加到本地缓存
			boolean isAdded = sagRoot.addNodeToTree(zkNode);
			if (isAdded) {
				log.info("add node(1). " + zkNode);
				if (registerListener) {
					// 增加侦听
					ZkNodeHelper.registerOneNodeListener(connector, zkNode);
				}
			}
		} catch (Exception e) {
			log.error("failed to create node, zkNode=" + zkNode, e);
			throw new Exception(
					OmpAppContext.getMessage("failed_to_create_node") + ":"
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteZkNode(Long zkClusterId, String path) throws Exception {
		this.deleteZkNode(zkClusterId, path, true);
	}

	public void deleteZkNode(Long zkClusterId, String path,
			boolean deregisterListener) throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode sagRoot = connector.getSagRoot();
		ZkNode zkNode = sagRoot.findByPath(path);
		// 查找要删除的节点
		if (zkNode == null)
			throw new Exception(
					OmpAppContext.getMessage("node_to_be_deleted_not_exist")
							+ ": " + path);
		if (deregisterListener) {
			// 删除侦听
			ZkNodeHelper.deregisterOneNodeListener(connector, zkNode);
		}
		log.debug("remove node, path=" + path);
		// 从ZK树中删除
		connector.delete(path);
		// 从本地缓存移除
		sagRoot.removeNodeFromTree(zkNode);
	}

	@Override
	public void addZkNodeRecursive(Long zkClusterId, ZkNode zkNode,
			int createMode) throws Exception {
		if (zkNode.getZkNodeVO() instanceof ZkSagGroupVO) {
			this.addSagGroup(zkClusterId, zkNode, false);
		} else {
			Iterator<ZkNode> itr = zkNode.createLevelOrderIterator();
			while (itr.hasNext()) {
				ZkNode node = itr.next();
				this.addZkNode(zkClusterId, node, createMode, false);
			}
		}
	}

	public void addSagGroup(final Long zkClusterId, ZkNode sagGroup,
			boolean deRegSagRoot) throws Exception {
		Date startTime = new Date();
		List<ZkNode> nodeList = sagGroup.getTreeNodeList();
		List<List<ZkNode>> layerList = new LinkedList<List<ZkNode>>();
		// 计算树深
		int maxLayer = getMaxLayer(nodeList);
		// 从上层向底层依次获取
		for (int i = 2; i <= maxLayer; i++) {
			layerList.add(getNodesByLayer(nodeList, i));
		}
		// 获取最大的层节点数
		int maxNodeNum = getMaxNodeNumofAllLayers(layerList);
		int threadNum = 0;
		// 如果最大的层节点数大于10，则创建大小为10的线程池；否则按照节点数创建线程池
		if (maxNodeNum > 10) {
			threadNum = 10;
		} else {
			threadNum = maxNodeNum;
		}

		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) getConnectorBy(zkClusterId);
		Lock lock = connector.getLock();
		if (!lock.tryLock()) {
			throw new Exception(
					OmpAppContext.getMessage("connection_is_occupied"));
		}
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		try {
			// 取消sagRoot的监听
			if (deRegSagRoot) {
				ZkNodeHelper.deregisterNodeListener(connector,
						connector.getSagRoot());
			}
			for (final List<ZkNode> nodesOnLayer : layerList) {
				// 创建任务列表
				List<AddSagGroupTask> taskList = new LinkedList<AddSagGroupTask>();
				for (ZkNode node : nodesOnLayer) {
					taskList.add(new AddSagGroupTask(zkClusterId, node));
				}
				// 执行任务
				List<Future<Result>> futureList = executor.invokeAll(taskList);
				// 查看任务是否有异常，有则抛出
				for (Future<Result> future : futureList) {
					Result result = future.get();
					if (result != null) {
						throw result.getException();
					}
				}
			}
			ZkNodeHelper.registerNodeListener(connector, sagGroup);
		} catch (Exception e) {
			throw e;
		} finally {
			// 恢复sagRoot节点监听
			if (deRegSagRoot) {
				ZkNodeHelper.registerNodeListener(connector,
						connector.getSagRoot());
			}
			executor.shutdown();
			Date endTime = new Date();
			double cost = (endTime.getTime() - startTime.getTime()) / 1000;
			System.out.println("add sagGroup cost time " + cost + "s");
			lock.unlock();
		}
	}

	@Override
	public void deleteZkNodeRecursive(Long zkClusterId, ZkNode zkNode)
			throws Exception {
		if (zkNode.getZkNodeVO() instanceof ZkSagGroupVO) {
			this.deleteSagGroup(zkClusterId, zkNode, false);
		} else {
			List<ZkNode> nodeList = new LinkedList<ZkNode>();
			ZkClusterConnector connector = getConnectorBy(zkClusterId);
			Iterator<ZkNode> itr = zkNode.createLevelOrderIterator();
			while (itr.hasNext()) {
				ZkNode node = itr.next();
				nodeList.add(0, node);
				// 先注销侦听器
				ZkNodeHelper.deregisterOneNodeListener(connector, node);
			}
			for (ZkNode node : nodeList) {
				this.deleteZkNode(zkClusterId, node.getPath(), false);
			}
		}
	}

	public void deleteSagGroup(final Long zkClusterId, ZkNode sagGroup,
			boolean deRegSagRoot) throws Exception {

		Date startTime = new Date();
		List<ZkNode> nodeList = sagGroup.getTreeNodeList();
		List<List<ZkNode>> layerList = new LinkedList<List<ZkNode>>();
		// 计算树深
		int maxLayer = getMaxLayer(nodeList);
		// 从最底层向上层依次获取
		for (int i = maxLayer; i > 1; i--) {
			layerList.add(getNodesByLayer(nodeList, i));
		}
		// 获取最大的层节点数
		int maxNodeNum = getMaxNodeNumofAllLayers(layerList);
		int threadNum = 0;
		// 如果最大的层节点数大于10，则创建大小为10的线程池；否则按照节点数创建线程池
		if (maxNodeNum > 10) {
			threadNum = 10;
		} else {
			threadNum = maxNodeNum;
		}

		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) getConnectorBy(zkClusterId);
		Lock lock = connector.getLock();
		if (!lock.tryLock()) {
			throw new Exception(
					OmpAppContext.getMessage("connection_is_occupied"));
		}
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		try {
			if (deRegSagRoot) {
				ZkNodeHelper.deregisterNodeListener(connector,
						connector.getSagRoot());
			}
			// 取消sagGroup下所有节点的监听
			ZkNodeHelper.deregisterNodeListener(connector, sagGroup);
			for (final List<ZkNode> nodesOnLayer : layerList) {
				List<DeleteSagGroupTask> taskList = new LinkedList<DeleteSagGroupTask>();
				for (ZkNode node : nodesOnLayer) {
					taskList.add(new DeleteSagGroupTask(zkClusterId, node));
				}
				List<Future<Result>> futureList = executor.invokeAll(taskList);
				for (Future<Result> future : futureList) {
					Result result = future.get();
					if (result != null) {
						throw result.getException();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			executor.shutdown();
			if (deRegSagRoot) {
				ZkNodeHelper.registerNodeListener(connector,
						connector.getSagRoot());
			}
			Date endTime = new Date();
			double cost = (endTime.getTime() - startTime.getTime()) / 1000;
			System.out.println("delete sagGroup cost time " + cost + "s");
			lock.unlock();
		}
	}

	@Override
	public void updateSagRoot(long zkClusterId) throws Exception {

		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) getConnectorBy(zkClusterId);
		Lock lock = connector.getLock();
		if (!lock.tryLock()) {
			throw new Exception(
					OmpAppContext.getMessage("connection_is_occupied"));
		}
		try {
			log.debug("start to update sagRoot");
			ZkNode oldSagRoot = connector.getSagRoot();
			ZkNode newSagRoot = ZkNodeHelper.querySagBizTree(connector);

			List<ZkNode> newNodeList = getNewNodes(oldSagRoot, newSagRoot);
			List<ZkNode> delNodeList = getDeletedNodes(oldSagRoot, newSagRoot);
			// 处理节点变化
			for (ZkNode zkNode : delNodeList) {
				oldSagRoot.removeNodeFromTree(zkNode);
			}

			for (ZkNode zkNode : newNodeList) {
				oldSagRoot.addNodeToTree(zkNode);
			}
			// 新增的节点统一添加监听
			for (ZkNode zkNode : newNodeList) {
				ZkNodeHelper.registerOneNodeListener(connector, zkNode);
			}

			// 处理节点数据变化
			List<ZkNode> changedNodeList = getChangedNodes(oldSagRoot,
					newSagRoot);

			for (ZkNode zkNode : changedNodeList) {
				ZkNode newNode = newSagRoot.findByPath(zkNode.getPath());
				zkNode.setZkNodeVO(newNode.getZkNodeVO());
				zkNode.setHeader(newNode.getHeader());
				zkNode.setStat(newNode.getStat());
			}
			boolean changed = false;
			if (delNodeList.size() > 0 || newNodeList.size() > 0
					|| changedNodeList.size() > 0)
				changed = true;
			// 如果有变化，则通知客户端刷新界面
			if (changed) {
				notifyZkClientRefresh();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取新增节点列表
	 * 
	 * @param oldSagRoot
	 * @param newSagRoot
	 * @return
	 */
	private List<ZkNode> getNewNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> newNodeList = new LinkedList<ZkNode>();
		Iterator<ZkNode> newNodes = newSagRoot.createLevelOrderIterator();
		boolean isFound = false;
		while (newNodes.hasNext()) {
			isFound = false;
			ZkNode newNode = newNodes.next();
			Iterator<ZkNode> oldNodes = oldSagRoot.createLevelOrderIterator();
			while (oldNodes.hasNext()) {
				ZkNode oldNode = oldNodes.next();
				if (oldNode.getPath().equals(newNode.getPath())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				newNodeList.add(newNode);
			}
		}
		return newNodeList;
	}

	/**
	 * 获取已删除的节点列表
	 * 
	 * @param oldSagRoot
	 * @param newSagRoot
	 * @return
	 */
	private List<ZkNode> getDeletedNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> delNodeList = new LinkedList<ZkNode>();
		Iterator<ZkNode> oldNodes = oldSagRoot.createLevelOrderIterator();
		boolean isFound = false;
		while (oldNodes.hasNext()) {
			isFound = false;
			ZkNode oldNode = oldNodes.next();
			Iterator<ZkNode> newNodes = newSagRoot.createLevelOrderIterator();
			while (newNodes.hasNext()) {
				ZkNode newNode = newNodes.next();
				if (oldNode.getPath().equals(newNode.getPath())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				delNodeList.add(oldNode);
			}
		}
		return delNodeList;
	}

	/**
	 * 获取数据改变的节点列表
	 * 
	 * @param oldSagRoot
	 * @param newSagRoot
	 * @return
	 */
	private List<ZkNode> getChangedNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> changedNodeList = new LinkedList<ZkNode>();
		Iterator<ZkNode> oldNodes = oldSagRoot.createLevelOrderIterator();
		while (oldNodes.hasNext()) {
			ZkNode oldNode = oldNodes.next();
			// 如果是rules节点的子节点，则跳过
			if (oldNode.getZkNodeVO() instanceof ZkSagNodeLockVO)
				continue;
			ZkNode newNode = newSagRoot.findByPath(oldNode.getPath());
			if (!oldNode.getZkNodeVO().equals(newNode.getZkNodeVO())) {
				changedNodeList.add(oldNode);
			}
			if (!oldNode.getStat().equals(newNode.getStat())) {
				changedNodeList.add(oldNode);
			}
		}

		return changedNodeList;
	}

	// 通知客户端更新界面
	private void notifyZkClientRefresh() {
		LoginUserCache.getInstance().callback(new CallbackScript() {
			public void execute(MinasClientFacade minasClientFacade)
					throws Exception {
				ZkCallbackFacade zkCallbackFacade = minasClientFacade
						.getFacade(ZkCallbackFacade.class);
				if (zkCallbackFacade != null) {
					zkCallbackFacade
							.notifyZkClientRefresh(ZkNodeConstant.SAG_ROOT_PATH);
				}
			}
		});
	}

	public ZkClusterConnectorManager getConnectorManager() {
		return connectorManager;
	}

	public void setConnectorManager(ZkClusterConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
	}

	private ZkClusterConnector getConnectorBy(Long zkClusterId)
			throws Exception {
		ZkClusterConnector connector = connectorManager
				.getConnector(zkClusterId);
		if (connector == null) {
			throw new Exception(
					OmpAppContext
							.getMessage("conn_to_specified_nk_cluster_not_existing"));
		}
		if (connector.getConnectState() == ZkClusterConstant.STATE_DISCONNECTED) {
			throw new Exception(
					OmpAppContext
							.getMessage("conn_to_specified_nk_cluster_not_existing"));
		}
		return connectorManager.getConnector(zkClusterId);
	}

	/**
	 * 获取指定类型的节点列表
	 * 
	 * @param nodeRoot
	 * @param nodeType
	 * @return
	 */
	private List<ZkNode> getNodesByType(ZkNode nodeRoot, int nodeType) {
		List<ZkNode> nodeList = new LinkedList<ZkNode>();
		Iterator<ZkNode> iterator = nodeRoot.createLevelOrderIterator();
		while (iterator.hasNext()) {
			ZkNode node = iterator.next();
			if (node.getNodeType() == nodeType) {
				nodeList.add(node);
			}
		}
		return nodeList;
	}

	/**
	 * 获取指定层级上的所有节点
	 * 
	 * @param nodeList
	 * @param layer
	 * @return
	 */
	private List<ZkNode> getNodesByLayer(List<ZkNode> nodeList, int layer) {
		List<ZkNode> nodesOnLayer = new LinkedList<ZkNode>();
		for (ZkNode zkNode : nodeList) {
			if (zkNode.getLayer() == layer) {
				nodesOnLayer.add(zkNode);
			}
		}
		return nodesOnLayer;
	}

	/**
	 * 获取最大层号
	 * 
	 * @param nodeList
	 * @return
	 */
	private int getMaxLayer(List<ZkNode> nodeList) {
		int maxLayer = 0;
		int layer = 0;
		for (ZkNode zkNode : nodeList) {
			layer = zkNode.getLayer();
			if (layer > maxLayer)
				maxLayer = layer;
		}
		return maxLayer;
	}

	/**
	 * 获取节点数最多的层中的节点数
	 * 
	 * @param layerList
	 * @return 节点数最多的层中的节点数
	 */
	private int getMaxNodeNumofAllLayers(List<List<ZkNode>> layerList) {
		int maxNum = 0;
		int num = 0;
		for (List<ZkNode> layer : layerList) {
			num = layer.size();
			if (num > maxNum)
				maxNum = num;
		}
		return maxNum;
	}

	/**
	 * 删除SAG群组任务
	 * 
	 * @author fanhaoyu
	 * 
	 */
	class DeleteSagGroupTask implements Callable<Result> {

		private ZkNode node;

		private long zkClusterId;

		public DeleteSagGroupTask(long zkClusterId, ZkNode node) {
			this.zkClusterId = zkClusterId;
			this.node = node;
		}

		@Override
		public Result call() throws Exception {
			Result result = null;
			try {
				ZkClusterConnector connector = getConnectorBy(zkClusterId);
				ZkNode sagRoot = connector.getSagRoot();
				// 查找节点
				if (sagRoot.findByPath(node.getPath()) == null)
					throw new Exception(
							OmpAppContext
									.getMessage("node_to_be_deleted_not_exist")
									+ ": " + node.getPath());
				log.debug("remove node, path=" + node.getPath());
				// 从ZK树中删除
				connector.delete(node.getPath());
				// 从本地缓存移除
				sagRoot.removeNodeFromTree(node);
			} catch (Exception e) {
				result = new Result(node, e);
			}
			return result;
		}

	}

	class AddSagGroupTask implements Callable<Result> {

		private ZkNode node;

		private long zkClusterId;

		public AddSagGroupTask(long zkClusterId, ZkNode node) {
			this.zkClusterId = zkClusterId;
			this.node = node;
		}

		@Override
		public Result call() throws Exception {
			Result result = null;
			try {
				ZkClusterConnector connector = getConnectorBy(zkClusterId);
				// 查找父节点是否存在
				if (!connector.exists(node.getParentPath())) {
					throw new Exception(
							OmpAppContext.getMessage("path_not_exist"));
				}
				// 查找本节点是否存在
				if (connector.exists(node.getPath())) {
					throw new Exception(
							OmpAppContext.getMessage("dupliated_node"));
				}
				ZkNode sagRoot = connector.getSagRoot();
				// 创建节点
				try {
					connector.create(node.getPath(), node.encode(),
							CreateMode.PERSISTENT);
					// 获取stat信息
					Stat stat = new Stat();
					connector.readData(node.getPath(), stat);
					node.setStat(ZkNodeHelper.buildZkNodeStat(stat));
					// 增加到本地缓存
					boolean isAdded = sagRoot.addNodeToTree(node);
					if (isAdded) {
						log.info("add node(1). " + node);
					}
				} catch (Exception e) {
					log.error("failed to create node, zkNode=" + node, e);
					throw new Exception(
							OmpAppContext.getMessage("failed_to_create_node")
									+ ":" + e.getLocalizedMessage());
				}
			} catch (Exception e) {
				result = new Result(node, e);
			}
			return result;
		}

	}

	/**
	 * 操作结果
	 * 
	 * @author fanhaoyu
	 * 
	 */
	class Result {
		private ZkNode node;
		private Exception exception;

		public Result(ZkNode node, Exception exception) {
			this.node = node;
			this.exception = exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}

		public Exception getException() {
			return exception;
		}

		public void setNode(ZkNode node) {
			this.node = node;
		}

		public ZkNode getNode() {
			return node;
		}
	}
}