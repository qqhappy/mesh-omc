/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.simplenms;

import java.io.Serializable;

/**
 * 
 * 基站用户全部信息整合
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsUserAllInfo implements Serializable {

	private Long pid;
	// 用户ID
	private Long uid;
	// 电话号码
	private String telNo;
	
	private String mac;
	
	private String ip;
	// 优先级
	private Integer priority;
	// 用户所在组的ID
	private Long groupId;
	
	private String groupName;
	// 组优先级
	private Integer groupPrior;
	// 组内优先级
	private Integer priorInGroup;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String name) {
		this.groupName = name;
	}

	public Integer getGroupPrior() {
		return groupPrior;
	}

	public void setGroupPrior(Integer groupPrior) {
		this.groupPrior = groupPrior;
	}

	public Integer getPriorInGroup() {
		return priorInGroup;
	}

	public void setPriorInGroup(Integer priorInGroup) {
		this.priorInGroup = priorInGroup;
	}

}
