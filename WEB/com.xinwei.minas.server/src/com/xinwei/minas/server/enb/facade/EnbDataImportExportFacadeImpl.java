/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbDataImportExportFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.service.EnbDataImportExportService;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB数据导入导出门面接口实现
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbDataImportExportFacadeImpl extends UnicastRemoteObject
		implements EnbDataImportExportFacade {

	protected EnbDataImportExportFacadeImpl() throws RemoteException {
		super();
	}

	@Override
	public Map<Enb, List<XBizTable>> exportEnbData() throws Exception {
		return getService().exportEnbData();
	}

	@Override
	public Map<Enb, List<XBizTable>> exportEnbData(List<Long> enbIdList)
			throws Exception {
		return getService().exportEnbData(enbIdList);
	}

	@Override
	public void importEnbData(OperObject operObject,
			Map<Enb, List<XBizTable>> dataMap) throws Exception {
		getService().importEnbData(dataMap);
	}

	private EnbDataImportExportService getService() {
		return OmpAppContext.getCtx().getBean(EnbDataImportExportService.class);
	}

	@Override
	public List<String>  getAllFieldNames(int enbTypeId, String protocolVersion,
			String tableName) throws Exception {
		return getService().getAllFieldNames( enbTypeId,  protocolVersion,
				 tableName);
	}

}
