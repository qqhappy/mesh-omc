/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| yinyuelin 	| 	create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.system.action.model.LoginUser;
import com.xinwei.system.action.web.WebConstants;
import com.xinwei.system.action.web.action.XwBaseAction;

/**
 * 
 * 登录action
 * 
 * <p>
 * 登录action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class LoginAction extends XwBaseAction{
	private Logger logger = LoggerFactory.getLogger(LoginAction.class);
	
	private String userName;
	
	private String passWord;
	
	@Value("${user_name}")
	private String defaultUsername;
	
	@Value("${pass_word}")
	private String defaultPassword;

	// 登陆成功后跳转的URL
	private String requestURL = "";
	/**
	 * 跳转至登录界面
	 */
	public String loginRedirect(){
		
		return SUCCESS;
	}
		
	/**
	 * 登录
	 */
	public String loginSystem(){	
		logger.debug("login - start");
		try{
			if(userName == null || userName.trim().equals("")){
				this.addActionError("用户名不可为空");
				return INPUT;
			}
			
			if(passWord == null || passWord.trim().equals("")){
				this.addActionError("密码不可为空");
				logger.debug("passWordError");
				return INPUT;
			}
			
			if(!defaultUsername.equals(userName.trim())){
				this.addActionError("不存在该用户");
				return INPUT;
			}	
			
			if(!defaultPassword.equals(passWord.trim())){
				this.addActionError("密码错误");
				return INPUT;
			}
			
			LoginUser loginUser = new LoginUser();
			loginUser.setUserName(userName);
			loginUser.setPassword(passWord);
			ActionContext.getContext().getSession().put(WebConstants.KEY_LOGIN_USER_OBJECT, loginUser);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("login error:"+e.toString());
		}		
		logger.debug("login - end");
		return NONE;
	}
	
	/**
	 * 跳转到主界面
	 */
	public String toHome(){
		logger.debug("toHome");
		return SUCCESS;
	}

	/**
	 * 注销登录
	 * @return
	 */
	public String logoutSystem(){
		
		ServletActionContext.getRequest().getSession().invalidate();
		
		return SUCCESS;
	}

	//异步请求
	public void ajaxMethod(String str){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			out.println(str);
			out.flush();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getPassWord(){
		return passWord;
	}

	public void setPassWord(String passWord){
		this.passWord = passWord;
	}
	
	public String getRequestURL()
	{
		return requestURL;
	}

	public void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}

	public static void main(String[] args){
		String requestURL = "aa/bbb";
		System.out.println(requestURL.substring(requestURL.indexOf("/")+1));
	}
}
