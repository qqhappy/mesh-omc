/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;

/**
 * 
 * eNBʵʱ����ͳ����DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeItemConfigDAO {

	/**
	 * ��ѯͳ��������
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception;

	/**
	 * ����ͳ��������
	 * 
	 * @param configList
	 * @throws Exception
	 */
	public void saveItemConfig(List<EnbRealtimeItemConfig> configList)
			throws Exception;

}
