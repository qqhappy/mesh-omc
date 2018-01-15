/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.omp.core.facade.GenericOperateFacade;
import com.xinwei.omp.core.model.generic.GenericModel;
import com.xinwei.omp.core.model.generic.GenericOperate;
import com.xinwei.omp.server.OmpAppContext;
import com.xinwei.omp.server.service.GenericOperateService;

/**
 * 
 * 通用操作门面
 * 
 * @author chenjunhua
 * 
 */

public class GenericOperateFacadeImpl extends UnicastRemoteObject implements GenericOperateFacade {

	public GenericOperateFacadeImpl() throws RemoteException {
		super();
	}
	
	/**
	 * 对指定的模型执行指定的操作
	 * 
	 * @param operate
	 *            操作
	 * @param model
	 *            通用模型
	 * @return 操作结果
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericModel execute(GenericOperate operate, GenericModel model)
			throws Exception {
		GenericOperateService service = getService();
		return service.execute(operate, model);
	}
	
	
	private GenericOperateService getService() {
		return OmpAppContext.getCtx().getBean(GenericOperateService.class);
	}

}
