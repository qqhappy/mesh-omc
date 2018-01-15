/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaSnmpField;
import com.xinwei.omp.core.model.meta.XMetaSnmpTable;

/**
 * 
 * ҵ����һ�м�¼ģ��
 * 
 * @author chenjunhua
 * 
 */

public class XBizRecord implements Serializable, Cloneable {

	// ��¼��ϣ�� (Key-����, Value-��ֵ)
	private Map<String, XBizField> fieldMap = new LinkedHashMap<String, XBizField>();

	private Long timestamp;

	public XBizRecord() {
	}

	/**
	 * ��ȡ������¼
	 * 
	 * @param bizMeta
	 * @return
	 */
	public XBizRecord getKeyRecord(XList bizMeta) {
		XBizRecord keyRecord = new XBizRecord();
		List<String> indexList = bizMeta.getIndexList();
		for (String index : indexList) {
			XBizField field = fieldMap.get(index);
			keyRecord.addField(field.clone());
		}
		return keyRecord;
	}

	/**
	 * ����һ���ֶ�
	 * 
	 * @param bizField
	 */
	public void addField(XBizField bizField) {
		fieldMap.put(bizField.getName(), bizField);
	}

	/**
	 * ��ȡInt�����ֶ�ֵ
	 * @param fieldName
	 * @return
	 */
	public int getIntValue(String fieldName) {
		return Integer.valueOf(getFieldBy(fieldName).getValue());
	}

	/**
	 * ��ȡLong�����ֶ�ֵ
	 * @param fieldName
	 * @return
	 */
	public long getLongValue(String fieldName) {
		return Long.valueOf(getFieldBy(fieldName).getValue());
	}

	/**
	 * ��ȡString�����ֶ�ֵ
	 * @param fieldName
	 * @return
	 */
	public String getStringValue(String fieldName) {
		return getFieldBy(fieldName).getValue();
	}

	/**
	 * ��ȡDouble�����ֶ�ֵ
	 * @param fieldName
	 * @return
	 */
	public double getDoubleValue(String fieldName) {
		return Double.valueOf(getFieldBy(fieldName).getValue());
	}
	
	/**
	 * ��ȡָ���ֶε���ֵ
	 * 
	 * @param fieldName
	 * @return
	 */
	public XBizField getFieldBy(String fieldName) {
		return fieldMap.get(fieldName);
	}

	public Map<String, XBizField> getFieldMap() {
		return fieldMap;
	}

	// public void setFieldMap(Map<String, XBizField> fieldMap) {
	// this.fieldMap = fieldMap;
	// }

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void update(XBizRecord other) {
		Map<String, XBizField> otherFieldMap = other.getFieldMap();
		Iterator<String> keyItr = otherFieldMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			if (fieldMap.containsKey(key)) {
				XBizField otherField = otherFieldMap.get(key);
				fieldMap.put(key, otherField);
			}
		}
	}

	/**
	 * �Ƿ��������
	 * 
	 * @param condition
	 * @return
	 */
	public boolean match(XBizRecord condition, XList bizMeta) {
		if (condition == null) {
			return true;
		}
		Map<String, XBizField> conditionMap = condition.getFieldMap();
		Iterator<String> keyItr = conditionMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String fieldName = keyItr.next();
			XList fieldMeta = bizMeta.getFieldMeta(fieldName);
			XBizField conditonFiled = conditionMap.get(fieldName);
			XBizField thisField = fieldMap.get(fieldName);
			if (conditonFiled == null) {
				continue;
			}
			//
			Object conditionFieldValue = conditonFiled.getValue();
			if (conditionFieldValue == null) {
				continue;
			}
			Object thisFieldValue = thisField.getValue();
			//
			if (fieldMeta.isDisplayString()) {
				// �ַ����ֶ�ģ����ѯ
				String conditionValue = (String) conditionFieldValue;
				String thisValue = (String) thisFieldValue;
				if (thisValue.indexOf(conditionValue) < 0) {
					return false;
				}
			} else {
				// �����ֶξ�ȷ��ѯ
				Long conditionValue = Long.parseLong(String
						.valueOf(conditionFieldValue));
				Long thisValue = Long.parseLong(String.valueOf(thisFieldValue));
				if (conditionValue.longValue() != thisValue.longValue()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �Ƿ��������
	 * 
	 * @param condition
	 * @return
	 */
	public boolean match(XBizRecord condition, XMetaSnmpTable metaSnmpTable) {
		if (condition == null) {
			return true;
		}
		Map<String, XBizField> conditionMap = condition.getFieldMap();
		Iterator<String> keyItr = conditionMap.keySet().iterator();
		while (keyItr.hasNext()) {
			String fieldName = keyItr.next();
			XMetaSnmpField metaSnmpField = metaSnmpTable
					.getFieldByName(fieldName);
			XBizField conditonFiled = conditionMap.get(fieldName);
			XBizField thisField = fieldMap.get(fieldName);
			//
			Object conditionFieldValue = conditonFiled.getValue();
			Object thisFieldValue = thisField.getValue();
			//
			if (metaSnmpField.isDisplayString()) {
				// �ַ����ֶ�ģ����ѯ
				String conditionValue = (String) conditionFieldValue;
				String thisValue = (String) thisFieldValue;
				if (thisValue.indexOf(conditionValue) < 0) {
					return false;
				}
			} else if (!conditionFieldValue.equals(thisFieldValue)) {
				// �����ֶξ�ȷ��ѯ
				return false;
			}
		}
		return true;
	}

	public XBizRecord clone() {
		XBizRecord record = new XBizRecord();
		Collection<XBizField> fields = fieldMap.values();
		for (XBizField field : fields) {
			record.addField(field.clone());
		}
		return record;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldMap == null) ? 0 : fieldMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XBizRecord other = (XBizRecord) obj;
		if (fieldMap == null) {
			if (other.fieldMap != null)
				return false;
		} else if (!fieldMap.equals(other.fieldMap))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "XBizRecord [fieldMap=" + fieldMap + "]";
	}

}
