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
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 通用网元配置数据的一行记录
 * 
 * @author chenjunhua
 * 
 */

public class MoBizRecord implements Serializable{
	
	// 字段-值 映射
	private Map<String, String> itemValues = new HashMap();
	
	public static final String PROPERTY_MO_ID = "moId";
	
	public static final String PROPERTY_IDX = "idx";

	/**
	 * 获取MO ID
	 * @return
	 */
	public Long getMoId() {
		Long moId = -1L;
		try {
			moId = Long.parseLong(itemValues.get(PROPERTY_MO_ID));
		} catch (NumberFormatException e) {
		}
		return moId;
	}
	
	
	/**
	 * 获取记录ID
	 * @return
	 */
	public Long getRecordId() {
		Long id = -1L;
		try {
			id = Long.parseLong(itemValues.get(PROPERTY_IDX));
		} catch (NumberFormatException e) {
		}
		return id;
	}
	
	/**
	 * 设置记录ID
	 * @param id
	 */
	public void setRecordId(Long id) {
		if (id != null) {
			itemValues.put(PROPERTY_IDX, String.valueOf(id));
		}
	}
	
	
	/**
	 * 设置moId
	 * @param id
	 */
	public void setMoId(Long moId) {
		if (moId != null) {
			itemValues.put(PROPERTY_MO_ID, String.valueOf(moId));
		}
	}
	
	
	/**
	 * 获取字段数目
	 * 
	 * @return
	 */
	public int numbers() {
		return itemValues.size();
	}

	/**
	 * 判断是否是主键
	 * @param propertyName
	 * @return
	 */
	public boolean isPrimaryKey(String propertyName) {
		return PROPERTY_IDX.endsWith(propertyName);
	}
	
	
	/**
	 * 获取字段名列表
	 * 
	 * @return
	 */
	public String[] getItemNames() {
		return (String[]) itemValues.keySet().toArray(new String[0]);
	}
	
	 
	
	/**
	 * 获取指定字段的值
	 * @param itemName
	 * @return
	 */
	public String getItemValue(String itemName) {
		return itemValues.get(itemName);
	}

	/**
	 * 增加字段值
	 * 
	 * @param itemName
	 * @param value
	 */
	public void addItemValue(String itemName, Object value) {
		itemValues.put(itemName, String.valueOf(value));
	}
	
	/**
	 * 增加字段值
	 * 
	 * @param itemName
	 * @param value
	 */
	public void addItemValue(String itemName, String value) {
		itemValues.put(itemName, value);
	}

	public Map<String, String> getItemValues() {
		return itemValues;
	}

	public void setItemValues(Map<String, String> itemValues) {
		this.itemValues = itemValues;
	}
	
	
}
