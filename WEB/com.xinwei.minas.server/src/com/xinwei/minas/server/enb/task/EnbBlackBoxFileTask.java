
package com.xinwei.minas.server.enb.task;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.xinwei.minas.server.mcbts.utils.FtpClient;

import sun.util.logging.resources.logging;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

/**
 * 
 * Enb黑匣子文件任务
 * 
 * 
 * @author sunzhangbin
 * 
 */

public class EnbBlackBoxFileTask {

	private static Log log = LogFactory.getLog(EnbBlackBoxFileTask.class);

	// FTP地址
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

	private static final String SEPARATOR = File.separator;

	// 遍历ftpserver上的文件夹
	public List<String> listFtpFilePath(String firstFilePath,
			String ftpServerIp, int ftpPort, String ftpUsername,
			String ftpPassword) {
		List<String> FileList = null;
		try {
			FileList = FtpClient.getInstance().listAll(firstFilePath,
					ftpServerIp, ftpPort, ftpUsername, ftpPassword);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return FileList;
	}

	//
	public void makeFile(String filePath) {
		File file = new File(filePath);
		file.mkdir();
	}

	// 在本地创建和ftpserver一样的本地目录结构
	public void createLocalFilePath(String localFirstFilePath,
			String firstFilePath, String ftpServerIp, int ftpPort,
			String ftpUsername, String ftpPassword) {
		String localPath = localFirstFilePath + SEPARATOR + firstFilePath;
		// 在本地创建一级目录
		if (!new File(localPath).exists()) {
			makeFile(localPath);
		}
		// 遍历ftpserver上的第一级目录，获取对应的enb目录
		List<String> ftpFileList = listFtpFilePath(firstFilePath, ftpServerIp,
				ftpPort, ftpUsername, ftpPassword);
		for (String filePath : ftpFileList) {
			if (!new File(localPath + SEPARATOR + filePath).exists()) {
				makeFile(localPath + SEPARATOR + filePath);

			}
			List<String> ftpBlackBoxFileList = listFtpFilePath(firstFilePath
					+ "/" + filePath, ftpServerIp, ftpPort, ftpUsername,
					ftpPassword);
			for (String blackBoxFile : ftpBlackBoxFileList) {
				try {
					FtpClient.getInstance()
							.fetchFile(
									firstFilePath + "/" + filePath + "/"
											+ blackBoxFile,
									localPath + SEPARATOR + filePath
											+ SEPARATOR + blackBoxFile,
									ftpServerIp, ftpPort, ftpUsername,
									ftpPassword);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String getFtpServerIp() {
		return ftpServerIp;
	}

	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFirstFilePath() {
		return firstFilePath;
	}

	public void setFirstFilePath(String firstFilePath) {
		this.firstFilePath = firstFilePath;
	}

	public String getLocalFirstFilePath() {
		return localFirstFilePath;
	}

	public void setLocalFirstFilePath(String localFirstFilePath) {
		this.localFirstFilePath = localFirstFilePath;
	}

}
