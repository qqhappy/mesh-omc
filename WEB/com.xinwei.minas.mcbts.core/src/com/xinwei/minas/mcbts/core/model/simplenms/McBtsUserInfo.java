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
 * 基站用户信息
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsUserInfo implements Serializable {

	private Long pid;

	// 用户ID
	private Long uid;
	
	// 电话号码
	private String telNo;
	
	// 优先级
	private Integer priority;

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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
