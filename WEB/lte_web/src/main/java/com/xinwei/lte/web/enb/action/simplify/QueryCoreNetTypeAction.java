package com.xinwei.lte.web.enb.action.simplify;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;

/**
 * 获取核心网类型
 * @author zhangqiang
 *
 */
public class QueryCoreNetTypeAction extends ActionSupport {
	/**
	 * 核心网类类型
	 */
	@Value("${coreNetType}")
	private String coreNetType;
	
	/**
	 * 获取核心网类型
	 * @return
	 */
	public String queryCoreNetType(){
		Util.ajaxSimpleUtil(coreNetType);
		return NONE;
	}
	

	public String getCoreNetType() {
		return coreNetType;
	}

	public void setCoreNetType(String coreNetType) {
		this.coreNetType = coreNetType;
	}
	
	
}
