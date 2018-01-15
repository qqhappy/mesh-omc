/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.dao.EnbStudyDataConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbStudyDataConfigParser;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.proxy.EnbExtBizProxy;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbBizTemplateService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbExtBizService;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.enb.service.EnbStudyDataConfigCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB扩展业务服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbExtBizServiceImpl implements EnbExtBizService {

	private Log log = LogFactory.getLog(EnbExtBizServiceImpl.class);

	private EnbStudyDataConfigDAO enbStudyDataConfigDAO;

	private EnbExtBizProxy enbExtBizProxy;

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizConfigService enbBizConfigService;

	private EnbBizTemplateService enbBizTemplateService;

	@Override
	public void reset(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 可配置时才下发，不可配置抛出异常
		if (enb.isConfigurable()) {
			enbExtBizProxy.reset(enb.getEnbId());
		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

	@Override
	public void reset(long moId, int rackId, int shelfId, int boardId)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 可配置时才下发，不可配置抛出异常
		if (enb.isConfigurable()) {
			enbExtBizProxy.reset(enb.getEnbId(), rackId, shelfId, boardId);
		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

	@Override
	public String exportActiveData(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 尚未开站，不能导出开站参数
		if (!enb.isActive()) {
			throw new Exception(
					OmpAppContext
							.getMessage("enb_not_active_cannot_export_data"));
		}
		// 生成sql语句
		EnbFullTableConfigService service = AppContext.getCtx().getBean(
				EnbFullTableConfigService.class);
		Map<String, List<String>> fullTableSql = service.generateFullTableSql(
				moId, enb.getProtocolVersion());
		StringBuilder data = new StringBuilder();
		for (List<String> sqls : fullTableSql.values()) {
			for (String sql : sqls) {
				data.append(sql + "\n");
			}
		}
		// 去掉最后的换行符
		if (data.length() > 0) {
			data.deleteCharAt(data.length() - 1);
		}
		return data.toString();
	}

	@Override
	public void studyEnbDataConfig(long moId, boolean reStudy) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		if (enb.isConfigurable()) {
			String version = enb.getSoftwareVersion();
			// 相同版本的需要做同步
			synchronized (version) {
				// 当不需要重新学习时，如果配置已存在，则抛出异常
				boolean configExist = EnbStudyDataConfigCache.getInstance()
						.isConfigExist(version);
				// 如果配置已存在，但不需要重新学习，则提示
				if (configExist && !reStudy) {
					throw new BizException(
							OmpAppContext
									.getMessage("current_version_already_study"));
				}
				// 可配置时才下发，不可配置抛出异常
				String dataConfig = enbExtBizProxy.studyEnbDataConfig(enb
						.getEnbId());

				Map<String, XList> tableMap = null;
				try {
					log.debug("enbId=" + enb.getHexEnbId() + ", version="
							+ version + ", dataConfig=" + dataConfig);
					// 解析数据
					tableMap = EnbStudyDataConfigParser.parse(dataConfig);
				} catch (Exception e) {
					log.error(
							"parse enb study data config with error. version="
									+ version, e);
					throw e;
				}
				try {
					// 保存之前先将之前的配置删除，然后存入新配置
					enbStudyDataConfigDAO.delete(version);
					// 保存数据到数据库和缓存
					enbStudyDataConfigDAO.saveDataConfig(version, dataConfig);
					EnbStudyDataConfigCache.getInstance().addConfig(version,
							tableMap);
				} catch (Exception e) {
					log.error("save enb study data config failed. enb version="
							+ version, e);
					throw e;
				}
			}
		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

	@Override
	public Map<String, Map<String, XList>> queryStudyDataConfig()
			throws Exception {
		return EnbStudyDataConfigCache.getInstance().queryAllConfig();
	}

	@Override
	public void recoverDefaultData(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 同一时刻只允许一个用户对某一个基站进行增量配置操作
		synchronized (enb) {
			String hexEnbId = enb.getHexEnbId();
			log.debug("begin recover default data. enbId=" + hexEnbId);
			Map<String, XBizTable> dataMap = null;
			Map<String, XBizTable> backupDataMap = null;
			try {
				// 查询模板数据
				dataMap = queryBizTemplateData(enb);
			} catch (Exception e) {
				log.error("query default data failed. enbId=" + hexEnbId, e);
				throw new Exception(
						OmpAppContext.getMessage("recover_default_data_failed"));
			}
			try {
				// 查询备份数据
				backupDataMap = queryBackUpData(moId, dataMap.keySet());
			} catch (Exception e) {
				log.error("query backup data failed. enbId=" + hexEnbId, e);
				throw new Exception(
						OmpAppContext.getMessage("recover_default_data_failed"));
			}
			List<String> rollBackTables = new ArrayList<String>();
			try {
				for (String tableName : dataMap.keySet()) {
					// 先删除表中所有数据，再将模板数据复制给基站
					enbBizConfigDAO.delete(moId, tableName, null);
					// 删除数据成功后，记录表名，失败回退时使用
					rollBackTables.add(tableName);
					XBizTable tableData = dataMap.get(tableName);
					for (XBizRecord record : tableData.getRecords()) {
						// 将ENB参数表中的enbId改成本基站的enbId，名称改成本基站的enbName
						if (tableName
								.equals(EnbConstantUtils.TABLE_NAME_T_ENB_PARA)) {
							handleEnbParamRecord(enb, record);
						}
						// 将记录中的hexArray类型字段的值转成小写
						EnbBizHelper.changeHexArrayToLowerCase(
								enb.getEnbType(), enb.getProtocolVersion(),
								tableName, record);
						enbBizConfigDAO.add(moId, tableName, record);
					}
				}
			} catch (Exception e) {
				log.error("recover default data failed. enbId=" + hexEnbId, e);
				// 失败后回退
				try {
					rollBack(enb, rollBackTables, backupDataMap);
				} catch (Exception e2) {
					log.error("roll back data failed. enbId=" + hexEnbId, e2);
				}
				throw new Exception(
						OmpAppContext.getMessage("recover_default_data_failed"));
			}
			log.debug("recover default data success. enbId=" + hexEnbId);
			if (enb.isConfigurable()) {
				// 恢复默认数据后，发起数据同步过程
				compareAndSyncDataToNe(enb);
			}
		}
	}

	/**
	 * 将ENB参数表中的enbId改成本基站的enbId，名称改成本基站的enbName
	 * 
	 * @param enb
	 * @param bizRecord
	 * @return
	 */
	private XBizRecord handleEnbParamRecord(Enb enb, XBizRecord bizRecord) {
		XBizField idField = new XBizField(EnbConstantUtils.FIELD_NAME_ENB_ID,
				enb.getEnbId().toString());
		bizRecord.addField(idField);
		XBizField nameField = new XBizField(
				EnbConstantUtils.FIELD_NAME_ENB_NAME, enb.getName());
		bizRecord.addField(nameField);
		return bizRecord;
	}

	/**
	 * 恢复默认值失败后回退
	 * 
	 * @param moId
	 * @param tableList
	 * @param dataMap
	 * @throws Exception
	 */
	private void rollBack(Enb enb, List<String> tableList,
			Map<String, XBizTable> dataMap) throws Exception {
		for (String tableName : tableList) {
			XBizTable tableData = dataMap.get(tableName);
			if (tableData == null || tableData.getRecords().isEmpty())
				continue;
			for (XBizRecord record : tableData.getRecords()) {
				// 将记录中的hexArray类型字段的值转成小写
				EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
						enb.getProtocolVersion(), tableName, record);
				enbBizConfigDAO.add(enb.getMoId(), tableName, record);
			}
		}
	}

	/**
	 * 查询备份数据
	 * 
	 * @param moId
	 * @param tableList
	 * @return
	 */
	private Map<String, XBizTable> queryBackUpData(long moId,
			Set<String> tableList) {
		Map<String, XBizTable> dataMap = new HashMap<String, XBizTable>();
		for (String tableName : tableList) {
			try {
				XBizTable data = enbBizConfigDAO.query(moId, tableName, null);
				dataMap.put(tableName, data);
			} catch (Exception e) {
				log.error("query backup data failed. tableName=" + tableName, e);
			}
		}
		return dataMap;
	}

	/**
	 * 比较并同步数据到网元
	 * 
	 * @param enb
	 */
	private void compareAndSyncDataToNe(Enb enb) {
		try {
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"compare and synchronize data to enb.");
			// 执行操作
			enbBizConfigService.compareAndSyncEmsDataToNe(enb.getMoId());
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"compare and synchronize data to enb successfully.");
			// 数据同步告警恢复
			createDataSyncAlarm(enb, false);
		} catch (Exception e) {
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"failed to compare and synchronize data to enb." + ": " + e);
			// 数据同步告警
			createDataSyncAlarm(enb, true);
		}
	}

	/**
	 * 创建数据同步告警
	 * 
	 * @param enb
	 * @param failed
	 *            同步是否失败
	 */
	private void createDataSyncAlarm(Enb enb, boolean failed) {
		EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
				EnbAlarmHelper.class);
		if (failed) {
			enbAlarmHelper.fireDataSyncFailedAlarm(enb);
		} else {
			enbAlarmHelper.fireDataSyncFailedAlarmRestored(enb);
		}
	}

	/**
	 * 查询业务表模板数据
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, XBizTable> queryBizTemplateData(Enb enb)
			throws Exception {
		String protocolVersion = enb.getProtocolVersion();
		Set<String> tables = EnbBizHelper.getAllTableNames(enb.getEnbType(),
				protocolVersion);
		Map<String, XBizTable> dataMap = new HashMap<String, XBizTable>();
		for (String tableName : tables) {
			XList tableConfig = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
					protocolVersion, tableName);
			// 平台表不处理，只恢复业务表数据
			if (tableConfig.getType().equals(XList.TYPE_PLATFORM)) {
				continue;
			}
			XBizTable data = enbBizTemplateService.queryTemplateData(enb.getEnbType(),
					protocolVersion, tableName);
			dataMap.put(tableName, data);
		}
		return dataMap;
	}

	public void setEnbExtBizProxy(EnbExtBizProxy enbExtBizProxy) {
		this.enbExtBizProxy = enbExtBizProxy;
	}

	public void setEnbStudyDataConfigDAO(
			EnbStudyDataConfigDAO enbStudyDataConfigDAO) {
		this.enbStudyDataConfigDAO = enbStudyDataConfigDAO;
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizConfigService(EnbBizConfigService enbBizConfigService) {
		this.enbBizConfigService = enbBizConfigService;
	}

	public void setEnbBizTemplateService(
			EnbBizTemplateService enbBizTemplateService) {
		this.enbBizTemplateService = enbBizTemplateService;
	}

}
