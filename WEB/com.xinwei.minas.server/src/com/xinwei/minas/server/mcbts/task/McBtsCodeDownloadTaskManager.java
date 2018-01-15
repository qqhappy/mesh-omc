/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-6	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionHistoryManageDAO;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.McBtsMessageConstants;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsVersionManageService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts基站版本下载任务
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsCodeDownloadTaskManager {

	private Log log = LogFactory.getLog(McBtsCodeDownloadTaskManager.class);

	private McBtsVersionHistoryManageDAO mcBtsVersionHistoryManageDAO;
	private SequenceService sequenceService;

	private static final McBtsCodeDownloadTaskManager instance = new McBtsCodeDownloadTaskManager();
	private static Logger logger = Logger
			.getLogger(McBtsCodeDownloadTaskManager.class);

	private Executor executor;

	private McBtsVersionManageService btsVersionManageService;

	// eNB版本下载超时时间，默认30秒
	private long timeout = 30000;

	private McBtsCodeDownloadTaskManager() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
		// 定时删除超时进度的线程；
		executor = Executors.newSingleThreadExecutor();
		executor.execute(new BTSDownloadMonitorProcessor());
	}

	public static McBtsCodeDownloadTaskManager getInstance() {
		return instance;
	}

	// 不同基站的当前正在执行的任务
	Map<Long, McBtsCodeDownloadTask> tasks = new ConcurrentHashMap<Long, McBtsCodeDownloadTask>();

	// Map<TDLHistoryKey, McBtsCodeDownloadTask>
	// Map<TDLHistoryKey, McBtsCodeDownloadTask> history = new
	// ConcurrentHashMap<TDLHistoryKey, McBtsCodeDownloadTask>();

	public void handleResponse(McBtsMessage response) {
		Long btsId = response.getBtsId();

		// 通过btsId获取下载任务
		McBtsCodeDownloadTask task = tasks.get(btsId);
		if (task == null) {
			logger.debug("Cannot retrieve task from task list. "
					+ "The download may called by MCBTS BATCH UPGRADE.");
			return;
		}
		// 获取消息的moc值
		int moc = response.getMoc();

		if (moc == McBtsMessageConstants.MOC_MCBTS_FILE_RESULT_RESPONSE) {
			// 如果为文件下载结果消息执行:
			byte[] _result = response.getContent();
			int result = ByteUtils.toInt(_result, 0, 2);

			// 如果result的码为>0的数,则有错误.
			finishTask(btsId, result > 0 ? result : response.getActionResult());
		} else if (moc == McBtsMessageConstants.MOC_MCBTS_FILE_PROGRESS) {
			// 如果为文件下载进度消息执行:
			McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
			if (mcBts.getBtsType() != McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
				// 仅对小基站(即微蜂窝基站)进行下载进度监控,其它类型基站直接返回
				return;
			}

			// 更新Task中的进度
			Integer dlProgress = ByteUtils.toInt(response.getContent(), 0, 1);
			updateProgress(btsId, dlProgress);
		}
	}

	public McBtsVersionManageService getBtsVersionManageService() {
		return btsVersionManageService;
	}

	public void setBtsVersionManageService(
			McBtsVersionManageService btsVersionManageService) {
		this.btsVersionManageService = btsVersionManageService;
	}

	// 处理enb 基站版本下载的结果和进度
	public void handleEnbResponse(EnbAppMessage response) {
		// FIXME 封装转化方法；
		Long enbId = response.getEnbId();

		// 通过btsId获取下载任务
		McBtsCodeDownloadTask task = tasks.get(enbId);
		if (task == null) {
			logger.debug("Cannot retrieve task from task list. "
					+ "The download may called by MCBTS BATCH UPGRADE.");
			return;
		}
		// 获取消息的moc值
		int moc = response.getMoc();

		if (moc == EnbMessageConstants.MOC_ENB_VERSION_RESULT_NOTIFY) {

			// 设置enb状态为可配置
			Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
			try {
				// 基站文件下载完成
				int result = response.getIntValue(TagConst.ENB_DOWNLOAD_RESULT);
				enb.setBizName("");
				// 如果result为结果或者则有错误.
				finishTask(enbId, result);
			} finally {
				if (enb != null) {
					enb.setFullTableOperation(false);
				}
			}

			// enb基站软软件升级方法
			// try {
			// btsVersionManageService.upgrade(enb.getMoId(),
			// EnbConstantUtils.BBU_RRU);
			// } catch (Exception e) {
			// log.error(e);
			// }

		} else if (moc == EnbMessageConstants.MOC_ENB_VERSION_PROGRESS_NOTIFY) {
			// 基站文件下载进行中；

			// 更新Task中的进度
			updateProgress(enbId,
					response.getIntValue(TagConst.ENB_DOWNLOAD_PROGRESS));
		}
	}

	public List<McBtsCodeDownloadTask> queryAllTasks() {
		List<McBtsCodeDownloadTask> taskList = new ArrayList<McBtsCodeDownloadTask>();
		for (Long btsId : tasks.keySet()) {
			McBtsCodeDownloadTask task = tasks.get(btsId);
			if (task != null) {
				taskList.add(task);
			}
		}
		return taskList;

	}

	public McBtsCodeDownloadTask getBtsTask(Long btsId) {
		return tasks.get(btsId);
	}

	public void removeBtsTask(Long btsId) {
		if (tasks.get(btsId) != null)
			tasks.remove(btsId);
	}

	// 向tasks和history中添加task
	public void addBtsTask(Long btsId, McBtsCodeDownloadTask task) {
		mcBtsVersionHistoryManageDAO = OmpAppContext.getCtx().getBean(
				McBtsVersionHistoryManageDAO.class);
		// 从history中取出时间和状态放入task中
		task.setStartTime(task.getMcBtsVersionHistory().getStartTime());
		task.setActionResult(task.getMcBtsVersionHistory().getActionResult());

		// 如果histroy中的idx为空,插入一个idx
		if (task.getMcBtsVersionHistory().getIdx() == null)
			task.getMcBtsVersionHistory().setIdx(sequenceService.getNext());
		// 设置上传进度的时间
		task.setLastTime(new Date());
		// 向*tasks*插入task
		tasks.put(btsId, task);

		mcBtsVersionHistoryManageDAO
				.saveOrUpdate(task.getMcBtsVersionHistory());
	}

	/**
	 * 修改tasks中和history中的任务状态
	 * 
	 * @param btsId
	 * @param actionResult
	 */
	public void finishTask(Long btsId, int actionResult) {
		mcBtsVersionHistoryManageDAO = OmpAppContext.getCtx().getBean(
				McBtsVersionHistoryManageDAO.class);

		McBtsCodeDownloadTask task = this.getBtsTask(btsId);
		// 已经设置过超时的不再进行重复设置和存库；
		if (task.getActionResult() == McBtsCodeDownloadTask.OVERTIME)
			return;
		// 修改tasks里的任务为已完成
		task.finish(actionResult);
		// 修改history里的状态为完成
		mcBtsVersionHistoryManageDAO
				.saveOrUpdate(task.getMcBtsVersionHistory());
	}

	/**
	 * 更新基站下载时的进度
	 * 
	 * @param btsId
	 * @param byte_download_progress
	 */
	public void updateProgress(Long btsId, int progress) {
		McBtsCodeDownloadTask task = this.getBtsTask(btsId);
		task.setLastTime(new Date());
		McBtsVersionHistory history = task.getMcBtsVersionHistory();
		// FIXME:tiance 删除下边一行
		// dlProgress = new Random().nextInt(100);
		history.updateDownloadProgress(progress);
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	/**
	 * 基站版本下载时进度超时监测线程
	 */
	private class BTSDownloadMonitorProcessor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					doWork();
				} catch (Exception e) {
					log.error(e);
				} finally {
					try {
						// 延迟30秒
						Thread.sleep(McBtsCodeDownloadTask.OVERTIME_TRANSFER_PROGRESS);
					} catch (InterruptedException e) {
						log.error(e);

					}
				}
			}

		}

		private void doWork() {
			// FIXME 此方法暂时处理，以后需重新设计整体架构
			// 遍历task,对超时的基站 进行处理
			for (Long btsId : tasks.keySet()) {
				// Mcbts基站
				McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
				// Enb基站
				Enb enb = EnbCache.getInstance().queryByEnbId(btsId);
				if (mcBts == null && enb == null)
					continue;

				McBtsCodeDownloadTask task = tasks.get(btsId);

				if (mcBts != null
						&& mcBts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					// 仅对小基站(即微蜂窝基站)进行下载进度监控,其它类型基站不做任何处理
					if (task != null
							&& task.getActionResult() == McBtsCodeDownloadTask.UNDONE) {
						// 超时时间
						long overTime = System.currentTimeMillis()
								- task.getLastTime().getTime();
						// 升级未完成并且超时
						if (overTime > timeout) {
							// 超时处理
							finishTask(btsId, McBtsCodeDownloadTask.OVERTIME);
						}
					}
				} else if (enb != null) {
					// Enb基站

					if (task != null
							&& task.getActionResult() == McBtsCodeDownloadTask.UNDONE) {
						// 超时时间
						long overTime = System.currentTimeMillis()
								- task.getLastTime().getTime();
						// 升级未完成并且超时
						if (overTime > timeout) {
							// 超时处理
							enb.setFullTableOperation(false);
							finishTask(btsId, McBtsCodeDownloadTask.OVERTIME);
						}
					}
				}
			}

		}
	}

}
