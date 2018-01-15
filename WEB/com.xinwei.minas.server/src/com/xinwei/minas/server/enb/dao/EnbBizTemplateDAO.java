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
 * eNB业务数据模板DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBizTemplateDAO {

	/**
	 * 查询指定版本的所有模板数据
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String version) throws Exception;

	/**
	 * 查询指定版本中指定业务表的模板数据
	 * 
	 * @param version
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryTemplateData(int enbTypeId, String version,
			String tableName) throws Exception;

}
