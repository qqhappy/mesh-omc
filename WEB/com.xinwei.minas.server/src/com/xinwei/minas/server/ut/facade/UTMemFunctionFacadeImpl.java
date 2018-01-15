/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.ut.service.UTMemFunctionService;
import com.xinwei.minas.ut.core.facade.UTMemFunctionFacade;
import com.xinwei.minas.ut.core.model.MemSingalReport;
import com.xinwei.minas.ut.core.model.UTLayer3Param;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * MEM功能门面实现
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTMemFunctionFacadeImpl extends UnicastRemoteObject implements UTMemFunctionFacade{

	private UTMemFunctionService service;
	
	protected UTMemFunctionFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(UTMemFunctionService.class);		
	}

	@Override
	public void configMemSingalReport(OperObject operObject, Long moId,
			MemSingalReport memSingalReport) throws Exception {
		service.configMemSingalReport(moId, memSingalReport);
	}

	@Override
	public MemSingalReport queryMemSingalReport(OperObject operObject, Long pid)
			throws Exception {
		return service.queryMemSingalReport(pid);
	}

	@Override
	public List<UTLayer3Param> queryMemLayer3Param(OperObject operObject,
			Long moId, Long pid) throws Exception {
		return service.queryMemLayer3Param(moId, pid);
	}


}
