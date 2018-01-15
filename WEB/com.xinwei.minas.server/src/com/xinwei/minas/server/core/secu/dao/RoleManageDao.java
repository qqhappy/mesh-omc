/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao;

import java.util.List;

import com.xinwei.minas.core.model.secu.Role;

/**
 * 
 * 角色管理dao接口
 * 
 * @author fanhaoyu
 * 
 */

public interface RoleManageDao {

	/**
	 * 添加角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void addRole(Role role) throws Exception;

	/**
	 * 删除角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void deleteRole(Role role) throws Exception;

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void modifyRole(Role role) throws Exception;

	/**
	 * 查询所有角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> queryAllRoles() throws Exception;

}
