package com.xinwei.lte.web.enb.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbExtBizFacade;
import com.xinwei.minas.enb.core.facade.EnbFullTableConfigFacade;
import com.xinwei.system.action.web.WebConstants;

/**
 * 整表配置以及整表反构
 * 
 * @author zhangqiang
 * 
 */
public class EnbTotalConfigAction extends ActionSupport {
	/**
	 * MO编号（全局唯一,系统自动生成）
	 */
	private long moId;

	/**
	 * 是否已配置 1:是 2:否
	 */
	private int isConfiged;
	
	/**
	 * 是否已恢复 1:是 2:否
	 */
	private int isRestored;
	
	/**
	 * 是否已学习 1:是 2:否
	 */
	private int isStudied;

	/**
	 * 异常
	 */
	private String error;

	/**
	 * 整表配置
	 * 
	 * @return
	 */
	public String totalConfig() {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbFullTableConfigFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbFullTableConfigFacade.class);
			
			EnbBasicFacade facadeTwo = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			OperObject object = OperObject.createEnbOperObject(facadeTwo
					.queryByMoId(moId).getHexEnbId());
			facade.config(object,moId);
			isConfiged = 1;
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			if(error == null){
				error = "";
			}
			isConfiged = 2;
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 一键恢复
	 * @return
	 */
	public String oneClickRestore(){
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbExtBizFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbExtBizFacade.class);
			facade.recoverDefaultData(moId);
			isRestored = 1;
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			if(error == null){
				error = "";
			}
			isRestored = 2;
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 自学习
	 * @return
	 */
	public String selfStudy(){
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbExtBizFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, EnbExtBizFacade.class);
			facade.studyEnbDataConfig(moId,true);
			isStudied = 1;
		} catch (Exception e) {
			error = e.getLocalizedMessage();
			if(error == null){
				error = "";
			}
			isStudied = 2;
			return ERROR;
		}
		return SUCCESS;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getIsConfiged() {
		return isConfiged;
	}

	public void setIsConfiged(int isConfiged) {
		this.isConfiged = isConfiged;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getIsRestored() {
		return isRestored;
	}

	public void setIsRestored(int isRestored) {
		this.isRestored = isRestored;
	}

	public int getIsStudied() {
		return isStudied;
	}

	public void setIsStudied(int isStudied) {
		this.isStudied = isStudied;
	}
	

}
