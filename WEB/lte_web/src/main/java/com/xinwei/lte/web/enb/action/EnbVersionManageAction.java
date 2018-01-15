package com.xinwei.lte.web.enb.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.VersionDownloadPara;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.facade.conf.MoBasicFacade;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.system.action.web.WebConstants;

/**
 * 版本管理action
 * 
 * @author zhangqiang
 * 
 */
public class EnbVersionManageAction extends ActionSupport {

	/**
	 * 是否进行了删除 1:是,0：否
	 */
	private int isDeleted;

	/**
	 * 上传的文件
	 */
	private File file;

	/**
	 * 上传文件名
	 */
	private String fileFileName;

	/**
	 * 版本号
	 */
	private String versionName;

	/**
	 * 是否已上传,1：是 2:否
	 */
	private int isUploaded;

	/**
	 * 本基站是否有正在进行的下载,1：是 2 :否
	 */
	private int isdownloading;

	/**
	 * 所有基站中是否有正在进行的下载,1:是 2：否
	 */
	private int exsitDownloading;

	/**
	 * 基站类型
	 */
	private int enbType;

	/**
	 * 基站ID，16进制
	 */
	private String enbHexId;

	/**
	 * MO编号（全局唯一,系统自动生成）
	 */
	private long moId;

	/**
	 * 异常
	 */
	private String error = "";

	/**
	 * 查询到的版本文件列表
	 */
	private List<McBtsVersion> versionList;

	/**
	 * 查询到的版本文件个数
	 */
	private int listLength;

	/**
	 * 页面返回的参数字符串
	 */
	private String parameters;

	/**
	 * 查询版本文件
	 * 
	 * @return
	 */
	public String queryAllVersion() {
		try {
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);

			// 是否进行删除
			if (isDeleted == 1) {
				String[] strFir = parameters.split(";");
				for (int i = 0; i < strFir.length; i++) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					String[] strSec = strFir[i].split(",");
					for (int j = 0; j < strSec.length; j++) {
						String[] str = strSec[j].split("=");
						map.put(str[0], str[1]);
					}
					McBtsVersion mcBtsVersion = new McBtsVersion();
					mcBtsVersion
							.setFileName(String.valueOf(map.get("fileName")));
					mcBtsVersion.setBtsType(MoTypeDD.ENODEB);
					mcBtsVersion.setVersionName(String.valueOf(map
							.get("versionName")));
					facade.delete(OperObject.createSystemOperObject(),
							mcBtsVersion);
				}
			}
			// 判断此基站是否正在下载
			Map<Long, String> map = facade.queryCurrentDownloadTasks();
			if (map.keySet().contains(Long.valueOf(enbHexId, 16))) {
				isdownloading = 1;
				versionName = map.get(Long.valueOf(enbHexId, 16));
			} else {
				isdownloading = 2;
			}
			versionList = facade.queryByBtsType(MoTypeDD.ENODEB);
			if (versionList == null) {
				versionList = new ArrayList<McBtsVersion>();
			}
			// versionList = facade.queryAll();
			listLength = versionList.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			if (error == null) {
				error = "";
			}
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 上传版本
	 * 
	 * @return
	 */
	public String uploadEnbVersion() {
		FileInputStream fin = null;
		try {
			// 将文件转为byte[]
			fin = new FileInputStream(file);
			byte[] bt = new byte[fin.available()];
			fin.read(bt);
			// 已上传
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);
			// 构建版本对象
			McBtsVersion mcBtsVersion = new McBtsVersion();
			mcBtsVersion.setFileName(fileFileName);
			mcBtsVersion.setFileContent(bt);
			mcBtsVersion.setBtsType(MoTypeDD.ENODEB);
			mcBtsVersion.setVersionName(returnVersionName(fileFileName));
			// 添加
			facade.add(mcBtsVersion);
			isUploaded = 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			isUploaded = 2;
			return ERROR;
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 下载版本到基站
	 * 
	 * @return
	 */
	public String downloadEnbVersion() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		int downLoadPara = 0;
		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);
			// 构建mcBtsVersion
			McBtsVersion mcBtsVersion = new McBtsVersion();
			mcBtsVersion.setFileName(fileFileName);
			mcBtsVersion.setBtsType(MoTypeDD.ENODEB);
			mcBtsVersion.setVersionName(versionName);
			// 获得被管对象Mo
			MoBasicFacade moFacade = MinasSession.getInstance().getFacade(
					sessionId, MoBasicFacade.class);
			Mo mo = moFacade.queryByMoId(moId);
			// 下载
			downLoadPara = facade.download(OperObject.createEnbOperObject(enbHexId),
					mo, Long.parseLong(enbHexId, 16), mcBtsVersion);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			VersionDownloadPara para = new VersionDownloadPara();
			para.setDownLoadPara(downLoadPara);
			para.setError(error);
			para.setVersionName(versionName);
			JSONObject json = new JSONObject();
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(para);
			json.put("para", object);
			out.println(json.toString());
			out.flush();
			out.close();
		}
		return NONE;
	}

	/**
	 * 升级
	 * 
	 * @return
	 */
	public String upGrade() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);
			// 升级
			facade.upgrade(OperObject.createEnbOperObject(getEnbHexIdByMoId(moId)), moId, 2);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			JSONObject json = new JSONObject();
			json.put("error", error);
			out.println(json.toString());
			out.flush();
			out.close();
		}
		return NONE;
	}

	/**
	 * 查看是否有正在下载的基站
	 * 
	 * @return
	 */
	public String queryExsitDownloading() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);
			// 是否有正在下载的基站
			if (facade.queryCurrentDownloadTasks().keySet().size() == 0) {
				// 没有
				exsitDownloading = 2;
			} else {
				// 有
				exsitDownloading = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		}
		JSONObject json = new JSONObject();
		json.put("exsitDownloading", exsitDownloading);
		out.println(json.toString());
		out.flush();
		out.close();
		return NONE;
	}

	/**
	 * 请求下载百分比
	 * 
	 * @return
	 */
	public String queryPercent() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			McBtsVersionManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, McBtsVersionManageFacade.class);
			McBtsVersionHistory versionHistory = facade.getLatestStatus(
					Long.parseLong(enbHexId, 16)).getMcBtsVersionHistory();
			int percent = versionHistory.getDownloadProgress();
			String version = versionHistory.getVersion();

			VersionDownloadPara model = new VersionDownloadPara();

			int actionResult = versionHistory.getActionResult();
			if (actionResult == McBtsCodeDownloadTask.OVERTIME) {
				model.setError("版本下载超时");
			}
			// 基站回复的下载结果中result字段的值为1时，为下载失败
			if (actionResult == 1) {
				model.setError("版本下载失败");
			}
			if (actionResult == McBtsCodeDownloadTask.DONE) {
				model.setDownloadSuccess(1);
			}
			model.setPercent(percent);
			model.setVersionName(version);
			JSONObject json = new JSONObject();
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(model);

			json.put("model", object);
			out.println(json.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 返回版本的versionName
	 * 
	 * @param name
	 * @return
	 */
	public String returnVersionName(String name) {
		String versionName = null;
		String regex = "([0-9PT]{1,}\\.{1}){4}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			versionName = matcher.group();
		}
		if (versionName != null) {
			versionName = versionName.substring(0, versionName.length() - 1);
		}
		return versionName;
	}
	
	private String getEnbHexIdByMoId(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getHexEnbId();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public int getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(int isUploaded) {
		this.isUploaded = isUploaded;
	}

	public int getEnbType() {
		return enbType;
	}

	public void setEnbType(int enbType) {
		this.enbType = enbType;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getIsdownloading() {
		return isdownloading;
	}

	public void setIsdownloading(int isdownloading) {
		this.isdownloading = isdownloading;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<McBtsVersion> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<McBtsVersion> versionList) {
		this.versionList = versionList;
	}

	public int getListLength() {
		return listLength;
	}

	public void setListLength(int listLength) {
		this.listLength = listLength;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getExsitDownloading() {
		return exsitDownloading;
	}

	public void setExsitDownloading(int exsitDownloading) {
		this.exsitDownloading = exsitDownloading;
	}
}
