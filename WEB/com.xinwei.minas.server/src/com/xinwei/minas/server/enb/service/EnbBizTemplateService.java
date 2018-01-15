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
 * eNB业务数据模板服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizTemplateService {

	/**
	 * 查询指定版本的所有模板数据
	 * 
	 * @param protocolVersion
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String protocolVersion) throws Exception;

	/**
	 * 查询指定版本中指定业务表的模板数据
	 * 
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryTemplateData(int enbTypeId, String protocolVersion,
			String tableName) throws Exception;

}
