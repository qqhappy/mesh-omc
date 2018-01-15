/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.service;

import java.rmi.RemoteException;

import com.xinwei.omp.core.model.generic.GenericModel;
import com.xinwei.omp.core.model.generic.GenericOperate;

/**
 * 
 * ͨ�ò�������ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface GenericOperateService {
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
			throws Exception ;
}
