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
 * 用户安全管理
 * 
 * @author liuzhongyan
 * 
 */

public interface UserSecuDao {
	/**
	 * 新增用户
	 * 
	 * @param user
	 */
	public void addUser(User user) throws Exception;

	/**
	 * 修改用户
	 */
	public void modUser(User user) throws Exception;

	/**
	 * 修改用户密码
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception;

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userIdList) throws Exception;

	/**
	 * 查询单个用户
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public LoginUser queryUser(String userName) throws Exception;

	/**
	 * 查询所有用户
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws Exception;

	/**
	 * 查询所有在线用户
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws Exception;


	/**
	 * 用户登录
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser)throws  Exception;
	/**
	 * 用户登出
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String userName) throws  Exception;
}
