/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.xinwei.minas.server.zk.dao.ZkClusterDAO;
import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.server.zk.net.ZkClusterConnectorManager;
import com.xinwei.minas.server.zk.service.ZkBasicService;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.omp.server.OmpAppContext;

/**
 * ZK��������ʵ��
 * 
 * @author chenjunhua
 */

public class ZkBasicServiceImpl implements ZkBasicService {

	private static final Logger logger = Logger
			.getLogger(ZkBasicServiceImpl.class);

	// ZK��ȺDAO
	private ZkClusterDAO zkClusterDAO;

	// ZK��Ⱥ���ӹ�����
	private ZkClusterConnectorManager connectorManager;

	private ScheduledExecutorService service;

	private Lock lock = new ReentrantLock();

	@Override
	public void initialize() {
		service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(new ConnectorProbeTask(), 90, 5,
				TimeUnit.SECONDS);
	}

	@Override
	public List<ZkCluster> queryZkClusters() {
		// Ŀǰֻ֧��1����Ⱥ
		try {
			List<ZkCluster> zkClusters = zkClusterDAO.queryZkClusters();
			for (ZkCluster zkCluster : zkClusters) {
				ZkClusterConnector connector = connectorManager
						.getConnector(zkCluster.getId());
				if (connector != null) {
					// ��������״̬
					int connectState = connector.getConnectState();
					zkCluster.setConnectState(connectState);
				}
			}
			return zkClusters;
		} catch (Exception e) {
			logger.error("failed to queryZkClusters()", e);
			return Collections.EMPTY_LIST;
		} finally {

		}
	}

	@Override
	public ZkCluster queryZkClusterById(Long zkClusterId) {
		// Ŀǰֻ֧��1����Ⱥ
		List<ZkCluster> zkClusters = this.queryZkClusters();
		if (zkClusters != null || !zkClusters.isEmpty()) {
			return zkClusters.get(0);
		}
		return null;
	}

	@Override
	public void addZkCluster(ZkCluster zkCluster) throws Exception {
		try {
			lock.lock();
			List<ZkCluster> zkClusters = this.queryZkClusters();
			if (zkClusters != null && !zkClusters.isEmpty()) {
				throw new Exception(
						OmpAppContext.getMessage("one_nk_cluster_accepted"));
			}
			// �־û�
			zkClusterDAO.addZkCluster(zkCluster);
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void modifyZkCluster(ZkCluster newZkCluster) throws Exception {
		try {
			lock.lock();
			ZkCluster oldZkCluster = this.queryZkClusterById(newZkCluster
					.getId());
			if (oldZkCluster == null) {
				zkClusterDAO.addZkCluster(newZkCluster);
			} else {
				zkClusterDAO.modifyZkCluster(newZkCluster);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void deleteZkCluster(Long zkClusterId) throws Exception {
		try {
			lock.lock();
			// �־û�
			zkClusterDAO.deleteZkCluster(zkClusterId);
		} finally {
			lock.unlock();
		}
	}

	public void setZkClusterDAO(ZkClusterDAO zkClusterDAO) {
		this.zkClusterDAO = zkClusterDAO;
	}

	public ZkClusterDAO getZkClusterDAO() {
		return zkClusterDAO;
	}

	public ZkClusterConnectorManager getConnectorManager() {
		return connectorManager;
	}

	public void setConnectorManager(ZkClusterConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
	}

	class ConnectorProbeTask implements Runnable {

		@Override
		public void run() {
			try {
				// �ж���Ҫ���ӵ�Connector
				List<ZkCluster> zkClusters = zkClusterDAO.queryZkClusters();
				for (ZkCluster zkCluster : zkClusters) {
					if (connectorManager.getConnector(zkCluster.getId()) == null) {
						addConnector(zkCluster);
					}
				}
				// �ж���Ҫɾ���͸��µ�Connector
				List<Long> toBeDeleted = new LinkedList();
				List<ZkCluster> toBeUpdated = new LinkedList();
				Map<Long, ZkClusterConnector> connectorMap = connectorManager
						.getConnectorMap();
				Iterator<Long> itr = connectorMap.keySet().iterator();
				while (itr.hasNext()) {
					Long zkClusterId = itr.next();
					ZkCluster zkClusterInCache = connectorManager
							.getZkCluster(zkClusterId);
					ZkCluster zkClusterInDB = zkClusterDAO
							.queryZkClusterById(zkClusterId);
					if (zkClusterInDB == null) {
						toBeDeleted.add(zkClusterId);
					} else if (!zkClusterInCache.equals(zkClusterInDB)) {
						toBeUpdated.add(zkClusterInDB);
					}

				}
				// ɾ��
				for (Long zkClusterId : toBeDeleted) {
					removeConnector(zkClusterId);
				}
				// ����
				for (ZkCluster zkCluster : toBeUpdated) {
					// ���²�����ɾ�������ӵķ�ʽ����
					removeConnector(zkCluster.getId());
					addConnector(zkCluster);					
				}
			} catch (Exception e) {
				logger.error("AutoCreateConnectorTask error", e);
			} finally {
			}

		}

	}

	
	/**
	 * ɾ��ZK����
	 * @param zkClusterId
	 * @throws Exception
	 */
	private void removeConnector(Long zkClusterId) throws Exception {
		ZkClusterConnector connector = connectorManager
				.getConnector(zkClusterId);
		if (connector != null) {
			connector.unsubscribeAll();
			connector.close();
			connector = null;
			connectorManager.removeConnector(zkClusterId);
		}
	}
	
	/**
	 * ����ZK����
	 * @param zkCluster
	 * @throws Exception
	 */
	private void addConnector(ZkCluster zkCluster) throws Exception {
		connectorManager.addConnector(zkCluster);
	}

}
