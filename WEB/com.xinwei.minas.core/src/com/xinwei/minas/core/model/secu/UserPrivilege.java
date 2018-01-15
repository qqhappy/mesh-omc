/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-1	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 用户权限
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class UserPrivilege implements Serializable {

	// 用户名
	private String username;

	// 用户的权限
	private Map<String, List<String>> privilegeMap = new HashMap<String, List<String>>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPrivilegeMap(Map<String, List<String>> privilegeMap) {
		this.privilegeMap = privilegeMap;
	}

	public Map<String, List<String>> getPrivilegeMap() {
		return privilegeMap;
	}

}
