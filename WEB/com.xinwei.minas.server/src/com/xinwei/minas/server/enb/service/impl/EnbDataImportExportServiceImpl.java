/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBasicDAO;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbDataImportExportService;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB数据导入导出服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbDataImportExportServiceImpl implements
		EnbDataImportExportService {

	private Log log = LogFactory.getLog(EnbDataImportExportServiceImpl.class);

	private EnbBizConfigDAO enbBizConfigDAO;

	private SequenceService sequenceService;

	private EnbBasicDAO enbBasicDAO;

	private EnbBizDataValidateHelper validateHelper;


	@Override
	public List<String> getAllFieldNames(int enbTypeId, String protocolVersion,
			String tableName) throws Exception {
		List<String> list=EnbBizHelper.getAllFieldNames(enbTypeId, protocolVersion, tableName);
		return list;
	}
	@Override
	public Map<Enb, List<XBizTable>> exportEnbData() throws Exception {
		List<Enb> enbList = EnbCache.getInstance().queryAll();
		return createDataMap(enbList);
	}

	@Override
	public Map<Enb, List<XBizTable>> exportEnbData(List<Long> enbIdList)
			throws Exception {
		EnbCache enbCache = EnbCache.getInstance();
		List<Enb> enbList = new LinkedList<Enb>();
		for (Long enbId : enbIdList) {
			enbList.add(enbCache.queryByEnbId(enbId));
		}
		return createDataMap(enbList);
	}

	private Map<Enb, List<XBizTable>> createDataMap(List<Enb> enbList)
			throws Exception {
		Map<Enb, List<XBizTable>> dataMap = new HashMap<Enb, List<XBizTable>>();
		for (Enb enb : enbList) {
			List<XBizTable> bizTableList = new LinkedList<XBizTable>();
			dataMap.put(enb, bizTableList);
			// 获取所有表名
			Set<String> tableList = EnbBizHelper.getAllTableNames(
					enb.getEnbType(), enb.getProtocolVersion());
			for (String tableName : tableList) {
				XBizTable bizTable = enbBizConfigDAO.query(enb.getMoId(),
						tableName, null);
				// 如果有数据，则加入到结果中
				if (EnbBizHelper.hasRecord(bizTable)) {
					bizTableList.add(bizTable);
				}
			}
		}
		return dataMap;
	}

	@Override
	public void importEnbData(Map<Enb, List<XBizTable>> dataMap)
			throws Exception {
		if (dataMap == null || dataMap.isEmpty())
			return;
		EnbCache enbCache = EnbCache.getInstance();
		StringBuilder errorMsg = null;
		for (Enb enb : dataMap.keySet()) {
			try {
				List<XBizTable> tableList = dataMap.get(enb);
				Enb cachedEnb = enbCache.queryByEnbId(enb.getEnbId());
				// 如果eNB不存在，则添加到系统中
				if (cachedEnb == null) {
					addEnb(enb);
				} else {
					// 修改eNB基本信息
					modifyEnb(enb, cachedEnb);
					// 删除所有配置数据
					enbBizConfigDAO.deleteAll(cachedEnb.getMoId());
				}
				cachedEnb = enbCache.queryByEnbId(enb.getEnbId());
				// 将新数据批量插入数据库
				enbBizConfigDAO.batchAdd(cachedEnb.getMoId(), tableList);
				// 判断是否开站
				boolean isActive = validateHelper.checkEnbActive(cachedEnb);
				cachedEnb.setActive(isActive);
			} catch (Exception e) {
				if (errorMsg == null) {
					errorMsg = new StringBuilder();
				}
				errorMsg.append("eNB ID:").append(enb.getHexEnbId())
						.append("  ").append(e.getLocalizedMessage());
			}
		}
		if (errorMsg != null) {
			throw new Exception(errorMsg.toString());
		}
	}

	/**
	 * 添加新的eNB
	 * 
	 * @param enb
	 * @throws Exception
	 */
	private void addEnb(Enb enb) throws Exception {
		long moId = sequenceService.getNext("BTS");
		enb.setMoId(moId);
		enb.setTypeId(MoTypeDD.ENODEB);
		try {
			// 基本信息存库
			enbBasicDAO.saveOrUpdate(enb);
		} catch (Exception e) {
			log.error("add eNB failed.", e);
			throw new Exception(OmpAppContext.getMessage("add_eNB_failed"));
		}
		// 添加到缓存
		EnbCache.getInstance().addOrUpdate(enb);
	}

	/**
	 * 修改eNB
	 * 
	 * @param enb
	 * @param cachedEnb
	 * @throws Exception
	 */
	private void modifyEnb(Enb enb, Enb cachedEnb) throws Exception {
		// 克隆以前的eNB，更新新信息
		Enb newEnb = cachedEnb.clone();
		newEnb.setName(enb.getName());
		newEnb.setProtocolVersion(enb.getProtocolVersion());
		newEnb.setManageStateCode(enb.getManageStateCode());
		newEnb.setSyncDirection(enb.getSyncDirection());
		try {
			// 基本信息存库
			enbBasicDAO.saveOrUpdate(newEnb);
		} catch (Exception e) {
			log.error("modify eNB failed.", e);
			throw new Exception(OmpAppContext.getMessage("modify_eNB_failed"));
		}
		// 添加到缓存
		EnbCache.getInstance().addOrUpdate(newEnb);
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setEnbBasicDAO(EnbBasicDAO enbBasicDAO) {
		this.enbBasicDAO = enbBasicDAO;
	}

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}
	


}
