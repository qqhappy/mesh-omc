/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.proxy.EnbFileManagerProxy;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionHistoryManageDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionManageDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsTypeDDService;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsVersionManageService;
import com.xinwei.minas.server.mcbts.task.McBtsCodeDownloadTaskManager;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站版本管理服务类
 * 
 * @author chenshaohua
 * 
 */

public class McBtsVersionManageServiceImpl implements McBtsVersionManageService {

	private Log log = LogFactory.getLog(McBtsVersionManageServiceImpl.class);

	private McBtsVersionManageDAO mcBtsVersionManageDAO;

	private McBtsVersionHistoryManageDAO mcBtsVersionHistoryManageDAO;

	private McBtsBizProxy mcBtsBizProxy;

	private McBtsTypeDDService mcBtsTypeDDService;

	private EnbBizConfigService enbBizConfigService;

	private EnbFileManagerProxy enbFileManagerProxy;

	private EnbFullTableConfigService enbFullTableConfigService;
	// FTP地址
	private String emsServerIp;

	// FTP用户名
	private String ftpUsername;
	// enb Ftp 用户名
	private String enbFtpUsername;
	// FTP密码
	private String ftpPassword;

	// FTP端口
	private int ftpPort;

	// FTP目标路径
	private String ftpSoftwarePath;

	// 文件所在本地服务器路径
	private String btsFolder;

	// 过期时间
	private static long DEAD_TIME;

	// enb的ftp目标路径
	private String enbFtpPath;

	// enb文件所在本地服务器路径
	private String enbFolder;

	private McBtsCodeDownloadTaskManager mcBtsCodeDownloadTaskManager;

	/**
	 * 查询所有基站版本
	 */
	@Override
	public List<McBtsVersion> queryAll() throws Exception {
		List<McBtsVersion> result = mcBtsVersionManageDAO.queryAll();

		// 遍历所有获得的基站版本,然后为每个版本添加基站版本名称
		for (McBtsVersion mcBtsVersion : result) {
			mcBtsVersion.setBtsTypeName(getParentTypeName(mcBtsVersion
					.getBtsType()));
		}
		return result;
	}

	/**
	 * 从数据库删除基站版本
	 */
	@Override
	public void delete(McBtsVersion mcBtsVersion) throws Exception {

		mcBtsVersionManageDAO.delete(mcBtsVersion);
	}

	/**
	 * 更新数据库中的基站版本
	 */
	@Override
	public void saveOrUpdate(McBtsVersion mcbtsVersion) throws Exception {
		mcBtsVersionManageDAO.saveOrUpdate(mcbtsVersion);
	}

	/**
	 * 下载基站软件
	 * <p>
	 * 从服务器传到FTP,然后发送消息请求基站下载软件
	 * </p>
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersionz
	 * @return
	 * @throws ExceptionF
	 */
	@Override
	public int download(Mo mo, Long btsId, McBtsVersion mcBtsVersion)
			throws Exception {
		// 验证网元是否可配置，不存在或不可配置则抛出异常
		checkConfigurable(mo.getMoId());

		// 验证要下载的版本网管是否支持
		checkVersionSupported(mo, mcBtsVersion.getVersionName());

		try {
			McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
					.getBtsTask(btsId);
			// 如果当前有未完成的任务
			if (task != null && !task.isFinished()) {
				if (!task.isOverTime(DEAD_TIME)) {
					// 如果任务未超时，则给前台返回状态码
					return McBtsCodeDownloadTask.PREVIOUS_TASK_STATUS_UNDONE;
				} else if (task.isOverTime(DEAD_TIME)) {
					// 如果任务已超时，则结束该任务，设置结果为已超时
					mcBtsCodeDownloadTaskManager.finishTask(btsId,
							McBtsCodeDownloadTask.OVERTIME);
				}
			}
			// 上传版本文件
			uploadVersionToFtp(mo, mcBtsVersion);

			// 创建新的下载任务
			McBtsVersionHistory mvh = mcBtsVersionHistoryManageDAO
					.queryByBtsIdAndVersion(btsId,
							mcBtsVersion.getVersionName());
			if (mvh != null) {
				// 任务曾经被下载过,只需要修改开始时间,结束时间和状态
				mvh.setActionResult(McBtsCodeDownloadTask.UNDONE);
				mvh.setStartTime(new Date());
				mvh.setEndTime(null);
			} else {
				// 创建新的下载历史,准备存库
				mvh = new McBtsVersionHistory(btsId, mcBtsVersion.getBtsType(),
						mcBtsVersion.getVersionName(),
						McBtsCodeDownloadTask.UNDONE, new Date());
			}
			// 创建下载任务
			task = new McBtsCodeDownloadTask(mvh);
			// 添加任务到缓存
			mcBtsCodeDownloadTaskManager.addBtsTask(btsId, task);

			// 下载版本下载请求
			if (mo.getTypeId() == MoTypeDD.ENODEB) {
				// enb基站
				sendEnbCodeDownloadRequest(btsId, mcBtsVersion);
			} else if (mo.getTypeId() == MoTypeDD.MCBTS) {
				// McWILL基站
				sendMcBtsCodeDownloadRequest(mo.getMoId(), mcBtsVersion);
			}
		} catch (Exception e) {
			log.error("send version download request failed. version="
					+ mcBtsVersion.getVersionName(), e);
			try {
				undoDownload(btsId);
			} catch (Exception e2) {
				log.error("undo version download task failed. version="
						+ mcBtsVersion.getVersionName() + e2);
			}
			throw e;
		}

		return 0;
	}

	/**
	 * 下载过程报错时，删除下载任务，更新下载历史
	 * 
	 * @param btsId
	 */
	private void undoDownload(long btsId) throws Exception {
		McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
				.getBtsTask(btsId);
		// 删除下载任务
		mcBtsCodeDownloadTaskManager.removeBtsTask(btsId);
		McBtsVersionHistory history = task.getMcBtsVersionHistory();
		history.setActionResult(McBtsCodeDownloadTask.DOWNLOAD_WITH_ERROR);
		// 更新下载历史
		mcBtsVersionHistoryManageDAO.saveOrUpdate(history);
	}

	/**
	 * 判断ftp上是否存在该版本文件，如果不存在，则上传
	 * 
	 * @param mo
	 * @param version
	 * @throws Exception
	 */
	private void uploadVersionToFtp(Mo mo, McBtsVersion version)
			throws Exception {
		// 遍历FTP文件夹里的所有文件名,如果找到filename,就不再下载(分enb和mcBts两种)
		String ftpPath = (mo.getTypeId() == MoTypeDD.ENODEB) ? enbFtpPath
				: ftpSoftwarePath;
		String emsLocalFolder = (mo.getTypeId() == MoTypeDD.ENODEB) ? enbFolder
				: btsFolder;
		String ftpName = (mo.getTypeId() == MoTypeDD.ENODEB) ? enbFtpUsername
				: ftpUsername;
		String targetFileName = version.getFileName();
		boolean exists = false;
		try {
			List<String> filenames = FtpClient.getInstance().listAll(ftpPath,
					emsServerIp, ftpPort, ftpName, ftpPassword);
			for (String _filename : filenames) {
				if (_filename.equals(targetFileName)) {
					exists = true;
					break;
				}
			}
		} catch (Exception e) {
			throw new IOException("ftp");
		}

		// 如果文件不存在,就上传文件到FTP
		if (!exists) {
			FtpClient.getInstance().uploadFile(ftpPath,
					emsLocalFolder + File.separator + targetFileName,
					emsServerIp, ftpPort, ftpName, ftpPassword);
			log.info("upload version file to ftp, ftpPath=" + ftpPath);
		}
	}

	/**
	 * 下载McWILL基站版本下载请求
	 * 
	 * @param moId
	 * @param mcBtsVersion
	 * @throws Exception
	 */
	private void sendMcBtsCodeDownloadRequest(long moId,
			McBtsVersion mcBtsVersion) throws Exception {
		GenericBizData data = new GenericBizData("mcBtsVersionDownload");
		data.addProperty(new GenericBizProperty("ftpIp", emsServerIp));
		data.addProperty(new GenericBizProperty("ftpPort", ftpPort));
		data.addProperty(new GenericBizProperty("ftpUserName", ftpUsername));
		data.addProperty(new GenericBizProperty("ftpPassword", ftpPassword));
		data.addProperty(new GenericBizProperty("filePath", ftpSoftwarePath));
		data.addProperty(new GenericBizProperty("softwareType", 0));
		data.addProperty(new GenericBizProperty("version", mcBtsVersion
				.getVersionName()));

		mcBtsBizProxy.config(moId, data);
	}

	/**
	 * 校验要下载的版本是否支持
	 * 
	 * @param mo
	 * @param version
	 * @throws Exception
	 */
	private void checkVersionSupported(Mo mo, String version) throws Exception {
		if (mo.getTypeId() == MoTypeDD.ENODEB) {
			String protocolVersion = version.substring(0,
					version.lastIndexOf("."));
			// 获取所有支持的协议版本
			Set<String> protocolVersions = EnbBizHelper
					.getAllProtocolVersions(mo);
			if (!protocolVersions.contains(protocolVersion)) {
				throw new Exception(
						OmpAppContext
								.getMessage("version_not_supported_cannot_download"));
			}
		}
	}

	/**
	 * 判断网元是否可以进行配置，如不存在或不可配置，则抛出异常
	 * 
	 * @param moId
	 * @throws Exception
	 */
	private void checkConfigurable(long moId) throws Exception {
		Mo mo = MoCache.getInstance().queryByMoId(moId);
		if (mo == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (mo.getTypeId() == MoTypeDD.MCBTS) {
			McBts mcBts = (McBts) mo;
			if (!mcBts.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_can_not_config_alert"));
			}
		} else if (mo.getTypeId() == MoTypeDD.ENODEB) {
			Enb enb = (Enb) mo;
			if (!enb.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("enb_can_not_download_error"));
			}
		}
	}

	/**
	 * 下发enb版本下载请求
	 * 
	 * @param enbId
	 * @param mcBtsVersion
	 * @throws Exception
	 */
	private void sendEnbCodeDownloadRequest(Long enbId,
			McBtsVersion mcBtsVersion) throws Exception {
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb == null)
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		if (!enb.isConfigurable())
			throw new Exception(
					OmpAppContext.getMessage("enb_can_not_download_error"));
		if (enb.setFullTableOperation(true)) {// 可以配置
			enb.setBizName("enb_software_version_download");
			try {
				GenericBizData enbData = new GenericBizData("");
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_IP, emsServerIp));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_PORT, ftpPort));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_USER_NAME, enbFtpUsername));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FTP_PASSWORD, ftpPassword));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.FILE_DIRECTORY, enbFtpPath));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.SOFTWARE_TYPE,
						EnbConstantUtils.BBU_RRU));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.VERSION, mcBtsVersion.getVersionName()));
				// / 数据文件路径；
				String dataFileDirectory = EnbFullTableTaskManager
						.getInstance().getConfigFtpdDrectory();
				// 数据文件名
				String dataFileName = enbFullTableConfigService
						.generateFullTableSqlFile(enb.getMoId(), EnbBizHelper
								.getProtocolVersion(mcBtsVersion
										.getVersionName()));

				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.DATA_FILE_DIRECTORY, dataFileDirectory));
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.DATA_FILE_NAME, dataFileName));

				enbFileManagerProxy.enbVersionDownloadConfig(enbId, enbData);
			} catch (Exception e) {
				// 出现错误释放资源；
				enb.setFullTableOperation(false);
				log.error(e);
				throw e;
			}

		} else {
			// 抛出不可配置的异常
			throw new Exception(
					OmpAppContext.getMessage("enb_unconfig_alert",
							new Object[] { OmpAppContext.getMessage(enb
									.getBizName()) }));

		}
	}

	/**
	 * 基于基站的类型查询基站版本
	 * 
	 * @param btsType
	 * @return
	 */
	@Override
	public List<McBtsVersion> queryByBtsType(Integer btsType) {
		List<McBtsVersion> result = mcBtsVersionManageDAO
				.queryByBtsType(btsType);

		if (result == null || result.size() == 0) {
			return Collections.emptyList();
		}

		// 遍历所有获得的基站版本,然后为每个版本添加基站版本名称
		for (McBtsVersion mcBtsVersion : result) {
			mcBtsVersion.setBtsTypeName(getParentTypeName(mcBtsVersion
					.getBtsType()));
		}

		return result;
	}

	/**
	 * 基于基站ID查询所有这个基站的版本下载记录
	 * 
	 * @param btsId
	 * @return
	 */
	@Override
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId) {

		List<McBtsVersionHistory> result = mcBtsVersionHistoryManageDAO
				.queryHistroy(btsId);

		if (result == null || result.size() == 0)
			return null;

		// 遍历所有获得的基站版本,然后为每个版本添加基站版本名称
		for (McBtsVersionHistory history : result) {
			if (history.isOverTime(DEAD_TIME))
				history.setActionResult(UTCodeDownloadTask.OVERTIME);
			history.setBtsTypeName(getParentTypeName(history.getBtsType()));
		}
		return result;
	}

	/**
	 * 获取最后一个任务的状态
	 * 
	 * @param btsId
	 * @return
	 */
	@Override
	public McBtsCodeDownloadTask getLatestStatus(Long btsId) {
		McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
				.getBtsTask(btsId);
		int typeId = task.getMcBtsVersionHistory().getBtsType();
		task.getMcBtsVersionHistory().setBtsTypeName(getParentTypeName(typeId));
		return task;
	}

	@Override
	public Map<Long, String> queryCurrentDownloadTasks() throws Exception {
		Map<Long, String> currentTasks = new HashMap<Long, String>();
		List<McBtsCodeDownloadTask> taskList = mcBtsCodeDownloadTaskManager
				.queryAllTasks();
		for (McBtsCodeDownloadTask task : taskList) {
			if (task.getStatus() == McBtsCodeDownloadTask.UNDONE) {
				McBtsVersionHistory history = task.getMcBtsVersionHistory();
				currentTasks.put(history.getBtsId(), history.getVersion());
			}
		}
		return currentTasks;
	}

	/**
	 * 删除某个基站下的所有下载记录
	 * 
	 * @param btsId
	 * @return
	 */
	@Override
	public int deleteAllHistory(Long btsId) {
		mcBtsCodeDownloadTaskManager.removeBtsTask(btsId);
		return mcBtsVersionHistoryManageDAO.deleteAllHistory(btsId);
	}

	/**
	 * 删除基站下的某个下载历史
	 * 
	 * @param history
	 * @return
	 */
	@Override
	public int deleteHistory(McBtsVersionHistory history) {
		mcBtsCodeDownloadTaskManager.removeBtsTask(history.getBtsId());
		try {
			mcBtsVersionHistoryManageDAO.delete(history);
			return 1;
		} catch (Exception e) {
			log.error("删除历史记录时出错", e);
			return 0;
		}
	}

	/**
	 * 获取各种基站类型的名称
	 * 
	 * @param childType
	 * @return
	 */
	private String getParentTypeName(Integer type) {
		mcBtsTypeDDService = AppContext.getCtx().getBean(
				McBtsTypeDDService.class);
		return mcBtsTypeDDService.queryByTypeId(type).getBtsTypeName();
	}

	/**
	 * 向基站发送升级请求
	 */
	@Override
	public void upgrade(Long moId, Integer ho_type) throws Exception {
		Mo mo = MoCache.getInstance().queryByMoId(moId);
		if (mo.getTypeId() == MoTypeDD.ENODEB) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			if (enb == null)
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			// 离线管理或不在线时抛出异常
			if (!enb.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("enb_cannot_config"));
			}
			// 判断是否有备用版本，如果没有不允许进行版本切换
			checkEnbPrepareVersionExists(moId);

			// 下载任务是否完成，未完成不允许进行版本切换
			if (!downloadFinish(enb)) {
				throw new Exception(
						OmpAppContext
								.getMessage("download_not_finish_cannot_change_version"));
			}
			enb.setBizName("enb_software_upgrade");
			GenericBizData enbData = new GenericBizData("");
			switch (ho_type) {
			case 0:
			case 1:
			case 2:
				enbData.addProperty(new GenericBizProperty(
						EnbConstantUtils.SW_TYPE, ho_type));
				break;
			default:
				break;
			}
			// enb基站的升级处理
			enbFileManagerProxy.upgrade(enb.getEnbId(), enbData);
		} else if (mo.getTypeId() == MoTypeDD.MCBTS) {
			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
			// 离线管理或不在线时抛出异常
			if (!mcBts.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("enb_cannot_config"));
			}
			// McBts基站的升级处理
			GenericBizData data = null;
			// ho_type不为空,即为BBU升级,否则为RRU升级
			if (ho_type != null) {
				data = new GenericBizData("mcBtsVersionUpgradeRRU");
				data.addProperty(new GenericBizProperty("ho_type", ho_type));
			} else {
				data = new GenericBizData("mcBtsVersionUpgradeBBU");
			}
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error("基站升级请求失败", e);
				throw e;
			}
		}
	}

	/**
	 * 判断是否有备用版本，如果没有不允许进行版本切换
	 * 
	 * @param moId
	 * @throws Exception
	 */
	private void checkEnbPrepareVersionExists(long moId) throws Exception {
		XBizTable bizTable = enbBizConfigService.queryFromNe(moId,
				EnbConstantUtils.TABLE_NAME_T_SWPKG);
		List<XBizRecord> records = bizTable.getRecords();
		if (records == null || records.isEmpty()) {
			throw new Exception(
					OmpAppContext
							.getMessage("no_prepare_version_cannot_change_version"));
		}
		// 是否有备用版本
		boolean ok = false;
		for (XBizRecord bizRecord : records) {
			int status = Integer.valueOf(bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RUNSTATUS).getValue());
			if (status == EnbConstantUtils.RUNSTATUS_BACKUP) {
				ok = true;
				break;
			}
		}
		if (!ok) {
			throw new Exception(
					OmpAppContext
							.getMessage("no_prepare_version_cannot_change_version"));
		}
	}

	/**
	 * 下载任务是否完成
	 * 
	 * @param enb
	 * @return
	 */
	private boolean downloadFinish(Enb enb) {
		McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
				.getBtsTask(enb.getEnbId());
		// 如果未进行过下载，则返回true
		if (task == null) {
			return true;
		}
		return task.isFinished();
	}

	@Override
	public void add(String fileName, byte[] fileContent) throws Exception {
		// 将文件放到ems服务器中
		boolean uploaded = uploadFileToEms(fileName, fileContent, btsFolder);

		if (uploaded) {
			// 传到FTP
			try {
				FtpClient.getInstance().uploadFile(ftpSoftwarePath,
						btsFolder + File.separator + fileName, emsServerIp,
						ftpPort, ftpUsername, ftpPassword);
			} catch (Exception e) {
				throw new IOException("ftp");
			}
		}
	}

	@Override
	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception {
		List<McBtsVersion> versionList = queryByBtsType(mcBtsVersion
				.getBtsType());
		// 校验是否重复添加
		if (checkDuplicated(versionList, mcBtsVersion)) {
			String errorMsg = OmpAppContext.getMessage("mcbts_version_exists");
			throw new Exception(MessageFormat.format(
					errorMsg,
					OmpAppContext.getMessage("mcbts.type."
							+ mcBtsVersion.getBtsType()),
					mcBtsVersion.getVersionName()));
		}
		// 文件保存在EMS本地
		boolean uploaded = uploadFileToEms(mcBtsVersion.getFileName(),
				mcBtsVersion.getFileContent(), enbFolder);
		// 将文件上传至FTP
		if (uploaded) {
			try {
				FtpClient.getInstance()
						.uploadFile(
								enbFtpPath,
								enbFolder + File.separator
										+ mcBtsVersion.getFileName(),
								emsServerIp, ftpPort, enbFtpUsername,
								ftpPassword);
			} catch (Exception e) {
				log.error("upload version file to ftp failed. fileName="
						+ mcBtsVersion.getFileName(), e);
				throw new IOException("uploadFile to ftp fail");
			}

		}
		// 存库
		saveOrUpdate(mcBtsVersion);

	}

	private boolean checkDuplicated(List<McBtsVersion> versionList,
			McBtsVersion newVersion) {
		for (McBtsVersion mcBtsVersion : versionList) {
			if (mcBtsVersion.getFileName().equals(newVersion.getFileName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将文件放到ems服务器上
	 * 
	 * @param fileName
	 * @param fileContent
	 * @return
	 */
	private boolean uploadFileToEms(String fileName, byte[] fileContent,
			String EMSfilePath) {
		// 创建路径
		File content = new File(EMSfilePath);
		if (!content.exists()) {
			content.mkdirs();
		}
		// 要上传的文件路径
		String filePath = EMSfilePath + File.separator + fileName;

		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			fos = new FileOutputStream(file);
			fos.write(fileContent);
			log.debug("上传文件到服务器");
		} catch (Exception e) {
			log.error("上传文件到服务器失败", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("关闭文件输出流失败", e);
				}

			}
		}

		return true;
	}

	/**
	 * 从服务器和FTP删除文件(mcbts情况)；
	 */
	@Override
	public boolean deleteFile(String fileName) {

		String filePath = btsFolder + fileName;

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			if (file.exists())
				return false;
		}

		try {
			// 从服务器删除
			FtpClient.getInstance().delete(
					this.ftpSoftwarePath + File.separator + fileName,
					emsServerIp, ftpPort, ftpUsername, ftpPassword);
		} catch (Exception e) {
			log.error("Error deleting a mcbts version from ftp", e);
			return false;
		}

		return true;
	}

	/*
	 * 下边为spring配置的set方法
	 */
	public static void setDEAD_TIME(long dEAD_TIME) {
		DEAD_TIME = dEAD_TIME;
	}

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

	public void setFtpSoftwarePath(String ftpSoftwarePath) {
		this.ftpSoftwarePath = ftpSoftwarePath;
	}

	public void setBtsFolder(String btsFolder) {
		this.btsFolder = btsFolder;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setMcBtsCodeDownloadTaskManager(
			McBtsCodeDownloadTaskManager mcBtsCodeDownloadTaskManager) {
		this.mcBtsCodeDownloadTaskManager = mcBtsCodeDownloadTaskManager;
	}

	public void setMcBtsVersionManageDAO(
			McBtsVersionManageDAO mcBtsVersionManageDAO) {
		this.mcBtsVersionManageDAO = mcBtsVersionManageDAO;
	}

	public void setMcBtsVersionHistoryManageDAO(
			McBtsVersionHistoryManageDAO mcBtsVersionHistoryManageDAO) {
		this.mcBtsVersionHistoryManageDAO = mcBtsVersionHistoryManageDAO;
	}

	public String getEnbFtpPath() {
		return enbFtpPath;
	}

	public void setEnbFtpPath(String enbFtpPath) {
		this.enbFtpPath = enbFtpPath;
	}

	public String getEnbFolder() {
		return enbFolder;
	}

	public void setEnbFolder(String enbFolder) {
		this.enbFolder = enbFolder;
	}

	public EnbFullTableConfigService getEnbFullTableConfigService() {
		return enbFullTableConfigService;
	}

	public void setEnbFullTableConfigService(
			EnbFullTableConfigService enbFullTableConfigService) {
		this.enbFullTableConfigService = enbFullTableConfigService;
	}

	public void setEnbFileManagerProxy(EnbFileManagerProxy enbFileManagerProxy) {
		this.enbFileManagerProxy = enbFileManagerProxy;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

	public EnbBizConfigService getEnbBizConfigService() {
		return enbBizConfigService;
	}

	public String getEnbFtpUsername() {
		return enbFtpUsername;
	}

	public void setEnbFtpUsername(String enbFtpUsername) {
		this.enbFtpUsername = enbFtpUsername;
	}

}
