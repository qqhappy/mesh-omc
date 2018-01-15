/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-12	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.conf;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.utils.ReflectUtils;

/**
 * 
 * ͨ����Ԫҵ������
 * 
 * @author chenjunhua
 * 
 */

public class MoBizData implements Serializable{
	
	// ҵ������
	private String bizName;
	
	// ��¼��
	private List<MoBizRecord> records = new LinkedList();

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	/**
	 * ����ʵ��
	 * @param entity
	 */
	public void addEntity(Object entity) {
		MoBizRecord record = new MoBizRecord();		
		// ���÷����ȡgetter����
		List<Method> methods = ReflectUtils.findGetMethods(entity);
		for (Method method : methods) {
			String methodName = method.getName();
			int from = 3;
			String property = methodName.substring(from, from + 1)
					.toLowerCase()
					+ methodName.substring(from + 1);
			try {
				Object value = method.invoke(entity);
				record.addItemValue(property, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		records.add(record);
	}

	/**
	 * ��ȡ��¼����
	 * @return
	 */
	public List<MoBizRecord> getRecords() {
		return records;
	}

	/**
	 * ���ü�¼����
	 * @param records
	 */
	public void setRecords(List<MoBizRecord> records) {
		this.records = records;
	}
	
	/**
	 * ����һ����¼
	 * @param record
	 */
	public void addRecord(MoBizRecord record) {
		records.add(record);
	}
	
	
	
}
