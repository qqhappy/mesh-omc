/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-8-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

/**
 * ���ݿ������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface DataBaseChecker {

	/**
	 * �ж����ݿ���Ƿ����
	 * 
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	public boolean isTableExist(String dataBase, String tableName);

	/**
	 * �ж����ݿ��Ƿ����
	 * 
	 * @param dataBase
	 * @return
	 */
	public boolean isDataBaseExist(String dataBase);
}
