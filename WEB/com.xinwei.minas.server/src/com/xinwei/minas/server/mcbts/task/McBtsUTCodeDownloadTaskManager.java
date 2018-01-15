/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.mcbts.dao.sysManage.TerminalVersionManageDAO;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts�ն˰汾��������
 * 
 * @author tiance
 * 
 */

public class McBtsUTCodeDownloadTaskManager {

	private static final McBtsUTCodeDownloadTaskManager instance = new McBtsUTCodeDownloadTaskManager();
	private static Logger logger = Logger
			.getLogger(McBtsUTCodeDownloadTaskManager.class);

	private TerminalVersionManageDAO terminalVersionManageDAO;
	private SequenceService sequenceService;

	private McBtsUTCodeDownloadTaskManager() {
		terminalVersionManageDAO = OmpAppContext.getCtx().getBean(
				TerminalVersionManageDAO.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	public static McBtsUTCodeDownloadTaskManager getInstance() {
		return instance;
	}

	// ��ͬ��վ�ĵ�ǰ����ִ�е�����
	Map<Long, UTCodeDownloadTask> tasks = new ConcurrentHashMap<Long, UTCodeDownloadTask>();

	public void handleResponse(McBtsMessage response) {
		Long btsId = response.getBtsId();

		UTCodeDownloadTask task = tasks.get(btsId);

		if (task == null) {
			logger.error("Error: cannot retrieve task from task list.");
			return;
		}

		byte[] _result = response.getContent();
		int result = ByteUtils.toInt(_result, 0, 2);

		finishTask(btsId, result > 0 ? result : response.getActionResult());
	}

	public UTCodeDownloadTask getUTTask(Long btsId) {
		return tasks.get(btsId);
	}

	public void removeUTTask(Long btsId) {
		if (tasks.get(btsId) != null)
			tasks.remove(btsId);
	}

	/**
	 * ��ȡ�����ݿ������л�վ��������ʷtask
	 * 
	 * @param btsId
	 * @return
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> getAllMcBtsUTTasksFromHistory(
			Long btsId, long DEAD_TIME) {
		Map<TDLHistoryKey, UTCodeDownloadTask> history = terminalVersionManageDAO
				.queryHistroy(btsId);
		if (history == null) {
			return null;
		}
		for (UTCodeDownloadTask value : history.values()) {
			if (value.isOverTime(DEAD_TIME)) {
				value.setActionResult(UTCodeDownloadTask.OVERTIME);
			}
		}
		return history;
	}

	// ��tasks��history�����task
	public void addUTTask(Long btsId, UTCodeDownloadTask task) {
		// ��*tasks*����task
		tasks.put(btsId, task);

		TDLHistoryKey key = new TDLHistoryKey(btsId, task.getTerminalVersion()
				.getTypeId(), task.getTerminalVersion().getVersion(),
				Integer.parseInt(task.getTerminalVersion().getFileType()));
		task.setStartTime(new Date());
		if (terminalVersionManageDAO.queryHistoryByKey(key) == null) {
			Long id = sequenceService.getNext();
			task.setId(id);
			terminalVersionManageDAO.insertToHistory(btsId, task);
		} else {
			terminalVersionManageDAO.updateHistory(btsId, task);
		}
	}

	/**
	 * �޸�tasks�к�history�е�����״̬
	 * 
	 * @param btsId
	 * @param actionResult
	 */
	public void finishTask(Long btsId, int actionResult) {
		UTCodeDownloadTask task = this.getUTTask(btsId);
		// �޸�tasks�������Ϊ�����
		task.finish(actionResult);

		// �޸�history���״̬Ϊ���
		terminalVersionManageDAO.updateHistory(btsId, task);
	}
}
