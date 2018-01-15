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
 * 基站批量升级任务管理器
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeTaskManager {

	private Log log = LogFactory.getLog(McBtsBatchUpgradeTaskManager.class);

	// 当前下载数
	private volatile int curDownloads;

	// 最大同时下载数,可配
	private static int maxDownload = 5;

	private static long timeout = 100000;

	// FTP地址
	private String emsServerIp;

	// FTP用户名
	private String ftpUsername;

	// FTP密码
	private String ftpPassword;

	// FTP端口
	private int ftpPort;

	// FTP目标路径
	private String ftpSoftwarePath;

	// 维持下载升级的基站最新状态
	private Map<Long, UpgradeInfo> upgradeInfoMap = new ConcurrentHashMap<Long, UpgradeInfo>();

	private McBtsBatchUpgradeDAO mcBtsBatchUpgradeDAO;

	private McBtsBatchUpgradeArchiveDAO mcBtsBatchUpgradeArchiveDAO;

	private McBtsVersionManageDAO mcBtsVersionManageDAO;

	private McBtsBizProxy mcBtsBizProxy;

	private EnbFileManagerProxy enbFileManagerProxy;

	private EnbFullTableConfigService enbFullTableConfigService;
	// enb的ftp目标路径
	private String enbFtpPath;

	private static McBtsBatchUpgradeTaskManager instance = null;

	private McBtsBatchUpgradeTaskManager(int maxDownload, long timeout,
			McBtsBatchUpgradeDAO mcBtsBatchUpgradeDAO) {
		McBtsBatchUpgradeTaskManager.maxDownload = maxDownload;
		McBtsBatchUpgradeTaskManager.timeout = timeout;
		this.mcBtsBatchUpgradeDAO = mcBtsBatchUpgradeDAO;

		// 初始化数据
		initMap();

		// 创建下载线程
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
	 * 处理enb基站下载完成的通知
	 * 
	 * @param response
	 */
	public void handleEnbDLFinishNotify(EnbAppMessage response) {
		// FIXME 增加处理
		Enb enb = EnbCache.getInstance().queryByEnbId(response.getEnbId());
		if (enb == null) {
			log.error("Can not find enb by enbId in MCBTS-DOWNLOADING-FINISHED message!!!");
			return;
		}
		// 设置释放资源
		enb.setFullTableOperation(false);
		enb.setBizName("");
		// 进入公共处理方法；
		handleFinishResult(enb.getMoId(),
				response.getIntValue(TagConst.ENB_DOWNLOAD_RESULT),
				response.getTransactionId());
	}

	/**
	 * 处理基站下载完成的通知
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
		// 进入公共处理方法；
		handleFinishResult(mcbts.getMoId(), result, response.getTransactionId());

	}

	/*
	 * 升级结果处理共同走的方法
	 */
	public void handleFinishResult(long moId, int result, long transId) {
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryByMoId(moId);

		if (info == null)
			return;

		// 判断任务的状态,如果是已终止的任务或者已超时,就不会再改变任务状态,保持终止的状态
		// if (info.getStatus() == UpgradeInfo.STATUS_TERMINATED
		// || info.getStatus() == UpgradeInfo.STATUS_DOWNLOAD_TIMEOUT) {
		// return;
		// }

		// 只有处在下载状态，才应该接收该消息。
		if (info.getStatus() != UpgradeInfo.STATUS_DOWNLOADING
				|| info.getTransactionId() != transId) {
			return;
		}

		downloadCountDecrease();

		// 更新FDDI版本
		updateFddiVersion(info);

		if (result == 0) {
			// 成功
			// 下载完成状态
			markDownloaded(info);
			// 进行升级操作
			configUpgrade(info);
		} else {
			// 失败,状态改为失败
			info.setErrorCode(result);
			markDownloadError(info);
		}

	}

	/**
	 * 初始化upgradeInfoMap
	 */
	private void initMap() {
		List<UpgradeInfo> list = mcBtsBatchUpgradeDAO.queryAll();
		for (UpgradeInfo info : list) {
			upgradeInfoMap.put(info.getMoId(), info);
		}
	}

	/**
	 * 获取所有基站升级信息
	 * 
	 * @return 外层Map以btsType为Key; 内层Map以基站moId为Key
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
	 * 获取所有基站升级信息
	 * 
	 * @return 外层Map以btsType为Key; 内层Map以基站moId为Key
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

		// 排序:未完成 - 正在下载 - 下载超时 - 错误 - 完成
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
	 * 获取正在进行升级的任务
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
	 * 根据moId获取基站下载升级信息
	 * 
	 * @param moId
	 * @return 下载升级信息
	 */
	public UpgradeInfo get(long moId) {
		return upgradeInfoMap.get(moId);
	}

	/**
	 * 添加一个下载升级任务
	 * 
	 * @param upgradeInfo
	 */
	public void add(List<UpgradeInfo> list) {
		List<UpgradeInfo> listInDB = mcBtsBatchUpgradeDAO.queryAll();

		List<UpgradeInfoArchive> toArchive = new ArrayList<UpgradeInfoArchive>();
		List<UpgradeInfo> toArchive2 = new ArrayList<UpgradeInfo>();

		// 获取表中已经存在的基站升级任务,然后放入归档的列表中
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

		// 归档重复的升级项目
		mcBtsBatchUpgradeArchiveDAO.saveAll(toArchive);

		mcBtsBatchUpgradeDAO.deleteArchive(toArchive2);
		// 存储到数据库
		mcBtsBatchUpgradeDAO.saveAll(list);
	}

	/**
	 * 删除一个下载升级任务
	 * 
	 * @param moId
	 */
	public synchronized void delete(long moId) {
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryByMoId(moId);
		mcBtsBatchUpgradeDAO.delete(info);
		upgradeInfoMap.remove(moId);
	}

	/**
	 * 更新一个下载升级任务
	 * 
	 * @param moId
	 */
	public synchronized void update(UpgradeInfo info) {
		mcBtsBatchUpgradeDAO.saveOrUpdate(info);
		upgradeInfoMap.put(info.getMoId(), info);
	}

	/**
	 * 查看是否有超时的下载任务
	 * 
	 * @return 超时下载任务的列表
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
	 * 下载记录加1
	 */
	public void downloadCountIncrease() {
		curDownloads++;
	}

	/**
	 * 下载记录减1
	 */
	public void downloadCountDecrease() {
		curDownloads--;
	}

	/**
	 * 判断是否到达最大下载数
	 * 
	 * @return
	 */
	public synchronized boolean reachMaxDownload() {
		return curDownloads == maxDownload;
	}

	/**
	 * 修改超时的任务的状态
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
	 * 修改任务为正在下载
	 * 
	 * @param upgradeInfo
	 */
	public void markDownloading(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_DOWNLOADING);
		upgradeInfo.setStartTime(new Date());

		update(upgradeInfo);
	}

	/**
	 * 修改任务为下载完成
	 * 
	 * @param upgradeInfo
	 */
	public void markDownloaded(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_DOWNLOAD_FINISH);

		update(upgradeInfo);
	}

	/**
	 * 修改任务为已终止
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
			// 已经无法终止,抛出异常
			throw new Exception();
		}

		// 终止正在下载的任务时，当前下载任务应该减一
		if (status == UpgradeInfo.STATUS_DOWNLOADING) {
			downloadCountDecrease();
		}

		infoInDB.setStatus(UpgradeInfo.STATUS_TERMINATED);
		infoInDB.setEndTime(new Date());

		update(infoInDB);

	}

	/**
	 * 修改任务为下载软件时出错
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
	 * 修改任务为升级失败
	 * 
	 * @param info
	 */
	private void markUpgradeError(UpgradeInfo info) {
		info.setStatus(UpgradeInfo.STATUS_UPGRADE_FAIL);
		info.setEndTime(new Date());

		update(info);
	}

	/**
	 * 修改任务为升级命令已下发
	 * 
	 * @param upgradeInfo
	 */
	public void markUpgraded(UpgradeInfo upgradeInfo) {
		upgradeInfo.setStatus(UpgradeInfo.STATUS_UPGRADE);
		upgradeInfo.setEndTime(new Date());

		update(upgradeInfo);
	}

	/**
	 * 向基站发送下载请求
	 * 
	 * @throws Exception
	 */
	public synchronized void configDownload() throws Exception {
		if (mcBtsBatchUpgradeDAO == null)
			return;

		// 获取一个未执行的任务
		UpgradeInfo info = mcBtsBatchUpgradeDAO.queryFreeUpgradeInfo();

		if (info == null) {
			throw new NullPointerException();
		}

		// 更新光纤的版本
		updateFddiVersion(info);

		// 查出相应的version
		McBtsVersion mv = mcBtsVersionManageDAO.queryByBtsTypeAndVersion(
				info.getBtsType(), info.getDownloadVersion());

		// 获取基站的状态以判断是否继续
		// FIXME 此处增加enb时，需要改写方法；
		Mo mo = MoCache.getInstance().queryByMoId(info.getMoId());
		boolean stop = false;

		switch (mo.getTypeId()) {
		case MoTypeDD.MCBTS:
			// McBts基站
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
			// enb基站
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

		// 下载开始,记录下载状态
		downloadCountIncrease();
		int transactionId = TransactionIdGenerator.next();
		info.setTransactionId(transactionId);
		markDownloading(info);

		// 向网元发送消息
		try {
			if (mo.getTypeId() == MoTypeDD.MCBTS) {
				// Mcbts基站
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
				// Enb基站升级
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
	 * enb版本下载的方法处理
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
		if (enb.setFullTableOperation(true)) {// 可以配置
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
				// 数据文件路径；
				String dataFileDirectory = EnbFullTableTaskManager
						.getInstance().getConfigFtpdDrectory();
				// 数据文件名
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
	 * 向基站发送升级请求
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
	 * 更新光纤拉远的MCU和FPGA状态
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
	 * 获取将要升级某个版本软件的任务列表
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
		// 将list<Mo>转换成Map<moId,Mo>
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
	 * 归档
	 */
	public synchronized void archive() {
		List<UpgradeInfo> listToArchive = mcBtsBatchUpgradeDAO.queryToArchive();

		List<UpgradeInfoArchive> toArchive = new ArrayList<UpgradeInfoArchive>();

		// 获取表中已经存在的基站升级任务,然后放入归档的列表中
		for (UpgradeInfo info : listToArchive) {
			UpgradeInfoArchive archive = new UpgradeInfoArchive(info);
			toArchive.add(archive);
			upgradeInfoMap.remove(info.getMoId());
		}

		// 归档重复的升级项目
		mcBtsBatchUpgradeArchiveDAO.saveAll(toArchive);

		// 删除要归档的任务
		mcBtsBatchUpgradeDAO.deleteArchive(listToArchive);
	}

	/*
	 * 下边为spring需要的set方法
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
