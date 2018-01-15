/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao;

import java.util.List;

/**
 * 
 * �ֱ���Խӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface TableStrategy {

	/**
	 * ����������ȡ����Ӧ�ô洢�������ݿ��ı���
	 * 
	 * @param condition
	 * @return
	 */
	public String getTableName(int timeType, long btsId);

	/**
	 * ��ȡ���ڷֱ�����б�
	 * 
	 * @return
	 */
	public List<String> getAllTables();
	
	/**
	 * ����������ȡ���ݿ��ı����б�
	 * 
	 * @return
	 */
	public List<String> getTables(int condition);
}
