/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.Map;

/**
 * 
 * eNB��ѧϰ��������DAO
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStudyDataConfigDAO {

	/**
	 * ��ѯ������������
	 * 
	 * @return key=version, value=config
	 * @throws Exception
	 */
	public Map<String, String> queryAll() throws Exception;

	/**
	 * ���հ汾��ѯ��������
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public String queryByVersion(String version) throws Exception;

	/**
	 * ������ѧϰ����
	 * 
	 * @param version
	 * @param dataConfig
	 * @throws Exception
	 */
	public void saveDataConfig(String version, String dataConfig)
			throws Exception;

	/**
	 * ɾ��ָ���汾����������
	 * 
	 * @param version
	 * @throws Exception
	 */
	public void delete(String version) throws Exception;

}
