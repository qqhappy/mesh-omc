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
 * NK集群数据备份门面
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupFacade extends Remote {

	/**
	 * 创建备份
	 * 
	 * @param backupName
	 * @param createType
	 * @throws Exception
	 */
	@Loggable
	public void createBackup(String backupName, int createType) throws RemoteException,
			Exception;

	/**
	 * 查询备份
	 * 
	 * @param backupName
	 * @throws Exception
	 */
	public ZkBackup queryBackup(String backupName) throws RemoteException,
			Exception;

	/**
	 * 查询所有备份
	 * 
	 * @throws Exception
	 */
	public List<ZkBackup> queryAllBackups() throws RemoteException, Exception;

	/**
	 * 从本地磁盘重新获取备份
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void refreshBackups() throws RemoteException, Exception;
	
	/**
	 * 修改备份
	 * 
	 * @param backupName
	 * @throws Exception
	 */
	@Loggable
	public void deleteBackup(String backupName) throws RemoteException,
			Exception;

	/**
	 * 恢复备份
	 * 
	 * @param zkClusterId
	 * @param backupName
	 * @throws Exception
	 */
	@Loggable
	public void recoverBackup(long zkClusterId, String backupName) throws RemoteException,
			Exception;

	/**
	 * 创建备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void createBackupTask(ZkBackupTask task) throws RemoteException,
			Exception;

	/**
	 * 查询备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	public ZkBackupTask queryBackupTask(int taskId) throws RemoteException,
			Exception;

	/**
	 * 查询全部备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	public List<ZkBackupTask> queryBackupTasks() throws RemoteException,
			Exception;

	/**
	 * 删除备份任务
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	@Loggable
	public void deleteBackupTask(int taskId) throws RemoteException, Exception;

	/**
	 * 修改备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void modifyBackupTask(ZkBackupTask task) throws RemoteException,
			Exception;

	/**
	 * 开启备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void openBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * 关闭备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	@Loggable
	public void closeBackupTask(ZkBackupTask task) throws RemoteException, Exception;
	
	/**
	 * 释放备份空间
	 * 
	 * @throws Exception
	 */
	@Loggable
	public void releaseBackupSpace() throws RemoteException, Exception;
	
	/**
	 * 查询备份占空间大小
	 * 
	 * @throws Exception
	 */
	public double queryTotalBackupSpace() throws RemoteException, Exception;

	/**
	 * 下载备份
	 * 
	 * @throws Exception
	 */
	@Loggable
	public Map<String, byte[]> downloadBackup(String backupName) throws RemoteException,
	Exception;
}