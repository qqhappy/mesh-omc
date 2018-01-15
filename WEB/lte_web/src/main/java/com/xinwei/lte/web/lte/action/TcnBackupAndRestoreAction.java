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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.action.Utils.FileNameFilter;
import com.xinwei.lte.web.lte.model.TcnBackupFile;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * TCN1000数据库备份和恢复Action
 * 
 * @author chenjunhua
 * 
 */
public class TcnBackupAndRestoreAction extends ActionSupport {
	
	/**
	 * 文件路径
	 */
	@Value("${backup.path}")
	private String backupFilePath;

	private Log log = LogFactory.getLog(getClass());

	// 查询的备份文件列表
	private List<TcnBackupFile> backupFiles;

	/**
	 * 是否已上传,1：是 2:否
	 */
	private int isUploaded;

	/**
	 * 上传的文件
	 */
	private File file;

	/**
	 * 上传文件名
	 */
	private String fileFileName;

	/**
	 * 用于校验的文件名
	 */
	private String checkFileName;

	/**
	 * 异常
	 */
	private String error;

	@Resource
	private OssAdapter ossAdapter;

	/**
	 * 查询所有备份文件
	 * 
	 * @return 备份文件列表
	 */
	public String queryBackupFiles() {
		backupFiles = new ArrayList<TcnBackupFile>();
		String regex = ".+\\.(SQL|sql)";
		// 得到目录下文件列表，已根据文件后缀过滤
		String[] names = getFileLists(getBackupFilePath(), regex);
		if (names != null && names.length > 0) {
			Arrays.sort(names, Collections.reverseOrder());
			for (String name : names) {
				backupFiles.add(new TcnBackupFile(name));
			}
		}
		return SUCCESS;
	}

	/**
	 * 跳转至新增备份文件界面
	 * 
	 * @return
	 */
	public String toAddRestoreHtml() {
		return SUCCESS;
	}

	/**
	 * 上传备份文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	public String uploadBackupFile() {
		FileInputStream fin = null;
		try {
			// 将文件转为byte[]
			fin = new FileInputStream(file);
			byte[] bt = new byte[fin.available()];
			fin.read(bt);
			fin.close();
			// 写文件
			String objPath = getBackupFilePath() + "/" + fileFileName;
			FileOutputStream fout = new FileOutputStream(objPath);
			fout.write(bt);
			fout.close();
			isUploaded = 1;
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			isUploaded = 2;
		}finally{
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	/**
	 * 下载备份文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public String downloadBackupFile() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("Application/msexcel;charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileFileName);
		ServletOutputStream out = null;
		FileInputStream fin =  null;
		try {
			out = response.getOutputStream();
			// 获取文件字节流
			String objPath = getBackupFilePath() + "/" + fileFileName;
			File file = new File(objPath);
			fin = new FileInputStream(file);
			byte[] bt = new byte[fin.available()];
			fin.read(bt);
			fin.close();
			// 字节流写入输出流
			out.write(bt);
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}finally{
			try {
				fin.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}

	/**
	 * 删除备份文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public String deleteBackupFile() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String objPath = getBackupFilePath() + "/" + fileFileName;
			File file = new File(objPath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		}
		out.println(error);
		out.flush();
		out.close();
		return NONE;
	}

	/**
	 * 备份数据库
	 * 
	 * @return
	 */
	public String backup() {
		log.debug("TCN1000--backup database begin");
		JSONObject json = new JSONObject();
		try {
			// 基本信息
			Map<String, Object> request = new HashMap<String, Object>();
			// 调用适配层
			Map<String, Object> response = ossAdapter.invoke(0xb6, 0x09,
					request);
			// 处理返回结果
			String result = (String) response.get("lteFlag");
			if ("0".equals(result)) {
				// 成功
				json.put("status", "0");
			} else {
				// 失败
				json.put("status", result);
				// 转换失败的具体原因
				json.put("message", LteFlag.flagReturn(result));
			}

			log.debug("TCN1000--backup database OK.");
		} catch (Exception e) {
			json.put("status", "-1");
			log.error("TCN1000--backup database failure", e);
			json.put("error", e.getLocalizedMessage());
		}
		// AJAX返回Json
		AjaxHelper.ajaxMethod(json.toString());
		return NONE;
	}

	/**
	 * 按指定备份文件恢复数据库
	 * 
	 * @param fileName
	 *            备份文件名
	 * @return
	 * @throws Exception
	 */
	public String restore() {
		log.debug("TCN1000--restore database begin, fileName=" + fileFileName);
		JSONObject json = new JSONObject();
		try {
			// 基本信息
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("backupFile", fileFileName);
			// 调用适配层
			Map<String, Object> response = ossAdapter.invoke(0xb6, 0x0a,
					request);
			// 处理返回结果
			String result = (String) response.get("lteFlag");
			if ("0".equals(result)) {
				// 成功
				json.put("status", "0");
			} else {
				// 失败
				json.put("status", result);
				// 转换失败的具体原因
				json.put("message", LteFlag.flagReturn(result));
			}
			// AJAX返回Json

			log.debug("TCN1000--restore database OK. fileName=" + fileFileName);
		} catch (Exception e) {
			log.error("TCN1000--restore database failure, fileName="
					+ fileFileName, e);
			json.put("status", "-1");
			json.put("error", e.getLocalizedMessage());
		}
		AjaxHelper.ajaxMethod(json.toString());
		return NONE;
	}

	/**
	 * 根据路径和校验正则获得文件名列表
	 * 
	 * @param path
	 *            路径
	 * @param regex
	 *            校验的正则
	 * @return
	 */
	private String[] getFileLists(String path, String regex) {
		File file = new File(path);
		return file.list(new FileNameFilter(regex));
	}

	/**
	 * 校验是否有重名文件 0 :正常 1：有重名
	 * 
	 * @return
	 */
	public String checkRepetitiveName() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		// 0 :正常 1：有重名
		int fileExsitFlag = 0;
		try {
			out = response.getWriter();
			String regex = ".+\\.(SQL|sql)";
			String[] fileLists = getFileLists(getBackupFilePath(), regex);
			if (fileLists != null && fileLists.length > 0) {
				for (String name : fileLists) {
					if (checkFileName.equals(name)) {
						fileExsitFlag = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		out.println(fileExsitFlag);		
		return NONE;
	}

	public List<TcnBackupFile> getBackupFiles() {
		return backupFiles;
	}

	public void setBackupFiles(List<TcnBackupFile> backupFiles) {
		this.backupFiles = backupFiles;
	}

	public int getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(int isUploaded) {
		this.isUploaded = isUploaded;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCheckFileName() {
		return checkFileName;
	}

	public void setCheckFileName(String checkFileName) {
		this.checkFileName = checkFileName;
	}

	public String getBackupFilePath() {
//		return TcnBackupAndRestoreAction.class.getResource("/").getPath().toString();
		return backupFilePath;
	}

	public void setBackupFilePath(String backupFilePath) {
		this.backupFilePath = backupFilePath;
	}
}
