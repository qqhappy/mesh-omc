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
 * 通用网元业务数据
 * 
 * @author chenjunhua
 * 
 */

public class MoBizData implements Serializable{
	
	// 业务名称
	private String bizName;
	
	// 记录集
	private List<MoBizRecord> records = new LinkedList();

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	/**
	 * 增加实体
	 * @param entity
	 */
	public void addEntity(Object entity) {
		MoBizRecord record = new MoBizRecord();		
		// 利用反射获取getter方法
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
	 * 获取记录集合
	 * @return
	 */
	public List<MoBizRecord> getRecords() {
		return records;
	}

	/**
	 * 设置记录集合
	 * @param records
	 */
	public void setRecords(List<MoBizRecord> records) {
		this.records = records;
	}
	
	/**
	 * 增加一条记录
	 * @param record
	 */
	public void addRecord(MoBizRecord record) {
		records.add(record);
	}
	
	
	
}
