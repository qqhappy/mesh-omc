package com.xinwei.lte.web.enb.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.system.action.web.WebConstants;

/**
 * 查询单个ENB
 * @author zhangqiang
 *
 */
public class QuerySingleEnbAction extends ActionSupport {
	
	private String enbHexId;
	
	private String enbName;
	
	private String enbVersion;
	
	private String privateIp;
	
	private String error;
	
	
	/**
	 * 查询单个eNB
	 * 
	 * @return
	 */
	public String querySingleEnb() {
		try {
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			// 查询出需要的数据
			Enb enb = facade.queryByEnbId(Long.parseLong(enbHexId, 16));
			System.out.println(enbHexId);
			enbName = enb.getName();
			enbVersion = enb.getSoftwareVersion();
			privateIp = enb.getPrivateIp();
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}


	public String getEnbHexId() {
		return enbHexId;
	}


	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}


	public String getEnbName() {
		return enbName;
	}


	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}


	public String getEnbVersion() {
		return enbVersion;
	}


	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}


	public String getPrivateIp() {
		return privateIp;
	}


	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
	
}
