/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.hlr.HLRModule;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.lte.model.WebMasterModel;
import com.xinwei.lte.web.lte.service.WebMasterService;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.OperNameConstant;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogParam;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.shlr.communication.CommTcp;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author yinyuelin
 * 
 */

public class WebMasterAction extends ActionSupport {

	private Logger logger = LoggerFactory.getLogger(WebMasterAction.class);

	@Resource
	private OssAdapter ossAdapter;

	@Resource
	private WebMasterService webMasterService;

	private WebMasterModel webMasterModel;

	private Properties oss_config = new Properties();

	private static final String FILE_PATH = "/conf/oss/oss-config.properties";

	/**
	 * 显示网管配置管理左侧菜单栏
	 * 
	 * @return
	 */
	public String showWebMaster() {

		/*
		 * Properties properties = new Properties(); try {
		 * properties.load(WebMasterAction
		 * .class.getResourceAsStream("/loginPro.properties"));
		 * 
		 * String key = "password"; String value = "yyl";
		 * 
		 * properties.setProperty("password", "yyl");
		 * 
		 * String filePath =
		 * WebMasterAction.class.getResource("/").getPath()+"/loginPro.properties"
		 * .substring(1,"/loginPro.properties".length());
		 * 
		 * OutputStream out = new FileOutputStream(filePath);
		 * 
		 * properties.store(out, "Update password yyl");
		 * 
		 * out.flush();
		 * 
		 * out.close(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

		return SUCCESS;
	}

	/**
	 * 显示网管配置管理主界面
	 * 
	 * @return
	 */
	public String turntoWebMaster() {
		// 鉴权
		try {
			checkPrivilege(ActionTypeDD.QUERY);
		} catch (Exception e) {
			// TODO:鉴权异常处理
		}
		logger.info("turntoWebMaster - start");
		try {

			oss_config.clear();
			oss_config.load(new FileInputStream(WebMasterAction.class
					.getResource(FILE_PATH).getFile()));
			webMasterModel = new WebMasterModel();
			webMasterModel.setAddr(oss_config.getProperty("ip"));
			webMasterModel.setPort(oss_config.getProperty("port"));
			CommTcp commTcp = CommTcp.getInstance();
			commTcp.init(webMasterModel.getAddr(),
					Integer.parseInt(webMasterModel.getPort()));
			if (commTcp.isCloseConnect()) {
				webMasterModel.setStatus("0");
			} else {
				webMasterModel.setStatus("1");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("turntoWebMaster - error:" + e);
		}
		logger.info("turntoWebMaster - end");
		return SUCCESS;
	}

	/**
	 * 检查连接状态
	 * 
	 * @return
	 */
	public String checkLink() {

		logger.info("checkLink - start");

		JSONObject json = new JSONObject();
		try {
			CommTcp commTcp = CommTcp.getInstance();
			if (commTcp.isCloseConnect()) {
				json.put("status", 0);
			} else {
				json.put("status", 1);
			}
			ajaxMethod(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("checkLink error:" + e);
		}

		logger.info("checkLink - end");
		return NONE;
	}

	/**
	 * 跳转到网管配置管理修改界面
	 * 
	 * @return
	 */
	public String turntoWebMasterModify() {

		return SUCCESS;
	}

	/**
	 * 修改配置
	 * 
	 * @return
	 */
	public String modifyWebMaster() {
		// 鉴权
		try {
			checkPrivilege(ActionTypeDD.CONFIG);
		} catch (Exception e) {
			// TODO:鉴权异常处理
		}

		String filePath = null;
		JSONObject json = new JSONObject();
		try {
			/*
			 * String ip = webMasterModel.getAddr();
			 * 
			 * String port = webMasterModel.getPort();
			 */

			// 读取并修改文件
			oss_config.load(WebMasterAction.class
					.getResourceAsStream(FILE_PATH));
			// if(webMasterService.judgeConnect(webMasterModel.getAddr(),webMasterModel.getPort())){
			filePath = WebMasterAction.class.getResource("/").getPath()
					+ FILE_PATH.substring(1, FILE_PATH.length());
			System.out.println("modifyWebMaster :" + filePath);
			// 覆盖数据
			webMasterService.overwriteConf(filePath, webMasterModel.getAddr(),
					webMasterModel.getPort(),
					oss_config.getProperty("adapterXmlPath"),
					oss_config.getProperty("TVconfigXmlPath"));
			// 记录操作日志
			logOperation();

			// 重连
			// webMasterService.reConnect();
			json.put("status", 0);
			ajaxMethod(json.toString());
			// }else{
			// json.put("status", 1);
			// ajaxMethod(json.toString());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", 1);
			ajaxMethod(json.toString());
		}
		return NONE;
	}

	private void checkPrivilege(String actionType) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		MinasSession.getInstance().checkPrivilege(sessionId,
				OperNameConstant.SDC_CONFIG, actionType);
	}

	/**
	 * 记录修改网管配置的操作日志，特殊处理facade为OperNameConstant中的SDC_CONFIG，method为Config
	 */
	private void logOperation() {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		OperSignature signature = new OperSignature();
		signature.setFacade(OperNameConstant.SDC_CONFIG);
		signature.setMethod(ActionTypeDD.CONFIG);
		OperObject operObject = OperObject.createTcn1000OperObject();
		String data = "SDC IP address:" + webMasterModel.getAddr()
				+ ", SDC port:" + webMasterModel.getPort();
		LogParam logParam = new LogParam(sessionId, signature, operObject,
				new Object[] { data });
		MinasSession.getInstance().addLog(logParam);
	}

	/**
	 * 重连
	 * 
	 * @return
	 */
	public String relinkWebMaster() {
		logger.info("relinkWebMaster - start");
		try {

			HLRModule hlrModule = HLRModule.getInstance();
			hlrModule.destroy();
			ossAdapter.initialize();
			ajaxMethod("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("relinkWebMaster - error:" + e);
			ajaxMethod("error");
		}
		logger.info("relinkWebMaster - end");
		return NONE;
	}

	// 异步请求返回字符串
	private void ajaxMethod(String content) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

	public WebMasterModel getWebMasterModel() {
		return webMasterModel;
	}

	public void setWebMasterModel(WebMasterModel webMasterModel) {
		this.webMasterModel = webMasterModel;
	}

	public static void main(String[] args) {
		System.out.println(WebMasterAction.class.getResource(FILE_PATH)
				.getFile());
		System.out.println(WebMasterAction.class.getResource(FILE_PATH)
				.getPath());
	}
}
