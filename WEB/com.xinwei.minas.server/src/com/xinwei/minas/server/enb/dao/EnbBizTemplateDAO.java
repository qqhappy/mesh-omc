/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNBҵ������ģ��DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizTemplateDAO {

	/**
	 * ��ѯָ���汾������ģ������
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String version) throws Exception;

	/**
	 * ��ѯָ���汾��ָ��ҵ����ģ������
	 * 
	 * @param version
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryTemplateData(int enbTypeId, String version,
			String tableName) throws Exception;

}
