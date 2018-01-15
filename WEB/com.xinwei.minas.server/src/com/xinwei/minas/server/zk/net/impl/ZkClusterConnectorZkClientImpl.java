/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * ZK集群连接器的ZkClient实现
 * 
 * @author chenjunhua
 * 
 */

public class ZkClusterConnectorZkClientImpl extends ZkClient implements
		ZkClusterConnector {

	// 连接状态
	private int connectState = ZkClusterConstant.STATE_DISCONNECTED;

	// ZK状态侦听器
	private IZkStateListener zkStateListener;

	// ZK数据变化侦听器
	private IZkDataListener zkDataListener;

	// ZK孩子节点数据变化侦听器
	private IZkChildListener zkChildListener;

	// SAG ROOT 根节点
	private ZkNode sagRoot;

	private Lock lock = new ReentrantLock();

	/**
	 * 构造函数
	 * 
	 * @param zkServers
	 *            ZK集群连接字符串
	 * @param sessionTimeout
	 *            session超时时间
	 * @param connectionTimeout
	 *            连接超时时间
	 */
	public ZkClusterConnectorZkClientImpl(String zkServers, int sessionTimeout,
			int connectionTimeout) {
		super(zkServers, sessionTimeout, connectionTimeout,
				new BytesPushThroughSerializer());
	}

	public int getConnectState() {
		return connectState;
	}

	public void setConnectState(int connectState) {
		this.connectState = connectState;
	}

	public IZkStateListener getZkStateListener() {
		return zkStateListener;
	}

	public void setZkStateListener(IZkStateListener zkStateListener) {
		this.zkStateListener = zkStateListener;
	}

	public IZkDataListener getZkDataListener() {
		return zkDataListener;
	}

	public void setZkDataListener(IZkDataListener zkDataListener) {
		this.zkDataListener = zkDataListener;
	}

	public IZkChildListener getZkChildListener() {
		return zkChildListener;
	}

	public void setZkChildListener(IZkChildListener zkChildListener) {
		this.zkChildListener = zkChildListener;
	}

	public ZkNode getSagRoot() {
		return sagRoot;
	}

	public void setSagRoot(ZkNode sagRoot) {
		this.sagRoot = sagRoot;
	}

	public Lock getLock() {
		return lock;
	}
}