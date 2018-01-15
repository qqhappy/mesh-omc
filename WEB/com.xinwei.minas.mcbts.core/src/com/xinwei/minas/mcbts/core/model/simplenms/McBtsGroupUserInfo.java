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
 * 组内基站用户信息
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsGroupUserInfo implements Serializable {

	// 用户所在组的ID
	private Long groupId;
	
	// 用户ID
	private Long uid;
	
	// 组内优先级
	private Integer priorInGroup;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getPriorInGroup() {
		return priorInGroup;
	}

	public void setPriorInGroup(Integer priorInGroup) {
		this.priorInGroup = priorInGroup;
	}
}