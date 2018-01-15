/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.IZkChildListener;
import org.apache.log4j.Logger;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.server.zk.net.ZkNodeHelper;
import com.xinwei.minas.zk.core.facade.ZkCallbackFacade;
import com.xinwei.minas.zk.core.listener.BtsSagLinkChangedListener;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;

/**
 * 
 * ZK 孩子节点变化侦听器
 * 
 * @author chenjunhua
 * 
 */

public class ZkChildListenerImpl implements IZkChildListener {

	private static final Logger logger = Logger
			.getLogger(ZkChildListenerImpl.class);

	private ZkClusterConnector connector;

	private BtsSagLinkChangedListener btsSagLinkChangedListener;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	public ZkChildListenerImpl(ZkClusterConnector connector,
			BtsSagLinkChangedListener btsSagLinkChangedListener) {
		this.connector = connector;
		this.btsSagLinkChangedListener = btsSagLinkChangedListener;
	}

	@Override
	public void handleChildChange(final String parentPath,
			final List<String> currentChilds) throws Exception {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				processChildChange(parentPath, currentChilds);
			}

		});

	}

	private void processChildChange(final String parentPath,
			final List<String> currentChilds) {
		try {
			logger.debug("handleChildChange, path=" + parentPath + ", children=" + currentChilds);
			ZkNode sagRoot = connector.getSagRoot();
			if (sagRoot == null) {
				logger.error("handleChildChange error. sagRoot==null");
				return;
			}
			ZkNode parentNode = sagRoot.findByPath(parentPath);
			if (parentNode == null) {
				logger.error("handleChildChange node is null, path="
						+ parentPath);
				return;
			}
			Set<ZkNode> oldChildren = parentNode.getChildren();
			// 获取删除的孩子列表
			List<String> deletedChildrenName = getDeletedChildren(oldChildren,
					currentChilds);
			for (String deletedChildName : deletedChildrenName) {
				String path = parentPath + "/" + deletedChildName;
				ZkNode child = sagRoot.findByPath(path);
				// 根据节点类型判断是否是基站与SAG间链路间删除
				ZkNodeVO zkNodeVO = child.getZkNodeVO();
				if (zkNodeVO instanceof ZkBtsSagLinkVO) {
					// 基站与SAG间链路删除
					ZkBtsSagLinkVO zkBtsSagLink = (ZkBtsSagLinkVO) zkNodeVO;
					ZkBtsVO zkBts = (ZkBtsVO) (child.getParent().getZkNodeVO());
					// 通知基站模块基站与SAG间链路删除
					if (btsSagLinkChangedListener != null) {
						btsSagLinkChangedListener.linkDeleted(zkBts,
								zkBtsSagLink);
					}
				}
				// 取消订阅
				ZkNodeHelper.deregisterOneNodeListener(connector, child);
				// 删除缓存中的节点
				parentNode.removeChild(child);
			}
			// 获取新增的孩子列表
			List<String> newChildrenName = getNewChildren(oldChildren,
					currentChilds);
			for (String newChildName : newChildrenName) {
				// 构造子节点模型
				String path = parentPath + "/" + newChildName;
				ZkNode child = new ZkNode(path);				
				ZkNodeHelper.buildZKNode(connector, child);			
				// 缓存中增加节点
				boolean isAdded = parentNode.addChild(child);
				if (isAdded) {
					// 注册侦听
					logger.info("add node(2). " + child);
					ZkNodeHelper.registerOneNodeListener(connector, child);		
				}
			}

			// 通知客户端数据变化
			LoginUserCache.getInstance().callback(new CallbackScript() {
				public void execute(MinasClientFacade minasClientFacade)
						throws Exception {
					ZkCallbackFacade zkCallbackFacade = minasClientFacade
							.getFacade(ZkCallbackFacade.class);
					if (zkCallbackFacade != null) {
						zkCallbackFacade.handleChildrenChange(parentPath);
					}
				}
			});

		} catch (Exception e) {
			logger.error("handleChildChange error", e);
		}
	}

	/**
	 * 获取删除的孩子列表
	 * 
	 * @param oldChildren
	 * @param currentChilds
	 * @return
	 */
	private List<String> getDeletedChildren(Set<ZkNode> oldChildren,
			List<String> currentChilds) {
		if (currentChilds == null) {
			return Collections.EMPTY_LIST;
		}
		List<String> list = new LinkedList<String>();
		for (ZkNode oldChild : oldChildren) {
			if (!currentChilds.contains(oldChild.getName())) {
				list.add(oldChild.getName());
			}
		}
		return list;
	}

	/**
	 * 获取新增的孩子列表
	 * 
	 * @param oldChildren
	 * @param currentChilds
	 * @return
	 */
	private List<String> getNewChildren(Set<ZkNode> oldChildren,
			List<String> currentChilds) {
		if (currentChilds == null) {
			return Collections.EMPTY_LIST;
		}
		List<String> list = new LinkedList<String>();
		for (String currentChild : currentChilds) {
			boolean isFound = false;
			for (ZkNode oldChild : oldChildren) {
				if (oldChild.getName().equals(currentChild)) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				list.add(currentChild);
			}
		}
		return list;
	}
}
