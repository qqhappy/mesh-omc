package com.xinwei.lte.web.enb.interceptor;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.system.action.util.WebUtil;
import com.xinwei.system.action.web.WebConstants;

/**
 * 拦截器
 * 
 * @author zhangqiang
 * 
 */
public class LoginInterceptor extends AbstractInterceptor {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext actionContext = invocation.getInvocationContext(); 
		LoginUser identity = (LoginUser) actionContext.getSession().get(
				WebConstants.KEY_LOGIN_USER_OBJECT);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 1);
		if (identity == null) { 
			HttpServletRequest request = ServletActionContext.getRequest();
			/**
			 * ajax请求，session过期处理
			 */
			if (request.getHeader("X-Requested-With") != null) {
//				HttpServletResponse response = ServletActionContext
//						.getResponse();
//				response.setContentType("text/html; charset=utf-8");
//				response.setCharacterEncoding("UTF-8");	
				Writer out = response.getWriter();
				out.write("{\"loginFlag\":\"false\"}");
				out.flush();
				out.close();
				return "none";
			}
			actionContext.getSession().put("requestURL",
					WebUtil.getRequestURLWithParams());
			return Action.LOGIN;
		}
		long time1 = System.currentTimeMillis(); 
		// 执行后续操作
		String result = invocation.invoke();
		long time2 = System.currentTimeMillis();		
		// 计算耗时
		long usedTime = time2 - time1;
		String actionName = invocation.getAction().getClass().getCanonicalName();
		logger.debug("invoke action[" + actionName + "], result[" + result +"], use " +  usedTime + " ms");
		
		return result;

	}
}
