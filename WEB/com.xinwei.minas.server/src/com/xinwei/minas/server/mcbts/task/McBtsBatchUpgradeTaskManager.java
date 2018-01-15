/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsAttribute;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.proxy.EnbFileManagerProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.enb.task.EnbFullTableTaskManager;
import com.xinwei.minas.server.mcbts.dao.common.McBtsBatchUpgradeArchiveDAO;
import com.xinwei.minas.server.mcbts.dao.common.McBtsBatchUpgradeDAO;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionManageDAO;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.TransactionIdGenerator;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ�����������������
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeTaskManager {

	private Log log = LogFactory.getLog(McBtsBatchUpgradeTaskManager.class);

	// ��ǰ������
	private volatile int curDownloads;

	// ���ͬʱ������,����
	private static int maxDownload = 5;

	private static long timeout = 100000;

	// FTP��ַ
	private String emsServerIp;

	// FTP�û���
	private String ftpUsername;

	// FTP����
	private String ftpPassword;

	// FTP�˿�
	private int ftpPort;

	// FTPĿ��·��
	private String ftpSoftwarePath;

	// ά�����������Ļ�վ����״̬
	private Map<Long, UpgradeInfo> upgradeInfoMap = new ConcurrentHashMap<Long, UpgradeInfo>();

	private McBtsBatchUpgradeDAO mcBtsBatchUpgradeDAO;

	private McBtsBatchUpgradeArchiveDAO mcBtsBatchUpgradeArchiveDAO;

	private McBtsVersionManageDAO mcBtsVersionManageDAO;

	private McBtsBizProxy mcBtsBizProxy;

	private EnbFileManagerProxy enbFileManagerProxy;

	private EnbFullTableConfigService enbFullTableConfigService;
	// enb��ftpĿ��·��
	private String enbFtpPath;

	private static McBtsBatchUpgradeTaskManager instance = null;

	private McBtsBatchUpgradeTaskManager(int maxDownload, long timeout,
			McBtsBatchUpgradeDAO mcBtsBatchUpgradeDAO) {
		McBtsBatchUpgradeTaskManager.maxDownload = maxDownload;
		McBtsBatchUpgradeTaskManager.timeout = timeout;
		this.mcBtsBatchUpgradeDAO = mcBtsBatchUpgradeDAO;

		// ��ʼ������
		initMap();

		// ���������߳�
		ScheduledExecutorService ses = Executors
				.newSingleThreadScheduledExecutor();
		ses.schedule(new McBtsBatchUpgradeDownloadTask(this), 30,
				TimeUnit.SECONDS);
	}

	public static McBtsBatchUpgradeTaskManager getInstance(int maxDownload,
			long timeout, McBtsBatchUpgradeDAO mcBtsBatchUpgradeDAO) {
		if (instance == null) {
			return new McBtsBatchUpgradeTaskManager(maxDownload, timeout,
					mcBtsBatchUpgradeDAO);
		}
		return instance;
	}

	public static McBtsBatchUpgradeTaskManager getInstance() {
		return instance;
	}

	/**
	 * ����enb��վ������ɵ�֪ͨ
	 * 
	 * @param response
	 */
	public void handleEnbDLFinishNotify(EnbAppMessage response) {
		// FIXME ���Ӵ���
		Enb enb = EnbCache.getInstance().queryByEnbId(response.getEnbId());
		if (enb == null) {
			log.error("Can not find enb by enbId in MCBTS-DOWNLOADING-FINISHED message!!!");
			return;
		}
		// �����ͷ���Դ
		enb.setFullTableOperation(false);
		enb.setBizName("");
		// ���빫����������
		handleFinishResult(enb.getMoId(),
				response.getIntValue(TagConst.ENB_DOWNLOAD_RESULT),
				response.getTransactionId());
	}

	/**
	 * �����վ������ɵ�֪ͨ
	 * 
	 * @param response
	 */
	public void handleDLFinishNotify(McBtsMessage response) {
		McBts mcbts = McBtsCache.getInstance()
				.queryByBtsId(response.getBtsId());

		if (mcbts == null) {
			log.error("Cannot find the mcbts by btsId in MCBTS-DOWNLOADING-FINISHED message!!!");
			return;
		}
		byte[] content = response.getContent();
		int result = ByteUtils.toInt(content, 0, 2);
		// ���빫����������
		handleFinishResult(mcbts.getMoId(), result, response.getTransactionId());

	}

	/*
	 * �����������ͬ�ߵķ���
	 */
	public void handleFinishResult(long moId, int result, long transId) {
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryByMoId(moId);

		if (info == null)
			return;

		// �ж������״̬,���������ֹ����������ѳ�ʱ,�Ͳ����ٸı�����״̬,������ֹ��״̬
		// if (info.getStatus() == UpgradeInfo.STATUS_TERMINATED
		// || info.getStatus() == UpgradeInfo.STATUS_DOWNLOAD_TIMEOUT) {
		// return;
		// }

		// ֻ�д�������״̬����Ӧ�ý��ո���Ϣ��
		if (info.getStatus() != UpgradeInfo.STATUS_DOWNLOADING
				|| info.getTransactionId() != transId) {
			return;
		}

		downloadCountDecrease();

		// ����FDDI�汾
		updateFddiVersion(info);

		if (result == 0) {
			// �ɹ�
			// �������״̬
			markDownloaded(info);
			// ������������
			configUpgrade(info);
		} else {
			// ʧ��,״̬��Ϊʧ��
			info.setErrorCode(result);
			markDownloadError(info);
		}

	}

	/**
	 * ��ʼ��upgradeInfoMap
	 */
	private void initMap() {
		List<UpgradeInfo> list = mcBtsBatchUpgradeDAO.queryAll();
		for (UpgradeInfo info : list) {
			upgradeInfoMap.put(info.getMoId(), info);
		}
	}

	/**
	 * ��ȡ���л�վ������Ϣ
	 * 
	 * @return ���Map��btsTypeΪKey; �ڲ�Map�Ի�վmoIdΪKey
	 */
	@Deprecated
	public Map<Integer, Map<Long, UpgradeInfo>> getAll() {
		Map<Integer, Map<Long, UpgradeInfo>> result = new HashMap<Integer, Map<Long, UpgradeInfo>>();
		for (Entry<Long, UpgradeInfo> entry : upgradeInfoMap.entrySet()) {
			int btsType = entry.getValue().getBtsType();

			Map<Long, UpgradeInfo> infoMap = result.get(btsType);
			if (infoMap == null) {
				infoMap = new HashMap<Long, UpgradeInfo>();
				result.put(btsType, infoMap);
			}

			infoMap.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * ��ȡ���л�վ������Ϣ
	 * 
	 * @return ���Map��btsTypeΪKey; �ڲ�Map�Ի�վmoIdΪKey
	 */
	public Map<Integer, List<UpgradeInfo>> getAll2() {
		Map<Integer, List<UpgradeInfo>> result = new HashMap<Integer, List<UpgradeInfo>>();

		Map<Long, McBts> btsMap = McBtsCache.getInstance().getMapByMoId();

		for (UpgradeInfo info : upgradeInfoMap.values()) {
			int btsType = info.getBtsType();

			List<UpgradeInfo> infoList = result.get(btsType);
			if (infoList == null) {
				infoList = new ArrayList<UpgradeInfo>();
				result.put(btsType, infoList);
			}

			McBts mcbts = btsMap.get(info.getMoId());
			if (mcbts != null)
				info.setName(mcbts.getName());
			else
				info.setName("");

			infoList.add(info);

		}

		// ����:δ��� - �������� - ���س�ʱ - ���� - ���
		sort(result);

		return result;
	}

	private static void sort(Map<Integer, List<UpgradeInfo>> map) {
		for (List<UpgradeInfo> list : map.values()) {
			Collections.sort(list, new Comparator<UpgradeInfo>() {
				@Override
				public int compare(UpgradeInfo u1, UpgradeInfo u2) {
					int s1 = u1.getStatus();
					int s2 = u2.getStatus();

					if (s1 > s2)
						return 1;
					if (s1 < s2)
						return -1;

					long m1 = u1.getMoId();
					long m2 = u2.getMoId();

					if (m1 > m2)
						return 1;
					if (m1 < m2)
						return -1;

					return 0;
				}
			});
		}
	}

	/**
	 * ��ȡ���ڽ�������������
	 * 
	 * @return
	 */
	public List<UpgradeInfo> queryUpgrading() {
		List<UpgradeInfo> result = new ArrayList<UpgradeInfo>();
		for (UpgradeInfo info : upgradeInfoMap.values()) {
			if (isUpgrading(info))
				result.add(info);
		}

		return result;
	}

	private static boolean isUpgrading(UpgradeInfo info) {
		int status = info.getStatus();
		if (status == UpgradeInfo.STATUS_DOWNLOADING
				|| status == UpgradeInfo.STATUS_DOWNLOAD_FINISH
				|| status == UpgradeInfo.STATUS_NOT_START) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����moId��ȡ��վ����������Ϣ
	 * 
	 * @param moId
	 * @return ����������Ϣ
	 */
	public UpgradeInfo get(long moId) {
		return upgradeInfoMap.get(moId);
	}

	/**
	 * ���һ��������������
	 * 
	 * @param upgradeInfo
	 */
	public void add(List<UpgradeInfo> list) {
		List<UpgradeInfo> listInDB = mcBtsBatchUpgradeDAO.queryAll();

		List<UpgradeInfoArchive> toArchive = new ArrayList<UpgradeInfoArchive>();
		List<UpgradeInfo> toArchive2 = new ArrayList<UpgradeInfo>();

		// ��ȡ�����Ѿ����ڵĻ�վ��������,Ȼ�����鵵���б���
		for (UpgradeInfo info : list) {
			for (UpgradeInfo infoInDB : listInDB) {
				if (info.getMoId() == infoInDB.getMoId()) {
					toArchive2.add(infoInDB);

					UpgradeInfoArchive archive = new UpgradeInfoArchive(
							infoInDB);
					toArchive.add(archive);
				}
			}

			upgradeInfoMap.put(info.getMoId(), info);
		}

		// �鵵�ظ���������Ŀ
		mcBtsBatchUpgradeArchiveDAO.saveAll(toArchive);

		mcBtsBatchUpgradeDAO.deleteArchive(toArchive2);
		// �洢�����ݿ�
		mcBtsBatchUpgradeDAO.saveAll(list);
	}

	/**
	 * ɾ��һ��������������
	 * 
	 * @param moId
	 */
	public synchronized void delete(long moId) {
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryByMoId(moId);
		mcBtsBatchUpgradeDAO.delete(info);
		upgradeInfoMap.remove(moId);
	}

	/**
	 * ����һ��������������
	 * 
	 * @param moId
	 */
	public synchronized void update(UpgradeInfo info) {
		mcBtsBatchUpgradeDAO.saveOrUpdate(info);
		upgradeInfoMap.put(info.getMoId(), info);
	}

	/**
	 * �鿴�Ƿ��г�ʱ����������
	 * 
	 * @return ��ʱ����������б�
	 */
	public synchronized List<UpgradeInfo> hasTimeoutUpgradeInfo() {
		List<UpgradeInfo> result = new ArrayList<UpgradeInfo>();

		for (UpgradeInfo info : upgradeInfoMap.values()) {
			if (isTimeout(info))
				result.add(info);
		}
		return result;
	}

	private static boolean isTimeout(UpgradeInfo info) {
		if (info.getStartTime() == null)
			return false;

		if (info.getStatus() != UpgradeInfo.STATUS_DOWNLOADING)
			return false;
		long startTime = info.getStartTime().getTime();
		long curTime = System.currentTimeMillis();

		long diff = curTime - startTime;

		if (info.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			if (diff >= (timeout * 2))
				return true;
			else
				return false;
		} else {
			if (diff >= timeout)
				return true;
			else
				return false;
		}

	}

	/**
	 * ���ؼ�¼��1
	 */
	public void downloadCountIncrease() {
		curDownloads++;
	}

	/**
	 * ���ؼ�¼��1
	 */
	public void downloadCountDecrease() {
		curDownloads--;
	}

	/**
	 * �ж��Ƿ񵽴����������
	 * 
	 * @return
	 */
	public synchronized boolean reachMaxDownload() {
		return curDownloads == maxDownload;
	}

	/**
	 * �޸ĳ�ʱ�������״̬
	 * 
	 * @param infos
	 */
	public void markTimeout(UpgradeInfo info) {
		UpgradeInfo infoInDB = mcBtsBatchUpgradeDAO.queryByMoId(info.getMoId());

		infoInDB.setStatus(UpgradeInfo.STATUS_DOWNLOAD_TIMEOUT);
		infoInDB.setEndTime(new Date());

		update(infoInDB);
	}

	/**
	 * �޸�����Ϊ��������
	 * 
	 * @param upgradeInfo
	 */
	public void markDownloading(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_DOWNLOADING);
		upgradeInfo.setStartTime(new Date());

		update(upgradeInfo);
	}

	/**
	 * �޸�����Ϊ�������
	 * 
	 * @param upgradeInfo
	 */
	public void markDownloaded(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_DOWNLOAD_FINISH);

		update(upgradeInfo);
	}

	/**
	 * �޸�����Ϊ����ֹ
	 * 
	 * @param upgradeInfo
	 */
	public synchronized void markTerminated(UpgradeInfo info) throws Exception {
		UpgradeInfo infoInDB = mcBtsBatchUpgradeDAO.queryByMoId(info.getMoId());

		if (infoInDB == null)
			return;

		int status = infoInDB.getStatus();
		if (status == UpgradeInfo.STATUS_DOWNLOAD_FAIL
				|| status == UpgradeInfo.STATUS_DOWNLOAD_TIMEOUT
				|| status == UpgradeInfo.STATUS_UPGRADE
				|| status == UpgradeInfo.STATUS_UPGRADE_FAIL
				|| status == UpgradeInfo.STATUS_MCBTS_NOT_EXISTS
				|| status == UpgradeInfo.STATUS_MCBTS_DISCONNECTED
				|| status == UpgradeInfo.STATUS_MCBTS_OFFLINE_MANAGE
				|| status == UpgradeInfo.STATUS_TERMINATED) {
			// �Ѿ��޷���ֹ,�׳��쳣
			throw new Exception();
		}

		// ��ֹ�������ص�����ʱ����ǰ��������Ӧ�ü�һ
		if (status == UpgradeInfo.STATUS_DOWNLOADING) {
			downloadCountDecrease();
		}

		infoInDB.setStatus(UpgradeInfo.STATUS_TERMINATED);
		infoInDB.setEndTime(new Date());

		update(infoInDB);

	}

	/**
	 * �޸�����Ϊ�������ʱ����
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void markDownloadError(UpgradeInfo info) {
		if (info.getStatus() == UpgradeInfo.STATUS_DOWNLOADING
				|| info.getStatus() == UpgradeInfo.STATUS_DOWNLOAD_FINISH)
			info.setStatus(UpgradeInfo.STATUS_DOWNLOAD_FAIL);

		info.setEndTime(new Date());

		update(info);
	}

	/**
	 * �޸�����Ϊ����ʧ��
	 * 
	 * @param info
	 */
	private void markUpgradeError(UpgradeInfo info) {
		info.setStatus(UpgradeInfo.STATUS_UPGRADE_FAIL);
		info.setEndTime(new Date());

		update(info);
	}

	/**
	 * �޸�����Ϊ�����������·�
	 * 
	 * @param upgradeInfo
	 */
	public void markUpgraded(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_UPGRADE);
		upgradeInfo.setEndTime(new Date());

		update(upgradeInfo);
	}

	/**
	 * ���վ������������
	 * 
	 * @throws Exception
	 */
	public synchronized void configDownload() throws Exception {
		if (mcBtsBatchUpgradeDAO == null)
			return;

		// ��ȡһ��δִ�е�����
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryFreeUpgradeInfo();

		if (info == null) {
			throw new NullPointerException();
		}

		// ���¹��˵İ汾
		updateFddiVersion(info);

		// �����Ӧ��version
		McBtsVersion mv = mcBtsVersionManageDAO.queryByBtsTypeAndVersion(
				info.getBtsType(), info.getDownloadVersion());

		// ��ȡ��վ��״̬���ж��Ƿ����
		// FIXME �˴�����enbʱ����Ҫ��д������
		Mo mo = MoCache.getInstance().queryByMoId(info.getMoId());
		boolean stop = false;

		switch (mo.getTypeId()) {
		case MoTypeDD.MCBTS:
			// McBts��վ
			McBts mcbts = McBtsCache.getInstance().queryByMoId(info.getMoId());
			if (mcbts == null) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_NOT_EXISTS);
				info.setEndTime(new Date());
				stop = true;
			}
			if (mcbts.isDisconnected()) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_DISCONNECTED);
				info.setEndTime(new Date());
				stop = true;
			}
			if (mcbts.isOfflineManage()) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_OFFLINE_MANAGE);
				info.setEndTime(new Date());
				stop = true;
			}
			break;
		case MoTypeDD.ENODEB:
			// enb��վ
			Enb enb = EnbCache.getInstance().queryByMoId(info.getMoId());
			if (enb == null) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_NOT_EXISTS);
				info.setEndTime(new Date());
				stop = true;
			}
			if (enb.isDisconnected()) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_DISCONNECTED);
				info.setEndTime(new Date());
				stop = true;
			}
			if (enb.isOfflineManage()) {
				info.setStatus(UpgradeInfo.STATUS_MCBTS_OFFLINE_MANAGE);
				info.setEndTime(new Date());
				stop = true;
			}
			break;
		default:
			break;
		}
		if (stop) {
			markDownloadError(info);
			return;
		}

		// ���ؿ�ʼ,��¼����״̬
		downloadCountIncrease();
		int transactionId = TransactionIdGenerator.next();
		info.setTransactionId(transactionId);
		markDownloading(info);

		// ����Ԫ������Ϣ
		try {
			if (mo.getTypeId() == MoTypeDD.MCBTS) {
				// Mcbts��վ
				GenericBizData data = new GenericBizData("mcBtsVersionDownload");
				data.addProperty(new GenericBizProperty("ftpIp", emsServerIp));
				data.addProperty(new GenericBizProperty("ftpPort", ftpPort));
				data.addProperty(new GenericBizProperty("ftpUserName",
						ftpUsername));
				data.addProperty(new GenericBizProperty("ftpPassword",
						ftpPassword));
				data.addProperty(new GenericBizProperty("filePath",
						ftpSoftwarePath));
				data.addProperty(new GenericBizProperty("softwareType", 0));
				data.addProperty(new GenericBizProperty("version", mv
						.getVersionName()));

				data.setTransactionId(transactionId);

				mcBtsBizProxy.config(info.getMoId(), data);
			} else if (mo.getTypeId() == MoTypeDD.ENODEB) {
				// Enb��վ����
				enbVersionDownloadDispose(info.getMoId(), mv);
			}
		} catch (TimeoutException e) {
			info.setStatus(UpgradeInfo.STATUS_DOWNLOAD_TIMEOUT);
			stop = true;
		} catch (Exception e) {
			info.setStatus(UpgradeInfo.STATUS_DOWNLOAD_FAIL);
			stop = true;
		}

		if (stop) {
			downloadCountDecrease();
			markDownloadError(info);
			return;
		}

	}

	/**
	 * enb�汾���صķ�������
	 * 
	 * @param mcBtsVersion
	 * @param btsId
	 * @throws Exception
	 */
	private void enbVersionDownloadDispose(Long moId, McBtsVersion mcBtsVersion)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null)
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		if (enb.setFullTableOperation(true)) {// ��������
			enb.setBizName("enb_software_upgrade");
			try {
				GenericBizData enbData = new GenericBizData("");
				enbData.addProperty(new GenericBizProperty("ftpIp", emsServerIp));
				enbData.addProperty(new GenericBizProperty("ftpPort", ftpPort));
				enbData.addProperty(new GenericBizProperty("ftpUserName",
						ftpUsername));
				enbData.addProperty(new GenericBizProperty("ftpPassword",
						ftpPassword));
				enbData.addProperty(new GenericBizProperty("fileDirectory",
						enbFtpPath));
				enbData.addProperty(new GenericBizProperty("softwareType", 0));
				enbData.addProperty(new GenericBizProperty("version",
						mcBtsVersion.getVersionName()));
				// �����ļ�·����
				String dataFileDirectory = EnbFullTableTaskManager
						.getInstance().getConfigFtpdDrectory();
				// �����ļ���
				String dataFileName = enbFullTableConfigService
						.generateFullTableSqlFile(enb.getMoId(), EnbBizHelper
								.getProtocolVersion(mcBtsVersion
										.getVersionName()));

				enbData.addProperty(new GenericBizProperty("dataFilePath",
						dataFileDirectory));
				enbData.addProperty(new GenericBizProperty("dataFileName",
						dataFileName));

				enbFileManagerProxy.enbVersionDownloadConfig(enb.getEnbId(),
						enbData);
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
	 * ���վ������������
	 * 
	 * @throws Exception
	 */
	public void configUpgrade(UpgradeInfo info) {
		GenericBizData data = new GenericBizData("mcBtsVersionUpgradeBBU");

		if (info.getBtsType() == McBtsTypeDD.FDDI_MCBTS) {
			data.addProperty(new GenericBizProperty("swType", info.getTarget()));
		}
		try {
			mcBtsBizProxy.config(info.getMoId(), data);
			markUpgraded(info);
		} catch (Exception e) {
			markUpgradeError(info);
		}
	}

	/**
	 * ���¹�����Զ��MCU��FPGA״̬
	 * 
	 * @param info
	 */
	private void updateFddiVersion(UpgradeInfo info) {

		if (info.getStatus() == UpgradeInfo.STATUS_UPGRADE)
			return;

		if (StringUtils.isNotBlank(info.getMcuVersion())
				&& StringUtils.isNotBlank(info.getFpgaVersion())) {
			return;
		}

		McBts mcbts = McBtsCache.getInstance().queryByMoId(info.getMoId());

		String mcu = String.valueOf(mcbts
				.getAttribute(McBtsAttribute.Key.MCU_VERSION));
		String fpga = String.valueOf(mcbts
				.getAttribute(McBtsAttribute.Key.FPGA_VERSION));

		info.setMcuVersion(mcu);
		info.setFpgaVersion(fpga);
	}

	/**
	 * ��ȡ��Ҫ����ĳ���汾����������б�
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version) {
		List<UpgradeInfo> list = mcBtsBatchUpgradeDAO
				.queryUpgradingInfoByVersion(version);

		List<Mo> moList = MoCache.getInstance().queryAll();
		Map<Long, Mo> moMap = new ConcurrentHashMap<Long, Mo>();
		// ��list<Mo>ת����Map<moId,Mo>
		tranverseListToMap(moList, moMap);

		for (UpgradeInfo info : list)
			info.setName(moMap.get(info.getMoId()).getName());

		return list;
	}

	private void tranverseListToMap(List<Mo> moList, Map<Long, Mo> moMap) {
		for (Mo mo : moList)
			moMap.put(mo.getMoId(), mo);
	}

	/**
	 * �鵵
	 */
	public synchronized void archive() {
		List<UpgradeInfo> listToArchive = mcBtsBatchUpgradeDAO.queryToArchive();

		List<UpgradeInfoArchive> toArchive = new ArrayList<UpgradeInfoArchive>();

		// ��ȡ�����Ѿ����ڵĻ�վ��������,Ȼ�����鵵���б���
		for (UpgradeInfo info : listToArchive) {
			UpgradeInfoArchive archive = new UpgradeInfoArchive(info);
			toArchive.add(archive);
			upgradeInfoMap.remove(info.getMoId());
		}

		// �鵵�ظ���������Ŀ
		mcBtsBatchUpgradeArchiveDAO.saveAll(toArchive);

		// ɾ��Ҫ�鵵������
		mcBtsBatchUpgradeDAO.deleteArchive(listToArchive);
	}

	/*
	 * �±�Ϊspring��Ҫ��set����
	 */
	public void setMcBtsVersionManageDAO(
			McBtsVersionManageDAO mcBtsVersionManageDAO) {
		this.mcBtsVersionManageDAO = mcBtsVersionManageDAO;
	}

	public void setMcBtsBatchUpgradeArchiveDAO(
			McBtsBatchUpgradeArchiveDAO mcBtsBatchUpgradeArchiveDAO) {
		this.mcBtsBatchUpgradeArchiveDAO = mcBtsBatchUpgradeArchiveDAO;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
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

	public EnbFileManagerProxy getEnbFileManagerProxy() {
		return enbFileManagerProxy;
	}

	public void setEnbFileManagerProxy(EnbFileManagerProxy enbFileManagerProxy) {
		this.enbFileManagerProxy = enbFileManagerProxy;
	}

	public EnbFullTableConfigService getEnbFullTableConfigService() {
		return enbFullTableConfigService;
	}

	public void setEnbFullTableConfigService(
			EnbFullTableConfigService enbFullTableConfigService) {
		this.enbFullTableConfigService = enbFullTableConfigService;
	}

	public McBtsBizProxy getMcBtsBizProxy() {
		return mcBtsBizProxy;
	}

	public String getEnbFtpPath() {
		return enbFtpPath;
	}

	public void setEnbFtpPath(String enbFtpPath) {
		this.enbFtpPath = enbFtpPath;
	}

}
