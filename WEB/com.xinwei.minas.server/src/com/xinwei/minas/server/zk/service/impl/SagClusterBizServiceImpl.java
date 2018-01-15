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
 * SAG�ƻ�ҵ�����ӿ�ʵ��
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
	 * ��ѯָ����ȺID��ZkNode·��
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
			// У���վID�Ƿ�������GROUP�µĻ�վID�ظ�
			checkInputSagGroup(sagRoot, sagGroup);
			// �������µ�sagGroup������ͬ�Ľڵ�
			Set<ZkNode> childSet = sagRoot.getChildren();
			ZkNode sameNode = null;
			for (ZkNode child : childSet) {
				// ����
				if (child.getName().equals(sagGroup.getName())) {
					sameNode = child;
					break;
				}
			}
			// �������name��ͬ��sagGroup�ڵ�
			if (sameNode != null) {
				ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sameNode.getZkNodeVO();
				// ���ڳ�ʼ������ͣ��״̬
				if (sagGroupVO.isInitialized() || sagGroupVO.isStopped()) {
					this.deleteSagGroup(zkClusterId, sameNode, true);
				} else if (sagGroupVO.isStarted()) {
					String message = OmpAppContext
							.getMessage("sagGroup_has_been_started_up_cannot_be_deleted");
					throw new Exception(MessageFormat.format(message,
							String.valueOf(sameNode.getName())));
				}
			}
			// �ݹ�����SAG Group�ڵ�
			this.addSagGroup(zkClusterId, sagGroup, true);
		} finally {
			lock.unlock();
		}
		notifyZkClientRefresh();
	}

	/**
	 * У�������SAG Group����
	 * 
	 * @param sagRoot
	 * @param sagGroup
	 * @throws Exception
	 */
	private void checkInputSagGroup(ZkNode sagRoot, ZkNode sagGroup)
			throws Exception {
		Set<ZkNode> children = sagRoot.getChildren();
		// ��ȡ����Sag Group
		Set<ZkNode> otherSagGroups = new LinkedHashSet<ZkNode>();
		for (ZkNode child : children) {
			if (child.getName().equalsIgnoreCase(sagGroup.getName())) {
				continue;
			}
			otherSagGroups.add(child);
		}
		// У���վID�Ƿ��ظ�
		this.checkDuplicatedBtsId(sagGroup, otherSagGroups);

		// У��SAG ID�Ƿ��ظ�
		this.checkDuplicatedSagId(sagGroup, otherSagGroups);
	}

	/**
	 * У���վID�Ƿ��ظ�
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
	 * У��SAG ID�Ƿ��ظ�
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
	 * �жϸ�sag�Ƿ���� ������ڣ��жϸ�sag״̬ ���Ϊ������״̬���򷵻� ����sag״̬����Ϊ���� ע��watch
	 */
	@Override
	public void changeSagToClusterMode(Long zkClusterId, Long sagId)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode sagNode = null;
		// ����sagNode
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

		// �������������SAG
		if (sagNode != null) {
			ZkSagVO sagVo = (ZkSagVO) sagNode.getZkNodeVO();
			// ����Ѿ�����
			if (sagVo.isStarted()) {
				throw new Exception(
						OmpAppContext
								.getMessage("started_SAG_cannot_be_restarted"));
			}
			// ����
			sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STARTED);
			// �·�
			connector.writeData(sagNode.getPath(), sagNode.encode());

			ZkNode sagGroupNode = sagNode.getParent().getParent();
			ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sagGroupNode.getZkNodeVO();

			// TODO: �����SAG���ڵ���δ�����ã��������� (��Ҫȷ��)
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
		// ����sagNode
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

		// �������������SAG
		if (sagNode != null) {
			ZkSagVO sagVo = (ZkSagVO) sagNode.getZkNodeVO();
			// �����SAG������
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
					// ͣ��
					sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STOPPED);
					// �·�
					connector.writeData(sagNode.getPath(), sagNode.encode());
				}
			} else {
				// ͣ��
				sagVo.setSagStatus(ZkSagVO.SAG_STATUS_STOPPED);
				// �·�
				connector.writeData(sagNode.getPath(), sagNode.encode());
			}

			// TODO: �������sag��ͣ�ã���ͣ�ø�sagGroup(��ȷ��)
			ZkNode sagGroup = sagNode.getParent().getParent();
			if (isSagGroupStoped(sagGroup)) {
				ZkSagGroupVO sagGroupVO = (ZkSagGroupVO) sagGroup.getZkNodeVO();

				sagGroupVO.setGroupStatus(ZkSagGroupVO.SAGGROUP_STATUS_STOPPED);
				connector.writeData(sagGroup.getPath(), sagGroup.encode());

			}
		}
	}

	/**
	 * �ж�һ��SAGȺ���Ƿ���ͣ�� ����Ϊ��Ⱥ�������е�SAG����ͣ�ã����SAGȺ����ͣ�ã�����δͣ��
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
			// �����sag�����ã����sagGroupδͣ��
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
		// У���վID�Ƿ��ظ�
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
					// ��վID�ظ����׳��쳣
					throw new Exception(MessageFormat.format(
							OmpAppContext.getMessage("duplicated_bts_id"),
							node.getName()));
				}
			}
		}

		ZkNode btsGroup = null;
		ZkBtsGroupVO btsGroupVO = null;
		// ����������վҪ��ӵ���btsGroup
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

		// �������������btsGroup
		if (btsGroup != null) {
			int currentBtsNum = btsGroup.getChildren().size();
			if (currentBtsNum >= btsGroupVO.getMaxSubNode()) {
				throw new Exception(
						OmpAppContext
								.getMessage("reach_max_bts_num_cannot_add"));
			}
			// ���ӻ�վ�ڵ�
			this.addZkNode(zkClusterId, btsNode,
					ZkNodeConstant.CREATE_MODE_PERSISTENT);
			// ����SAGLink�ڵ�
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
			// 2013-06-04��wangdaojingȷ��BtsStatus�ڵ���SAG������Ϊ��ʱ�ڵ�
		}
	}

	@Override
	public void deleteBts(Long zkClusterId, String path, Long btsId)
			throws Exception {
		ZkClusterConnector connector = this.getConnectorBy(zkClusterId);
		ZkNode btsNode = null;
		// ����btsNode
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

		// �������������BTS
		if (btsNode != null) {
			Set<ZkNode> linkNodeSet = btsNode.getChildren();
			if (linkNodeSet != null && !linkNodeSet.isEmpty()) {
				ZkNode linkNode = (ZkNode) linkNodeSet.toArray()[0];
				Set<ZkNode> statusNodeSet = linkNode.getChildren();

				if (statusNodeSet != null && !statusNodeSet.isEmpty()) {
					ZkNode statusNode = (ZkNode) statusNodeSet.toArray()[0];
					ZkBtsStatusVO statusVO = (ZkBtsStatusVO) statusNode
							.getZkNodeVO();
					// ��������ý�嶼�ǶϿ�״̬
					if (statusVO.getMediaLinkState() == ZkBtsStatusVO.BTS_MEDIA_LINK_DISCONNECTED
							&& statusVO.getSignalLinkState() == ZkBtsStatusVO.BTS_SIGNAL_LINK_DISCONNECTED) {
						// ɾ��btsNode�����ӽڵ�
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
		// ���ҽڵ�
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
		// �����ϲ���ָ�����͵Ľڵ�
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

		// ���Ҹ��ڵ��Ƿ����
		if (!connector.exists(zkNode.getParentPath())) {
			throw new Exception(OmpAppContext.getMessage("path_not_exist"));
		}
		// ���ұ��ڵ��Ƿ����
		if (connector.exists(zkNode.getPath())) {
			throw new Exception(OmpAppContext.getMessage("dupliated_node"));
		}

		ZkNode sagRoot = connector.getSagRoot();
		// �����ڵ�
		try {
			if (createMode == ZkNodeConstant.CREATE_MODE_PERSISTENT) {
				connector.create(zkNode.getPath(), zkNode.encode(),
						CreateMode.PERSISTENT);
			} else if (createMode == ZkNodeConstant.CREATE_MODE_EPHEMERAL) {
				connector.create(zkNode.getPath(), zkNode.encode(),
						CreateMode.EPHEMERAL);
			}
			// ��ȡstat��Ϣ
			Stat stat = new Stat();
			connector.readData(zkNode.getPath(), stat);
			zkNode.setStat(ZkNodeHelper.buildZkNodeStat(stat));
			// ���ӵ����ػ���
			boolean isAdded = sagRoot.addNodeToTree(zkNode);
			if (isAdded) {
				log.info("add node(1). " + zkNode);
				if (registerListener) {
					// ��������
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
		// ����Ҫɾ���Ľڵ�
		if (zkNode == null)
			throw new Exception(
					OmpAppContext.getMessage("node_to_be_deleted_not_exist")
							+ ": " + path);
		if (deregisterListener) {
			// ɾ������
			ZkNodeHelper.deregisterOneNodeListener(connector, zkNode);
		}
		log.debug("remove node, path=" + path);
		// ��ZK����ɾ��
		connector.delete(path);
		// �ӱ��ػ����Ƴ�
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
		// ��������
		int maxLayer = getMaxLayer(nodeList);
		// ���ϲ���ײ����λ�ȡ
		for (int i = 2; i <= maxLayer; i++) {
			layerList.add(getNodesByLayer(nodeList, i));
		}
		// ��ȡ���Ĳ�ڵ���
		int maxNodeNum = getMaxNodeNumofAllLayers(layerList);
		int threadNum = 0;
		// ������Ĳ�ڵ�������10���򴴽���СΪ10���̳߳أ������սڵ��������̳߳�
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
			// ȡ��sagRoot�ļ���
			if (deRegSagRoot) {
				ZkNodeHelper.deregisterNodeListener(connector,
						connector.getSagRoot());
			}
			for (final List<ZkNode> nodesOnLayer : layerList) {
				// ���������б�
				List<AddSagGroupTask> taskList = new LinkedList<AddSagGroupTask>();
				for (ZkNode node : nodesOnLayer) {
					taskList.add(new AddSagGroupTask(zkClusterId, node));
				}
				// ִ������
				List<Future<Result>> futureList = executor.invokeAll(taskList);
				// �鿴�����Ƿ����쳣�������׳�
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
			// �ָ�sagRoot�ڵ����
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
				// ��ע��������
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
		// ��������
		int maxLayer = getMaxLayer(nodeList);
		// ����ײ����ϲ����λ�ȡ
		for (int i = maxLayer; i > 1; i--) {
			layerList.add(getNodesByLayer(nodeList, i));
		}
		// ��ȡ���Ĳ�ڵ���
		int maxNodeNum = getMaxNodeNumofAllLayers(layerList);
		int threadNum = 0;
		// ������Ĳ�ڵ�������10���򴴽���СΪ10���̳߳أ������սڵ��������̳߳�
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
			// ȡ��sagGroup�����нڵ�ļ���
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
			// ����ڵ�仯
			for (ZkNode zkNode : delNodeList) {
				oldSagRoot.removeNodeFromTree(zkNode);
			}

			for (ZkNode zkNode : newNodeList) {
				oldSagRoot.addNodeToTree(zkNode);
			}
			// �����Ľڵ�ͳһ��Ӽ���
			for (ZkNode zkNode : newNodeList) {
				ZkNodeHelper.registerOneNodeListener(connector, zkNode);
			}

			// ����ڵ����ݱ仯
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
			// ����б仯����֪ͨ�ͻ���ˢ�½���
			if (changed) {
				notifyZkClientRefresh();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ��ȡ�����ڵ��б�
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
	 * ��ȡ��ɾ���Ľڵ��б�
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
	 * ��ȡ���ݸı�Ľڵ��б�
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
			// �����rules�ڵ���ӽڵ㣬������
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

	// ֪ͨ�ͻ��˸��½���
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
	 * ��ȡָ�����͵Ľڵ��б�
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
	 * ��ȡָ���㼶�ϵ����нڵ�
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
	 * ��ȡ�����
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
	 * ��ȡ�ڵ������Ĳ��еĽڵ���
	 * 
	 * @param layerList
	 * @return �ڵ������Ĳ��еĽڵ���
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
	 * ɾ��SAGȺ������
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
				// ���ҽڵ�
				if (sagRoot.findByPath(node.getPath()) == null)
					throw new Exception(
							OmpAppContext
									.getMessage("node_to_be_deleted_not_exist")
									+ ": " + node.getPath());
				log.debug("remove node, path=" + node.getPath());
				// ��ZK����ɾ��
				connector.delete(node.getPath());
				// �ӱ��ػ����Ƴ�
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
				// ���Ҹ��ڵ��Ƿ����
				if (!connector.exists(node.getParentPath())) {
					throw new Exception(
							OmpAppContext.getMessage("path_not_exist"));
				}
				// ���ұ��ڵ��Ƿ����
				if (connector.exists(node.getPath())) {
					throw new Exception(
							OmpAppContext.getMessage("dupliated_node"));
				}
				ZkNode sagRoot = connector.getSagRoot();
				// �����ڵ�
				try {
					connector.create(node.getPath(), node.encode(),
							CreateMode.PERSISTENT);
					// ��ȡstat��Ϣ
					Stat stat = new Stat();
					connector.readData(node.getPath(), stat);
					node.setStat(ZkNodeHelper.buildZkNodeStat(stat));
					// ���ӵ����ػ���
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
	 * �������
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