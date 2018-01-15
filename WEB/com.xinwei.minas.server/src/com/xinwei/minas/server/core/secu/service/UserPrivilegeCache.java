/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-9	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.secu.Role;
import com.xinwei.minas.server.core.secu.dao.AuthorityManageDao;
import com.xinwei.minas.server.core.secu.dao.RoleManageDao;

/**
 * 
 * 用户权限缓存
 * 
 * @author fanhaoyu
 * 
 */

public class UserPrivilegeCache {

	private static UserPrivilegeCache instance = new UserPrivilegeCache();

	private static Map<Integer, List<OperAction>> privilegeMap = new ConcurrentHashMap<Integer, List<OperAction>>();

	public static UserPrivilegeCache getInstance() {
		return instance;
	}

	public void initialize(RoleManageDao roleManageDao,
			AuthorityManageDao authorityManageDao) throws Exception {
		List<Role> roleList = roleManageDao.queryAllRoles();
		for (Role role : roleList) {
			List<OperAction> operActionList = authorityManageDao
					.queryAuthority(role.getRoleId());
			privilegeMap.put(role.getRoleId(), operActionList);
		}
	}

	public void add(int roleId, List<OperAction> operActionList) {
		privilegeMap.put(roleId, operActionList);
	}

	public void remove(int roleId) {
		privilegeMap.remove(roleId);
	}

	public List<OperAction> getPrivilege(int roleId) {
		return privilegeMap.get(roleId);
	}

}
