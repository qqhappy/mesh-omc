/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service.impl;

import java.util.List;

import com.xinwei.minas.core.model.secu.Role;
import com.xinwei.minas.server.core.secu.dao.RoleManageDao;
import com.xinwei.minas.server.core.secu.service.RoleManageService;

/**
 * 
 * 角色管理服务实现
 * 
 * @author fanhaoyu
 * 
 */

public class RoleManageServiceImpl implements RoleManageService {

	private RoleManageDao roleManageDao;

	@Override
	public void addRole(Role role) throws Exception {
		roleManageDao.addRole(role);
	}

	@Override
	public void deleteRole(Role role) throws Exception {
		roleManageDao.deleteRole(role);
	}

	@Override
	public void modifyRole(Role role) throws Exception {
		roleManageDao.modifyRole(role);
	}

	@Override
	public List<Role> queryAllRoles() throws Exception {
		return roleManageDao.queryAllRoles();
	}

	public void setRoleManageDao(RoleManageDao roleManageDao) {
		this.roleManageDao = roleManageDao;
	}

}
