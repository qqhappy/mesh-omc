package com.xinwei.lte.web.enb.action;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbExtBizFacade;
import com.xinwei.system.action.web.WebConstants;

public class ExportActiveDataAction extends ActionSupport {

	private long moId;

	private String enbHexId;

	private String error;

	public String exportActiveData() {
		ServletOutputStream out = null;
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			// 获取facade
			EnbExtBizFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbExtBizFacade.class);
			EnbBasicFacade facadeTwo = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);

			OperObject object = OperObject.createEnbOperObject(facadeTwo
					.queryByMoId(moId).getHexEnbId());
			String sql = facade.exportActiveData(object, moId);

			HttpServletResponse response = ServletActionContext.getResponse();
			out = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ enbHexId + "_cfg.sql");

			byte[] bytes = sql.getBytes("utf-8");
			out.write(bytes);

		} catch (Exception e) {
			error = e.getLocalizedMessage();
			return ERROR;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

}
