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
 * NK��Ⱥ��������DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface ZkBackupTaskDAO {

	/**
	 * ��ѯ��������
	 * 
	 * @return
	 */
	public List<ZkBackupTask> queryBackupTasks()throws Exception;
	
	/**
	 * ����ID��ѯ��������
	 * @param id ����Id
	 * @return
	 */
	public ZkBackupTask queryBackupTask(int id) throws Exception;

	/**
	 * ���ӱ�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void createBackupTask(ZkBackupTask task) throws Exception;

	/**
	 * �޸ı�������
	 * 
	 * @param task
	 * @throws Exception
	 */
	public void modifyBackupTask(ZkBackupTask task) throws Exception;

	/**
	 * ɾ����������
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteBackupTask(int id) throws Exception;
}