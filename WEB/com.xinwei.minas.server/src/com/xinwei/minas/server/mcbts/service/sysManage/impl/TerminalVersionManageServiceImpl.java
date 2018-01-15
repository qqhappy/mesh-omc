/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.mcbts.dao.sysManage.TerminalVersionManageDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionManageService;
import com.xinwei.minas.server.mcbts.task.McBtsUTCodeDownloadTaskManager;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * 终端版本管理服务类
 * 
 * @author tiance
 * 
 */

public class TerminalVersionManageServiceImpl implements
		TerminalVersionManageService {

	private Log log = LogFactory.getLog(TerminalVersionManageServiceImpl.class);

	// FTP地址
	private String emsServerIp;

	// FTP用户名
	private String ftpUsername;

	// FTP密码
	private String ftpPassword;

	// FTP端口
	private int ftpPort;

	// FTP目标路径
	private String ftpTVPath;

	// 文件所在本地服务器路径
	private String utFolder;

	// 过期时间
	private static long DEAD_TIME;
	private McBtsBizProxy mcBtsBizProxy;
	private TerminalVersionManageDAO terminalVersionManageDao;
	private McBtsUTCodeDownloadTaskManager mcBtsUTCodeDownloadTaskManager;

	/**
	 * 获取服务器上的所有终端版本的列表
	 * 
	 */
	@Override
	public List<TerminalVersion> queryAll() {
		return terminalVersionManageDao.queryAll();
	}

	/**
	 * 根据typeId获取某个终端的所有版本
	 * 
	 */
	@Override
	public List<TerminalVersion> queryByTypeId(Integer typeId) {
		return terminalVersionManageDao.queryByTypeId(typeId);
	}

	/**
	 * 从服务器上删除一个终端版本
	 */
	@Override
	public void delete(TerminalVersion terminalVersion) {
		// 不从ems和ftp删除文件
		// boolean deleted = deleteFile(terminalVersion.getFileName());

		// 从数据库删除文件信息
		terminalVersionManageDao.delete(terminalVersion);
	}

	/**
	 * 根据typeId和version和fileType,从服务器获取某个终端版本信息
	 */
	@Override
	@Deprecated
	// 已改成服务器端验证
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) {
		return terminalVersionManageDao.queryByTypeIdAndVersion(typeId,
				version, fileType);
	}

	/**
	 * 更新终端版本
	 */
	// 修改操作已经去掉
	@Deprecated
	@Override
	public int modify(TerminalVersion terminalVersion) {
		// 从ems和ftp删除文件
		boolean deleted = deleteFile(terminalVersion.getFileName());

		if (deleted) {
			boolean uploaded = uploadFileToEms(terminalVersion);

			if (uploaded) {
				final String filename = terminalVersion.getFileName();

				new FTPAdd(filename).start();

				return terminalVersionManageDao.update(terminalVersion);
			}
		}
		return 0; // 0 为失败
	}

	/**
	 * 添加终端版本
	 */
	@Override
	public int add(TerminalVersion tv) {
		// 检验类型是否存在
		int count = terminalVersionManageDao.queryForType(tv.getTypeId());

		if (count == 0)
			return TerminalVersion.NO_TYPE;

		// 检验是否已存在
		TerminalVersion exists = terminalVersionManageDao
				.queryByTypeIdAndVersion(tv.getTypeId(), tv.getVersion(),
						Integer.parseInt(tv.getFileType()));

		if (exists != null)
			return TerminalVersion.EXISTS;

		// 上传到EMS
		boolean uploaded = uploadFileToEms(tv);

		if (!uploaded)
			return TerminalVersion.ERROR_UPLOADING_TO_EMS;

		// 上传到FTP
		final String filename = tv.getFileName();
		try {
			ftpAdd(filename);
		} catch (Exception e) {
			log.error("Error uploading terminal version to FTP.", e);
			return TerminalVersion.ERROR_UPLOADING_TO_FTP;
		}

		// 保存到数据库
		return terminalVersionManageDao.add(tv);

	}

	/**
	 * 上传到FTP
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	private void ftpAdd(String fileName) throws Exception {
		FtpClient.getInstance().uploadFile(ftpTVPath,
				utFolder + File.separator + fileName, emsServerIp, ftpPort,
				ftpUsername, ftpPassword);
	}

	/**
	 * 异步向ftp添加文件
	 * 
	 * @author tiance
	 * 
	 */
	@Deprecated
	private class FTPAdd extends Thread {
		private String fileName;

		public FTPAdd(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public void run() {
			doWork();
		}

		private void doWork() {
			try {

			} catch (Exception e) {
				log.error("FTP服务器上传文件操作出错", e);
			}
		}
	}

	/**
	 * 将终端版本下载到FTP服务器,并通知基站获取终端版本
	 * 
	 * @return 1: 如果之前的状态没有结束的话, 0: 如果下载成功, -1:基站应答超时
	 */
	@Override
	public int download(Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws Exception {

		McBts mcBts = McBtsCache.getInstance().queryByMoId(mo.getMoId());
		if (!mcBts.isConfigurable())
			return TerminalVersion.BTS_INVALID;

		String fileName = terminalVersion.getFileName();
		try {
			UTCodeDownloadTask task = mcBtsUTCodeDownloadTaskManager
					.getUTTask(btsId);

			if (task != null) {
				if (!task.isFinished()) {
					if (!task.isOverTime(DEAD_TIME)) {// 任务未完成且任务未超时
						return UTCodeDownloadTask.PREVIOUS_TASK_STATUS_UNDONE; // 有其它任务正在进行
					} else if (task.isOverTime(DEAD_TIME)) {
						mcBtsUTCodeDownloadTaskManager.finishTask(btsId,
								UTCodeDownloadTask.OVERTIME); // 超时
					}
				}
			}
			// 遍历ftp文件夹里的所有文件名,如果找到filename,就不再下载
			if (!fileExistsInFTP(fileName)) {
				File file = new File(utFolder + File.separator + fileName);

				if (!file.exists()) {
					return TerminalVersion.NO_FILE_ON_EMS;
				}

				ftpAdd(fileName);
				log.info("上传文件到ftp");
			}
			// 创建新的任务
			TerminalVersion tv = terminalVersionManageDao
					.queryByTypeIdAndVersion(terminalVersion.getTypeId(),
							terminalVersion.getVersion(),
							Integer.parseInt(terminalVersion.getFileType()));

			task = new UTCodeDownloadTask(tv);

			GenericBizData data = new GenericBizData("terminalVersionDownload");
			data.addProperty(new GenericBizProperty("ftpIp", emsServerIp));
			data.addProperty(new GenericBizProperty("ftpPort", ftpPort));
			data.addProperty(new GenericBizProperty("ftpUserName", ftpUsername));
			data.addProperty(new GenericBizProperty("ftpPassword", ftpPassword));
			data.addProperty(new GenericBizProperty("filePath", ftpTVPath));
			data.addProperty(new GenericBizProperty("fileName", fileName));

			mcBtsBizProxy.config(mo.getMoId(), data);

			mcBtsUTCodeDownloadTaskManager.addUTTask(btsId, task);
		} catch (IOException ioe) {
			throw ioe;
		} catch (Exception e) {
			log.error("发送终端版本下载请求失败", e);
			return TerminalVersion.BTS_INVALID;
		}

		return TerminalVersion.SUCCESS; // 成功
	}

	private boolean fileExistsInFTP(String fileName) throws Exception {
		List<String> filenames;
		try {
			filenames = FtpClient.getInstance().listAll(ftpTVPath, emsServerIp,
					ftpPort, ftpUsername, ftpPassword);

			for (String _filename : filenames) {
				if (_filename.equals(fileName)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw new IOException("ftp");
		}

	}

	/**
	 * 从数据库获得所有下载历史
	 * 
	 * @return
	 * 
	 */
	@Override
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long btsId) {
		return mcBtsUTCodeDownloadTaskManager.getAllMcBtsUTTasksFromHistory(
				btsId, DEAD_TIME);
	}

	/**
	 * 通过TDLHistoryKey从后台history中查询一个下载版本的最新状态
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId) {
		return mcBtsUTCodeDownloadTaskManager.getUTTask(btsId);
	}

	/**
	 * 删除历史记录里的记录
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int deleteHistory(Long btsId, UTCodeDownloadTask task) {
		mcBtsUTCodeDownloadTaskManager.removeUTTask(btsId);
		return terminalVersionManageDao.deleteFromHistory(btsId, task);
	}

	/**
	 * 删除某个网元的所有历史记录
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHis(Long btsId) {
		mcBtsUTCodeDownloadTaskManager.removeUTTask(btsId);
		return terminalVersionManageDao.deleteAllFromHistory(btsId);
	}

	/**
	 * 从ems和ftp删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean deleteFile(String fileName) {
		String filePath = utFolder + fileName;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			if (file.exists())
				return false;
		}

		try {
			// 从服务器删除
			FtpClient.getInstance().delete(
					ftpTVPath + File.separator + fileName, emsServerIp,
					ftpPort, ftpUsername, ftpPassword);
		} catch (Exception e) {
			log.error("Error deleting a mcbts version from ftp", e);
			return false;
		}

		return true;
	}

	/**
	 * 把文件放到ems服务器
	 * 
	 * @param terminalVersion
	 * @return
	 */
	private boolean uploadFileToEms(TerminalVersion terminalVersion) {
		// 创建路径
		File content = new File(utFolder);
		if (!content.exists()) {
			content.mkdirs();
		}
		String filePath = utFolder + File.separator
				+ terminalVersion.getFileName();

		try {
			File file = new File(filePath);
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
			FileUtils.writeByteArrayToFile(file,
					terminalVersion.getContentStr());
			log.debug("File uploaded to EMS.");
		} catch (Exception e) {
			log.error("Uploading file to EMS", e);
			return false;
		}

		return true;
	}

	/*
	 * 下边的是spring配置文件的设值操作
	 */
	public void setEmsServerIp(String emsServerIp) {
		this.emsServerIp = emsServerIp;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public void setFtpTVPath(String ftpTVPath) {
		this.ftpTVPath = ftpTVPath;
	}

	public void setUtFolder(String utFolder) {
		this.utFolder = utFolder;
	}

	public static void setDEAD_TIME(long dEAD_TIME) {
		DEAD_TIME = dEAD_TIME;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setTerminalVersionManageDao(
			TerminalVersionManageDAO terminalVersionManageDao) {
		this.terminalVersionManageDao = terminalVersionManageDao;
	}

	public void setMcBtsUTCodeDownloadTaskManager(
			McBtsUTCodeDownloadTaskManager mcBtsUTCodeDownloadTaskManager) {
		this.mcBtsUTCodeDownloadTaskManager = mcBtsUTCodeDownloadTaskManager;
	}

	@Override
	public List<TerminalVersionType> queryAllType() {
		return terminalVersionManageDao.queryAllType();
	}

	@Override
	public void addType(TerminalVersionType model) {
		// 入库
		// Id要前面补充1后面补充0
		model.setIdx(((model.getIdx() + 1000) * 100));
		terminalVersionManageDao.addType(model);

	}

	@Override
	public int modifyType(TerminalVersionType model) throws RemoteException,
			Exception {
		return terminalVersionManageDao.modifyType(model);
	}

	@Override
	public void deleteType(TerminalVersionType model) throws RemoteException,
			Exception {
		terminalVersionManageDao.deleteType(model.getIdx());

	}
}
