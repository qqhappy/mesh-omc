package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 为Alg表从小区表查询带宽
 * 
 * @author zhangqiang
 * 
 */
public class QuerySysBandWidthForAlgAction extends ActionSupport {

	/**
	 * MO编号（全局唯一,系统自动生成）
	 */
	private long moId;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 前端传来的参数数组的字符串格式
	 */
	private String parameters;

	/**
	 * 查询带宽
	 * 
	 * @return
	 */
	public String queryBandWidth() {
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
			// 构建查询单体数据条件
			XBizRecord condition = new XBizRecord();
			Map<String, String> map = getMap();
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				condition.addField(makeXBizField(key, map.get(key)));
			}
			// 查询出结果
			XBizTable xBizTable = facade.queryFromEms(moId, tableName,
					condition);
			int u8SysBandWidth = Integer.valueOf(xBizTable.getRecords().get(0)
					.getFieldMap().get("u8SysBandWidth").getValue().toString());

			JSONObject json = new JSONObject();
			json.put("u8SysBandWidth", u8SysBandWidth);
			out.println(json.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (out != null) {
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 生成xBizField
	 * 
	 * @param name
	 * @return
	 */
	public XBizField makeXBizField(String name, String value) {
		XBizField xBizField = new XBizField();
		xBizField.setName(name);
		xBizField.setValue(value);
		return xBizField;
	}

	/**
	 * 由获取的parameters参数获得键值对
	 * 
	 * @return
	 */
	public Map<String, String> getMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		// 根据";"进行拆分
		String[] strFir = parameters.split(";");
		for (int i = 0; i < strFir.length; i++) {
			// 根据"="进行拆分
			String[] str = strFir[i].split("=");
			map.put(str[0], str[1]);
		}
		return map;

	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}
