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
 * 用户模型
 * 
 * @author chenjunhua
 * 
 */

public class User implements Serializable{
	

	// 用户名
	private String username;
	
	// 密码
	private String password;
	
	// 用户描述
	private String desc;
	
	// 归属的角色
	private int roleId;
	
	//是否是永久用户 0-是永久用户 1-有效用户
	private int ispermanentuser;
	//用户有效时间(14位长度yyyyMMddhhmmss)
	private long validtime;

	//是否暂停使用 0-不暂停使用  1-暂停使用
	private int  canuse;

	public int getCanuse() {
		return canuse;
	}

	public void setCanuse(int canuse) {
		this.canuse = canuse;
	}

	public String getDesc() {
		return desc;
	}

	public int getIspermanentuser() {
		return ispermanentuser;
	}

	public void setIspermanentuser(int ispermanentuser) {
		this.ispermanentuser = ispermanentuser;
	}

	public long getValidtime() {
		return validtime;
	}

	public void setValidtime(long validtime) {
		this.validtime = validtime;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getUsername() {
		return username.toLowerCase();
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", desc=" + desc + ", roleId=" + roleId
				+ ", ispermanentuser=" + ispermanentuser + ", validtime="
				+ validtime + ", canuse=" + canuse + "]";
	}
	
	
	
	
}
