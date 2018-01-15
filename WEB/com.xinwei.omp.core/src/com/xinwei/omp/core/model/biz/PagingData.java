/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-27	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * ��ҳ����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class PagingData<T extends Serializable> implements Serializable {
	
	/**
	 * ����Ŀ
	 */
	private long totalNum;
	
	/**
	 * ÿҳ��ʾ����Ŀ��
	 */
	private int numPerPage = 25;
	
	/**
	 * ��ҳ��
	 */
	private int totalPages;
	
	/**
	 * ��ǰҳ��
	 */
	private int currentPage = 1;
	
	/**
	 * ��ǰҳ����б�
	 */
	private List<T> results = new LinkedList();
	
	public long getTotalNum() {
		return totalNum;
	}
	
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
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
	
	public List<T> getResults() {
		return results;
	}
	
	public void setResults(List<T> results) {
		this.results = results;
	}
	
	public void addResult(T result) {
		results.add(result);
	}
	
	public int calcTotalPageNum() {
		if (0 == totalNum % numPerPage) {
			return (int) (totalNum / numPerPage);
		}
		return (int) ((totalNum / numPerPage) + 1);
	}
	
	public static void main(String[] args) {
		int totalNum = 40;
		int numPerPage = 10;
		int result = totalNum % 10;
		System.out.println(result);
	}
}
