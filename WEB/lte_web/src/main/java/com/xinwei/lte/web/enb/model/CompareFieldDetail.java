/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model;

/**
 * 
 * 字段比较详情
 * 
 * @author fanhaoyu
 * 
 */

public class CompareFieldDetail {

	public static final int FLAG_EQUAL = 0;

	public static final int FLAG_NOT_EQUAL = 1;

	private String fieldName;

	private String emsValue;

	private String neValue;

	private int flag;

	public CompareFieldDetail(String fieldName, String emsValue,
			String neValue, int flag) {
		this.fieldName = fieldName;
		this.emsValue = emsValue;
		this.neValue = neValue;
		this.flag = flag;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getEmsValue() {
		return emsValue;
	}

	public void setEmsValue(String emsValue) {
		this.emsValue = emsValue;
	}

	public String getNeValue() {
		return neValue;
	}

	public void setNeValue(String neValue) {
		this.neValue = neValue;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
