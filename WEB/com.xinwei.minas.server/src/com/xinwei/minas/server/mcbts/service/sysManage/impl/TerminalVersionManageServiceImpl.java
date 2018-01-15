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
 * �ն˰汾���������
 * 
 * @author tiance
 * 
 */

public class TerminalVersionManageServiceImpl implements
		TerminalVersionManageService {

	private Log log = LogFactory.getLog(TerminalVersionManageServiceImpl.class);

	// FTP��ַ
	private String emsServerIp;

	// FTP�û���
	private String ftpUsername;

	// FTP����
	private String ftpPassword;

	// FTP�˿�
	private int ftpPort;

	// FTPĿ��·��
	private String ftpTVPath;

	// �ļ����ڱ��ط�����·��
	private String utFolder;

	// ����ʱ��
	private static long DEAD_TIME;
	private McBtsBizProxy mcBtsBizProxy;
	private TerminalVersionManageDAO terminalVersionManageDao;
	private McBtsUTCodeDownloadTaskManager mcBtsUTCodeDownloadTaskManager;

	/**
	 * ��ȡ�������ϵ������ն˰汾���б�
	 * 
	 */
	@Override
	public List<TerminalVersion> queryAll() {
		return terminalVersionManageDao.queryAll();
	}

	/**
	 * ����typeId��ȡĳ���ն˵����а汾
	 * 
	 */
	@Override
	public List<TerminalVersion> queryByTypeId(Integer typeId) {
		return terminalVersionManageDao.queryByTypeId(typeId);
	}

	/**
	 * �ӷ�������ɾ��һ���ն˰汾
	 */
	@Override
	public void delete(TerminalVersion terminalVersion) {
		// ����ems��ftpɾ���ļ�
		// boolean deleted = deleteFile(terminalVersion.getFileName());

		// �����ݿ�ɾ���ļ���Ϣ
		terminalVersionManageDao.delete(terminalVersion);
	}

	/**
	 * ����typeId��version��fileType,�ӷ�������ȡĳ���ն˰汾��Ϣ
	 */
	@Override
	@Deprecated
	// �Ѹĳɷ���������֤
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) {
		return terminalVersionManageDao.queryByTypeIdAndVersion(typeId,
				version, fileType);
	}

	/**
	 * �����ն˰汾
	 */
	// �޸Ĳ����Ѿ�ȥ��
	@Deprecated
	@Override
	public int modify(TerminalVersion terminalVersion) {
		// ��ems��ftpɾ���ļ�
		boolean deleted = deleteFile(terminalVersion.getFileName());

		if (deleted) {
			boolean uploaded = uploadFileToEms(terminalVersion);

			if (uploaded) {
				final String filename = terminalVersion.getFileName();

				new FTPAdd(filename).start();

				return terminalVersionManageDao.update(terminalVersion);
			}
		}
		return 0; // 0 Ϊʧ��
	}

	/**
	 * ����ն˰汾
	 */
	@Override
	public int add(TerminalVersion tv) {
		// ���������Ƿ����
		int count = terminalVersionManageDao.queryForType(tv.getTypeId());

		if (count == 0)
			return TerminalVersion.NO_TYPE;

		// �����Ƿ��Ѵ���
		TerminalVersion exists = terminalVersionManageDao
				.queryByTypeIdAndVersion(tv.getTypeId(), tv.getVersion(),
						Integer.parseInt(tv.getFileType()));

		if (exists != null)
			return TerminalVersion.EXISTS;

		// �ϴ���EMS
		boolean uploaded = uploadFileToEms(tv);

		if (!uploaded)
			return TerminalVersion.ERROR_UPLOADING_TO_EMS;

		// �ϴ���FTP
		final String filename = tv.getFileName();
		try {
			ftpAdd(filename);
		} catch (Exception e) {
			log.error("Error uploading terminal version to FTP.", e);
			return TerminalVersion.ERROR_UPLOADING_TO_FTP;
		}

		// ���浽���ݿ�
		return terminalVersionManageDao.add(tv);

	}

	/**
	 * �ϴ���FTP
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
	 * �첽��ftp����ļ�
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
				log.error("FTP�������ϴ��ļ���������", e);
			}
		}
	}

	/**
	 * ���ն˰汾���ص�FTP������,��֪ͨ��վ��ȡ�ն˰汾
	 * 
	 * @return 1: ���֮ǰ��״̬û�н����Ļ�, 0: ������سɹ�, -1:��վӦ��ʱ
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
					if (!task.isOverTime(DEAD_TIME)) {// ����δ���������δ��ʱ
						return UTCodeDownloadTask.PREVIOUS_TASK_STATUS_UNDONE; // �������������ڽ���
					} else if (task.isOverTime(DEAD_TIME)) {
						mcBtsUTCodeDownloadTaskManager.finishTask(btsId,
								UTCodeDownloadTask.OVERTIME); // ��ʱ
					}
				}
			}
			// ����ftp�ļ�����������ļ���,����ҵ�filename,�Ͳ�������
			if (!fileExistsInFTP(fileName)) {
				File file = new File(utFolder + File.separator + fileName);

				if (!file.exists()) {
					return TerminalVersion.NO_FILE_ON_EMS;
				}

				ftpAdd(fileName);
				log.info("�ϴ��ļ���ftp");
			}
			// �����µ�����
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
			log.error("�����ն˰汾��������ʧ��", e);
			return TerminalVersion.BTS_INVALID;
		}

		return TerminalVersion.SUCCESS; // �ɹ�
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
	 * �����ݿ�������������ʷ
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
	 * ͨ��TDLHistoryKey�Ӻ�̨history�в�ѯһ�����ذ汾������״̬
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId) {
		return mcBtsUTCodeDownloadTaskManager.getUTTask(btsId);
	}

	/**
	 * ɾ����ʷ��¼��ļ�¼
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
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHis(Long btsId) {
		mcBtsUTCodeDownloadTaskManager.removeUTTask(btsId);
		return terminalVersionManageDao.deleteAllFromHistory(btsId);
	}

	/**
	 * ��ems��ftpɾ���ļ�
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
			// �ӷ�����ɾ��
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
	 * ���ļ��ŵ�ems������
	 * 
	 * @param terminalVersion
	 * @return
	 */
	private boolean uploadFileToEms(TerminalVersion terminalVersion) {
		// ����·��
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
	 * �±ߵ���spring�����ļ�����ֵ����
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
		// ���
		// IdҪǰ�油��1���油��0
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
