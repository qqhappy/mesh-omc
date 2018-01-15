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
 * ZK�ص�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface ZkCallbackFacade extends Remote {

	/**
	 * ����ڵ����ݱ仯
	 * 
	 * @param zkNode
	 *            �½ڵ�ģ��
	 * @throws RuntimeException
	 */
	public void handleDataChange(ZkNode zkNode) throws RuntimeException,
			Exception;

	/**
	 * �����ӽڵ�仯
	 * 
	 * @param parentPath
	 *            ���ڵ�·��
	 * @throws RuntimeException
	 */
	public void handleChildrenChange(String parentPath)
			throws RuntimeException, Exception;

	/**
	 * ֪ͨ�ͻ���ˢ��ZK��ָ��·������ʾ
	 * 
	 * @param path
	 *            ·��
	 * @throws RuntimeException
	 */
	public void notifyZkClientRefresh(String path) throws RuntimeException,
			Exception;

}
