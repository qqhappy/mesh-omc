/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.IZkDataListener;
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
 * ZK�ڵ�����������
 * 
 * @author chenjunhua
 * 
 */

public class ZkDataListenerImpl implements IZkDataListener {

	private static final Logger logger = Logger
			.getLogger(ZkDataListenerImpl.class);

	private ZkClusterConnector connector;

	private BtsSagLinkChangedListener btsSagLinkChangedListener;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	public ZkDataListenerImpl(ZkClusterConnector connector,
			BtsSagLinkChangedListener btsSagLinkChangedListener) {
		this.connector = connector;
		this.btsSagLinkChangedListener = btsSagLinkChangedListener;
	}

	@Override
	public void handleDataChange(final String dataPath, final Object data)
			throws Exception {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				processDataChange(dataPath, data);
			}

		});

	}

	private void processDataChange(final String dataPath, final Object data) {
		try {
			logger.debug("handleDataChange, path=" + dataPath);
			ZkNode sagRoot = connector.getSagRoot();
			if (sagRoot == null) {
				logger.error("handleDataChange error. sagRoot==null");
				return;
			}
			final ZkNode cachedNode = sagRoot.findByPath(dataPath);
			if (cachedNode == null) {
				logger.debug("handleDataChange, node is null, path=" + dataPath);
				return;
			}
			ZkNode newZkNode = new ZkNode(dataPath);
			ZkNodeHelper.buildZKNode(connector, newZkNode);
			// �����µ���ֵ
			ZkNodeVO zkNodeVO = newZkNode.getZkNodeVO();
			cachedNode.setZkNodeVO(zkNodeVO);
			cachedNode.setStat(newZkNode.getStat());
			// �жϽڵ�����
			if (zkNodeVO instanceof ZkBtsSagLinkVO) {
				// ��վ��SAG����·�仯
				ZkBtsSagLinkVO zkBtsSagLink = (ZkBtsSagLinkVO) zkNodeVO;
				ZkBtsVO zkBts = (ZkBtsVO) (cachedNode.getParent().getZkNodeVO());
				// ֪ͨ��վģ���վ��SAG����·�仯
				if (btsSagLinkChangedListener != null) {
					btsSagLinkChangedListener.linkChanged(zkBts, zkBtsSagLink);
				}
			}
			// ֪ͨ�ͻ������ݱ仯
			LoginUserCache.getInstance().callback(new CallbackScript() {
				public void execute(MinasClientFacade minasClientFacade)
						throws Exception {
					ZkCallbackFacade zkCallbackFacade = minasClientFacade
							.getFacade(ZkCallbackFacade.class);
					if (zkCallbackFacade != null) {
						zkCallbackFacade.handleDataChange(cachedNode);
					}
				}
			});
		} catch (Exception e) {
			logger.error("handleDataChange error", e);
		}
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		// do nothing
		// logger.debug("handleDataDeleted, path=" + dataPath);
	}

}
