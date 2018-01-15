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
 * McBts��վ�汾��������
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

	// eNB�汾���س�ʱʱ�䣬Ĭ��30��
	private long timeout = 30000;

	private McBtsCodeDownloadTaskManager() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
		// ��ʱɾ����ʱ���ȵ��̣߳�
		executor = Executors.newSingleThreadExecutor();
		executor.execute(new BTSDownloadMonitorProcessor());
	}

	public static McBtsCodeDownloadTaskManager getInstance() {
		return instance;
	}

	// ��ͬ��վ�ĵ�ǰ����ִ�е�����
	Map<Long, McBtsCodeDownloadTask> tasks = new ConcurrentHashMap<Long, McBtsCodeDownloadTask>();

	// Map<TDLHistoryKey, McBtsCodeDownloadTask>
	// Map<TDLHistoryKey, McBtsCodeDownloadTask> history = new
	// ConcurrentHashMap<TDLHistoryKey, McBtsCodeDownloadTask>();

	public void handleResponse(McBtsMessage response) {
		Long btsId = response.getBtsId();

		// ͨ��btsId��ȡ��������
		McBtsCodeDownloadTask task = tasks.get(btsId);
		if (task == null) {
			logger.debug("Cannot retrieve task from task list. "
					+ "The download may called by MCBTS BATCH UPGRADE.");
			return;
		}
		// ��ȡ��Ϣ��mocֵ
		int moc = response.getMoc();

		if (moc == McBtsMessageConstants.MOC_MCBTS_FILE_RESULT_RESPONSE) {
			// ���Ϊ�ļ����ؽ����Ϣִ��:
			byte[] _result = response.getContent();
			int result = ByteUtils.toInt(_result, 0, 2);

			// ���result����Ϊ>0����,���д���.
			finishTask(btsId, result > 0 ? result : response.getActionResult());
		} else if (moc == McBtsMessageConstants.MOC_MCBTS_FILE_PROGRESS) {
			// ���Ϊ�ļ����ؽ�����Ϣִ��:
			McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
			if (mcBts.getBtsType() != McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
				// ����С��վ(��΢���ѻ�վ)�������ؽ��ȼ��,�������ͻ�վֱ�ӷ���
				return;
			}

			// ����Task�еĽ���
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

	// ����enb ��վ�汾���صĽ���ͽ���
	public void handleEnbResponse(EnbAppMessage response) {
		// FIXME ��װת��������
		Long enbId = response.getEnbId();

		// ͨ��btsId��ȡ��������
		McBtsCodeDownloadTask task = tasks.get(enbId);
		if (task == null) {
			logger.debug("Cannot retrieve task from task list. "
					+ "The download may called by MCBTS BATCH UPGRADE.");
			return;
		}
		// ��ȡ��Ϣ��mocֵ
		int moc = response.getMoc();

		if (moc == EnbMessageConstants.MOC_ENB_VERSION_RESULT_NOTIFY) {

			// ����enb״̬Ϊ������
			Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
			try {
				// ��վ�ļ��������
				int result = response.getIntValue(TagConst.ENB_DOWNLOAD_RESULT);
				enb.setBizName("");
				// ���resultΪ����������д���.
				finishTask(enbId, result);
			} finally {
				if (enb != null) {
					enb.setFullTableOperation(false);
				}
			}

			// enb��վ�������������
			// try {
			// btsVersionManageService.upgrade(enb.getMoId(),
			// EnbConstantUtils.BBU_RRU);
			// } catch (Exception e) {
			// log.error(e);
			// }

		} else if (moc == EnbMessageConstants.MOC_ENB_VERSION_PROGRESS_NOTIFY) {
			// ��վ�ļ����ؽ����У�

			// ����Task�еĽ���
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

	// ��tasks��history�����task
	public void addBtsTask(Long btsId, McBtsCodeDownloadTask task) {
		mcBtsVersionHistoryManageDAO = OmpAppContext.getCtx().getBean(
				McBtsVersionHistoryManageDAO.class);
		// ��history��ȡ��ʱ���״̬����task��
		task.setStartTime(task.getMcBtsVersionHistory().getStartTime());
		task.setActionResult(task.getMcBtsVersionHistory().getActionResult());

		// ���histroy�е�idxΪ��,����һ��idx
		if (task.getMcBtsVersionHistory().getIdx() == null)
			task.getMcBtsVersionHistory().setIdx(sequenceService.getNext());
		// �����ϴ����ȵ�ʱ��
		task.setLastTime(new Date());
		// ��*tasks*����task
		tasks.put(btsId, task);

		mcBtsVersionHistoryManageDAO
				.saveOrUpdate(task.getMcBtsVersionHistory());
	}

	/**
	 * �޸�tasks�к�history�е�����״̬
	 * 
	 * @param btsId
	 * @param actionResult
	 */
	public void finishTask(Long btsId, int actionResult) {
		mcBtsVersionHistoryManageDAO = OmpAppContext.getCtx().getBean(
				McBtsVersionHistoryManageDAO.class);

		McBtsCodeDownloadTask task = this.getBtsTask(btsId);
		// �Ѿ����ù���ʱ�Ĳ��ٽ����ظ����úʹ�⣻
		if (task.getActionResult() == McBtsCodeDownloadTask.OVERTIME)
			return;
		// �޸�tasks�������Ϊ�����
		task.finish(actionResult);
		// �޸�history���״̬Ϊ���
		mcBtsVersionHistoryManageDAO
				.saveOrUpdate(task.getMcBtsVersionHistory());
	}

	/**
	 * ���»�վ����ʱ�Ľ���
	 * 
	 * @param btsId
	 * @param byte_download_progress
	 */
	public void updateProgress(Long btsId, int progress) {
		McBtsCodeDownloadTask task = this.getBtsTask(btsId);
		task.setLastTime(new Date());
		McBtsVersionHistory history = task.getMcBtsVersionHistory();
		// FIXME:tiance ɾ���±�һ��
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
	 * ��վ�汾����ʱ���ȳ�ʱ����߳�
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
						// �ӳ�30��
						Thread.sleep(McBtsCodeDownloadTask.OVERTIME_TRANSFER_PROGRESS);
					} catch (InterruptedException e) {
						log.error(e);

					}
				}
			}

		}

		private void doWork() {
			// FIXME �˷�����ʱ�����Ժ��������������ܹ�
			// ����task,�Գ�ʱ�Ļ�վ ���д���
			for (Long btsId : tasks.keySet()) {
				// Mcbts��վ
				McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
				// Enb��վ
				Enb enb = EnbCache.getInstance().queryByEnbId(btsId);
				if (mcBts == null && enb == null)
					continue;

				McBtsCodeDownloadTask task = tasks.get(btsId);

				if (mcBts != null
						&& mcBts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
					// ����С��վ(��΢���ѻ�վ)�������ؽ��ȼ��,�������ͻ�վ�����κδ���
					if (task != null
							&& task.getActionResult() == McBtsCodeDownloadTask.UNDONE) {
						// ��ʱʱ��
						long overTime = System.currentTimeMillis()
								- task.getLastTime().getTime();
						// ����δ��ɲ��ҳ�ʱ
						if (overTime > timeout) {
							// ��ʱ����
							finishTask(btsId, McBtsCodeDownloadTask.OVERTIME);
						}
					}
				} else if (enb != null) {
					// Enb��վ

					if (task != null
							&& task.getActionResult() == McBtsCodeDownloadTask.UNDONE) {
						// ��ʱʱ��
						long overTime = System.currentTimeMillis()
								- task.getLastTime().getTime();
						// ����δ��ɲ��ҳ�ʱ
						if (overTime > timeout) {
							// ��ʱ����
							enb.setFullTableOperation(false);
							finishTask(btsId, McBtsCodeDownloadTask.OVERTIME);
						}
					}
				}
			}

		}
	}

}
