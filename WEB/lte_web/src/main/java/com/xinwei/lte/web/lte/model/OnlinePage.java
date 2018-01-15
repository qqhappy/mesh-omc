/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-02-23	| zhangjiqing 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.model;

import java.io.Serializable;
/**
 * 获取资源号码page对象
 * @author zhangjiqing
 * 20130228
 */
public class OnlinePage implements Serializable{
	
	private static final long serialVersionUID = -50683847555966438L;

	/**
	 * 数据总和条数
	 */
	private int totalObjects;
	
	/**
	 * 页面所含数目 默认为10条
	 */
	private int pageSize = 10;
	
	/**
	 * 页面数目
	 */
	private int totalPages;
	
	/**
	 * 当前页面序号
	 */
	private int currentPageNum = 1;
	
	/**
	 * 是否有下一页
	 */
	private boolean next = true;
	
	/**
	 * 是否有上一页
	 */
	private boolean previse = true;
	/**
	 * 是否第一次访问
	 */
	private boolean isFirstAccess = true;
	
	/**
	 * 页面偏移量,用于解决用户集中访问同一页面问题
	 */
	private int pageOffset;
	public OnlinePage() {
		
	}
    public OnlinePage(int pageSize) {
    	this.setPageSize(pageSize);
		
	}
	public int getPageSize()
	{
		return pageSize;
	}
	
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	
	public int getCurrentPageNum()
	{
		return currentPageNum;
	}
	
	public void setCurrentPageNum(int currentPageNum)
	{
		this.currentPageNum = currentPageNum;
	}
	
	public int getTotalObjects()
	{
		return totalObjects;
	}
	
	public void setTotalObjects(int totalObjects)
	{
		this.totalObjects = totalObjects;
	}
	
	public int getTotalPages()
	{
		return totalPages;
	}
	
	public void setTotalPages(int totalPages)
	{
		this.totalPages = totalPages;
	}
	
	public boolean isNext()
	{
		if (currentPageNum == getTotalPages())
		{
			return false;
		}
		return true;
	}
	
	public void setNext(boolean next)
	{
		this.next = next;
	}
	
	public boolean isPrevise()
	{
		if (currentPageNum == 1)
		{
			return false;
		}
		return true;
	}
	
	public void setPrevise(boolean previse)
	{
		this.previse = previse;
	}
	public boolean isFirstAccess() {
		return isFirstAccess;
	}
	public void setFirstAccess(boolean isFirstAccess) {
		this.isFirstAccess = isFirstAccess;
	}
	public int getPageOffset() {
		return pageOffset;
	}
	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}
}
