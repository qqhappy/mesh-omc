/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu.syslog;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 日志查询条件
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class LogQueryCondition implements Serializable {

	// 默认页大小
	public static final int DEFAULT_PAGE_SIZE = 30;
	// 按用户名
	private String username;
	// 按操作类型
	private String operType;
	// 按操作方式
	private String actionType;
	// 按操作对象类别
	private String operObjType;
	// 按操作对象ID
	private String objectId;
	// 开始时间
	private Date startTime;
	// 结束之间
	private Date endTime;
	// 分页大小
	private int pageSize = DEFAULT_PAGE_SIZE;
	// 当前第几页
	private int currentPage = 1;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperObjType() {
		return operObjType;
	}

	public void setOperObjType(String operObjType) {
		this.operObjType = operObjType;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "LogQueryCondition [username=" + username + ", operType="
				+ operType + ", actionType=" + actionType + ", operObjType="
				+ operObjType + ", objectId=" + objectId + ", startTime="
				+ startTime + ", endTime=" + endTime + ", pageSize=" + pageSize
				+ ", currentPage=" + currentPage + "]";
	}

	public int getCurrentPage() {
		return currentPage;
	}

}
