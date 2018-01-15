/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 类简要描述
 * <p>
 * 类详细描述:终端批量查询的分页模板
 * </p>
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeQueryModel implements Serializable {
	// 默认当前页
	private Integer DEFAULT_CUR_PAGE = 1;
	// 默认页面大小
	private Integer DEFAULT_PAGE_SIZE = 15;
	// 当前页
	private Integer curPage;
	// 页面大小
	private Integer pageSize;
	// 页面总数量
	private Integer pageNo;
	// 总记录数
	private Integer totalCount;
	// 查询批量结果的标志 1----查询批量升级成功记录  -1----查询批量升级失败记录  0----查询升级进度；
	private Integer successFlag;
	// 查询的数据集合
	private List<UTBacthUpgradeResult> utResults;
	// hibernate中分页每页第一条记录的起始条数；
	private Integer queryFirstResults;

	public UTBatchUpgradeQueryModel() {
		this.curPage = DEFAULT_CUR_PAGE;
		this.pageSize = DEFAULT_PAGE_SIZE;

	}

	/**
	 * 初始化总页数的方法；
	 * 
	 * @param totalCount
	 */
	public void InitPageNo(Integer totalCount) {
		this.totalCount = totalCount;
		this.pageNo = (totalCount - 1) / pageSize + 1;
	}

	/**
	 * 初始化分页查询第一条记录的方法；
	 * 
	 * @return
	 */
	public void initQueryFirstResult() {
		if (pageNo != null && pageNo != 0) {
			this.queryFirstResults = (curPage - 1) * pageSize;
		} else {
			this.queryFirstResults = 0;
		}
	}

	public Integer getQueryFirstResults() {
		return queryFirstResults;
	}

	public void setQueryFirstResults(Integer queryFirstResults) {
		this.queryFirstResults = queryFirstResults;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<UTBacthUpgradeResult> getUtResults() {
		return utResults;
	}

	public void setUtResults(List<UTBacthUpgradeResult> utResults) {
		this.utResults = utResults;
	}

	public Integer getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(Integer successFlag) {
		this.successFlag = successFlag;
	}
	
}
