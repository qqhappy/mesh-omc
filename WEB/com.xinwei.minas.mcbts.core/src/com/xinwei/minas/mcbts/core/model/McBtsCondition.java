/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-27	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model;

import com.xinwei.omp.core.model.biz.PagingCondition;

/**
 * 基站列别查询模型
 * 
 * @author liuzhongyan
 * 
 */

public class McBtsCondition extends PagingCondition {

	public static final Long SAG_ALL = -1L;

	// SAG ID: -1代表查询所有基站; -2代表网络模式基站; 0代表单站模式基站
	private Long sagId = -1L;

	private String btsId = "-1";

	private String btsName = "";

	private int sortBy;

	public Long getSagId() {
		return sagId;
	}

	public void setSagId(Long sagId) {
		this.sagId = sagId;
	}

	public String getBtsId() {
		return btsId;
	}

	public void setBtsId(String btsId) {
		this.btsId = btsId;
	}

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

}
