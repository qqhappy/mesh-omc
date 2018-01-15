/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 终端升级进度和结果记录的任务管理器
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsUTUpgradeTaskManager {

	// Map<btsId,Map<终端pid,进度>>
	private Map<Long, Map<String, String>> map = new ConcurrentHashMap<Long, Map<String, String>>();

	private McBtsUTUpgradeTaskManager() {
	}

	private static McBtsUTUpgradeTaskManager instance = new McBtsUTUpgradeTaskManager();

	public static McBtsUTUpgradeTaskManager getInstance() {
		return instance;
	}

	/**
	 * 处理终端升级进度通知
	 * 
	 * @param message
	 */
	public void handleUTUpgradeProgressNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();

		byte[] content = message.getContent();
		int offset = 0;

		String pid = ByteUtils.toHexString(content, offset, 4);
		offset += 4;

		int value = ByteUtils.toInt(content, offset, 1);
		offset += 1;

		setUTProgress(btsId, pid, String.valueOf(value));
	}

	/**
	 * 处理终端升级结果通知
	 * 
	 * @param message
	 */
	public void handleUTUpgradeResultNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();

		byte[] content = message.getContent();
		int offset = 0;

		String pid = ByteUtils.toHexString(content, offset, 4);
		offset += 4;

		String value = ByteUtils.toHexString(content, offset, 2);
		offset += 2;

		setUTProgress(btsId, pid, value.equals("0000") ? "100" : value);
	}

	/**
	 * 获取一些终端的进度
	 * 
	 * @param utList
	 *            要查询进度的终端
	 * @return
	 */
	public synchronized Map<String, String> getUTProgress(
			List<UserTerminal> utList) {

		if (utList == null || utList.isEmpty())
			return null;

		Map<String, String> result = new HashMap<String, String>();

		// 遍历要查询进度的终端的列表
		for (UserTerminal ut : utList) {
			Long btsId = ut.getBtsId();

			Map<String, String> utMap = map.get(btsId);

			// 如果整个基站的map都是空的,就插入null
			if (utMap == null || utMap.isEmpty()) {
				// TODO fake
//				utMap = new HashMap<String, String>();
//				map.put(btsId, utMap);
//				utMap.put(ut.getPid(), "100");
				// TODO fake end

				result.put(ut.getPid(), null);
				continue;
			}

			String progress = utMap.get(ut.getPid());
			
			if (progress == null) {
				result.put(ut.getPid(), null);
				continue;
			}

			// 未完成时显示如35之类的2位长度,完成后显示100或2934等错误码.因此长度>2时删除完成的结果
			if (progress.length() > 2)
				utMap.remove(ut.getPid());

			result.put(ut.getPid(), progress);
		}
		return result;
	}

	public synchronized void setUTProgress(Long btsId, String pid, String value) {
		Map<String, String> utMap = map.get(btsId);

		if (utMap == null) {
			utMap = new HashMap<String, String>();
			map.put(btsId, utMap);
		}

		utMap.put(pid, value);
	}
}
