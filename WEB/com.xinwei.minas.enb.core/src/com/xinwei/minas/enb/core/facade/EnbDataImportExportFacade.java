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
 * eNB���ݵ��뵼������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbDataImportExportFacade extends Remote {

	/**
	 * ����ϵͳ����eNB����
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Enb, List<XBizTable>> exportEnbData() throws Exception;

	/**
	 * ����ϵͳָ��eNB����
	 * 
	 * @param enbIdList
	 * @return
	 * @throws Exception
	 */
	public Map<Enb, List<XBizTable>> exportEnbData(List<Long> enbIdList)
			throws Exception;

	/**
	 * �����ݵ��뵽ϵͳ��
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
