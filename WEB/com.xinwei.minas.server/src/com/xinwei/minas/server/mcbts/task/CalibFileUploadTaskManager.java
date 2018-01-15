/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationResultService;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * 处理校准结果发送到FTP后发送的消息
 * 
 * 
 * @author tiance
 * 
 */

public class CalibFileUploadTaskManager {

	private static final Log log = LogFactory
			.getLog(CalibFileUploadTaskManager.class);

	private String ftpPath;
	private String localPath;
	private String ftpIp;
	private String ftpPort;
	private String ftpUsername;
	private String ftpPassword;

	private static CalibFileUploadTaskManager instance = null;

	private CalibrationResultService service;

	public static CalibFileUploadTaskManager getInstance() {
		if (instance == null)
			return new CalibFileUploadTaskManager();
		return instance;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public static void setInstance(CalibFileUploadTaskManager instance) {
		CalibFileUploadTaskManager.instance = instance;
	}

	public void handleCalibFileNotify(McBtsMessage message) {
		service = AppContext.getCtx().getBean(CalibrationResultService.class);
		try {
			byte[] body = message.getContent();
			String fileName = ByteUtils.toString(body, 0, body.length,
					"US-ASCII").trim();

			if (fileName == null || fileName.length() == 0)
				return;

			long btsId = Long.parseLong(fileName.split("_")[0]);
			McBts mcbts = McBtsCache.getInstance().queryByBtsId(btsId);

			String folderName = localPath + File.separator + "bts_"
					+ String.valueOf(StringUtils.to8HexString(btsId));

			File folder = new File(folderName);

			// 如果没有folder,就创建
			if (!folder.exists())
				folder.mkdir();

			File[] files = listFilesByBtsId(folder, btsId);
			// 删除当前除了最新的校准结果外的所有其它结果,等待从ftp拷入更新的
			if (files != null && files.length >= 2) {
				sortFile(files);
				for (int i = 1; i < files.length; i++) {
					files[i].delete();
				}
			}

			// 把文件从ftp移到服务器目录
			FtpClient.getInstance().fetchFile(
					ftpPath + File.separator + fileName,
					folderName + File.separator + fileName, ftpIp,
					Integer.parseInt(ftpPort), ftpUsername, ftpPassword);
			log.debug("从FTP下载校准结果文件成功, 文件名: " + fileName + ". 路径: "
					+ folderName + "/");

			// 将校准结果的一部分普通信息存库
			service.updateCalibGenericConfig(mcbts.getMoId(), new File(
					folderName + File.separator + fileName));
		} catch (Exception e) {
			// 一般情况下,如果FTP没有配置成功,这里会报错(如:没有将ftp ip从127.0.0.1修改为server真实ip)
			log.error(
					"Error fetching calib file and updating database for calib result! "
							+ "FTP Server may not configured succrssfully.", e);
		}
	}

	private File[] listFilesByBtsId(File folder, final long btsId) {
		return folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(String.valueOf(btsId)))
					return true;
				else
					return false;
			}
		});
	}

	private void sortFile(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				long name1 = Long.parseLong(f1.getName().split("_")[1]);
				long name2 = Long.parseLong(f2.getName().split("_")[1]);

				if (name1 > name2)
					return -1;
				else if (name1 < name2)
					return 1;
				return 0;
			}
		});
	}

}
