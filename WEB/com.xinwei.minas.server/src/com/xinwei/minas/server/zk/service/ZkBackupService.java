/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;

/**
 * 
 * NK��Ⱥ���ݱ��ݷ���
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupService {
	
	/**
	 * ��������
	 * 
	 * @param backupName
	 * @param createType
	 * @throws Exception
	 */
	public void createBackup(String backupName, int createType) throws RemoteException,
			Exception;

	/**
	 * ���������Ʋ�ѯ����
	 * 
	 * @param backupName
	 * @throws Exception
	 */
	public ZkBackup queryBackup(String backupName) throws RemoteException,
			Exception;

	/**
	 * ��ѯ���б���
	 * 
	 * @throws Exception
	 */
	public List<ZkBackup> queryAllBackups() throws RemoteException, Exception;

	/**
	 * �ӱ��ش������»�ȡ����
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void refreshBackups() throws RemoteException, Exception;
	
	/**
	 * ɾ������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void deleteBackup(String backupName) throws RemoteException,
			Exception;

	/**
	 * �ָ�����
	 * 
	 * @param zkClusterId
	 * @param task
	 * @throws Exception
	 */
	public void recoverBackup(long zkClusterId, String backupName) throws RemoteException,
			Exception;

	/**
	 * ������������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void createBackupTask(ZkBackupTask task) throws RemoteException,
			Exception;

	/**
	 * ��ѯ��������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public ZkBackupTask queryBackupTask(int taskId) throws RemoteException,
			Exception;

	/**
	 * ��ѯȫ����������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public List<ZkBackupTask> queryBackupTasks() throws RemoteException,
			Exception;

	/**
	 * ɾ����������
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteBackupTask(int taskId) throws RemoteException, Exception;

	/**
	 * �޸ı�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void modifyBackupTask(ZkBackupTask task) throws RemoteException,
			Exception;

	/**
	 * ������������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void openBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * �رձ�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void closeBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * ��ѯ����ռ�ռ��С
	 * 
	 * @throws Exception
	 */
	public double queryTotalBackupSpace() throws RemoteException, Exception;
	
	/**
	 * �ͷű��ݿռ�
	 * 
	 * @throws Exception
	 */
	public void releaseBackupSpace() throws RemoteException, Exception;

	/**
	 * ���ر���
	 * 
	 * @throws Exception
	 */
	public Map<String, byte[]> downloadBackup(String backupName) throws RemoteException,
			Exception;
}