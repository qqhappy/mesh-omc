/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;

/**
 * 用户安全管理
 * 
 * @author liuzhongyan
 * 
 */

public interface UserSecuService {
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
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser, MinasClientFacade clientFacade)
			throws Exception;

	/**
	 * 用户登出
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String sessionId) throws Exception;

	/**
	 * 检查用户
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkUser(String sessionId) throws Exception;

	/**
	 * 验证用户密码
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void checkUserPassword(String username, String password)
			throws Exception;

	/**
	 * 
	 * 握手
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws Exception;

	/**
	 * 判断当前登录用户对被操作用户是否有操作权限
	 * <p>
	 * 权限规则：
	 * </p>
	 * <p>
	 * 超级管理员对所有用户具有增删改查权限，上级用户对下级用户有增删改查权限，同级别用户之间只有查看权限
	 * </p>
	 * <p>
	 * 管理员有增删改权限，操作员无增删用户权限，只能修改个人信息；用户不可删除自己；
	 * </p>
	 * 
	 * @param loginUser
	 *            当前登录用户
	 * @param operatedUser
	 *            被操作用户
	 * @param actionType
	 *            操作类型
	 * @return 是否有权限
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean hasPrivilege(LoginUser loginUser, User operatedUser,
			String actionType) throws Exception;

	/**
	 * 会话ID为sessionId的用户强制用户名为username的用户下线
	 * 
	 * @param sessionId
	 * @param username
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void kickUser(String sessionId, String username) throws Exception;
}
