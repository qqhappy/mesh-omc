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
 * �û�ģ��
 * 
 * @author chenjunhua
 * 
 */

public class User implements Serializable{
	

	// �û���
	private String username;
	
	// ����
	private String password;
	
	// �û�����
	private String desc;
	
	// �����Ľ�ɫ
	private int roleId;
	
	//�Ƿ��������û� 0-�������û� 1-��Ч�û�
	private int ispermanentuser;
	//�û���Чʱ��(14λ����yyyyMMddhhmmss)
	private long validtime;

	//�Ƿ���ͣʹ�� 0-����ͣʹ��  1-��ͣʹ��
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
