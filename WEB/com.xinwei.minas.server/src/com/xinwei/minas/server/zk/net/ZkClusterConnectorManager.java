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
 * ZK连接管理器
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

	// 基站与SAG间链路变化侦听器
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
	 * 获取连接Map
	 * 
	 * @return
	 */
	public Map<Long, ZkClusterConnector> getConnectorMap() {
		return connectorMap;
	}

	/**
	 * 获取指定ID的ZK集群连接
	 * 
	 * @param zkClusterId
	 * @return
	 */
	public ZkClusterConnector getConnector(Long zkClusterId) {
		return connectorMap.get(zkClusterId);
	}
	
	/**
	 * 获取指定ID的ZK集群
	 * 
	 * @param zkClusterId
	 * @return
	 */
	public ZkCluster getZkCluster(Long zkClusterId) {
		return zkClusterMap.get(zkClusterId);
	}

	/**
	 * 创建ZK集群连接
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
			// 初始化连接
			this.initializeConnector(connector);
			// 放入缓存
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
	 * 删除ZK集群连接
	 * 
	 * @param zkClusterId
	 *            集群ID
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
	 * 初始化连接
	 * 
	 * @param connector
	 * @throws Exception
	 */
	private void initializeConnector(ZkClusterConnector connector)
			throws Exception {
		// 设置连接状态为已连接
		connector.setConnectState(ZkClusterConstant.STATE_CONNECTED);
		// 获取并设置SAG业务节点树
		ZkNode sagRoot = ZkNodeHelper.querySagBizTree(connector);		
		if (sagRoot == null) {
			throw new Exception("Failed to query SAG ROOT.");
		}
		// 设置SAG业务根节点
		connector.setSagRoot(sagRoot);
		// ZK状态侦听器
		IZkStateListener zkStateListener = new ZkStateListenerImpl(
				connector);
		connector.setZkStateListener(zkStateListener);
		// ZK数据变化侦听器
		IZkDataListener zkDataListener = new ZkDataListenerImpl(connector,
				btsSagLinkChangedListener);
		connector.setZkDataListener(zkDataListener);
		// ZK孩子节点数据变化侦听器
		IZkChildListener zkChildListener = new ZkChildListenerImpl(
				connector, btsSagLinkChangedListener);
		connector.setZkChildListener(zkChildListener);
		// 注册侦听器
		ZkNodeHelper.registerListener(connector, sagRoot);
	}
	

	/**
	 * 创建滚动文件日志
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
