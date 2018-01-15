/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.minas.server.zk.net.ZkClusterConnectorManager;
import com.xinwei.minas.server.zk.net.ZkNodeHelper;
import com.xinwei.minas.server.zk.net.impl.ZkClusterConnectorZkClientImpl;
import com.xinwei.minas.server.zk.service.ZkDataSyncService;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;
import com.xinwei.minas.zk.core.facade.ZkCallbackFacade;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagNodeLockVO;

/**
 * 
 * 定时数据同步服务实现
 * 
 * @author fanhaoyu
 * 
 */

public class ZkDataSyncServiceImpl implements ZkDataSyncService {

	private static final Logger logger = Logger
			.getLogger(ZkDataSyncServiceImpl.class);

	private ZkClusterConnectorManager connectorManager;

	public ZkClusterConnectorManager getConnectorManager() {
		return connectorManager;
	}

	public void setConnectorManager(ZkClusterConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
	}

	@Override
	public void updateData() throws Exception {
		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) connectorManager
				.getConnector(ZkClusterConstant.ZKCLUSTER_ID);

		if (connector != null) {
			Lock lock = connector.getLock();
			try {
				lock.lock();
				ZkNode oldSagRoot = connector.getSagRoot();
				System.out.println(new Date() + " start query sagRoot");
				ZkNode newSagRoot = ZkNodeHelper.querySagBizTree(connector);
				System.out.println(new Date() + " finish query sagRoot");

				List<ZkNode> newNodeList = getNewNodes(oldSagRoot, newSagRoot);
				List<ZkNode> delNodeList = getDeletedNodes(oldSagRoot,
						newSagRoot);
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
				}
				boolean changed = false;
				if (delNodeList.size() > 0 || newNodeList.size() > 0
						|| changedNodeList.size() > 0)
					changed = true;
				// 如果有变化，则通知客户端刷新界面
				if (changed)
					notifyZkClientRefresh();
				System.out
						.println("ZkDataSyncService.updateData() successfully!");
				logger.info("ZkDataSyncService.updateData() successfully!");
			} finally {
				lock.unlock();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private List<ZkNode> getNewNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> newNodeList = new ArrayList<ZkNode>();
		Iterator newNodes = newSagRoot.createLevelOrderIterator();
		boolean isFound = false;
		while (newNodes.hasNext()) {
			isFound = false;
			ZkNode newNode = (ZkNode) newNodes.next();
			Iterator oldNodes = oldSagRoot.createLevelOrderIterator();
			while (oldNodes.hasNext()) {
				ZkNode oldNode = (ZkNode) oldNodes.next();
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

	@SuppressWarnings("rawtypes")
	private List<ZkNode> getDeletedNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> delNodeList = new ArrayList<ZkNode>();
		Iterator oldNodes = oldSagRoot.createLevelOrderIterator();
		boolean isFound = false;
		while (oldNodes.hasNext()) {
			isFound = false;
			ZkNode oldNode = (ZkNode) oldNodes.next();
			Iterator newNodes = newSagRoot.createLevelOrderIterator();
			while (newNodes.hasNext()) {
				ZkNode newNode = (ZkNode) newNodes.next();
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

	@SuppressWarnings("rawtypes")
	private List<ZkNode> getChangedNodes(ZkNode oldSagRoot, ZkNode newSagRoot) {

		List<ZkNode> changedNodeList = new ArrayList<ZkNode>();
		Iterator oldNodes = oldSagRoot.createLevelOrderIterator();
		while (oldNodes.hasNext()) {
			ZkNode oldNode = (ZkNode) oldNodes.next();
			// 如果是rules节点的子节点，则跳过
			if (oldNode.getZkNodeVO() instanceof ZkSagNodeLockVO)
				continue;
			ZkNode newNode = newSagRoot.findByPath(oldNode.getPath());
			if (!oldNode.equals(newNode)) {
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
}