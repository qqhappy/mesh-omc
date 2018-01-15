/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;

/**
 * 
 * 类简要描述
 * 
 * @author tiance
 * 
 */

public class UTCondition implements Serializable {
	public static final int ID_TYPE_UID = 1;
	public static final int ID_TYPE_TEL_NO = 2;
	public static final int ID_TYPE_PID = 3;
	public static final int ID_TYPE_ALIAS = 4;
	public static final int ID_TYPE_UT_TYPE = 5;
	public static final int ID_TYPE_UT_STATUS = 6;
	public static final int ID_TYPE_GROUP = 7;
	public static final int ID_TYPE_BTS_ID = 8;
	public static final int ID_TYPE_IP = 9;
	public static final int ID_TYPE_MAC_IP = 10;

	public static final int SORT_BY_NONE = 0;
	public static final int SORT_BY_UID = 1;
	public static final int SORT_BY_TEL_NO = 2;
	public static final int SORT_BY_PID = 3;
	public static final int SORT_BY_ALIAS = 4;
	public static final int SORT_BY_UT_TYPE = 5;
	public static final int SORT_BY_UT_STATUS = 6;
	public static final int SORT_BY_GROUP = 7;

	// 默认页大小
	public static final int DEFAULT_QUERY_COUNT = 20;
	// 默认排序
	public static final int DEFAULT_SORT_BY = 0;
	public static final int DEFAULT_SORT_TYPE = 0;

	// 升序
	public static final int UP = 1;
	// 降序
	public static final int DOWN = -1;

	// 按PID查询
	private String pid;
	// 按电话号码查询
	private String telNo;
	// 按UID查询
	private String uid;
	// 按别名查询
	private String alias;
	// 按终端类型查询
	private Long utType;
	// 按终端状态查询
	private Integer utStatus;
	// 按组名查询
	private String utGroup;
	// 按BID查询
	private Long btsId;
	// 按IP查询
	private String ip;
	// 按MAC地址查询
	private String macIp;
	// 页大小
	private Integer queryCount;
	// 排序
	private Integer sortBy;
	// PID索引
	private Long pidIndex;
	// 分页
	private Integer pageShift;
	// 排序方式:1升序,-1降序
	private Integer sortType;

	public UTCondition() {
		this.queryCount = DEFAULT_QUERY_COUNT;
		this.sortBy = DEFAULT_SORT_BY;
		this.sortType = DEFAULT_SORT_TYPE;
		// pidIndex总是0
		this.pidIndex = 0l;
		this.pageShift = 0;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getUtType() {
		return utType;
	}

	public void setUtType(Long utType) {
		this.utType = utType;
	}

	public Integer getUtStatus() {
		return utStatus;
	}

	public void setUtStatus(Integer utStatus) {
		this.utStatus = utStatus;
	}

	public String getUtGroup() {
		return utGroup;
	}

	public void setUtGroup(String utGroup) {
		this.utGroup = utGroup;
	}

	public Long getBtsId() {
		return btsId;
	}

	public void setBtsId(Long btsId) {
		this.btsId = btsId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMacIp() {
		return macIp;
	}

	public void setMacIp(String macIp) {
		this.macIp = macIp;
	}

	public Integer getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(Integer queryCount) {
		this.queryCount = queryCount;
	}

	public Integer getSortBy() {
		return sortBy;
	}

	public void setSortBy(Integer sortBy) {
		this.sortBy = sortBy;
	}

	public Long getPidIndex() {
		return pidIndex;
	}

	public void setPidIndex(Long pidIndex) {
		this.pidIndex = pidIndex;
	}

	public Integer getPageShift() {
		return pageShift;
	}

	public void setPageShift(Integer pageShift) {
		this.pageShift = pageShift;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

}
