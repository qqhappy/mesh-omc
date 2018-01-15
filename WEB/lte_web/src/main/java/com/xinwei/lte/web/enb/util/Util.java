package com.xinwei.lte.web.enb.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.system.action.web.WebConstants;

public class Util {
	/**
	 * 当前登陆用户获取facade
	 * @param <T>
	 * @param facadeInterface
	 * @return
	 * @throws Exception
	 */
	public static <T> T getFacadeInstance(Class<T> facadeInterface)
			throws Exception {

		 String sessionId = getSessionId();
//		String sessionId = "~!@#$%^&*ABCDEFG1234567";
		return MinasSession.getInstance().getFacade(sessionId, facadeInterface);
	}

	/**
	 * 当前登陆用户获取 sessionId
	 * @return
	 */
	public static String getSessionId() {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		return sessionId;
	}
	
	/**
	 * 当前登陆用户获取用户名
	 * @return
	 */
	public static String getUserName() {
		LoginUser user = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT));
		if (user == null) {
			return "admin";
		}
		String userName = user.getUsername();
		return userName;
	}
	
	/**
	 * 获取版本号
	 * @param pkgName
	 * @return
	 */
	public static String getVersiongByPkgName(String pkgName) {
		String[] temp = pkgName.split("_");
		return temp[1];
	}
	
	/**
	 * 将json串或普通字符串放入响应流
	 * @param result
	 */
	public static void ajaxSimpleUtil(String result){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(result);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
}
