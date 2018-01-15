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

/**
 * 
 * 角色模型
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class Role implements Serializable {

	// 角色编号
	private int roleId;

	// 角色名称
	private String roleName;

	// 角色描述
	private String roleDesc;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleDesc=" + roleDesc + "]";
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

}
