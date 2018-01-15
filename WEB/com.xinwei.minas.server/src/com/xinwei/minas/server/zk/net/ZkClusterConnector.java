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
 * ZK��Ⱥ�������ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface ZkClusterConnector {
	
	/**
	 * �ر�����
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	/**
	 * ����һ���ڵ�.
	 * 
	 * @param path
	 *            ·��
	 * @param data
	 *            �ڵ�����
	 * @param mode
	 *            ����ģʽ
	 * @return �������ڵ��·��
	 * @throws Exception
	 *             if any other exception occurs
	 */
	public String create(final String path, Object data, final CreateMode mode)
			throws Exception;

	/**
	 * ��ȡָ��·���ڵ������
	 * 
	 * @param <T>
	 * @param path
	 * @return
	 */
	public <T extends Object> T readData(String path);
	
	/**
	 * ��ȡָ��·���ڵ������
	 * @param <T>
	 * @param path
	 * @param stat
	 * @return
	 */
	public <T extends Object> T readData(String path, Stat stat);

	/**
	 * д���ݵ�ָ���ڵ�·��
	 * 
	 * @param path
	 * @param object
	 * @return
	 */
	public Stat writeData(String path, Object object);

	/**
	 * ��ȡָ��·���ڵ�ĺ��ӽڵ�
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getChildren(String path);

	/**
	 * ɾ��ָ��·��
	 * 
	 * @param path
	 * @return
	 */
	public boolean delete(final String path);

	/**
	 * �ݹ�ɾ��ָ��·��
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteRecursive(String path);

	/**
	 * �ж�ĳ·���Ƿ����
	 * 
	 * @param path
	 * @return
	 */
	public boolean exists(final String path);

	/**
	 * ����ĳ·���º��ӽڵ�ı�������
	 * 
	 * @param path
	 * @param listener
	 * @return
	 */
	public List<String> subscribeChildChanges(String path,
			IZkChildListener listener);

	/**
	 * ����ĳ·���ڵ�����ݱ仯������
	 * 
	 * @param path
	 * @param listener
	 */
	public void subscribeDataChanges(String path, IZkDataListener listener);

	/**
	 * ����ZK��Ⱥ״̬�仯������
	 * 
	 * @param listener
	 */
	public void subscribeStateChanges(final IZkStateListener listener);

	/**
	 * ȡ��ĳ·���º��ӽڵ�ı�������
	 * 
	 * @param path
	 * @param childListener
	 */
	public void unsubscribeChildChanges(String path,
			IZkChildListener childListener);

	/**
	 * ȡ��ĳ·���ڵ�����ݱ仯������
	 * 
	 * @param path
	 * @param dataListener
	 */
	public void unsubscribeDataChanges(String path, IZkDataListener dataListener);

	/**
	 * ȡ��ZK��Ⱥ״̬�仯������
	 * 
	 * @param stateListener
	 */
	public void unsubscribeStateChanges(IZkStateListener stateListener);

	/**
	 * ȡ�����ж���
	 */
	public void unsubscribeAll();
	
	/**
	 * ��ȡ����״̬
	 * @return
	 */
	public int getConnectState();

	/**
	 * ��������״̬
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
