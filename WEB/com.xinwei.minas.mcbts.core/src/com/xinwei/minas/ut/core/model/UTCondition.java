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
 * ���Ҫ����
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

	// Ĭ��ҳ��С
	public static final int DEFAULT_QUERY_COUNT = 20;
	// Ĭ������
	public static final int DEFAULT_SORT_BY = 0;
	public static final int DEFAULT_SORT_TYPE = 0;

	// ����
	public static final int UP = 1;
	// ����
	public static final int DOWN = -1;

	// ��PID��ѯ
	private String pid;
	// ���绰�����ѯ
	private String telNo;
	// ��UID��ѯ
	private String uid;
	// ��������ѯ
	private String alias;
	// ���ն����Ͳ�ѯ
	private Long utType;
	// ���ն�״̬��ѯ
	private Integer utStatus;
	// ��������ѯ
	private String utGroup;
	// ��BID��ѯ
	private Long btsId;
	// ��IP��ѯ
	private String ip;
	// ��MAC��ַ��ѯ
	private String macIp;
	// ҳ��С
	private Integer queryCount;
	// ����
	private Integer sortBy;
	// PID����
	private Long pidIndex;
	// ��ҳ
	private Integer pageShift;
	// ����ʽ:1����,-1����
	private Integer sortType;

	public UTCondition() {
		this.queryCount = DEFAULT_QUERY_COUNT;
		this.sortBy = DEFAULT_SORT_BY;
		this.sortType = DEFAULT_SORT_TYPE;
		// pidIndex����0
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
