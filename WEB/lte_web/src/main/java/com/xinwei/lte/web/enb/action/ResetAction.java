package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbExtBizFacade;
import com.xinwei.system.action.web.WebConstants;

/**
 * 站点复位以及单板复位
 * 
 * @author zhangqiang
 * 
 */
public class ResetAction extends ActionSupport {

	/**
	 * MO编号（全局唯一,系统自动生成）
	 */
	private long moId;

	/**
	 * 机架号
	 */
	private int rackId;

	/**
	 * 机框号
	 */
	private int shelfId;

	/**
	 * 单板号
	 */
	private int boardId;

	/**
	 * 错误
	 */
	private String error = "";

	/**
	 * 站点复位
	 * 
	 * @return
	 */
	public String resetEnb() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbExtBizFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbExtBizFacade.class);
			EnbBasicFacade facadeTwo = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			OperObject object = OperObject.createEnbOperObject(facadeTwo
					.queryByMoId(moId).getHexEnbId());
			// 复位
			facade.reset(object, moId);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				json.put("error", error);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 单板复位
	 * 
	 * @return
	 */
	public String resetBoard() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			// 获取facade
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbExtBizFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbExtBizFacade.class);
			EnbBasicFacade facadeTwo = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			OperObject object = OperObject.createEnbOperObject(facadeTwo
					.queryByMoId(moId).getHexEnbId());
			// 复位
			facade.reset(object, moId, rackId, shelfId, boardId);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				json.put("error", error);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getRackId() {
		return rackId;
	}

	public void setRackId(int rackId) {
		this.rackId = rackId;
	}

	public int getShelfId() {
		return shelfId;
	}

	public void setShelfId(int shelfId) {
		this.shelfId = shelfId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
