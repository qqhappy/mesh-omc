/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB数据导入导出门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbDataImportExportFacade extends Remote {

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
	@Loggable
	public void importEnbData(OperObject operObject,
			Map<Enb, List<XBizTable>> dataMap) throws Exception;

	public List<String>  getAllFieldNames(int enbTypeId, String protocolVersion,
			String tableName)throws Exception;

}
