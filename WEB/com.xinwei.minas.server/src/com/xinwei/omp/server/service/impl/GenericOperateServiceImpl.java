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
 * ͨ�ò�������
 * 
 * @author chenjunhua
 * 
 */

public class GenericOperateServiceImpl implements GenericOperateService {

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
		// ���ݲ�����ģ�ͻ�ȡ������ģ�͵���ͷ���
		GenericServiceAndMethod serviceAndMethod = this.getServiceAndMethod(operate, model);
		Class serviceClazz = serviceAndMethod.getServiceClazz();
		String methodName = serviceAndMethod.getMethodName();
		Method method = ReflectUtils.findMethod(serviceClazz, methodName);
		if (method == null) {
			throw new Exception("Unsupported operation!");
		}
		// �����������
		Class[] methodParametersTypes = method.getParameterTypes();
		// ��������ֵ����
		Class returnType = method.getReturnType();
		// ��ȡ����ʵ��
		Object serviceObject = OmpAppContext.getCtx().getBean(serviceClazz);
		// ȷ�����(Ŀǰֻ֧��0-1�����)
		int parameterNum = methodParametersTypes.length;
		Object[] input = new Object[parameterNum];
		if (parameterNum == 1) {
			input[0] = model;
		}
		// ��������
		Object result = method.invoke(serviceObject, input);
		if (result == null) {
			return null;
		} else {
			return (GenericModel) result;
		}
	}

	/**
	 * ���ݲ�����ģ�ͻ�ȡ������ģ�͵���ͷ���
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
