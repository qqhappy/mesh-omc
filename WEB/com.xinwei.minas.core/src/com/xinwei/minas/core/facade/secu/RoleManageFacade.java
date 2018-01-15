/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.secu;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.secu.Role;

/**
 * 
 * 角色管理门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface RoleManageFacade extends Remote {

	/**
	 * 添加角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void addRole(Role role) throws RemoteException, Exception;

	/**
	 * 删除角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void deleteRole(Role role) throws RemoteException, Exception;

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void modifyRole(Role role) throws RemoteException, Exception;

	/**
	 * 查询所有角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> queryAllRoles() throws RemoteException, Exception;

}
