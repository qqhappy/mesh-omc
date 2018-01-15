/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.server.mcbts.dao.common.McBtsBatchUpgradeArchiveDAO;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.McBtsBatchUpgradeService;
import com.xinwei.minas.server.mcbts.task.McBtsBatchUpgradeTaskManager;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站批量升级服务的类
 * 
 * @author tiance
 * 
 */

public class McBtsBatchUpgradeServiceImpl implements McBtsBatchUpgradeService {

	private Log log = LogFactory.getLog(McBtsBatchUpgradeServiceImpl.class);

	private McBtsBatchUpgradeTaskManager mcBtsBatchUpgradeTaskManager;

	private SequenceService sequenceService;

	private McBtsBatchUpgradeArchiveDAO mcBtsBatchUpgradeArchiveDAO;

	public McBtsBatchUpgradeServiceImpl() {
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);

		mcBtsBatchUpgradeArchiveDAO = AppContext.getCtx().getBean(
				McBtsBatchUpgradeArchiveDAO.class);
	}

	@Override
	public void addTask(List<UpgradeInfo> list) throws Exception {
		for (UpgradeInfo info : list) {
			info.setIdx(sequenceService.getNext());
		}

		mcBtsBatchUpgradeTaskManager.add(list);
	}

	@Override
	public Map<Integer, Map<Long, UpgradeInfo>> queryAll() throws Exception {
		return mcBtsBatchUpgradeTaskManager.getAll();
	}

	@Override
	public Map<Integer, List<UpgradeInfo>> queryAll2() throws Exception {
		return mcBtsBatchUpgradeTaskManager.getAll2();
	}

	@Override
	public List<UpgradeInfo> queryUpgrading() throws Exception {
		return mcBtsBatchUpgradeTaskManager.queryUpgrading();
	}

	@Override
	public void terminate(List<UpgradeInfo> list) throws Exception {
		for (UpgradeInfo info : list) {
			try {
				mcBtsBatchUpgradeTaskManager.markTerminated(info);
			} catch (Exception e) {
				log.debug("Some tasks could not be terminated.");
			}
		}
	}

	@Override
	public void archive() throws Exception {
		mcBtsBatchUpgradeTaskManager.archive();
	}

	@Override
	public Map<Integer, List<UpgradeInfoArchive>> queryLatestArchive()
			throws Exception {
		List<UpgradeInfoArchive> listFromDB = mcBtsBatchUpgradeArchiveDAO
				.queryLatestArchive();

		if (listFromDB == null || listFromDB.isEmpty())
			return null;

		Map<Long, McBts> btsMap = McBtsCache.getInstance().getMapByMoId();

		Map<Long, UpgradeInfoArchive> map = new HashMap<Long, UpgradeInfoArchive>();
		// 每个基站只取最新的一个升级完成的结果,在dao中已经做了排序
		for (UpgradeInfoArchive archive : listFromDB) {
			map.put(archive.getMoId(), archive);
		}

		Map<Integer, List<UpgradeInfoArchive>> result = new LinkedHashMap<Integer, List<UpgradeInfoArchive>>();

		for (Entry<Long, UpgradeInfoArchive> entry : map.entrySet()) {
			UpgradeInfoArchive archive = entry.getValue();

			// 设置名字
			McBts mcbts = btsMap.get(archive.getMoId());
			if (mcbts != null)
				archive.setName(mcbts.getName());

			// 添加到map中的list去,如果没有list,先创建list给map
			int btsType = archive.getBtsType();
			List<UpgradeInfoArchive> list = result.get(btsType);
			if (list == null) {
				list = new ArrayList<UpgradeInfoArchive>();
				result.put(btsType, list);
			}

			list.add(archive);
		}

		return result;
	}

	@Override
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId)
			throws Exception {
		List<UpgradeInfoArchive> list = mcBtsBatchUpgradeArchiveDAO
				.queryArchiveByMoId(moId);

		Map<Long, McBts> btsMap = McBtsCache.getInstance().getMapByMoId();

		// 设置名字
		for (UpgradeInfoArchive archive : list) {
			McBts mcbts = btsMap.get(archive.getMoId());
			if (mcbts != null)
				archive.setName(mcbts.getName());
		}

		return list;
	}

	// 注入McBtsBatchUpgradeTaskManager
	public void setMcBtsBatchUpgradeTaskManager(
			McBtsBatchUpgradeTaskManager mcBtsBatchUpgradeTaskManager) {
		this.mcBtsBatchUpgradeTaskManager = mcBtsBatchUpgradeTaskManager;
	}

	@Override
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version)
			throws Exception {
		return mcBtsBatchUpgradeTaskManager
				.queryUpgradingInfoByVersion(version);
	}

	public McBtsBatchUpgradeTaskManager getMcBtsBatchUpgradeTaskManager() {
		return mcBtsBatchUpgradeTaskManager;
	}

}
