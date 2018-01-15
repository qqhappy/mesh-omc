/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-8	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.enb.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpSession;

import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * HttpSession注册表
 * 
 * @author chenjunhua
 *  
 */
public class HttpSessionRegistry {

	private static final HttpSessionRegistry instance = new HttpSessionRegistry();

	/**
	 * 登录用户Map<webSessionId, loginUser>
	 */
	private Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

	private HttpSessionRegistry() {

	}

	public static HttpSessionRegistry getInstance() {
		return instance;
	}

	/**
	 * 增加HttpSession
	 * @param session
	 * @return
	 */
	public HttpSession add(HttpSession session) {
		return sessions.put(session.getId(), session);
	}

	/**
	 * 删除HttpSession
	 * @param sessionId
	 * @return
	 */
	public HttpSession remove(String sessionId) {
		return sessions.remove(sessionId);
	}

	/**
	 * 获取所有的登录用户
	 * @return
	 */
	public List<LoginUser> getAllLoginUser() {
		List<LoginUser> users = new LinkedList<LoginUser>();		
		List<HttpSession> sessionList = new CopyOnWriteArrayList<HttpSession>(sessions.values());
		for (HttpSession session : sessionList) {
			LoginUser user = this.getLoginUser(session);
			if (user != null) {
				users.add(user);
			}
		}
		return users;
	}
	
	/**
	 * 根据用户名查找HttpSession
	 * @param username 用户名
	 * @return
	 */
	public HttpSession findSessionByUsername(String username) {
		List<HttpSession> sessionList = new CopyOnWriteArrayList<HttpSession>(sessions.values());
		for (HttpSession session : sessionList) {
			LoginUser user = this.getLoginUser(session);
			if (user != null && user.getUsername().equals(username)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * 获取session内保存的LoginUser
	 * @param session
	 * @return
	 */
	public LoginUser getLoginUser(HttpSession session) {
		LoginUser loginUser = (LoginUser) session
				.getAttribute(WebConstants.KEY_LOGIN_USER_OBJECT);
		return loginUser;
	}
	
	/**
	 * 获取当前session的数量
	 * @return
	 */
	public int getSessionLength(){
		return sessions.values().size();
	}
}
