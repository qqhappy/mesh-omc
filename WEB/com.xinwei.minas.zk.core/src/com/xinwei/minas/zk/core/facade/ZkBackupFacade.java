/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;

/**
 * 
 * NK��Ⱥ���ݱ�������
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupFacade extends Remote {

	/**
	 * ��������
	 * 
	 * @param backupName
	 * @param createType
	 * @throws Exception
	 */
	@Loggable
	public void createBackup(String backupName, int createType) throws RemoteException,
			Exception;

	/**
	 * ��ѯ����
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
	 * �޸ı���
	 * 
	 * @param backupName
	 * @throws Exception
	 */
	@Loggable
	public void deleteBackup(String backupName) throws RemoteException,
			Exception;

	/**
	 * �ָ�����
	 * 
	 * @param zkClusterId
	 * @param backupName
	 * @throws Exception
	 */
	@Loggable
	public void recoverBackup(long zkClusterId, String backupName) throws RemoteException,
			Exception;

	/**
	 * ������������
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
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
	@Loggable
	public void deleteBackupTask(int taskId) throws RemoteException, Exception;

	/**
	 * �޸ı�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void modifyBackupTask(ZkBackupTask task) throws RemoteException,
			Exception;

	/**
	 * ������������
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void openBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * �رձ�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void closeBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * �ͷű��ݿռ�
	 * 
	 * @throws Exception
	 */
	@Loggable
	public void releaseBackupSpace() throws RemoteException, Exception;
	
	/**
	 * ��ѯ����ռ�ռ��С
	 * 
	 * @throws Exception
	 */
	public double queryTotalBackupSpace() throws RemoteException, Exception;

	/**
	 * ���ر���
	 * 
	 * @throws Exception
	 */
	@Loggable
	public Map<String, byte[]> downloadBackup(String backupName) throws RemoteException,
	Exception;
}