/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

/**
 * 
 * IMSI批量操作Action
 * 
 * @author chenjunhua
 * 
 */
public class UserImsiBatchAction extends ActionSupport {

	private static final Log log = LogFactory.getLog(UserImsiBatchAction.class);

	// 最大文件大小(byte)
	private static final int MAX_FILE_BYTES = 1 * 1024 * 1024;

	@Resource
	private OssAdapter ossAdapter;

	// IMSI文件内容
	private File file;

	// IMSI文件名
	private String fileFileName;

	// IMSI文件上传后保存的路径
	@Value("${imsiUploadPath}")
	private String imsiUploadPath;

	// IMSI文件批量执行结果文件的路径
	@Value("${imsiResultPath}")
	private String imsiResultPath;

	// 操作结果文件名
	private String resultFileName;

	// 错误提示信息
	private String errorMessage = "";
	
	private static int percent = 0;

	/**
	 * 批量增加IMSI
	 * 
	 * @return
	 */
	public String batchAddImsi() {
		JSONObject json = new JSONObject();
		File srcFile = file;
		// 生成缺省的目标文件名
		String destFileName = generateDefaultImsiFileName();
		File destFile = new File(imsiUploadPath, destFileName);		
		if (FileUtils.sizeOf(srcFile) > MAX_FILE_BYTES) {
			// 判断文件大小
			errorMessage = "文件不能大于1M.";
		} else {
			// 将上传的文件拷贝到指定的目录下
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (Exception e) {
				log.error("failed to copy file " + srcFile + " to " + destFile,
						e);
				errorMessage = "文件上传失败.";
			}
		}
		if (StringUtils.isEmpty(errorMessage)) {
			// 通知网元执行批量操作
			try {
				// 构造请求数据
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("imsiFile", imsiUploadPath + "/" + destFileName);
				// 调用适配层发送消息
				OssAdapterInputMessage req = new OssAdapterInputMessage(0xa0,
						0x08, data);
				OssAdapterOutputMessage resp = ossAdapter.invoke(req);
				if (resp.isFailed()) {
					// 应答失败
					errorMessage = resp.getReason();
				}
			} catch (Exception e) {
				log.error("failed to send batch add imsi command.", e);
				errorMessage = "向网元发送批量增加IMSI指令执行失败.";
			}
		}
		if (StringUtils.isEmpty(errorMessage)) {
			// 成功
			json.put("status", "0");
		} else {
			// 失败
			json.put("status", "1");
			// 转换失败的具体原因
			json.put("message", errorMessage);
		}
		AjaxHelper.ajaxMethod(json.toString());
		return NONE;
	}

	/**
	 * 查询操作进度
	 * 
	 * @return
	 */
	public String queryBatchAddImsiResult() {
		log.debug("queryBatchAddImsiResult-start");
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			// 调用适配层发送消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xa0, 0x07,
					data);
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			// 处理返回结果
			if (resp.isSuccesful()) {
				// 成功
				json.put("status", "0");
				json.put("batchAddImsiPercent",
						resp.getStringValue("batchAddImsiPercent"));
				json.put("batchAddImsiResultFile",
						resp.getStringValue("batchAddImsiResultFile"));
			} else {
				// 失败
				json.put("status", resp.getResult());
				json.put("message", resp.getReason());
			}
			AjaxHelper.ajaxMethod(json.toString());
			log.debug("queryBatchAddImsiResult-end");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("queryBatchAddImsiResult error:", e);
			json.put("status", "-1");
			json.put("error", "查询批量增加IMSI进度失败");
			AjaxHelper.ajaxMethod(json.toString());
		}
			
		return NONE;
	}

	/**
	 * 获取批量增加IMSI结果日志文件
	 * 
	 * @return
	 */
	public String obtainResultFile() {
		try {
			String fileNameWithoutPath = getFileNameWithoutPath(resultFileName);
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileNameWithoutPath);
			File file = new File(resultFileName);
			if (file.exists()) {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				out.write(bytes);
			}
		} catch (Exception e) {
			log.error("Failed to get result file: ", e);
			return ERROR;
		}
		return NONE;
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

	/**
	 * 构造上传后的IMSI文件名
	 * 
	 * @return
	 */
	private String generateDefaultImsiFileName() {		
		String fileName = "IMSI_"
				+ DateUtils.getBriefTimeFromMillisecondTime(System
						.currentTimeMillis()) + ".csv";
		return fileName;
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

	public String getImsiUploadPath() {
		return imsiUploadPath;
	}

	public void setImsiUploadPath(String imsiUploadPath) {
		this.imsiUploadPath = imsiUploadPath;
	}

	public String getImsiResultPath() {
		return imsiResultPath;
	}

	public void setImsiResultPath(String imsiResultPath) {
		this.imsiResultPath = imsiResultPath;
	}

	public String getResultFileName() {
		return resultFileName;
	}

	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
