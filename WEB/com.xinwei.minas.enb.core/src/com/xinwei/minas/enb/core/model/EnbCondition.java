/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import com.xinwei.omp.core.model.biz.PagingCondition;

/**
 * eNB查询条件
 * 
 * @author chenjunhua
 * 
 */

public class EnbCondition extends PagingCondition {

	/**
	 * 按enbId排序
	 */
	public static final int SORT_BY_ENB_ID = 1;

	public static final int SORT_BY_ENB_NAME = 2;

	public static final int SORT_BY_STATE = 3;

	public static final int SORT_BY_MANAGE_STATE = 4;

	public static final int SORT_BY_PUBLIC_IP = 5;

	public static final int SORT_BY_PUBLIC_PORT = 6;

	public static final int SORT_BY_SOFTWARE_VERSION = 7;

	public static final int SORT_BY_ALARM_LEVEL = 8;

	public static final int SORT_BY_PRIVATE_IP = 9;

	public static final int SORT_BY_PROTOCOL_VERSION = 10;

	public static final int SORT_BY_MONITOR_STATE = 11;

	private int sortBy;

	private String enbId;

	private String enbName;

	private String publicIp;

	private String softwareVersion;

	private Integer monitorState;

	public int getSortBy() {
		return sortBy;
	}

	/**
	 * 设置排序方式，正数升序，负数降序
	 * 
	 * @param sortBy
	 */
	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public String getEnbId() {
		return enbId;
	}

	/**
	 * 设置16进制形式的enbId字符串
	 * 
	 * @param enbId
	 */
	public void setEnbId(String enbId) {
		this.enbId = enbId;
	}

	public String getEnbName() {
		return enbName;
	}

	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setMonitorState(Integer monitorState) {
		this.monitorState = monitorState;
	}

	public Integer getMonitorState() {
		return monitorState;
	}

}
