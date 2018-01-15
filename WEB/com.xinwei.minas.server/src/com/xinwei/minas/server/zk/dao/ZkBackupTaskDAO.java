/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.dao;

import java.util.List;

import com.xinwei.minas.zk.core.basic.ZkBackupTask;

/**
 * 
 * NK集群备份任务DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupTaskDAO {

	/**
	 * 查询备份任务
	 * 
	 * @return
	 */
	public List<ZkBackupTask> queryBackupTasks()throws Exception;
	
	/**
	 * 根据ID查询备份任务
	 * @param id 任务Id
	 * @return
	 */
	public ZkBackupTask queryBackupTask(int id) throws Exception;

	/**
	 * 增加备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void createBackupTask(ZkBackupTask task) throws Exception;

	/**
	 * 修改备份任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void modifyBackupTask(ZkBackupTask task) throws Exception;

	/**
	 * 删除备份任务
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteBackupTask(int id) throws Exception;
}