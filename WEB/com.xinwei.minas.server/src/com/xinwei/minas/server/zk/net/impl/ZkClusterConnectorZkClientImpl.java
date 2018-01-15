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
 * ZK��Ⱥ��������ZkClientʵ��
 * 
 * @author chenjunhua
 * 
 */

public class ZkClusterConnectorZkClientImpl extends ZkClient implements
		ZkClusterConnector {

	// ����״̬
	private int connectState = ZkClusterConstant.STATE_DISCONNECTED;

	// ZK״̬������
	private IZkStateListener zkStateListener;

	// ZK���ݱ仯������
	private IZkDataListener zkDataListener;

	// ZK���ӽڵ����ݱ仯������
	private IZkChildListener zkChildListener;

	// SAG ROOT ���ڵ�
	private ZkNode sagRoot;

	private Lock lock = new ReentrantLock();

	/**
	 * ���캯��
	 * 
	 * @param zkServers
	 *            ZK��Ⱥ�����ַ���
	 * @param sessionTimeout
	 *            session��ʱʱ��
	 * @param connectionTimeout
	 *            ���ӳ�ʱʱ��
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