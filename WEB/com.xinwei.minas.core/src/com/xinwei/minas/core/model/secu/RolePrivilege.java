/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-10-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 地区管理员角色权限
 * 
 * @author chenjunhua
 * 
 */

public class RolePrivilege implements Serializable{
	
	// 角色编号
	private long roleId;

	// 角色管理的地区列表(按顺序排列)
	private List<Long> regionIds = new LinkedList();

	public List<Long> getRegionIds() {
		return regionIds;
	}

	public void setRegionIds(List<Long> regionIds) {
		this.regionIds = regionIds;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	
	
	

}
