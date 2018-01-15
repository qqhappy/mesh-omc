/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * lte页面跳转action
 * 
 * <p>
 * lte页面跳转action
 * </p>
 * 
 * @author yinyuelin
 * 
 */

public class LteTurnAction extends ActionSupport {
	
	/**
	 * 核心网类型
	 */
	@Value("${coreNetType}")
	private String coreNetType;
	
	/**
	 * 已登录的用户
	 */
	private LoginUser loginUser ;
	
	// 记录日志
	private static Logger logger = LoggerFactory
			.getLogger(UserImsiAction.class);

	/**
	 * 跳转至LTE首页
	 * 
	 * @return
	 */
	public String turnToLteHome() {

		logger.debug("turnToLteHome");

		return SUCCESS;
	}

	/**
	 * 显示头部
	 * 
	 * @return
	 */
	public String showHead() {

		loginUser = (LoginUser) ActionContext.getContext()
				.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT);
		if(loginUser == null){
			loginUser = new LoginUser();
		}
		logger.debug("showHead");

		return SUCCESS;
	}

	/**
	 * 显示尾部
	 * 
	 * @return
	 */
	public String showFoot() {

		logger.debug("showFoot");

		return SUCCESS;
	}

	/**
	 * 显示左侧系统配置栏
	 * 
	 * @return
	 */
	public String showSysConfigLeft() {
		logger.debug("showSysConfigLeft");
		return SUCCESS;
	}

	/**
	 * 显示左侧系统用户信息栏
	 * 
	 * @return
	 */
	public String showUserInfoLeft() {
		logger.debug("showUserInfoLeft");
		return SUCCESS;
	}

	/**
	 * 显示视频监控左侧菜单栏
	 * 
	 * @return
	 */
	public String showVmgInfoLeft() {
		logger.debug("showUserInfoLeft");
		return SUCCESS;
	}

	/**
	 * 显示首页
	 * 
	 * @return
	 */
	public String showHomePage() {
		logger.debug("showHomePage");
		return SUCCESS;
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	public String getCoreNetType() {
		return coreNetType;
	}

	public void setCoreNetType(String coreNetType) {
		this.coreNetType = coreNetType;
	}
	
	
	
}
