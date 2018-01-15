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
 * ��ҳ����
 * 
 * @author chenjunhua
 * 
 */

public class PagingCondition implements java.io.Serializable{
	
	// ��ѯ�ĵ�ǰҳ
	private int currentPage = 1;

	// ÿҳ��ʾ����Ŀ��
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
