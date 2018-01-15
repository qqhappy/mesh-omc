/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB数据导入导出服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbDataImportExportService {

	/**
	 * 导出系统所有eNB配置
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Enb, List<XBizTable>> exportEnbData() throws Exception;

	/**
	 * 导出系统指定eNB配置
	 * 
	 * @param enbIdList
	 * @return
	 * @throws Exception
	 */
	public Map<Enb, List<XBizTable>> exportEnbData(List<Long> enbIdList)
			throws Exception;

	/**
	 * 将数据导入到系统中
	 * 
	 * @param dataMap
	 * @throws Exception
	 */
	public void importEnbData(Map<Enb, List<XBizTable>> dataMap)
			throws Exception;

	public List<String> getAllFieldNames(int enbTypeId, String protocolVersion,
			String tableName)throws Exception;

}
