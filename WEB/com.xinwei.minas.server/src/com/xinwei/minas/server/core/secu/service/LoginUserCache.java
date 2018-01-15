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
 * 登录用户缓存
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

	// 用户超时的时间阈值(单位: 秒)
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
	 * 握手
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
	 * 新增在线用户
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
		// 客户端传入的clientFacade不为空时才放入缓存
		if (clientFacade != null) {
			clientFacades.put(sessionId, clientFacade);
			clientThreadPool
					.put(sessionId, Executors.newSingleThreadExecutor());
		}

	}

	public void updateLoginUser(LoginUser loginUser) {
		// 从缓存中找到要更新的用户
		String sessionId = loginUser.getSessionId();
		LoginUser user = userCache.get(sessionId);
		if (user != null) {
			userCache.put(sessionId, loginUser);
		}
	}

	/**
	 * 移除在线用户
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
	 * 检查用户
	 * 
	 * @param sessionId
	 * @return
	 */
	public boolean checkUser(String sessionId) {
		return userCache.containsKey(sessionId);
	}

	/**
	 * 查询在线用户
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
	 * 根据条件查找用户信息
	 * 
	 * @param condition
	 * @param byName
	 *            按名称查找或按sessionId查找
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
	 * 登录状态修改
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
	 * 回调客户端
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
				// 每个客户端一个线程
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
	 * 定时器
	 * 
	 * @author liuzhongyan
	 * 
	 */
	class CheckUserStatusTask implements Runnable {

		@Override
		public void run() {
			try {
				long nowTime = System.currentTimeMillis();
				// 遍历usercache
				Iterator<Entry<String, LoginUser>> it = userCache.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, LoginUser> user = (Map.Entry<String, LoginUser>) it
							.next();
					LoginUser loginUser = (LoginUser) user.getValue();
					if (loginUser != null) {
						long lastHandshake = loginUser.getLastHandshakeTime();
						long time = nowTime - lastHandshake;
						// 上次握手时间与当前时间的间隔大于指定阈值的记录
						boolean isHandshakeOverTime = (time / 1000) > overTimeThreshold;
						// 用户不是永久使用，且已过期
						long thisDate = DateUtils
								.getBriefTimeFromMillisecondTime(nowTime) / 1000000;
						boolean isUserExpire = (loginUser.getIspermanentuser() == 1 && loginUser
								.getValidtime() < thisDate);
						// 用户已暂停使用
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
