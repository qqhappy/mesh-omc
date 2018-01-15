/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-27	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

/**
 * 
 * 分页条件
 * 
 * @author chenjunhua
 * 
 */

public class PagingCondition implements java.io.Serializable{
	
	// 查询的当前页
	private int currentPage = 1;

	// 每页显示的条目数
	private int numPerPage = 150;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

}
