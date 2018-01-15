/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 查询终端的结果的模型
 * 
 * @author tiance
 * 
 */

public class UTQueryResult implements Serializable {
	// 查询成功
	public static final int SUCCESS = 0x00;
	// EMS无相关权限
	public static final int NO_AUTHORITY = 0x01;
	// SHLR内部错误
	public static final int SHLR_INNER_ERROR = 0x02;

	// 从HLR搜索到的所有符合规则的结果的数量
	private int totalNumInHlr;
	// 本次查询所返回的结果的数量
	private int resultNum;
	// 页数
	private int pageNum;
	// 当前页
	private int curPage;

	// 查询到的终端集合
	private List<UserTerminal> utList = new ArrayList<UserTerminal>();

	public int getTotalNumInHlr() {
		return totalNumInHlr;
	}

	public void setTotalNumInHlr(int totalNumInHlr) {
		this.totalNumInHlr = totalNumInHlr;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List<UserTerminal> getUtList() {
		return utList;
	}

	public void setUtList(List<UserTerminal> utList) {
		this.utList = utList;
	}

	public void addUt(UserTerminal ut) {
		this.utList.add(ut);
	}

	public void addUtList(List<UserTerminal> utList) {
		this.utList.addAll(utList);
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
