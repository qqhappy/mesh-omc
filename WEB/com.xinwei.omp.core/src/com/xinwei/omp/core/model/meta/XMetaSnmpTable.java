/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * SNMP表定义元数据
 * 
 * @author chenjunhua
 * 
 */

public class XMetaSnmpTable {
	
	private String name;
	
	private String table;
	
	private String status;
	
	private String isScalarTable;
	
	private String oid;
	
	private XMetaSnmpField[] typeAttribute = new XMetaSnmpField[0];
	
	private Map<String, XMetaSnmpField> fieldsByName = new LinkedHashMap();

	/**
	 * 返回主键字段
	 * @return
	 */
	public List<XMetaSnmpField> getKeyFields() {
		List<XMetaSnmpField> keyFields = new LinkedList();
		for (XMetaSnmpField field : typeAttribute) {
			if (field.isKey()) {
				keyFields.add(field);
			}
		}
		return keyFields;
	}
	
	
	public XMetaSnmpField getFieldByName(String name) {
		return fieldsByName.get(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsScalarTable() {
		return isScalarTable;
	}

	public void setIsScalarTable(String isScalarTable) {
		this.isScalarTable = isScalarTable;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public XMetaSnmpField[] getTypeAttribute() {
		return typeAttribute;
	}

	public void setTypeAttribute(XMetaSnmpField[] typeAttribute) {
		this.typeAttribute = typeAttribute;
		for (XMetaSnmpField field : typeAttribute) {
			fieldsByName.put(field.getName(), field);
		}
	}
	
	
	
	
}
