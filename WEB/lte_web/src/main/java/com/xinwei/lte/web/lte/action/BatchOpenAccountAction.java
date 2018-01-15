/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-08	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.BatchUserInfoModel;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 批量开户Action
 * 
 * @author chenjunhua
 * 
 */
public class BatchOpenAccountAction extends ActionSupport {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private OssAdapter ossAdapter;

	// 前台传递过来的带完整路径的失败文件名
	private String fileName;

	// 批量开户用户模型
	private BatchUserInfoModel batchUserInfoModel;

	/**
	 * 批量增加用户信息
	 * 
	 * @return
	 */
	public String batchAddUserInfo() {
		log.debug("batchAddUserInfo-start");
		JSONObject json = new JSONObject();
		try {
			// 基本信息
			Map<String, Object> baseInfoMap = new HashMap<String, Object>();
			baseInfoMap.put("startUserNumber",
					batchUserInfoModel.getStartUserNumber());
			baseInfoMap.put("numberType", batchUserInfoModel.getNumberType());
			baseInfoMap.put("userState", batchUserInfoModel.getUserState());
			baseInfoMap.put("haveImsi", batchUserInfoModel.getHaveImsi());
			if ("1".equals(batchUserInfoModel.getHaveImsi())) {
				baseInfoMap.put("startImsi", batchUserInfoModel.getStartImsi());
				baseInfoMap.put("userParamTempletId",
						batchUserInfoModel.getUserParamTempletId());
			}
			baseInfoMap.put("authFlag", batchUserInfoModel.getAuthFlag());
			baseInfoMap.put("openBizFlag", batchUserInfoModel.getOpenBizFlag());
			baseInfoMap.put("batchCount", batchUserInfoModel.getBatchCount());

			// 调用适配层
			Map<String, Object> baseInfoResultMap = ossAdapter.invoke(0xa1,
					0x08, baseInfoMap);
			// 处理返回结果
			String baseInfoflag = (String) baseInfoResultMap.get("lteFlag");
			if ("0".equals(baseInfoflag)) {
				// 成功
				json.put("status", "0");
			} else {
				// 失败
				json.put("status", baseInfoflag);
				// 转换失败的具体原因
				json.put("message", LteFlag.flagReturn(baseInfoflag));
			}
			AjaxHelper.ajaxMethod(json.toString());
			log.debug("batchAddUserInfo-end");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("batchAddUserInfo error:", e);
			json.put("status", "-1");
			json.put("error", e.getLocalizedMessage());
			AjaxHelper.ajaxMethod(json.toString());
		}
		return NONE;
	}

	/**
	 * 查询批量开户结果
	 * 
	 * @return
	 */
	public String queryBatchOpenAccountResult() {
		log.debug("queryBatchOpenAccountResult-start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> request = new HashMap<String, Object>();
			// 查询进度
			Map<String, Object> response = ossAdapter.invoke(0xa1, 0x07,
					request);
			// 处理返回结果
			String result = (String) response.get("lteFlag");
			if ("0".equals(result)) {
				// 成功
				json.put("status", "0");
				json.put("percent", (String) response.get("batchOpenAccountPercent"));
			} else {
				// 失败
				json.put("status", result);
				// 转换失败的具体原因
				json.put("message", LteFlag.flagReturn(result));
				String failureFile = (String)response.get("failureFile");
				failureFile = (failureFile == null ? "" : failureFile);
				json.put("failureFile", failureFile);
			}
			AjaxHelper.ajaxMethod(json.toString());
			log.debug("queryBatchOpenAccountResult-end");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("queryBatchOpenAccountResult error:", e);
			json.put("status", "-1");
			json.put("error", e.getLocalizedMessage());
			AjaxHelper.ajaxMethod(json.toString());
		}
		return NONE;
	}

	/**
	 * 获取批量开户失败日志文件
	 * 
	 * @return
	 */
	public String getBatchOpenAccountFailureResult() {
		try {
			// fileName = "1.txt";
			String fileNameWithoutPath = getFileNameWithoutPath(fileName);
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileNameWithoutPath);
			File file = new File(fileName);
			if (file.exists()) {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				out.write(bytes);
			}
		} catch (Exception e) {
			log.error("Failed to getBatchOpenAccountFailureResult: ", e);
			return ERROR;
		}
		return NONE;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BatchUserInfoModel getBatchUserInfoModel() {
		return batchUserInfoModel;
	}

	public void setBatchUserInfoModel(BatchUserInfoModel batchUserInfoModel) {
		this.batchUserInfoModel = batchUserInfoModel;
	}

	/**
	 * 获取不带路径的文件名
	 * 
	 * @param fileName
	 * @return
	 */
	private String getFileNameWithoutPath(String fileName) {
		int index = fileName.lastIndexOf("/");
		if (index >= 0) {
			return fileName.substring(index + 1);
		} else {
			return fileName;
		}
	}

}
