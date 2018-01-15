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
 * ��վ�汾���������
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
	// FTP��ַ
	private String emsServerIp;

	// FTP�û���
	private String ftpUsername;
	// enb Ftp �û���
	private String enbFtpUsername;
	// FTP����
	private String ftpPassword;

	// FTP�˿�
	private int ftpPort;

	// FTPĿ��·��
	private String ftpSoftwarePath;

	// �ļ����ڱ��ط�����·��
	private String btsFolder;

	// ����ʱ��
	private static long DEAD_TIME;

	// enb��ftpĿ��·��
	private String enbFtpPath;

	// enb�ļ����ڱ��ط�����·��
	private String enbFolder;

	private McBtsCodeDownloadTaskManager mcBtsCodeDownloadTaskManager;

	/**
	 * ��ѯ���л�վ�汾
	 */
	@Override
	public List<McBtsVersion> queryAll() throws Exception {
		List<McBtsVersion> result = mcBtsVersionManageDAO.queryAll();

		// �������л�õĻ�վ�汾,Ȼ��Ϊÿ���汾��ӻ�վ�汾����
		for (McBtsVersion mcBtsVersion : result) {
			mcBtsVersion.setBtsTypeName(getParentTypeName(mcBtsVersion
					.getBtsType()));
		}
		return result;
	}

	/**
	 * �����ݿ�ɾ����վ�汾
	 */
	@Override
	public void delete(McBtsVersion mcBtsVersion) throws Exception {

		mcBtsVersionManageDAO.delete(mcBtsVersion);
	}

	/**
	 * �������ݿ��еĻ�վ�汾
	 */
	@Override
	public void saveOrUpdate(McBtsVersion mcbtsVersion) throws Exception {
		mcBtsVersionManageDAO.saveOrUpdate(mcbtsVersion);
	}

	/**
	 * ���ػ�վ���
	 * <p>
	 * �ӷ���������FTP,Ȼ������Ϣ�����վ�������
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
		// ��֤��Ԫ�Ƿ�����ã������ڻ򲻿��������׳��쳣
		checkConfigurable(mo.getMoId());

		// ��֤Ҫ���صİ汾�����Ƿ�֧��
		checkVersionSupported(mo, mcBtsVersion.getVersionName());

		try {
			McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
					.getBtsTask(btsId);
			// �����ǰ��δ��ɵ�����
			if (task != null && !task.isFinished()) {
				if (!task.isOverTime(DEAD_TIME)) {
					// �������δ��ʱ�����ǰ̨����״̬��
					return McBtsCodeDownloadTask.PREVIOUS_TASK_STATUS_UNDONE;
				} else if (task.isOverTime(DEAD_TIME)) {
					// ��������ѳ�ʱ����������������ý��Ϊ�ѳ�ʱ
					mcBtsCodeDownloadTaskManager.finishTask(btsId,
							McBtsCodeDownloadTask.OVERTIME);
				}
			}
			// �ϴ��汾�ļ�
			uploadVersionToFtp(mo, mcBtsVersion);

			// �����µ���������
			McBtsVersionHistory mvh = mcBtsVersionHistoryManageDAO
					.queryByBtsIdAndVersion(btsId,
							mcBtsVersion.getVersionName());
			if (mvh != null) {
				// �������������ع�,ֻ��Ҫ�޸Ŀ�ʼʱ��,����ʱ���״̬
				mvh.setActionResult(McBtsCodeDownloadTask.UNDONE);
				mvh.setStartTime(new Date());
				mvh.setEndTime(null);
			} else {
				// �����µ�������ʷ,׼�����
				mvh = new McBtsVersionHistory(btsId, mcBtsVersion.getBtsType(),
						mcBtsVersion.getVersionName(),
						McBtsCodeDownloadTask.UNDONE, new Date());
			}
			// ������������
			task = new McBtsCodeDownloadTask(mvh);
			// ������񵽻���
			mcBtsCodeDownloadTaskManager.addBtsTask(btsId, task);

			// ���ذ汾��������
			if (mo.getTypeId() == MoTypeDD.ENODEB) {
				// enb��վ
				sendEnbCodeDownloadRequest(btsId, mcBtsVersion);
			} else if (mo.getTypeId() == MoTypeDD.MCBTS) {
				// McWILL��վ
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
	 * ���ع��̱���ʱ��ɾ���������񣬸���������ʷ
	 * 
	 * @param btsId
	 */
	private void undoDownload(long btsId) throws Exception {
		McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
				.getBtsTask(btsId);
		// ɾ����������
		mcBtsCodeDownloadTaskManager.removeBtsTask(btsId);
		McBtsVersionHistory history = task.getMcBtsVersionHistory();
		history.setActionResult(McBtsCodeDownloadTask.DOWNLOAD_WITH_ERROR);
		// ����������ʷ
		mcBtsVersionHistoryManageDAO.saveOrUpdate(history);
	}

	/**
	 * �ж�ftp���Ƿ���ڸð汾�ļ�����������ڣ����ϴ�
	 * 
	 * @param mo
	 * @param version
	 * @throws Exception
	 */
	private void uploadVersionToFtp(Mo mo, McBtsVersion version)
			throws Exception {
		// ����FTP�ļ�����������ļ���,����ҵ�filename,�Ͳ�������(��enb��mcBts����)
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

		// ����ļ�������,���ϴ��ļ���FTP
		if (!exists) {
			FtpClient.getInstance().uploadFile(ftpPath,
					emsLocalFolder + File.separator + targetFileName,
					emsServerIp, ftpPort, ftpName, ftpPassword);
			log.info("upload version file to ftp, ftpPath=" + ftpPath);
		}
	}

	/**
	 * ����McWILL��վ�汾��������
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
	 * У��Ҫ���صİ汾�Ƿ�֧��
	 * 
	 * @param mo
	 * @param version
	 * @throws Exception
	 */
	private void checkVersionSupported(Mo mo, String version) throws Exception {
		if (mo.getTypeId() == MoTypeDD.ENODEB) {
			String protocolVersion = version.substring(0,
					version.lastIndexOf("."));
			// ��ȡ����֧�ֵ�Э��汾
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
	 * �ж���Ԫ�Ƿ���Խ������ã��粻���ڻ򲻿����ã����׳��쳣
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
	 * �·�enb�汾��������
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
		if (enb.setFullTableOperation(true)) {// ��������
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
				// / �����ļ�·����
				String dataFileDirectory = EnbFullTableTaskManager
						.getInstance().getConfigFtpdDrectory();
				// �����ļ���
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
				// ���ִ����ͷ���Դ��
				enb.setFullTableOperation(false);
				log.error(e);
				throw e;
			}

		} else {
			// �׳��������õ��쳣
			throw new Exception(
					OmpAppContext.getMessage("enb_unconfig_alert",
							new Object[] { OmpAppContext.getMessage(enb
									.getBizName()) }));

		}
	}

	/**
	 * ���ڻ�վ�����Ͳ�ѯ��վ�汾
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

		// �������л�õĻ�վ�汾,Ȼ��Ϊÿ���汾��ӻ�վ�汾����
		for (McBtsVersion mcBtsVersion : result) {
			mcBtsVersion.setBtsTypeName(getParentTypeName(mcBtsVersion
					.getBtsType()));
		}

		return result;
	}

	/**
	 * ���ڻ�վID��ѯ���������վ�İ汾���ؼ�¼
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

		// �������л�õĻ�վ�汾,Ȼ��Ϊÿ���汾��ӻ�վ�汾����
		for (McBtsVersionHistory history : result) {
			if (history.isOverTime(DEAD_TIME))
				history.setActionResult(UTCodeDownloadTask.OVERTIME);
			history.setBtsTypeName(getParentTypeName(history.getBtsType()));
		}
		return result;
	}

	/**
	 * ��ȡ���һ�������״̬
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
	 * ɾ��ĳ����վ�µ��������ؼ�¼
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
	 * ɾ����վ�µ�ĳ��������ʷ
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
			log.error("ɾ����ʷ��¼ʱ����", e);
			return 0;
		}
	}

	/**
	 * ��ȡ���ֻ�վ���͵�����
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
	 * ���վ������������
	 */
	@Override
	public void upgrade(Long moId, Integer ho_type) throws Exception {
		Mo mo = MoCache.getInstance().queryByMoId(moId);
		if (mo.getTypeId() == MoTypeDD.ENODEB) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			if (enb == null)
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			// ���߹��������ʱ�׳��쳣
			if (!enb.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("enb_cannot_config"));
			}
			// �ж��Ƿ��б��ð汾�����û�в�������а汾�л�
			checkEnbPrepareVersionExists(moId);

			// ���������Ƿ���ɣ�δ��ɲ�������а汾�л�
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
			// enb��վ����������
			enbFileManagerProxy.upgrade(enb.getEnbId(), enbData);
		} else if (mo.getTypeId() == MoTypeDD.MCBTS) {
			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
			// ���߹��������ʱ�׳��쳣
			if (!mcBts.isConfigurable()) {
				throw new Exception(
						OmpAppContext.getMessage("enb_cannot_config"));
			}
			// McBts��վ����������
			GenericBizData data = null;
			// ho_type��Ϊ��,��ΪBBU����,����ΪRRU����
			if (ho_type != null) {
				data = new GenericBizData("mcBtsVersionUpgradeRRU");
				data.addProperty(new GenericBizProperty("ho_type", ho_type));
			} else {
				data = new GenericBizData("mcBtsVersionUpgradeBBU");
			}
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error("��վ��������ʧ��", e);
				throw e;
			}
		}
	}

	/**
	 * �ж��Ƿ��б��ð汾�����û�в�������а汾�л�
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
		// �Ƿ��б��ð汾
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
	 * ���������Ƿ����
	 * 
	 * @param enb
	 * @return
	 */
	private boolean downloadFinish(Enb enb) {
		McBtsCodeDownloadTask task = mcBtsCodeDownloadTaskManager
				.getBtsTask(enb.getEnbId());
		// ���δ���й����أ��򷵻�true
		if (task == null) {
			return true;
		}
		return task.isFinished();
	}

	@Override
	public void add(String fileName, byte[] fileContent) throws Exception {
		// ���ļ��ŵ�ems��������
		boolean uploaded = uploadFileToEms(fileName, fileContent, btsFolder);

		if (uploaded) {
			// ����FTP
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
		// У���Ƿ��ظ����
		if (checkDuplicated(versionList, mcBtsVersion)) {
			String errorMsg = OmpAppContext.getMessage("mcbts_version_exists");
			throw new Exception(MessageFormat.format(
					errorMsg,
					OmpAppContext.getMessage("mcbts.type."
							+ mcBtsVersion.getBtsType()),
					mcBtsVersion.getVersionName()));
		}
		// �ļ�������EMS����
		boolean uploaded = uploadFileToEms(mcBtsVersion.getFileName(),
				mcBtsVersion.getFileContent(), enbFolder);
		// ���ļ��ϴ���FTP
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
		// ���
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
	 * ���ļ��ŵ�ems��������
	 * 
	 * @param fileName
	 * @param fileContent
	 * @return
	 */
	private boolean uploadFileToEms(String fileName, byte[] fileContent,
			String EMSfilePath) {
		// ����·��
		File content = new File(EMSfilePath);
		if (!content.exists()) {
			content.mkdirs();
		}
		// Ҫ�ϴ����ļ�·��
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
			log.debug("�ϴ��ļ���������");
		} catch (Exception e) {
			log.error("�ϴ��ļ���������ʧ��", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("�ر��ļ������ʧ��", e);
				}

			}
		}

		return true;
	}

	/**
	 * �ӷ�������FTPɾ���ļ�(mcbts���)��
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
			// �ӷ�����ɾ��
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
	 * �±�Ϊspring���õ�set����
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
