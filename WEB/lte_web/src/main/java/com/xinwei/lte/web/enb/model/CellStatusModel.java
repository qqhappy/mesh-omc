/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-30	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model;

/**
 * 
 * 小区状态模型
 * 
 * @author fanhaoyu
 * 
 */

public class CellStatusModel {

	private int cellId;

	private String cellName;

	private int status;

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
