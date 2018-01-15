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
 * ��ɫ��������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface RoleManageFacade extends Remote {

	/**
	 * ��ӽ�ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void addRole(Role role) throws RemoteException, Exception;

	/**
	 * ɾ����ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void deleteRole(Role role) throws RemoteException, Exception;

	/**
	 * �޸Ľ�ɫ
	 * 
	 * @param role
	 * @throws Exception
	 */
	public void modifyRole(Role role) throws RemoteException, Exception;

	/**
	 * ��ѯ���н�ɫ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> queryAllRoles() throws RemoteException, Exception;

}
