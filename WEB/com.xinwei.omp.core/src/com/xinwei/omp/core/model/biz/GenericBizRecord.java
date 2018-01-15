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
 * ͨ��ҵ���¼
 * 
 * @author chenjunhua
 * 
 */

public class GenericBizRecord implements Serializable {

	// �ֶ�-ֵ ӳ��
	private Map<String, GenericBizProperty> properties = new ConcurrentHashMap();

	/**
	 * ��ȡ�ֶ���Ŀ
	 * 
	 * @return
	 */
	public int numbers() {
		return properties.size();
	}

	/**
	 * ��ȡ�ֶ����б�
	 * 
	 * @return
	 */
	public String[] getPropertyNames() {
		return (String[]) properties.keySet().toArray(new String[0]);
	}

	/**
	 * ������������ȡ����ֵ
	 * 
	 * @param propertyName
	 * @return
	 */
	public GenericBizProperty getPropertyValue(String propertyName) {
		return properties.get(propertyName);
	}

	/**
	 * ��������
	 * 
	 * @param property
	 */
	public void addProperty(GenericBizProperty property) {
		properties.put(property.getName(), property);
	}

	
	/**
	 * ɾ������
	 * @param propertyName
	 * @return
	 */
	public GenericBizProperty removeProperty(String propertyName) {
		return properties.remove(propertyName);
	}
}
