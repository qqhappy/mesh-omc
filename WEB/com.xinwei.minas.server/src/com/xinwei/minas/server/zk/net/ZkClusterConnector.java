/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * ZK集群连接器接口
 * 
 * @author chenjunhua
 * 
 */

public interface ZkClusterConnector {
	
	/**
	 * 关闭连接
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	/**
	 * 创建一个节点.
	 * 
	 * @param path
	 *            路径
	 * @param data
	 *            节点数据
	 * @param mode
	 *            创建模式
	 * @return 所创建节点的路径
	 * @throws Exception
	 *             if any other exception occurs
	 */
	public String create(final String path, Object data, final CreateMode mode)
			throws Exception;

	/**
	 * 读取指定路径节点的数据
	 * 
	 * @param <T>
	 * @param path
	 * @return
	 */
	public <T extends Object> T readData(String path);
	
	/**
	 * 读取指定路径节点的数据
	 * @param <T>
	 * @param path
	 * @param stat
	 * @return
	 */
	public <T extends Object> T readData(String path, Stat stat);

	/**
	 * 写数据到指定节点路径
	 * 
	 * @param path
	 * @param object
	 * @return
	 */
	public Stat writeData(String path, Object object);

	/**
	 * 获取指定路径节点的孩子节点
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getChildren(String path);

	/**
	 * 删除指定路径
	 * 
	 * @param path
	 * @return
	 */
	public boolean delete(final String path);

	/**
	 * 递归删除指定路径
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteRecursive(String path);

	/**
	 * 判断某路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public boolean exists(final String path);

	/**
	 * 订阅某路径下孩子节点改变侦听器
	 * 
	 * @param path
	 * @param listener
	 * @return
	 */
	public List<String> subscribeChildChanges(String path,
			IZkChildListener listener);

	/**
	 * 订阅某路径节点的数据变化侦听器
	 * 
	 * @param path
	 * @param listener
	 */
	public void subscribeDataChanges(String path, IZkDataListener listener);

	/**
	 * 订阅ZK集群状态变化侦听器
	 * 
	 * @param listener
	 */
	public void subscribeStateChanges(final IZkStateListener listener);

	/**
	 * 取消某路径下孩子节点改变侦听器
	 * 
	 * @param path
	 * @param childListener
	 */
	public void unsubscribeChildChanges(String path,
			IZkChildListener childListener);

	/**
	 * 取消某路径节点的数据变化侦听器
	 * 
	 * @param path
	 * @param dataListener
	 */
	public void unsubscribeDataChanges(String path, IZkDataListener dataListener);

	/**
	 * 取消ZK集群状态变化侦听器
	 * 
	 * @param stateListener
	 */
	public void unsubscribeStateChanges(IZkStateListener stateListener);

	/**
	 * 取消所有订阅
	 */
	public void unsubscribeAll();
	
	/**
	 * 获取连接状态
	 * @return
	 */
	public int getConnectState();

	/**
	 * 设置连接状态
	 * @param connectState
	 */
	public void setConnectState(int connectState);

	public IZkStateListener getZkStateListener();

	public void setZkStateListener(IZkStateListener zkStateListener);

	public IZkDataListener getZkDataListener();

	public void setZkDataListener(IZkDataListener zkDataListener);

	public IZkChildListener getZkChildListener();

	public void setZkChildListener(IZkChildListener zkChildListener) ;

	public ZkNode getSagRoot();

	public void setSagRoot(ZkNode sagRoot);
}
