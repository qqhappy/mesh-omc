/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.xinwei.minas.server.zk.net.impl.ZkChildListenerImpl;
import com.xinwei.minas.server.zk.net.impl.ZkClusterConnectorZkClientImpl;
import com.xinwei.minas.server.zk.net.impl.ZkDataListenerImpl;
import com.xinwei.minas.server.zk.net.impl.ZkStateListenerImpl;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;
import com.xinwei.minas.zk.core.listener.BtsSagLinkChangedListener;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * ZK���ӹ�����
 * 
 * @author chenjunhua
 * 
 */

public class ZkClusterConnectorManager {

	private static Logger logger = null;

	private ConcurrentHashMap<Long, ZkClusterConnector> connectorMap = new ConcurrentHashMap<Long, ZkClusterConnector>();
	
	private ConcurrentHashMap<Long, ZkCluster> zkClusterMap = new ConcurrentHashMap<Long, ZkCluster>();

	private int sessionTimeout = 30000;

	private int connectionTimeout = 20000;

	// ��վ��SAG����·�仯������
	private BtsSagLinkChangedListener btsSagLinkChangedListener;

	public ZkClusterConnectorManager() {
		this(30000, 20000);
	}

	public ZkClusterConnectorManager(int sessionTimeout, int connectionTimeout) {
		this.sessionTimeout = sessionTimeout;
		this.connectionTimeout = sessionTimeout;
		this.createLoggerIfNotExist();
	}
	
	/**
	 * ��ȡ����Map
	 * 
	 * @return
	 */
	public Map<Long, ZkClusterConnector> getConnectorMap() {
		return connectorMap;
	}

	/**
	 * ��ȡָ��ID��ZK��Ⱥ����
	 * 
	 * @param zkClusterId
	 * @return
	 */
	public ZkClusterConnector getConnector(Long zkClusterId) {
		return connectorMap.get(zkClusterId);
	}
	
	/**
	 * ��ȡָ��ID��ZK��Ⱥ
	 * 
	 * @param zkClusterId
	 * @return
	 */
	public ZkCluster getZkCluster(Long zkClusterId) {
		return zkClusterMap.get(zkClusterId);
	}

	/**
	 * ����ZK��Ⱥ����
	 * 
	 * @param zkCluster
	 * @return
	 */
	public ZkClusterConnector addConnector(ZkCluster zkCluster) {
		if (connectorMap.containsKey(zkCluster.getId())) {
			return connectorMap.get(zkCluster.getId());
		}
		ZkClusterConnector connector = null;
		try {
			connector = new ZkClusterConnectorZkClientImpl(
					zkCluster.getZkServersString(), sessionTimeout,
					connectionTimeout);
			if (connector == null) {
				return null;
			}
			// ��ʼ������
			this.initializeConnector(connector);
			// ���뻺��
			connectorMap.putIfAbsent(zkCluster.getId(), connector);
			zkClusterMap.putIfAbsent(zkCluster.getId(), zkCluster);
			return connector;
		} catch (Exception e) {
			if (connector != null) {
				try {
					connector.close();
				} catch (Exception e1) {
				}
			}
			logger.error("Failed to create ZkClusterConnector, " + zkCluster, e);
			return null;
		}
	}



	/**
	 * ɾ��ZK��Ⱥ����
	 * 
	 * @param zkClusterId
	 *            ��ȺID
	 */
	public void removeConnector(Long zkClusterId) {
		ZkClusterConnector connector = connectorMap.get(zkClusterId);
		try {
			if (connector != null) {
				connector.unsubscribeAll();
				connector.close();
			}
			zkClusterMap.remove(zkClusterId);
			connectorMap.remove(zkClusterId);
		} catch (Exception e) {
			logger.error("Failed to remove ZkClusterConnector, id="
					+ zkClusterId, e);
		}
	}

	public BtsSagLinkChangedListener getBtsSagLinkChangedListener() {
		return btsSagLinkChangedListener;
	}

	public void setBtsSagLinkChangedListener(
			BtsSagLinkChangedListener btsSagLinkChangedListener) {
		this.btsSagLinkChangedListener = btsSagLinkChangedListener;
	}

	/**
	 * ��ʼ������
	 * 
	 * @param connector
	 * @throws Exception
	 */
	private void initializeConnector(ZkClusterConnector connector)
			throws Exception {
		// ��������״̬Ϊ������
		connector.setConnectState(ZkClusterConstant.STATE_CONNECTED);
		// ��ȡ������SAGҵ��ڵ���
		ZkNode sagRoot = ZkNodeHelper.querySagBizTree(connector);		
		if (sagRoot == null) {
			throw new Exception("Failed to query SAG ROOT.");
		}
		// ����SAGҵ����ڵ�
		connector.setSagRoot(sagRoot);
		// ZK״̬������
		IZkStateListener zkStateListener = new ZkStateListenerImpl(
				connector);
		connector.setZkStateListener(zkStateListener);
		// ZK���ݱ仯������
		IZkDataListener zkDataListener = new ZkDataListenerImpl(connector,
				btsSagLinkChangedListener);
		connector.setZkDataListener(zkDataListener);
		// ZK���ӽڵ����ݱ仯������
		IZkChildListener zkChildListener = new ZkChildListenerImpl(
				connector, btsSagLinkChangedListener);
		connector.setZkChildListener(zkChildListener);
		// ע��������
		ZkNodeHelper.registerListener(connector, sagRoot);
	}
	

	/**
	 * ���������ļ���־
	 */
	private void createLoggerIfNotExist() {
		if (logger != null) {
			return;
		}
		String fileName = "log/ZkClusterConnectorManager.log";

		try {
			logger = Logger.getLogger(ZkClusterConnectorManager.class);
			logger.setAdditivity(false);
			logger.setLevel(Level.DEBUG);

			logger.removeAllAppenders();

			PatternLayout patterLayout = new PatternLayout();
			patterLayout.setConversionPattern("%d %m%n");
			RollingFileAppender appender = null;
			appender = new RollingFileAppender(patterLayout, fileName);
			appender.setMaxBackupIndex(5);
			appender.setMaxFileSize("4096kb");
			logger.addAppender(appender);

		} catch (Exception e) {
			logger.error("createLoggerIfNotExist()", e); //$NON-NLS-1$
			logger = Logger.getLogger(ZkClusterConnectorManager.class);
		}
	}

}
