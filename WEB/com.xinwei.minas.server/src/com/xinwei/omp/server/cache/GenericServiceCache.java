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
 * 通用服务缓存
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
	 * 增加模型操作与服务方法的映射关系
	 * 
	 * @param operate
	 *            操作
	 * @param modelName
	 *            模型名称 
	 * @param serviceName
	 *            服务接口
	 * @param methodName
	 *            方法
	 */
	public void addMapping(GenericOperate operate, Class modelClazz,
			Class serviceClazz, String methodName) {
		String key = this.generateKey(operate, modelClazz);
		GenericServiceAndMethod serviceAndMethod = new GenericServiceAndMethod(
				serviceClazz, methodName);
		cache.put(key, serviceAndMethod);
	}

	/**
	 * 获取指定模型操作的服务方法
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
	 * 生成主键
	 * 
	 * @param operate
	 * @param modelClazz
	 * @return
	 */
	private String generateKey(GenericOperate operate, Class modelClazz) {
		return operate.toString() + "/" + modelClazz.getCanonicalName();
	}

}
