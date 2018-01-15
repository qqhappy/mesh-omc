/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

/**
 * 
 * SNMP表字段元数据定义
 * 
 * @author chenjunhua
 * 
 */

public class XMetaSnmpField {

	private String name;

	private String visualKey;

	private String KeyIndex;

	private String isRowStatus;

	private String mandatory;
	
	private String oid;
	
	private String dataType;
	
	private String rootDataType;
	
	private String minLength;

	private String maxLength;
	
	private String access;

	private String savetodb;
	
	private String column;
	
	private String status;

	private String isAutoGenerateValue;

	private String EnumNumber;

	/**
	 * 判断是否是主键字段
	 * @return
	 */
	public boolean isKey() {
		return visualKey != null && "Y".equals(visualKey);
	}
	
	/**
	 * 判断是否是字符串格式
	 * @return
	 */
	public boolean isDisplayString() {
		return "DisplayString".equals(dataType);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisualKey() {
		return visualKey;
	}

	public void setVisualKey(String visualKey) {
		this.visualKey = visualKey;
	}

	public String getKeyIndex() {
		return KeyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		KeyIndex = keyIndex;
	}

	public String getIsRowStatus() {
		return isRowStatus;
	}

	public void setIsRowStatus(String isRowStatus) {
		this.isRowStatus = isRowStatus;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getRootDataType() {
		return rootDataType;
	}

	public void setRootDataType(String rootDataType) {
		this.rootDataType = rootDataType;
	}

	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getSavetodb() {
		return savetodb;
	}

	public void setSavetodb(String savetodb) {
		this.savetodb = savetodb;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsAutoGenerateValue() {
		return isAutoGenerateValue;
	}

	public void setIsAutoGenerateValue(String isAutoGenerateValue) {
		this.isAutoGenerateValue = isAutoGenerateValue;
	}

	public String getEnumNumber() {
		return EnumNumber;
	}

	public void setEnumNumber(String enumNumber) {
		EnumNumber = enumNumber;
	}
	
	
	
}
