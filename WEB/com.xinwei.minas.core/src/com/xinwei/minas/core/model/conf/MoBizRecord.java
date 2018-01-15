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
 * ͨ����Ԫ�������ݵ�һ�м�¼
 * 
 * @author chenjunhua
 * 
 */

public class MoBizRecord implements Serializable{
	
	// �ֶ�-ֵ ӳ��
	private Map<String, String> itemValues = new HashMap();
	
	public static final String PROPERTY_MO_ID = "moId";
	
	public static final String PROPERTY_IDX = "idx";

	/**
	 * ��ȡMO ID
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
	 * ��ȡ��¼ID
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
	 * ���ü�¼ID
	 * @param id
	 */
	public void setRecordId(Long id) {
		if (id != null) {
			itemValues.put(PROPERTY_IDX, String.valueOf(id));
		}
	}
	
	
	/**
	 * ����moId
	 * @param id
	 */
	public void setMoId(Long moId) {
		if (moId != null) {
			itemValues.put(PROPERTY_MO_ID, String.valueOf(moId));
		}
	}
	
	
	/**
	 * ��ȡ�ֶ���Ŀ
	 * 
	 * @return
	 */
	public int numbers() {
		return itemValues.size();
	}

	/**
	 * �ж��Ƿ�������
	 * @param propertyName
	 * @return
	 */
	public boolean isPrimaryKey(String propertyName) {
		return PROPERTY_IDX.endsWith(propertyName);
	}
	
	
	/**
	 * ��ȡ�ֶ����б�
	 * 
	 * @return
	 */
	public String[] getItemNames() {
		return (String[]) itemValues.keySet().toArray(new String[0]);
	}
	
	 
	
	/**
	 * ��ȡָ���ֶε�ֵ
	 * @param itemName
	 * @return
	 */
	public String getItemValue(String itemName) {
		return itemValues.get(itemName);
	}

	/**
	 * �����ֶ�ֵ
	 * 
	 * @param itemName
	 * @param value
	 */
	public void addItemValue(String itemName, Object value) {
		itemValues.put(itemName, String.valueOf(value));
	}
	
	/**
	 * �����ֶ�ֵ
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
