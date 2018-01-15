/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model;

import java.util.List;

/**
 * 
 * 配置数据比较结果
 * 
 * @author fanhaoyu
 * 
 */

public class CompareResult {

	public static int RESULT_EQUAL = 0;

	public static int RESULT_NOT_EQUAL = 1;

	private String tableName;

	private String tableDesc;

	/**
	 * 字段名列表，与emsFields和neFields的顺序一致
	 */
	private List<String> fieldList;

	private List<CompareDetail> dataDetails;

	/**
	 * 比较结果的附加信息
	 */
	private String message;

	// 比较结果，0相同，1不同
	private int result;

	public void setDataDetails(List<CompareDetail> dataDetails) {
		this.dataDetails = dataDetails;
	}

	public List<CompareDetail> getDataDetails() {
		return dataDetails;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getResult() {
		return result;
	}
}
