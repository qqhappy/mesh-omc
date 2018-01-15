/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.omp.core.model.generic.GenericOperate;
import com.xinwei.omp.server.model.GenericServiceAndMethod;

/**
 * 
 * ͨ�÷��񻺴�
 * 
 * @author chenjunhua
 * 
 */

public class GenericServiceCache {

	private static final GenericServiceCache instance = new GenericServiceCache();

	private Map<String, GenericServiceAndMethod> cache = new ConcurrentHashMap();

	private GenericServiceCache() {

	}

	public static GenericServiceCache getInstance() {
		return instance;
	}

	/**
	 * ����ģ�Ͳ�������񷽷���ӳ���ϵ
	 * 
	 * @param operate
	 *            ����
	 * @param modelName
	 *            ģ������ 
	 * @param serviceName
	 *            ����ӿ�
	 * @param methodName
	 *            ����
	 */
	public void addMapping(GenericOperate operate, Class modelClazz,
			Class serviceClazz, String methodName) {
		String key = this.generateKey(operate, modelClazz);
		GenericServiceAndMethod serviceAndMethod = new GenericServiceAndMethod(
				serviceClazz, methodName);
		cache.put(key, serviceAndMethod);
	}

	/**
	 * ��ȡָ��ģ�Ͳ����ķ��񷽷�
	 * 
	 * @param operate
	 * @param model
	 * @return
	 */
	public GenericServiceAndMethod getServiceAndMethodBy(
			GenericOperate operate, Class modelClazz) {
		String key = this.generateKey(operate, modelClazz);
		return cache.get(key);
	}

	/**
	 * ��������
	 * 
	 * @param operate
	 * @param modelClazz
	 * @return
	 */
	private String generateKey(GenericOperate operate, Class modelClazz) {
		return operate.toString() + "/" + modelClazz.getCanonicalName();
	}

}
