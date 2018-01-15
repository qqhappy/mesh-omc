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
 * eNB��չҵ�����ӿ�ʵ��
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
		// ������ʱ���·������������׳��쳣
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
		// ������ʱ���·������������׳��쳣
		if (enb.isConfigurable()) {
			enbExtBizProxy.reset(enb.getEnbId(), rackId, shelfId, boardId);
		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

	@Override
	public String exportActiveData(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ��δ��վ�����ܵ�����վ����
		if (!enb.isActive()) {
			throw new Exception(
					OmpAppContext
							.getMessage("enb_not_active_cannot_export_data"));
		}
		// ����sql���
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
		// ȥ�����Ļ��з�
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
			// ��ͬ�汾����Ҫ��ͬ��
			synchronized (version) {
				// ������Ҫ����ѧϰʱ����������Ѵ��ڣ����׳��쳣
				boolean configExist = EnbStudyDataConfigCache.getInstance()
						.isConfigExist(version);
				// ��������Ѵ��ڣ�������Ҫ����ѧϰ������ʾ
				if (configExist && !reStudy) {
					throw new BizException(
							OmpAppContext
									.getMessage("current_version_already_study"));
				}
				// ������ʱ���·������������׳��쳣
				String dataConfig = enbExtBizProxy.studyEnbDataConfig(enb
						.getEnbId());

				Map<String, XList> tableMap = null;
				try {
					log.debug("enbId=" + enb.getHexEnbId() + ", version="
							+ version + ", dataConfig=" + dataConfig);
					// ��������
					tableMap = EnbStudyDataConfigParser.parse(dataConfig);
				} catch (Exception e) {
					log.error(
							"parse enb study data config with error. version="
									+ version, e);
					throw e;
				}
				try {
					// ����֮ǰ�Ƚ�֮ǰ������ɾ����Ȼ�����������
					enbStudyDataConfigDAO.delete(version);
					// �������ݵ����ݿ�ͻ���
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
		// ͬһʱ��ֻ����һ���û���ĳһ����վ�����������ò���
		synchronized (enb) {
			String hexEnbId = enb.getHexEnbId();
			log.debug("begin recover default data. enbId=" + hexEnbId);
			Map<String, XBizTable> dataMap = null;
			Map<String, XBizTable> backupDataMap = null;
			try {
				// ��ѯģ������
				dataMap = queryBizTemplateData(enb);
			} catch (Exception e) {
				log.error("query default data failed. enbId=" + hexEnbId, e);
				throw new Exception(
						OmpAppContext.getMessage("recover_default_data_failed"));
			}
			try {
				// ��ѯ��������
				backupDataMap = queryBackUpData(moId, dataMap.keySet());
			} catch (Exception e) {
				log.error("query backup data failed. enbId=" + hexEnbId, e);
				throw new Exception(
						OmpAppContext.getMessage("recover_default_data_failed"));
			}
			List<String> rollBackTables = new ArrayList<String>();
			try {
				for (String tableName : dataMap.keySet()) {
					// ��ɾ�������������ݣ��ٽ�ģ�����ݸ��Ƹ���վ
					enbBizConfigDAO.delete(moId, tableName, null);
					// ɾ�����ݳɹ��󣬼�¼������ʧ�ܻ���ʱʹ��
					rollBackTables.add(tableName);
					XBizTable tableData = dataMap.get(tableName);
					for (XBizRecord record : tableData.getRecords()) {
						// ��ENB�������е�enbId�ĳɱ���վ��enbId�����Ƹĳɱ���վ��enbName
						if (tableName
								.equals(EnbConstantUtils.TABLE_NAME_T_ENB_PARA)) {
							handleEnbParamRecord(enb, record);
						}
						// ����¼�е�hexArray�����ֶε�ֵת��Сд
						EnbBizHelper.changeHexArrayToLowerCase(
								enb.getEnbType(), enb.getProtocolVersion(),
								tableName, record);
						enbBizConfigDAO.add(moId, tableName, record);
					}
				}
			} catch (Exception e) {
				log.error("recover default data failed. enbId=" + hexEnbId, e);
				// ʧ�ܺ����
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
				// �ָ�Ĭ�����ݺ󣬷�������ͬ������
				compareAndSyncDataToNe(enb);
			}
		}
	}

	/**
	 * ��ENB�������е�enbId�ĳɱ���վ��enbId�����Ƹĳɱ���վ��enbName
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
	 * �ָ�Ĭ��ֵʧ�ܺ����
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
				// ����¼�е�hexArray�����ֶε�ֵת��Сд
				EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
						enb.getProtocolVersion(), tableName, record);
				enbBizConfigDAO.add(enb.getMoId(), tableName, record);
			}
		}
	}

	/**
	 * ��ѯ��������
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
	 * �Ƚϲ�ͬ�����ݵ���Ԫ
	 * 
	 * @param enb
	 */
	private void compareAndSyncDataToNe(Enb enb) {
		try {
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"compare and synchronize data to enb.");
			// ִ�в���
			enbBizConfigService.compareAndSyncEmsDataToNe(enb.getMoId());
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"compare and synchronize data to enb successfully.");
			// ����ͬ���澯�ָ�
			createDataSyncAlarm(enb, false);
		} catch (Exception e) {
			EnbUtils.log(enb.getEnbId(), "Recovery",
					"failed to compare and synchronize data to enb." + ": " + e);
			// ����ͬ���澯
			createDataSyncAlarm(enb, true);
		}
	}

	/**
	 * ��������ͬ���澯
	 * 
	 * @param enb
	 * @param failed
	 *            ͬ���Ƿ�ʧ��
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
	 * ��ѯҵ���ģ������
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
			// ƽ̨������ֻ�ָ�ҵ�������
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
