/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.model;

/**
 * 
 * 分表策略模型
 * 
 * @author fanhaoyu
 * 
 */

public class TableStrategyModel {

	private String targetDataBase;

	private String targetTable;

	public TableStrategyModel(String targetDataBase, String targetTable) {
		this.targetDataBase = targetDataBase;
		this.targetTable = targetTable;
	}

	public void setTargetDataBase(String targetDataBase) {
		this.targetDataBase = targetDataBase;
	}

	public String getTargetDataBase() {
		return targetDataBase;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getTargetTable() {
		return targetTable;
	}

}
