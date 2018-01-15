package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 接收页面心跳，与服务器进行握手鉴权
 * 
 * @author zhangqiang
 * 
 */
public class HandShakeAction extends ActionSupport {

	/**
	 * 握手
	 * 
	 * @return
	 */
	public String handShake() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			// 无需操作，只需"握手"
			out = response.getWriter();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return NONE;
	}
}
