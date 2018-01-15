/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-14	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.xinwei.minas.server.mcbts.utils.FtpClient;

import sun.util.logging.resources.logging;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * Enb黑匣子文件管理
 * 
 * 
 * @author sunzhangbin
 * 
 */

public class EnbBlackBoxFileTaskManager {

	private static final Log log = LogFactory
	.getLog(EnbBlackBoxFileDeleteTask.class);
	private EnbBlackBoxFileTask enbBlackBoxFileTask;
	private static String ftpServerIp;

	// FTP用户名
	private static String ftpUsername;

	// FTP密码
	private static String ftpPassword;

	// FTP端口
	private static int ftpPort;

	// 黑匣子文件再FTP中的一级目录
	private static String firstFilePath;
	// 本地文件的第一级目录
	private static String localFirstFilePath;

	//復制文件的定時任務
	public void doTask() {
		log.debug("在本地创建和ftpserver一样的本地目录结构并复制文件");
		enbBlackBoxFileTask.createLocalFilePath(localFirstFilePath,
				firstFilePath, ftpServerIp, ftpPort, ftpUsername, ftpPassword);
		log.debug("在本地创建和ftpserver一样的本地目录结构并复制文件结束");

	}

	public static String getFtpServerIp() {
		return ftpServerIp;
	}

	public static void setFtpServerIp(String ftpServerIp) {
		EnbBlackBoxFileTaskManager.ftpServerIp = ftpServerIp;
	}

	public static String getFtpUsername() {
		return ftpUsername;
	}

	public static void setFtpUsername(String ftpUsername) {
		EnbBlackBoxFileTaskManager.ftpUsername = ftpUsername;
	}

	public static String getFtpPassword() {
		return ftpPassword;
	}

	public static void setFtpPassword(String ftpPassword) {
		EnbBlackBoxFileTaskManager.ftpPassword = ftpPassword;
	}

	public static int getFtpPort() {
		return ftpPort;
	}

	public static void setFtpPort(int ftpPort) {
		EnbBlackBoxFileTaskManager.ftpPort = ftpPort;
	}

	public static String getFirstFilePath() {
		return firstFilePath;
	}

	public static void setFirstFilePath(String firstFilePath) {
		EnbBlackBoxFileTaskManager.firstFilePath = firstFilePath;
	}

	public static String getLocalFirstFilePath() {
		return localFirstFilePath;
	}

	public static void setLocalFirstFilePath(String localFirstFilePath) {
		EnbBlackBoxFileTaskManager.localFirstFilePath = localFirstFilePath;
	}

	private EnbBlackBoxFileTaskManager() {
	}

	private static final EnbBlackBoxFileTaskManager instance = new EnbBlackBoxFileTaskManager();

	public static EnbBlackBoxFileTaskManager getInstance() {
		return instance;

	}

	public EnbBlackBoxFileTask getEnbBlackBoxFileTask() {
		return enbBlackBoxFileTask;
	}

	public void setEnbBlackBoxFileTask(EnbBlackBoxFileTask enbBlackBoxFileTask) {
		this.enbBlackBoxFileTask = enbBlackBoxFileTask;
	}

}
