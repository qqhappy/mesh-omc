package com.xinwei.lte.web.enb.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.EnbModel;
import com.xinwei.lte.web.enb.model.check.HealthCheckShow;
import com.xinwei.lte.web.enb.service.EnbCheckService;
import com.xinwei.lte.web.enb.service.impl.EnbCheckServiceImpl;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.system.action.web.WebConstants;

public class EnbHealthInspectAction extends ActionSupport {

	private List<EnbModel> enbModelList = new ArrayList<EnbModel>();

	private String enbids;

	private String fileName;
	
	private EnbCheckService enbCheckService;

	public String getHealthInspectFile() {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String checkHealth() {
		JSONObject json = new JSONObject();
		json.put("status", 0);
		try {
			List<Enb> list = new ArrayList<Enb>();
			String[] enbIdArray = enbids.split(",");
			EnbBasicFacade facade = Util
					.getFacadeInstance(EnbBasicFacade.class);
			for (int i = 0; i < enbIdArray.length; i++) {
				if (enbIdArray[i] != null && !"".equals(enbIdArray[i])) {
					Enb enb = facade.queryByMoId(Long.valueOf(enbIdArray[i]));
					list.add(enb);
				}
			}
			String realPath = ServletActionContext.getServletContext()
					.getRealPath("/");
			int result = enbCheckService.doCheck(list, realPath);
			json.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	public String queryCheckFile() {
		JSONObject json = new JSONObject();
		String realPath = ServletActionContext.getServletContext().getRealPath(
				"/");
		List<HealthCheckShow> list = enbCheckService.queryCheckFile(realPath);
		if (list == null) {
			list = new ArrayList<HealthCheckShow>();
		}
		json.put("message", JSONArray.fromObject(list));
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	public String downLoadHealthFile() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("Application/msexcel;charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName);
		ServletOutputStream out = null;
		FileInputStream fin = null;
		try {
			String realPath = ServletActionContext.getServletContext()
					.getRealPath("/") + "check/" + fileName;
			File file = new File(realPath);
			fin = new FileInputStream(file);
//			byte[] bt = new byte[fin.available()];
			out = response.getOutputStream();
//			out.write(bt);
			HSSFWorkbook wb = new HSSFWorkbook(fin);;
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}

	
	public void makeEnbModel(PagingData<Enb> data) {
		if (data != null) {
			List<Enb> enbList = data.getResults();
			// 构建EnbModel的集合
			for (Enb enb : enbList) {
				String enbHexId = enb.getHexEnbId();
				EnbModel enbModel = new EnbModel();
				enbModel.setEnb(enb);
				enbModel.setEnbId(enbHexId);
				//过滤掉微站
				if(enb.getEnbType() != 200){
					enbModelList.add(enbModel);
				}				
			}

		}
	}

	// 创建查询条件
	public EnbCondition makeCondition() {
		EnbCondition condition = new EnbCondition();
		condition.setCurrentPage(1);
		condition.setNumPerPage(250);
		// condition.setSoftwareVersion("");
		return condition;
	}

	public List<EnbModel> getEnbModelList() {
		return enbModelList;
	}

	public void setEnbModelList(List<EnbModel> enbModelList) {
		this.enbModelList = enbModelList;
	}

	public String getEnbids() {
		return enbids;
	}

	public void setEnbids(String enbids) {
		this.enbids = enbids;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public EnbCheckService getEnbCheckService() {
		return enbCheckService;
	}

	public void setEnbCheckService(EnbCheckService enbCheckService) {
		this.enbCheckService = enbCheckService;
	}
	
	

}
