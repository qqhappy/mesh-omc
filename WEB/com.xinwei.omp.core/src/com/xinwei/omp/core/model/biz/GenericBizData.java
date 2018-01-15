/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-17	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinwei.omp.core.utils.ReflectUtils;

/**
 * 
 * ͨ��ҵ������ģ��
 * 
 * @author chenjunhua
 * 
 */

public class GenericBizData implements java.io.Serializable, Listable {

	/*
	 * ��������֤�в�Ӧ�ñ��Ƚϵ�����.�б����ʽΪprotocol�е�<bizName + "." + item.name>
	 */
	private static String[] IGNORE_LIST = new String[] {
			"mcbts_chargeParamConfig.CB3000IP", "mcbts_chargeParamConfig.Port",
			"mcbts_chargeParamConfig.RealTimeChargeSwitch",
			"mcbts_trunkConfig.rsv2", "mcbts_trunkConfig.rsv3" };

	// ҵ������
	private String bizName;
	
	//��Ϣ���к�
	private int transactionId;

	// ҵ�������б�
	private Set<String> primaryKeys = new HashSet<String>();

	// ҵ�����Թ�ϣ��Key=������, Value=����ֵ�б�
	private Map<String, List<GenericBizProperty>> propertyMap = new LinkedHashMap();

	public GenericBizData(String bizName) {
		this.bizName = bizName;
	}

	/**
	 * ��ȡ������Ŀ
	 * 
	 * @return
	 */
	public int getPropertyNum() {
		return propertyMap.keySet().size();
	}
	
	
	public Map<String, List<GenericBizProperty>> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public List<GenericBizRecord> getRecords() {
		List<GenericBizRecord> records = new LinkedList();
		Iterator<String> keyItr = propertyMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String propertyName = keyItr.next();
			List<GenericBizProperty> propertyValues = propertyMap
					.get(propertyName);
			int recordNum = propertyValues.size();
			if (records.size() == 0) {
				for (int i = 0; i < recordNum; i++) {
					records.add(new GenericBizRecord());
				}
			}
			int i = 0;
			for (GenericBizProperty property : propertyValues) {
				GenericBizRecord record = records.get(i);
				record.addProperty(property);
				i++;
			}
		}
		return records;
	}

	/**
	 * ����ʵ��
	 * 
	 * @param entity
	 */
	public void addEntity(Object entity) {
		// ���÷����ȡgetter����
		List<Method> methods = ReflectUtils.findGetMethods(entity);
		for (Method method : methods) {
			String methodName = method.getName();
			int from = "get".length();
			String propertyName = methodName.substring(from, from + 1)
					.toLowerCase() + methodName.substring(from + 1);
			try {
				Object propertyValue = method.invoke(entity);
				if (propertyValue != null) {
					GenericBizProperty property = new GenericBizProperty();
					property.setName(propertyName);
					property.setValue(propertyValue);
					this.addProperty(property);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ������ʵ��
	 * 
	 * @param clazz
	 * @return
	 */
	public Object getModel(Object obj) {
		try {
			List<Method> methods = ReflectUtils.findSetMethods(obj.getClass());

			for (Method method : methods) {
				String methodName = method.getName();
				int from = "set".length();
				String propertyName = methodName.substring(from, from + 1)
						.toLowerCase() + methodName.substring(from + 1);

				GenericBizProperty property = this.getProperty(propertyName);
				if (property != null) {
					Object value = property.getValue();

					if (value instanceof String) {
						method.invoke(obj, String.valueOf(value));
					} else if (value instanceof Integer) {
						method.invoke(obj,
								Integer.valueOf(String.valueOf(value)));
					} else if (value instanceof Long) {
						try {
							method.invoke(obj,
									Long.valueOf(String.valueOf(value)));
						} catch (Exception e) {
							method.invoke(obj,
									Integer.valueOf(String.valueOf(value)));
						}
					} else if (value instanceof Double) {
						method.invoke(obj,
								Double.valueOf(String.valueOf(value)));
					}
				}
			}

			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * ���ò����� ʹ�ø÷���ֻ��Ϊһ����������һ��ֵ��
	 * 
	 * @param property
	 *            ����
	 */
	public void addProperty(GenericBizProperty property) {
		String propertyName = property.getName();
		if (!propertyMap.containsKey(propertyName)) {
			propertyMap.put(propertyName, new LinkedList());
		}
		List<GenericBizProperty> values = propertyMap.get(propertyName);
		values.add(property);
	}

	/**
	 * ���ò����� ʹ�ø÷���Ϊһ����������һ��ֵ��
	 * 
	 * @param propertyName
	 *            ��������
	 * @param properties
	 *            �����б�
	 */
	public void addProperty(String propertyName,
			List<GenericBizProperty> properties) {
		if (!propertyMap.containsKey(propertyName)) {
			propertyMap.put(propertyName, new LinkedList());
		}
		List<GenericBizProperty> values = propertyMap.get(propertyName);
		values.addAll(properties);
	}

	/**
	 * ��ȡָ���������ĵ�1����ֵ
	 * 
	 * @param propertyName
	 * @return
	 */
	public GenericBizProperty getProperty(String propertyName) {
		if (propertyMap.containsKey(propertyName)) {
			List<GenericBizProperty> values = propertyMap.get(propertyName);
			if (values.isEmpty()) {
				return null;
			} else {
				return values.get(0);
			}
		} else {
			return null;
		}
	}

	/**
	 * ��ȡָ����������������ֵ
	 * 
	 * @param propertyName
	 * @return
	 */
	public List<GenericBizProperty> getProperties(String propertyName) {
		if (propertyMap.containsKey(propertyName)) {
			List<GenericBizProperty> values = propertyMap.get(propertyName);
			return values;
		} else {
			return new LinkedList();
		}
	}

	/**
	 * �����������б�
	 * 
	 * @return
	 */
	public Set<String> getPropertyNames() {
		return propertyMap.keySet();
	}

	@Override
	public String getBizName() {
		return bizName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bizName == null) ? 0 : bizName.hashCode());
		result = prime * result
				+ ((propertyMap == null) ? 0 : propertyMap.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericBizData other = (GenericBizData) obj;
		if (bizName == null) {
			if (other.bizName != null)
				return false;
		} else if (!bizName.equals(other.bizName))
			return false;
		if (propertyMap == null) {
			if (other.propertyMap != null)
				return false;
		} else {
			for (String key : propertyMap.keySet()) {
				if (key.equals("idx") || key.equals("moId"))
					continue;
				if (ignore(key)) {
					continue;
				}
				List<GenericBizProperty> thisPropertyList = propertyMap
						.get(key);
				List<GenericBizProperty> otherPropertyList = other.propertyMap
						.get(key);
				if (thisPropertyList == null) {
					if (otherPropertyList != null)
						return false;
				} else {
					if (otherPropertyList == null)
						return false;
				}
				for (int i = 0; i < thisPropertyList.size(); i++) {
					String tempThis = String.valueOf(thisPropertyList.get(i)
							.getValue());
					String tempOther = String.valueOf(otherPropertyList.get(i)
							.getValue());

					if (!tempThis.equals(tempOther))
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		for (List<GenericBizProperty> propertyList : propertyMap.values()) {
			for (GenericBizProperty property : propertyList) {
				if (property.getName().equalsIgnoreCase("idx")
						|| property.getName().equalsIgnoreCase("moId")) {
					continue;
				}

				if (ignore(property.getName())) {
					continue;
				}

				allProperties.add(new FieldProperty(0, "listable."
						+ this.bizName + "." + property.getName(), String
						.valueOf(property.getValue())));

			}
		}
		return allProperties;
	}

	public Set<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(Set<String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	// ĳЩ���������,���������ݿ��д洢�����Ժʹӻ�վ��ѯ�õ�������������һ��,����,�����г�ĳЩ���������,�������IGNORE_LIST
	private boolean ignore(String propertyName) {
		for (String toIgnore : IGNORE_LIST) {
			String key = bizName + "." + propertyName;
			if (key.equalsIgnoreCase(toIgnore))
				return true;
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (List<GenericBizProperty> propertyList : propertyMap.values()) {
			for (GenericBizProperty property : propertyList) {
				sb.append(property.getName()).append(": ")
						.append(String.valueOf(property.getValue()))
						.append("\n");
			}
		}
		return sb.toString();
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionId() {
		return transactionId;
	}
}
