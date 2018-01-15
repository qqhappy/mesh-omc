/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.service.impl;

import java.lang.reflect.Method;
import java.rmi.RemoteException;

import com.xinwei.omp.core.model.generic.GenericModel;
import com.xinwei.omp.core.model.generic.GenericOperate;
import com.xinwei.omp.core.utils.ReflectUtils;
import com.xinwei.omp.server.OmpAppContext;
import com.xinwei.omp.server.cache.GenericServiceCache;
import com.xinwei.omp.server.model.GenericServiceAndMethod;
import com.xinwei.omp.server.service.GenericOperateService;

/**
 * 
 * 通用操作服务
 * 
 * @author chenjunhua
 * 
 */

public class GenericOperateServiceImpl implements GenericOperateService {

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
		// 根据操作和模型获取操作该模型的类和方法
		GenericServiceAndMethod serviceAndMethod = this.getServiceAndMethod(operate, model);
		Class serviceClazz = serviceAndMethod.getServiceClazz();
		String methodName = serviceAndMethod.getMethodName();
		Method method = ReflectUtils.findMethod(serviceClazz, methodName);
		if (method == null) {
			throw new Exception("Unsupported operation!");
		}
		// 方法入参类型
		Class[] methodParametersTypes = method.getParameterTypes();
		// 方法返回值类型
		Class returnType = method.getReturnType();
		// 获取服务实例
		Object serviceObject = OmpAppContext.getCtx().getBean(serviceClazz);
		// 确定入参(目前只支持0-1个入参)
		int parameterNum = methodParametersTypes.length;
		Object[] input = new Object[parameterNum];
		if (parameterNum == 1) {
			input[0] = model;
		}
		// 方法调用
		Object result = method.invoke(serviceObject, input);
		if (result == null) {
			return null;
		} else {
			return (GenericModel) result;
		}
	}

	/**
	 * 根据操作和模型获取操作该模型的类和方法
	 * 
	 * @param operate
	 * @param model
	 * @return
	 */
	private GenericServiceAndMethod getServiceAndMethod(GenericOperate operate,
			GenericModel model) {
		return GenericServiceCache.getInstance().getServiceAndMethodBy(operate, model.getClass());
	}

}
