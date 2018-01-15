/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.zk.core.xnode.common.ZkNode;

/**
 * 
 * ZK回调门面接口
 * 
 * @author chenjunhua
 * 
 */

public interface ZkCallbackFacade extends Remote {

	/**
	 * 处理节点数据变化
	 * 
	 * @param zkNode
	 *            新节点模型
	 * @throws RuntimeException
	 */
	public void handleDataChange(ZkNode zkNode) throws RuntimeException,
			Exception;

	/**
	 * 处理孩子节点变化
	 * 
	 * @param parentPath
	 *            父节点路径
	 * @throws RuntimeException
	 */
	public void handleChildrenChange(String parentPath)
			throws RuntimeException, Exception;

	/**
	 * 通知客户端刷新ZK树指定路径的显示
	 * 
	 * @param path
	 *            路径
	 * @throws RuntimeException
	 */
	public void notifyZkClientRefresh(String path) throws RuntimeException,
			Exception;

}
