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
 * ��ɫģ��
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class Role implements Serializable {

	// ��ɫ���
	private int roleId;

	// ��ɫ����
	private String roleName;

	// ��ɫ����
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
