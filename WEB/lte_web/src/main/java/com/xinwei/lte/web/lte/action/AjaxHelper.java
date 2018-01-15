package com.xinwei.lte.web.lte.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class AjaxHelper {

	/**
	 * 异步请求返回字符串
	 * @param content
	 */
	public static void ajaxMethod(String content){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}
	
	/**
	 * 根据结果返回AJAX应答
	 * @param resp
	 */
	public static void ajaxMethod(OssAdapterOutputMessage resp) {
		JSONObject json = new JSONObject();
		if(resp.isSuccesful()){
			json.put("status", 0);
			ajaxMethod(json.toString());
		}else{
			json.put("status", 1);
			json.put("message", resp.getReason());
			ajaxMethod(json.toString());
		}
	}
}
