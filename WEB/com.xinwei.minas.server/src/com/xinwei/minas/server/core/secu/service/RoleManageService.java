/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.util.List;

import com.xinwei.minas.core.model.secu.Role;

/**
 * 
 * ��ɫ�������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface RoleManageService {

	/**
	 * ��ӽ�ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void addRole(Role role) throws Exception;

	/**
	 * ɾ����ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void deleteRole(Role role) throws Exception;

	/**
	 * �޸Ľ�ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void modifyRole(Role role) throws Exception;

	/**
	 * ��ѯ���н�ɫ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> queryAllRoles() throws Exception;

}
