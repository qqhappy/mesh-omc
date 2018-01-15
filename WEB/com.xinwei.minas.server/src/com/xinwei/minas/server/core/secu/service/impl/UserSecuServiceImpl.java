/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service.impl;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.RoleType;
import com.xinwei.minas.core.model.secu.User;
import com.xinwei.minas.server.core.secu.dao.UserSecuDao;
import com.xinwei.minas.server.core.secu.service.AuthorityManageService;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.core.secu.service.UserSecuService;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * �û���ȫ����
 * 
 * @author liuzhongyan
 * 
 */

public class UserSecuServiceImpl implements UserSecuService {

	private static Log log = LogFactory.getLog(UserSecuServiceImpl.class);

	public static final String DEFAULT_SESSION_ID = "~!@#$%^&*ABCDEFG1234567";

	public static final String SUPER_ADMIN = "admin";

	private UserSecuDao userSecuDao;

	private AuthorityManageService authorityManageService;

	/**
	 * �����û�
	 * 
	 * @param user
	 */
	public void addUser(User user) throws Exception {
		List<LoginUser> userList = userSecuDao.queryAllUser();
		String username = user.getUsername();
		// ��ѯ�û����Ƿ����
		for (LoginUser existUser : userList) {
			if (username.equals(existUser.getUsername())) {
				throw new Exception(
						OmpAppContext.getMessage("user_already_exist"));
			}
		}
		userSecuDao.addUser(user);
	}

	/**
	 * �޸��û�
	 */
	public void modUser(User user) throws Exception {
		userSecuDao.modUser(user);
		LoginUserCache cache = LoginUserCache.getInstance();
		LoginUser loginUser = cache.queryOnlineUser(user.getUsername(), true);
		// �û������ߣ���ֱ�ӷ��أ����򣬸��»�������Ϣ
		if (loginUser == null)
			return;
		loginUser.setRoleId(user.getRoleId());
		loginUser.setDesc(user.getDesc());
		loginUser.setIspermanentuser(user.getIspermanentuser());
		loginUser.setCanuse(user.getCanuse());
		loginUser.setValidtime(user.getValidtime());
		cache.updateLoginUser(loginUser);
	}

	/**
	 * �޸��û�����
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception {
		userSecuDao.modUserPassword(userName, userPassword);
	}

	/**
	 * ɾ���û�
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userIdList) throws RemoteException,
			Exception {
		// �������ߵ��û�������ɾ��
		checkOnlineUser(userIdList);
		userSecuDao.delUser(userIdList);
	}

	/**
	 * �������ߵ��û�������ɾ��
	 * 
	 * @param userIdList
	 * @throws Exception
	 */
	private void checkOnlineUser(List<String> userIdList) throws Exception {
		List<LoginUser> loginUsers = LoginUserCache.getInstance()
				.queryOnlineUser();
		Set<String> userNameSet = new HashSet<String>();
		for (LoginUser loginUser : loginUsers) {
			userNameSet.add(loginUser.getUsername());
		}
		for (String userName : userIdList) {
			if (userNameSet.contains(userName)) {
				throw new Exception(
						MessageFormat.format(OmpAppContext
								.getMessage("secu_userOnlineCannotDelete"),
								userName));
			}
		}

	}

	@Override
	public LoginUser queryUser(String userName) throws Exception {
		return userSecuDao.queryUser(userName);
	}

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws Exception {
		List<LoginUser> userList = userSecuDao.queryAllUser();
		// ����ɫ����
		Collections.sort(userList, new Comparator<LoginUser>() {
			@Override
			public int compare(LoginUser user1, LoginUser user2) {
				if (user1.getUsername().equals(SUPER_ADMIN))
					return -1;
				if (user2.getUsername().equals(SUPER_ADMIN))
					return 1;

				int result = user1.getRoleId() - user2.getRoleId();
				if (result != 0)
					return result;
				else
					return user1.getUsername().compareTo(user2.getUsername());
			}
		});
		return LoginUserCache.getInstance().allUserStatus(userList);
	}

	/**
	 * ��ѯ���������û�
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws Exception {
		return LoginUserCache.getInstance().queryOnlineUser();
	}

	/**
	 * �û���¼
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser, MinasClientFacade clientFacade)
			throws Exception {
		userSecuDao.userLogin(loginUser);
		// �����ݿ��в����¼�û�����Ϣ
		LoginUser user = userSecuDao.queryUser(loginUser.getUsername());
		user.setLoginIp(loginUser.getLoginIp());
		user.setLoginTime(DateUtils.getStandardTimeFromDate(new Date()));
		user.setOnline(true);
		user.setSessionId(loginUser.getSessionId());
		// �ѵ�¼���û����뻺��
		LoginUserCache.getInstance().addLoginUser(user, clientFacade);
		// ��¼��־
		addLog(user, true);
	}

	private void addLog(LoginUser user, boolean isLogin) throws Exception {
		OperSignature signature = new OperSignature();
		signature.setFacade("com.xinwei.minas.core.facade.secu.UserSecuFacade");
		if (isLogin)
			signature.setMethod("userLogin");
		else
			signature.setMethod("userLogout");
		OperObject operObject = OperObject.createUserOperObject();
		Object[] params = new Object[1];
		params[0] = user;
		try {
			authorityManageService.addLog(user.getSessionId(), signature,
					operObject, params);
		} catch (Exception e) {
			log.warn("add log failed.", e);
		}
	}

	/**
	 * �û��ǳ�
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String sessionId) throws Exception {
		LoginUser user = getCurrentUser(sessionId, false);
		if (user == null)
			return;
		// ��¼��־
		addLog(user, false);
		// �ӵ�¼�û��������Ƴ����û�
		LoginUserCache.getInstance().removeLoginUser(sessionId);
	}

	/**
	 * ����������ȡ��ǰ�û���Ϣ
	 * 
	 * @param condition
	 * @param byName
	 * @return
	 */
	private LoginUser getCurrentUser(String condition, boolean byName) {
		List<LoginUser> userList = LoginUserCache.getInstance()
				.queryOnlineUser();
		LoginUser currentUser = null;
		for (LoginUser loginUser : userList) {
			if (byName) {
				if (loginUser.getUsername().equals(condition))
					currentUser = loginUser;
			} else {
				if (loginUser.getSessionId().equals(condition))
					currentUser = loginUser;
			}
		}
		return currentUser;
	}

	/**
	 * ����û�
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkUser(String sessionId) throws Exception {
		if (DEFAULT_SESSION_ID.equals(sessionId)) {
			return true;
		}
		return LoginUserCache.getInstance().checkUser(sessionId);
	}

	@Override
	public void checkUserPassword(String username, String password)
			throws RemoteException, Exception {
		LoginUser user = new LoginUser();
		user.setUsername(username);
		user.setPassword(password);
		userSecuDao.userLogin(user);
	}

	/**
	 * 
	 * ����
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws Exception {
		if (DEFAULT_SESSION_ID.equals(sessionId)) {
			return;
		}
		LoginUserCache.getInstance().handshake(sessionId);
	}

	@Override
	public void kickUser(String sessionId, String username) throws Exception {
		// �ж��û��Ƿ�����
		LoginUser loginUser = getCurrentUser(sessionId, false);
		if (loginUser == null) {
			throw new Exception(OmpAppContext.getMessage("session_overdue"));
		}
		// �������Լ�����
		if (loginUser.getUsername().equals(username)) {
			throw new Exception(OmpAppContext.getMessage("cannot_kick_youself"));
		}
		LoginUser targetUser = getCurrentUser(username, true);
		// Ŀ���û�������
		if (targetUser == null) {
			throw new Exception(
					OmpAppContext.getMessage("target_user_not_online"));
		}
		// ��������Ա���ܱ�������
		if (username.equals(SUPER_ADMIN)) {
			throw new Exception(
					OmpAppContext.getMessage("super_admin_cannot_be_kicked"));
		}
		// �����û��������������û�
		if (loginUser.getUsername().equals(SUPER_ADMIN)) {
			LoginUserCache.getInstance().removeLoginUser(
					targetUser.getSessionId());
		} else {
			// ����Ա�ܹ��ߵͼ�����û�
			if (loginUser.getRoleId() == RoleType.ADMIN) {
				if (loginUser.getRoleId() < targetUser.getRoleId()) {
					LoginUserCache.getInstance().removeLoginUser(
							targetUser.getSessionId());
				} else {
					throw new Exception(
							OmpAppContext
									.getMessage("cannot_kick_higher_level_user"));
				}
			} else {
				// ֻ�й���Ա�����û�
				throw new Exception(
						OmpAppContext.getMessage("no_privilege_to_kick_user"));
			}
		}
	}

	@Override
	public boolean hasPrivilege(LoginUser loginUser, User operatedUser,
			String actionType) throws Exception {

		// ��ͨ����Ա
		if (loginUser.getRoleId() == RoleType.ADMIN) {
			// ��������Ա
			if (loginUser.getUsername().equals(SUPER_ADMIN)) {
				// ����ɾ�Լ��������޸��Լ�����Ϣ
				if (actionType.equals(ActionTypeDD.DELETE)
						|| actionType.equals(ActionTypeDD.MODIFY))
					if (operatedUser.getUsername().equals(SUPER_ADMIN))
						return false;
				return true;
			}
			// ���ܶ�ͬ��������ɾ��
			if (actionType.equals(ActionTypeDD.DELETE)
					|| actionType.equals(ActionTypeDD.MODIFY)) {
				if (loginUser.getRoleId() == operatedUser.getRoleId())
					return false;
			}
			if (actionType.equals(ActionTypeDD.MODIFY_PWD)) {
				// ͬ����ֻ�ܸ��Լ�������
				if (loginUser.getRoleId() == operatedUser.getRoleId()) {
					if (operatedUser.getUsername().equals(
							loginUser.getUsername()))
						return true;
					return false;
				}
			}
			return true;
		}
		// ���ǹ���Ա
		else if (loginUser.getRoleId() != RoleType.ADMIN) {
			if (actionType.equals(ActionTypeDD.MODIFY_PWD)) {
				// ֻ�ܸ��Լ�������
				if (operatedUser.getUsername().equals(loginUser.getUsername()))
					return true;
			}
			// ֻ�ܲ�
			else {
				if (actionType.equals(ActionTypeDD.QUERY))
					return true;
			}
			return false;
		}
		return false;
	}

	public UserSecuDao getUserSecuDao() {
		return userSecuDao;
	}

	public void setUserSecuDao(UserSecuDao userSecu) {
		this.userSecuDao = userSecu;
	}

	public void setAuthorityManageService(
			AuthorityManageService authorityManageService) {
		this.authorityManageService = authorityManageService;
	}

	public AuthorityManageService getAuthorityManageService() {
		return authorityManageService;
	}
}
