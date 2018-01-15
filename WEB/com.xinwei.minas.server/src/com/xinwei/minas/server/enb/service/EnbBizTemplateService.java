/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNBҵ������ģ�����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizTemplateService {

	/**
	 * ��ѯָ���汾������ģ������
	 * 
	 * @param protocolVersion
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String protocolVersion) throws Exception;

	/**
	 * ��ѯָ���汾��ָ��ҵ����ģ������
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryTemplateData(int enbTypeId, String protocolVersion,
			String tableName) throws Exception;

}
