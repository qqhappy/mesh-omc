/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-6	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��¼�û�����
 * 
 * @author liuzhongyan
 * 
 */

public class LoginUserCache {

	private static final Log log = LogFactory.getLog(LoginUserCache.class);

	// KEY:SESSIONID
	private Map<String, MinasClientFacade> clientFacades = new ConcurrentHashMap<String, MinasClientFacade>();

	private Map<String, LoginUser> userCache = new ConcurrentHashMap<String, LoginUser>();

	private Map<String, ExecutorService> clientThreadPool = new ConcurrentHashMap<String, ExecutorService>();

	private static final LoginUserCache instance = new LoginUserCache();

	private ScheduledExecutorService service;

	// �û���ʱ��ʱ����ֵ(��λ: ��)
	private int overTimeThreshold = 60;

	private LoginUserCache() {
		service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(new CheckUserStatusTask(), 60, 30,
				TimeUnit.SECONDS);
	}

	public static LoginUserCache getInstance() {
		return instance;
	}

	/**
	 * ����
	 * 
	 * @param sessionId
	 */
	public void handshake(String sessionId) throws Exception {
		LoginUser loginUser = (LoginUser) userCache.get(sessionId);
		if (loginUser != null) {
			long nowTime = System.currentTimeMillis();
			loginUser.setLastHandshakeTime(nowTime);
		} else {
			throw new Exception(OmpAppContext.getMessage("session_overdue"));
		}
	}

	/**
	 * ���������û�
	 * 
	 * @param loginUser
	 * @param clientFacade
	 */
	public void addLoginUser(LoginUser loginUser, MinasClientFacade clientFacade)
			throws Exception {
		long nowTime = System.currentTimeMillis();
		loginUser.setLastHandshakeTime(nowTime);
		String sessionId = loginUser.getSessionId();
		Iterator<Entry<String, LoginUser>> it = userCache.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, LoginUser> user = (Map.Entry<String, LoginUser>) it
					.next();
			LoginUser userLogin = (LoginUser) user.getValue();
			if (userLogin != null) {
				if (userLogin.getUsername().equals(loginUser.getUsername())) {
					throw new Exception(OmpAppContext.getMessage(
							"user_has_login",
							new Object[] { userLogin.getUsername() }));
				}
			}

		}
		userCache.put(sessionId, loginUser);
		// �ͻ��˴����clientFacade��Ϊ��ʱ�ŷ��뻺��
		if (clientFacade != null) {
			clientFacades.put(sessionId, clientFacade);
			clientThreadPool
					.put(sessionId, Executors.newSingleThreadExecutor());
		}

	}

	public void updateLoginUser(LoginUser loginUser) {
		// �ӻ������ҵ�Ҫ���µ��û�
		String sessionId = loginUser.getSessionId();
		LoginUser user = userCache.get(sessionId);
		if (user != null) {
			userCache.put(sessionId, loginUser);
		}
	}

	/**
	 * �Ƴ������û�
	 * 
	 * @param sessionId
	 */
	public void removeLoginUser(String sessionId) {
		ExecutorService service = clientThreadPool.remove(sessionId);
		if (service != null) {
			service.shutdown();
		}
		clientFacades.remove(sessionId);
		userCache.remove(sessionId);
	}

	/**
	 * ����û�
	 * 
	 * @param sessionId
	 * @return
	 */
	public boolean checkUser(String sessionId) {
		return userCache.containsKey(sessionId);
	}

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 */
	public List<LoginUser> queryOnlineUser() {
		List<LoginUser> userList = new ArrayList<LoginUser>();
		Iterator<Entry<String, LoginUser>> it = userCache.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, LoginUser> user = (Map.Entry<String, LoginUser>) it
					.next();
			LoginUser loginUser = (LoginUser) user.getValue();
			if (loginUser != null) {
				userList.add(loginUser);
			}

		}
		return userList;
	}

	/**
	 * �������������û���Ϣ
	 * 
	 * @param condition
	 * @param byName
	 *            �����Ʋ��һ�sessionId����
	 * @return
	 */
	public LoginUser queryOnlineUser(String condition, boolean byName) {
		if (byName) {
			List<LoginUser> userList = queryOnlineUser();
			LoginUser currentUser = null;
			for (LoginUser loginUser : userList) {
				if (loginUser.getUsername().equals(condition)) {
					currentUser = loginUser;
				}
			}
			return currentUser;
		} else
			return userCache.get(condition);
	}

	/**
	 * ��¼״̬�޸�
	 * 
	 * @param userList
	 * @return
	 */
	public List<LoginUser> allUserStatus(List<LoginUser> userList) {
		List<LoginUser> onLineUserList = queryOnlineUser();
		for (LoginUser loginUser : onLineUserList) {
			for (LoginUser user : userList) {
				if (user.getUsername().equals(loginUser.getUsername())) {
					user.setOnline(true);
				}
			}
		}

		return userList;
	}

	/**
	 * �ص��ͻ���
	 * 
	 * @param runnable
	 */
	public void callback(final CallbackScript callbackScript) {
		CopyOnWriteArrayList<String> sessionIds = new CopyOnWriteArrayList<String>(
				clientFacades.keySet());
		for (final String sessionId : sessionIds) {
			final MinasClientFacade clientFacade = clientFacades.get(sessionId);
			ExecutorService executorService = clientThreadPool.get(sessionId);
			if (clientFacade != null && executorService != null) {
				// ÿ���ͻ���һ���߳�
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						try {
							callbackScript.execute(clientFacade);
						} catch (Exception e) {
							log.error("failed to callback client, sessionId="
									+ sessionId + ",script=" + callbackScript,
									e);
						}
					}

				});
			}

		}
	}

	/**
	 * ��ʱ��
	 * 
	 * @author liuzhongyan
	 * 
	 */
	class CheckUserStatusTask implements Runnable {

		@Override
		public void run() {
			try {
				long nowTime = System.currentTimeMillis();
				// ����usercache
				Iterator<Entry<String, LoginUser>> it = userCache.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, LoginUser> user = (Map.Entry<String, LoginUser>) it
							.next();
					LoginUser loginUser = (LoginUser) user.getValue();
					if (loginUser != null) {
						long lastHandshake = loginUser.getLastHandshakeTime();
						long time = nowTime - lastHandshake;
						// �ϴ�����ʱ���뵱ǰʱ��ļ������ָ����ֵ�ļ�¼
						boolean isHandshakeOverTime = (time / 1000) > overTimeThreshold;
						// �û���������ʹ�ã����ѹ���
						long thisDate = DateUtils
								.getBriefTimeFromMillisecondTime(nowTime) / 1000000;
						boolean isUserExpire = (loginUser.getIspermanentuser() == 1 && loginUser
								.getValidtime() < thisDate);
						// �û�����ͣʹ��
						boolean isUserPause = (loginUser.getCanuse() == 1);
						if (isHandshakeOverTime || isUserExpire || isUserPause) {
							log.info("remove login user: " + loginUser);
							try {
								removeLoginUser(loginUser.getSessionId());
							} catch (Exception e) {
								log.info("failed to remove login user: "
										+ loginUser, e);
							}
						}
					}

				}
			} catch (Exception e) {
				log.error("Error while checking login user status.", e);
			}
		}
	}

}
