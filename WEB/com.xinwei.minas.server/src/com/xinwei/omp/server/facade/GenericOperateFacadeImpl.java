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
 * ͨ�ò�������
 * 
 * @author chenjunhua
 * 
 */

public class GenericOperateFacadeImpl extends UnicastRemoteObject implements GenericOperateFacade {

	public GenericOperateFacadeImpl() throws RemoteException {
		super();
	}
	
	/**
	 * ��ָ����ģ��ִ��ָ���Ĳ���
	 * 
	 * @param operate
	 *            ����
	 * @param model
	 *            ͨ��ģ��
	 * @return �������
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
