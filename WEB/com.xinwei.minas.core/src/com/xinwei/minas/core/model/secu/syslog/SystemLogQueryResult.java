/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu.syslog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 系统日志查询结果
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SystemLogQueryResult implements Serializable {

	private int totalNum;
	// 本次查询所返回的结果的数量
	private int resultNum;
	// 页数
	private int totalPage;
	// 当前页
	private int currentPage;

	// 查询到的集合
	private List<SystemLog> logList = new ArrayList<SystemLog>();

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<SystemLog> getLogList() {
		return logList;
	}

	public void setLogList(List<SystemLog> logList) {
		this.logList = logList;
	}

	/**
	 * 返回是否有结果
	 * 
	 * @return
	 */
	public boolean hasResult() {
		return resultNum != 0;
	}
}
