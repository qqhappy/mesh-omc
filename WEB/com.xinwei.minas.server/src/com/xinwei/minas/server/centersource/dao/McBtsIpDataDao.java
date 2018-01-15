/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-26	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.centersource.dao;

import java.util.List;

import com.xinwei.minas.server.centersource.model.McBtsIpData;

/**
 * 
 * ��վIP���ݳ־û��ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface McBtsIpDataDao {

	/**
	 * ��������
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveOrUpdate(McBtsIpData data) throws Exception;

	/**
	 * ��btsId��ѯ
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBtsIpData query(long btsId) throws Exception;

	/**
	 * ��ѯ��������
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsIpData> queryAll() throws Exception;

	/**
	 * ɾ��
	 * 
	 * @param btsId
	 * @throws Exception
	 */
	public void delete(long btsId) throws Exception;

}
