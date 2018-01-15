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
 * 基站用户组
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class McBtsUserGroup implements Serializable {

	private Long groupId;
	
	private String groupName;
	
	// 组优先级
	private Integer groupPrior;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long id) {
		this.groupId = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((groupPrior == null) ? 0 : groupPrior.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof McBtsUserGroup))
			return false;
		McBtsUserGroup other = (McBtsUserGroup) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (groupPrior == null) {
			if (other.groupPrior != null)
				return false;
		} else if (!groupPrior.equals(other.groupPrior))
			return false;
		return true;
	}
}