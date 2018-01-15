/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net.impl;

import org.I0Itec.zkclient.IZkStateListener;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;

/**
 * 
 * ZK集群状态变化侦听器
 * 
 * @author chenjunhua
 * 
 */

public class ZkStateListenerImpl implements IZkStateListener {

	private  static final Logger logger = Logger.getLogger(ZkStateListenerImpl.class);
	
	private ZkClusterConnector connector;
	
	public ZkStateListenerImpl(ZkClusterConnector connector) {
		this.connector = connector;
	}
	
	/**
	 * Called when the zookeeper connection state has changed.
	 * 
	 * @param state
	 *            The new state.
	 * @throws Exception
	 *             On any error.
	 */
	public void handleStateChanged(KeeperState state) throws Exception {
		switch (state) {
			case Disconnected:
				connector.setConnectState(ZkClusterConstant.STATE_DISCONNECTED);
				break;
			case SyncConnected:
				connector.setConnectState(ZkClusterConstant.STATE_CONNECTED);
				break;
			default:
				logger.warn("handleStateChanged unknown state: " + state);
				break;
		}
	}

	/**
	 * Called after the zookeeper session has expired and a new session has been
	 * created. You would have to re-create any ephemeral nodes here.
	 * 
	 * @throws Exception
	 *             On any error.
	 */
	public void handleNewSession() throws Exception {

	}
}
