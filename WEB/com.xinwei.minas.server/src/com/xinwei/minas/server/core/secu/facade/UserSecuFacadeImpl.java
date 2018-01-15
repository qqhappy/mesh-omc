/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.facade.secu.UserSecuFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;
import com.xinwei.minas.server.core.secu.service.UserSecuService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 
 * 用户安全管理实现
 * 
 * @author liuzhongyan
 * 
 */

@SuppressWarnings("serial")
public class UserSecuFacadeImpl extends UnicastRemoteObject implements
		UserSecuFacade {
	protected UserSecuFacadeImpl() throws RemoteException {
		super();
	}

	private UserSecuService getService() {
		return AppContext.getCtx().getBean(UserSecuService.class);
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 */
	public void addUser(User user) throws RemoteException, Exception {
		getService().addUser(user);
	}

	/**
	 * 修改用户
	 */
	public void modUser(User user) throws RemoteException, Exception {
		getService().modUser(user);
	}

	/**
	 * 修改用户密码
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception {
		getService().modUserPassword(userName, userPassword);
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userIdList) throws RemoteException,
			Exception {
		getService().delUser(userIdList);
	}

	@Override
	public LoginUser queryUser(String userName) throws RemoteException,
			Exception {
		return getService().queryUser(userName);
	}

	/**
	 * 查询所有用户
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws RemoteException, Exception {
		return getService().queryAllUser();
	}

	/**
	 * 查询所有在线用户
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws RemoteException,
			Exception {
		return getService().queryAllUserByOnline();
	}

	/**
	 * 用户登录
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser, MinasClientFacade clientFacade)
			throws RemoteException, Exception {
		// 用户登录时间以服务器的时间为准
		Date date = new Date();
		long standardTime = DateUtils.getStandardTimeFromDate(date);
		loginUser.setLoginTime(standardTime);
		//
		getService().userLogin(loginUser, clientFacade);
	}

	/**
	 * 用户登出
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String sessionId) throws RemoteException, Exception {
		getService().userLogout(sessionId);
	}

	/**
	 * 检查用户
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkUser(String sessionId) throws RemoteException,
			Exception {
		return getService().checkUser(sessionId);
	}

	@Override
	public void checkUserPassword(String username, String password)
			throws RemoteException, Exception {
		getService().checkUserPassword(username, password);
	}

	/**
	 * 
	 * 握手
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws RemoteException, Exception {
		getService().handshake(sessionId);
	}

	@Override
	public boolean hasPrivilege(LoginUser loginUser, User operatedUser,
			String actionType) throws RemoteException, Exception {
		return getService().hasPrivilege(loginUser, operatedUser, actionType);
	}

	@Override
	public void kickUser(String sessionId, String username)
			throws RemoteException, Exception {
		getService().kickUser(sessionId, username);
	}

}
