package com.xinwei.lte.web.enb.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.EnbModel;
import com.xinwei.lte.web.enb.util.DateJsonValueProcess;
import com.xinwei.lte.web.enb.util.DownLoadFileUtil;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbBlackBoxFileFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileCondition;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.system.action.web.WebConstants;

public class EnbBlackBoxFileAction extends ActionSupport {

	// 查询条件封装类
	private EnbBlackBoxFileCondition blackBoxCondition;
	// 获取enbid
	private List<EnbModel> enbModelList = new ArrayList<EnbModel>();
	// 分页内容

	private PagingData<EnbBlackBoxFileModel> pagingData;
	// 接受文件名，用于文件的下載
	private String fileName;

	// 复位原因，并排序
	private Map<String, String> resetReasonMap = new TreeMap<String, String>(
			new Comparator<String>() {

				@Override
				public int compare(String s1, String s2) {

					int value1 = Integer.parseInt(s1
							.substring("enb.reset.reason.".length()));
					int value2 = Integer.parseInt(s2
							.substring("enb.reset.reason.".length()));

					if (value1 > value2) {
						return 1;
					} else if (value1 < value2) {
						return -1;
					}
					return 0;
				}
			});

	// 获取EnbBlackBoxFileFacade
	public EnbBlackBoxFileFacade getEnbBlackBoxFileFacade() {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		EnbBlackBoxFileFacade blackBoxFileFacade = null;
		try {
			blackBoxFileFacade = MinasSession.getInstance().getFacade(
					EnbBlackBoxFileFacade.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blackBoxFileFacade;
	}

	// 获取本地的黑匣子文件名封装到黑匣子文件模型中
	public String getBlackBoxFile() {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbBasicFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			EnbBlackBoxFileFacade blackBoxFileFacade = MinasSession
					.getInstance().getFacade(EnbBlackBoxFileFacade.class);
			// 构造查询eNB的condition
			EnbCondition condition = makeCondition();
			// 查询出需要的数据
			PagingData<Enb> data = facade.queryAllByCondition(condition);
			makeEnbModel(data);
			Map<String, String> map = blackBoxFileFacade.getAllResetReason();
			for (String key : map.keySet()) {
				resetReasonMap.put(key, map.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	// 获取pagingdata
	public String getPagingDataAction() {
		JSONObject json = new JSONObject();
		try {
			EnbBlackBoxFileFacade blackBoxFileFacade = getEnbBlackBoxFileFacade();

			pagingData = blackBoxFileFacade.getBlackBoxFile(blackBoxCondition);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateJsonValueProcess());
			json.put("status", 0);
			json.put("message", JSONObject.fromObject(pagingData, jsonConfig));
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}

	// 单个文件下载
	public String downLoadBlackBoxFile() {
		byte[] bytes = getBytesByFileName(fileName);
		new DownLoadFileUtil().downLoadFile(fileName, bytes);
		return NONE;
	}

	// 根据文件名字获取byte[]
	public byte[] getBytesByFileName(String name) {
		byte[] bytes = null;
		try {
			EnbBlackBoxFileFacade blackBoxFileFacade = getEnbBlackBoxFileFacade();
			bytes = blackBoxFileFacade.getBytesByFileName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public byte[] getZipFile(String name) {
		EnbBlackBoxFileFacade blackBoxFileFacade = getEnbBlackBoxFileFacade();
		byte[] zipFile = null;
		try {
			zipFile = blackBoxFileFacade.getZipFile(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFile;
	}

	// 批量文件下载
	public String batchDownloadBlackBoxFile() {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now = sf.format(new Date());
		String tempFile = now + "blackboxfile.zip";
		byte[] bytes = getZipFile(fileName);
		new DownLoadFileUtil().downLoadFile(tempFile, bytes);

		return NONE;
	}

	// 创建查询条件

	public void makeEnbModel(PagingData<Enb> data) {
		if (data != null) {
			List<Enb> enbList = data.getResults();
			// 构建EnbModel的集合
			for (Enb enb : enbList) {
				    int enbType= enb.getEnbType();
				    String version = enb.getProtocolVersion();
				    
					String enbHexId = enb.getHexEnbId();
					EnbModel enbModel = new EnbModel();
					enbModel.setEnb(enb);
					enbModel.setEnbId(enbHexId);
					enbModelList.add(enbModel);
				}
				
		}
	}

	public EnbCondition makeCondition() {
		EnbCondition condition = new EnbCondition();
		condition.setCurrentPage(1);
		condition.setNumPerPage(250);
		//condition.setSoftwareVersion("");
		return condition;
	}

	public Map<String, String> getResetReasonMap() {
		return resetReasonMap;
	}

	public void setResetReasonMap(Map<String, String> resetReasonMap) {
		this.resetReasonMap = resetReasonMap;
	}

	public PagingData<EnbBlackBoxFileModel> getPagingData() {
		return pagingData;
	}

	public void setPagingData(PagingData<EnbBlackBoxFileModel> pagingData) {
		this.pagingData = pagingData;
	}

	public List<EnbModel> getEnbModelList() {
		return enbModelList;
	}

	public void setEnbModelList(List<EnbModel> enbModelList) {
		this.enbModelList = enbModelList;
	}

	private List<EnbBlackBoxFileModel> blackBoxFileList = new ArrayList<EnbBlackBoxFileModel>();

	public List<EnbBlackBoxFileModel> getBlackBoxFileList() {
		return blackBoxFileList;
	}

	public void setBlackBoxFileList(List<EnbBlackBoxFileModel> blackBoxFileList) {
		this.blackBoxFileList = blackBoxFileList;
	}

	public EnbBlackBoxFileCondition getBlackBoxCondition() {
		return blackBoxCondition;
	}

	public void setBlackBoxCondition(EnbBlackBoxFileCondition blackBoxCondition) {
		this.blackBoxCondition = blackBoxCondition;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
