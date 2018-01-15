/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.basic;

import java.io.File;

/**
 * 
 * ������س���
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupConstant {

	public static final long ONE_MIN = 60000;

	public static final long ONE_HOUR = 60 * ONE_MIN;

	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * ������ռ���ռ�
	 */
	public static final long MAX_SPACE_SIZE = 500 * 1024 * 1024; // 500M

	/**
	 * ÿ���ͷŵĿռ��С
	 */
	public static final long RELEASE_LIMIT = 100 * 1024 * 1024; // 100M

	/**
	 * ���ݸ�Ŀ¼��
	 */
	public static final String BACKUP_DIR_NAME = new StringBuffer()
			.append(System.getProperty("user.dir")).append(File.separator)
			.append("plugins").append(File.separator).append("zk")
			.append(File.separator).append("backup").toString();

	/**
	 * ���ݸ�Ŀ¼
	 */
	public static final File BACKUP_DIR = new File(BACKUP_DIR_NAME);

	/**
	 * ��󱸷�������
	 */
	public static final int MAX_TASK_NUM = 1;

}