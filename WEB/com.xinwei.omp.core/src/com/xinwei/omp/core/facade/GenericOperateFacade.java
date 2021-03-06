/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.omp.core.model.generic.GenericModel;
import com.xinwei.omp.core.model.generic.GenericOperate;

/**
 * 
 * 通用操作门面
 * 
 * @author chenjunhua
 * 
 */

public interface GenericOperateFacade extends Remote{

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
			throws RemoteException, Exception;
}
