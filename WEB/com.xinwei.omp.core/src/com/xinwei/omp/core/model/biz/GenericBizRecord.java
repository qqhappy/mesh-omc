/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 通用业务记录
 * 
 * @author chenjunhua
 * 
 */

public class GenericBizRecord implements Serializable {

	// 字段-值 映射
	private Map<String, GenericBizProperty> properties = new ConcurrentHashMap();

	/**
	 * 获取字段数目
	 * 
	 * @return
	 */
	public int numbers() {
		return properties.size();
	}

	/**
	 * 获取字段名列表
	 * 
	 * @return
	 */
	public String[] getPropertyNames() {
		return (String[]) properties.keySet().toArray(new String[0]);
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 * @param propertyName
	 * @return
	 */
	public GenericBizProperty getPropertyValue(String propertyName) {
		return properties.get(propertyName);
	}

	/**
	 * 增加属性
	 * 
	 * @param property
	 */
	public void addProperty(GenericBizProperty property) {
		properties.put(property.getName(), property);
	}

	
	/**
	 * 删除属性
	 * @param propertyName
	 * @return
	 */
	public GenericBizProperty removeProperty(String propertyName) {
		return properties.remove(propertyName);
	}
}
