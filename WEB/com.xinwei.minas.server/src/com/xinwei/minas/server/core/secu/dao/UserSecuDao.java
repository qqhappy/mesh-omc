/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;

/**
 * �û���ȫ����
 * 
 * @author liuzhongyan
 * 
 */

public interface UserSecuDao {
	/**
	 * �����û�
	 * 
	 * @param user
	 */
	public void addUser(User user) throws Exception;

	/**
	 * �޸��û�
	 */
	public void modUser(User user) throws Exception;

	/**
	 * �޸��û�����
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception;

	/**
	 * ɾ���û�
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userIdList) throws Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public LoginUser queryUser(String userName) throws Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws Exception;

	/**
	 * ��ѯ���������û�
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws Exception;


	/**
	 * �û���¼
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser)throws  Exception;
	/**
	 * �û��ǳ�
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String userName) throws  Exception;
}
