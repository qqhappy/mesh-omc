package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.QosFkModel;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 查询QOS参数表所需的四个外键
 * 
 * @author zhangqiang
 * 
 */
public class QueryQosFkAction extends ActionSupport {

	private Long moId;

	private QosFkModel qosFkModel = new QosFkModel();

	public String queryQosFk() {
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
			XEnbBizConfigFacade facade = MinasSession.getInstance().getFacade(
					sessionId, XEnbBizConfigFacade.class);
			// macFk
			XBizTable xBizTable1 = facade.queryFromEms(moId, "T_ENB_SRVMAC",
					new XBizRecord());
			List<XBizRecord> macFk = xBizTable1.getRecords();
			if (macFk != null && macFk.size() != 0) {
				qosFkModel.setMacFk(macFk);
			}
			// pcFk
			XBizTable xBizTable2 = facade.queryFromEms(moId, "T_ENB_SRVPC",
					new XBizRecord());
			List<XBizRecord> pcFk = xBizTable2.getRecords();
			if (pcFk != null && pcFk.size() != 0) {
				qosFkModel.setPcFk(pcFk);
			}
			// pdcpFk
			XBizTable xBizTable3 = facade.queryFromEms(moId, "T_ENB_PDCP",
					new XBizRecord());
			List<XBizRecord> pdcpFk = xBizTable3.getRecords();
			if (pdcpFk != null && pdcpFk.size() != 0) {
				qosFkModel.setPdcpFk(pdcpFk);
			}
			// rlcFk
			XBizTable xBizTable4 = facade.queryFromEms(moId, "T_ENB_RLC",
					new XBizRecord());
			List<XBizRecord> rlcFk = xBizTable4.getRecords();
			if (rlcFk != null && rlcFk.size() != 0) {
				qosFkModel.setRlcFk(rlcFk);
			}
			JSONObject json = new JSONObject();
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(qosFkModel);
			json.put("qosFkModel", object);
			out.println(json.toString());
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

	public QosFkModel getQosFkModel() {
		return qosFkModel;
	}

	public void setQosFkModel(QosFkModel qosFkModel) {
		this.qosFkModel = qosFkModel;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}
}
