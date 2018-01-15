/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.service.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBasicDAO;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.proxy.EnbBizConfigProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbNeighbourService;
import com.xinwei.minas.server.enb.service.EnbStudyDataConfigCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidatorRegistry;
import com.xinwei.minas.server.micro.service.XMicroBizConfigService;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;
import com.xinwei.omp.server.cache.XUIMetaCache;

/**
 * 
 * eNBͨ��ҵ�����÷���ӿ�
 * 
 * @author chenjunhua
 * 
 */

public class XMicroBizConfigServiceImpl implements XMicroBizConfigService {

	private Log log = LogFactory.getLog(XMicroBizConfigServiceImpl.class);

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizConfigProxy enbBizConfigProxy;

	private EnbBasicDAO enbBasicDAO;

	private EnbBizDataValidateHelper validateHelper;
	
	private EnbNeighbourService enbNeighbourService;

	// �·�ʧ����ֵ��Ĭ���ط�3�Σ�������ֵ����������ͬ��ʧ�ܸ澯
	private int failedTimeThreshold = 3;

	@Override
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition) throws Exception {
		// Ĭ�ϻ�վ����ʱ���վ��ѯ״̬�ֶ�
		XBizTable bizTable = queryFromEms(moId, tableName, condition, true);
		EnbBizHelper.makeFullData(moId, bizTable);
		return bizTable;
	}

	@Override
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition, boolean queryStatus) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// �����ݿ��ȡ����
		XBizTable bizTableOfEms = enbBizConfigDAO.query(moId, tableName,
				condition);
		if (enb.isConfigurable()) {
			// ����ʱ���վ��ѯ״̬�ֶ�
			if (queryStatus) {
				// �������ֻ���ֶΣ�����Ҫ�������豸�����������ʵʱ��ѯ
				XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
						enb.getProtocolVersion(), tableName);
				List<XList> readonlyFields = bizMeta.getReadonlyFields();
				if (!readonlyFields.isEmpty()) {
					XBizTable bizTableOfNe = this.queryFromNe(moId, tableName);
					EnbBizHelper.updateField(bizTableOfEms, bizTableOfNe,
							readonlyFields);
				}
			}
		}
		return bizTableOfEms;
	}

	@Override
	public XBizTable queryFromNe(Long moId, String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		if (!enb.isConfigurable()) {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
		// ���û����ѧϰ���ã�������Գ���վ������������ݽ�����ɾ�Ĳ�
		// checkStudyConfig(enb, tableName);
		// ����֧�ֵ��ֶ����վ��ѯ����
		List<XList> fieldMetas = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName).getAllFields();
		XBizTable bizTableOfNe = enbBizConfigProxy.query(moId, tableName,
				fieldMetas);
		return bizTableOfNe;
	}


	private void compareAndSyncData(long moId, boolean emsToNe)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		if (!enb.isConfigurable()) {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
		String protocolVersion = enb.getProtocolVersion();
		// ���Ƚ���TOPO�������򣬵õ�����������޸�˳��
		List<String> topoTableNames = EnbBizHelper.getReverseTopoTableNames(
				enb.getEnbType(), protocolVersion);

		// ��̬����������ͬ��
		List<String> dynamicTables = EnbBizHelper.getDynamicTables(
				enb.getEnbType(), protocolVersion);
		topoTableNames.removeAll(dynamicTables);

		// �Ƚ��������ݺ���Ԫ����
		Map<String, CompareResult> compareResults = new HashMap<String, CompareResult>();
		for (String tableName : topoTableNames) {
			// �����ܻ�ȡ����
			XBizTable emsBizTable = enbBizConfigDAO
					.query(moId, tableName, null);
			// ����Ԫ��ȡ����
			XBizTable neBizTable = repeatQueryFromNe(moId, tableName);

			// ��Ҫ���ջ�վ��ѯ�����ݸ���״̬�ֶε�ֵ�ٽ��бȽ�
			XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
					protocolVersion, tableName);
			List<XList> readonlyFields = bizMeta.getReadonlyFields();
			if (!readonlyFields.isEmpty()) {
				EnbBizHelper.updateField(emsBizTable, neBizTable,
						readonlyFields);
			}
			XBizTable leftTable = neBizTable;
			XBizTable rightTable = emsBizTable;
			if (emsToNe) {
				leftTable = emsBizTable;
				rightTable = neBizTable;
			}
			// ������Ҫ���ӵ�����
			List<XBizRecord> addRecords = EnbBizHelper.computeAddRecords(
					leftTable, rightTable);
			// ������Ҫ�޸ĵ�����
			List<XBizRecord> updateRecords = EnbBizHelper.computeUpdateRecords(
					leftTable, rightTable);
			// ������Ҫɾ��������
			List<XBizRecord> deleteRecords = EnbBizHelper.computeDeleteRecords(
					leftTable, rightTable);
			// �ȽϽ��
			CompareResult compareResult = new CompareResult(addRecords,
					updateRecords, deleteRecords);
			compareResults.put(tableName, compareResult);
		}
		// �Ƚ���ɾ�����ٽ�����Ӻ��޸�
		// ��ת��˳��
		Collections.reverse(topoTableNames);
		// ִ��ɾ������
		for (String tableName : topoTableNames) {
			CompareResult compareResult = compareResults.get(tableName);
			List<XBizRecord> deleteRecords = compareResult.getDeleteRecords();
			if (!deleteRecords.isEmpty()) {
				if (emsToNe) {
					// ɾ����վ����
					process(moId, tableName, deleteRecords, ActionTypeDD.DELETE);
				} else {
					// ɾ����������
					for (XBizRecord bizRecord : deleteRecords) {
						XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
								tableName, bizRecord);
						enbBizConfigDAO.delete(moId, tableName, bizKey);
					}
				}
			}
		}
		// ��ת��˳��
		Collections.reverse(topoTableNames);
		for (String tableName : topoTableNames) {
			CompareResult compareResult = compareResults.get(tableName);
			// ����
			List<XBizRecord> addRecords = compareResult.getAddRecords();
			if (!addRecords.isEmpty()) {
				if (emsToNe) {
					// ���ӻ�վ����
					process(moId, tableName, addRecords, ActionTypeDD.ADD);
				} else {
					// ���ӿ�������
					for (XBizRecord bizRecord : addRecords) {
						// ���ö�̬�ֶε�ֵΪĬ��ֵ
						setDynamicFieldDefaultValue(enb, tableName, bizRecord);
						enbBizConfigDAO.add(moId, tableName, bizRecord);
					}
				}
			}
			// ����
			List<XBizRecord> updateRecords = compareResult.getUpdateRecords();
			if (!updateRecords.isEmpty()) {
				if (emsToNe) {
					// ���»�վ����
					process(moId, tableName, updateRecords, ActionTypeDD.MODIFY);
				} else {
					if (tableName
							.equals(EnbConstantUtils.TABLE_NAME_T_ENB_PARA)) {
						// �޸�eNBģ���е�����
						updateEnbNameOfEms(moId, updateRecords.get(0));
					}
					// ���¿�������
					for (XBizRecord bizRecord : updateRecords) {
						// ���ö�̬�ֶε�ֵΪĬ��ֵ
						setDynamicFieldDefaultValue(enb, tableName, bizRecord);
						enbBizConfigDAO.update(moId, tableName, bizRecord);
					}
				}
			}
		}
		EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
				EnbAlarmHelper.class);
		enbAlarmHelper.fireDataSyncFailedAlarmRestored(enb);
	}

	/**
	 * ��վ����ͬ��������ʱ���޸�eNBģ�͵�����
	 * 
	 * @param moId
	 * @param enbParaRecord
	 */
	private void updateEnbNameOfEms(long moId, XBizRecord enbParaRecord) {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null)
			return;
		String enbName = enbParaRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENB_NAME).getValue();
		// �ޱ仯���޸�
		if (enbName.equals(enb.getName()))
			return;
		enb.setName(enbName);
		try {
			enbBasicDAO.saveOrUpdate(enb);
		} catch (Exception e) {
			log.warn(
					"update eNB name Of ems failed. enbId=" + enb.getHexEnbId(),
					e);
		}
	}

	/**
	 * ���ղ�����ʽ�����¼������ʧ�ܴ���
	 * 
	 * @param moId
	 * @param tableName
	 *            ҵ����
	 * @param records
	 *            Ҫ����ļ�¼
	 * @param actionType
	 *            ������ʽ����ɾ��
	 * @return
	 * @throws Exception
	 */
	private void process(long moId, String tableName, List<XBizRecord> records,
			String actionType) throws Exception {
		int failedTimes = 0;
		// �ط�����
		while (true) {
			// ������ǵ�һ���·��������»�ȡ��Ҫ�������
			if (failedTimes != 0) {
				XBizTable emsTable = this.queryFromEms(moId, tableName, null);
				XBizTable neTable = repeatQueryFromNe(moId, tableName);
				if (actionType.equals(ActionTypeDD.ADD)) {
					records = EnbBizHelper.computeAddRecords(emsTable, neTable);
				} else if (actionType.equals(ActionTypeDD.DELETE)) {
					records = EnbBizHelper.computeDeleteRecords(emsTable,
							neTable);
				} else if (actionType.equals(ActionTypeDD.MODIFY)) {
					records = EnbBizHelper.computeUpdateRecords(emsTable,
							neTable);
				}
			}
			try {
				// �·�
				for (XBizRecord record : records) {
					if (actionType.equals(ActionTypeDD.ADD)) {
						enbBizConfigProxy.add(moId, tableName, record);
					} else if (actionType.equals(ActionTypeDD.DELETE)) {
						enbBizConfigProxy.delete(moId, tableName, record);
					} else if (actionType.equals(ActionTypeDD.MODIFY)) {
						enbBizConfigProxy.update(moId, tableName, record);
					}
				}
			} catch (Exception e) {
				// ������ִ��󣬴��������һ
				failedTimes++;
				log.warn("config failed times=" + failedTimes + ", tableName="
						+ tableName + ", ", e);
				// ���δ�����ط���ֵ�����ط��������׳��쳣
				if (failedTimes < failedTimeThreshold) {
					continue;
				} else {
					XList tableConfig = EnbBizHelper.getBizMetaBy(moId,
							tableName);
					throw new Exception(tableConfig.getDesc()
							+ OmpAppContext.getMessage("config_data_failed"));
				}
			}
			// ����·�δ���������ط�
			break;
		}
	}

	private XBizTable repeatQueryFromNe(long moId, String tableName)
			throws Exception {
		int failedTimes = 0;
		// ����Ԫ��ȡ����
		XBizTable neBizTable = null;
		while (true) {
			try {
				neBizTable = this.queryFromNe(moId, tableName);
			} catch (Exception e) {
				failedTimes++;
				log.warn("query failed times=" + failedTimes + ", tableName="
						+ tableName + ", ", e);
				// ���δ�����ط���ֵ�������²�ѯ�������׳��쳣
				if (failedTimes < failedTimeThreshold) {
					continue;
				} else {
					XList tableConfig = EnbBizHelper.getBizMetaBy(moId,
							tableName);
					throw new Exception(tableConfig.getDesc()
							+ OmpAppContext.getMessage("query_data_failed"));
				}
			}
			// �����ѯδ�������ٲ�ѯ
			break;
		}
		return neBizTable;
	}

	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// ��վ������״̬����Ҫ�·�������Ϣ
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// У�����ݸ�ʽ�Ƿ��뵱ǰ��վ�汾�ĸ�ʽƥ�䣬��ƥ���򱨴�
		checkDataFormat(enb.getEnbType(), enb.getProtocolVersion(), tableName,
				bizRecord);
		// ͬһʱ��ֻ����һ���û���ĳһ����վ����������ò���
		synchronized (enb) {
			// У�������
			this.checkTableSize(enb, tableName);
			// �Ƿ��ظ����
			validateHelper.checkRecordDuplicated(moId, tableName, bizRecord);
			// ���ݺϷ���У��
			validateData(moId, tableName, bizRecord, ActionTypeDD.ADD);
			if (enb.isConfigurable()) {
				// ���û����ѧϰ���ã�������Գ���վ������������ݽ�����ɾ�Ĳ�
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.add(moId, tableName, bizRecord);
			}
			// ����¼�е�hexArray�����ֶε�ֵת��Сд
			EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
					enb.getProtocolVersion(), tableName, bizRecord);
			enbBizConfigDAO.add(moId, tableName, bizRecord);

			if (EnbBizHelper.isActiveRelatedTable(tableName)) {
				// ���ӹ��󣬿��Ƿ�����������Ϊ��վ������
				boolean isActive = validateHelper.checkEnbActive(enb);
				enb.setActive(isActive);
			}
			
			// ����pci,rsi����
			//EnbBizUniqueIdHelper.postUpdate(moId, tableName, bizRecord);
		}
	}

	@Override
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// У�����ݸ�ʽ�Ƿ��뵱ǰ��վ�汾�ĸ�ʽƥ�䣬��ƥ���򱨴�
		checkDataFormat(enb.getEnbType(), enb.getProtocolVersion(), tableName,
				bizRecord);
		// ͬһʱ��ֻ����һ���û���ĳһ����վ�����޸����ò���
		synchronized (enb) {
			// У���Ƿ���������
			// checkDirtyData(moId, tableName, bizRecord);
			// �޸ĵļ�¼�Ƿ����
			validateHelper.checkRecordExist(moId, tableName, bizRecord);
			// ���ݺϷ���У��
			validateData(moId, tableName, bizRecord, ActionTypeDD.MODIFY);
//			// vlan�����⴦��
//			if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_VLAN)) {
//				updateVlanRecord(enb, tableName, bizRecord);
//				return;
//			}
			// ��վ������״̬����Ҫ�·�������Ϣ
			if (enb.isConfigurable()) {
				// ���û����ѧϰ���ã�������Գ���վ������������ݽ�����ɾ�Ĳ�
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.update(moId, tableName, bizRecord);
			}
			// ����ʱ���
			bizRecord.setTimestamp(System.currentTimeMillis());
			// ��̬�ֶε�ֵ����Ĭ��ֵ
			setDynamicFieldDefaultValue(enb, tableName, bizRecord);
			// ����¼�е�hexArray�����ֶε�ֵת��Сд
			EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
					enb.getProtocolVersion(), tableName, bizRecord);
			enbBizConfigDAO.update(moId, tableName, bizRecord);
			// ����pci,rsi����
			//EnbBizUniqueIdHelper.postUpdate(moId, tableName, bizRecord);
		}
	}

	/**
	 * ����Vlan��¼������κ���һ����¼��VlanTag/VlanId/VlanPri�޸ģ��������Զ�Ҳ�޸�Ϊ��ֵͬ
	 * 
	 * @param enb
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void updateVlanRecord(Enb enb, String tableName,
			XBizRecord bizRecord) throws Exception {
		long moId = enb.getMoId();
		// ��ѯ���ð汾VLAN���ֶ�
		List<String> allVlanFieldNames = EnbBizHelper.getAllFieldNames(enb.getEnbType(),
				enb.getProtocolVersion(), EnbConstantUtils.TABLE_NAME_T_VLAN);
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		XBizTable bizTable = queryFromEms(moId, tableName, bizKey, false);
		if (EnbBizHelper.hasRecord(bizTable)) {
			XBizRecord oldRecord = bizTable.getRecords().get(0);
			boolean changed = validateHelper.checkVlanContentChanged(enb,bizRecord,
					oldRecord);
			if (changed) {
				XBizTable vlanTable = queryFromEms(moId, tableName, null, false);
				if (EnbBizHelper.hasRecord(bizTable)) {
					XBizField newVlanTag = null;
					if(allVlanFieldNames.contains(EnbConstantUtils.FIELD_NAME_VLAN_TAG)) {
						newVlanTag = bizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_VLAN_TAG);
					}
					XBizField newVlanId = bizRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_VLAN_ID);
					XBizField newVlanPri = bizRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_VLAN_PRI);
					for (XBizRecord vlanRecord : vlanTable.getRecords()) {
						if(allVlanFieldNames.contains(EnbConstantUtils.FIELD_NAME_VLAN_TAG)) {
							vlanRecord.addField(newVlanTag);
						}
						vlanRecord.addField(newVlanId);
						vlanRecord.addField(newVlanPri);
						// ��վ������״̬����Ҫ�·�������Ϣ
						if (enb.isConfigurable()) {
							// ���û����ѧϰ���ã�������Գ���վ������������ݽ�����ɾ�Ĳ�
							// checkStudyConfig(enb, tableName);
							enbBizConfigProxy.update(moId, tableName,
									vlanRecord);
						}
						// ����ʱ���
						vlanRecord.setTimestamp(System.currentTimeMillis());
						// ��̬�ֶε�ֵ���տ�������
						updateWritableField(enb, tableName, vlanRecord);
						// ����¼�е�hexArray�����ֶε�ֵת��Сд
						EnbBizHelper
								.changeHexArrayToLowerCase(enb.getEnbType(),
										enb.getProtocolVersion(), tableName,
										vlanRecord);
						enbBizConfigDAO.update(moId, tableName, vlanRecord);
					}
				}
			}
		}
	}

	private void validateData(Long moId, String tableName,
			XBizRecord bizRecord, String actionType) throws Exception {
		com.xinwei.minas.server.enb.validator.EnbBizDataValidator validator = EnbBizDataValidatorRegistry
				.getInstance().getValidator(tableName);
		if (validator == null)
			return;
		validator.validate(moId, bizRecord, actionType);
	}

	/**
	 * ����¼�еĶ�̬�ֶ�ֵ��ΪĬ��ֵ�������Ķ�̬�ֶ�ֵҪΪĬ��ֵ
	 * 
	 * @param enb
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void setDynamicFieldDefaultValue(Enb enb, String tableName,
			XBizRecord bizRecord) throws Exception {
		XList tableMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		for (XList fieldMeta : tableMeta.getAllFields()) {
			if (fieldMeta.isWritable())
				continue;
			// ���ü�¼�еĶ�̬�ֶ�ΪĬ��ֵ
			XBizField field = bizRecord.getFieldBy(fieldMeta.getName());
			if (field != null) {
				String defaultValue = fieldMeta
						.getPropertyValue(XList.P_DEFAULT);
				if (defaultValue != null && defaultValue != "")
					field.setValue(defaultValue);
				bizRecord.addField(field);
			}
		}
	}
	
	
	/**
	 * ���¿�д���Եļ�¼
	 * 
	 * @param enb
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void updateWritableField(Enb enb, String tableName,
			XBizRecord bizRecord) throws Exception {
		XList tableMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		// ��ȡ����
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName, bizRecord);
		// ��ѯ��������
		XBizRecord recordInDb = enbBizConfigDAO.queryByKey(enb.getMoId(),
				tableName, bizKey);
		for (XList fieldMeta : tableMeta.getAllFields()) {
			if (fieldMeta.isWritable())
				continue;
			// �����������ж�̬�ֶε�ֵ���µ���������
			XBizField field = recordInDb.getFieldBy(fieldMeta.getName());
			if (field != null) {

				bizRecord.addField(field);
			}
		}
	}
	

	/**
	 * У�����ݸ�ʽ�Ƿ��뵱ǰ��վ�汾�ĸ�ʽƥ�䣬��ƥ���򱨴�
	 * 
	 * @param enb
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkDataFormat(int enbTypeId, String version,
			String tableName, XBizRecord bizRecord) throws Exception {
		if (!EnbBizHelper.isRecordMatchVersion(enbTypeId, version, tableName,
				bizRecord)) {
			throw new Exception(
					OmpAppContext.getMessage("data_not_match_current_version"));
		}
	}

	/**
	 * У�������Ƿ���������
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkDirtyData(Long moId, String tableName,
			XBizRecord bizRecord) throws Exception {
		XBizRecord recordInDb = enbBizConfigDAO.queryByKey(moId, tableName,
				bizRecord);
		if (!recordInDb.getTimestamp().equals(bizRecord.getTimestamp())) {
			throw new BizException(
					OmpAppContext.getMessage("data_invalid_reload_please"));
		}
	}

	@Override
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// ͬһʱ��ֻ����һ���û���ĳһ����վ����ɾ�����ò���
		synchronized (enb) {
			// ҵ���߼�У��
			validateHelper.checkReference(enb, tableName, bizKey);
			// ���ݺϷ���У��
			validateData(moId, tableName, bizKey, ActionTypeDD.DELETE);
			// ��վ������״̬����Ҫ�·�������Ϣ
			if (enb.isConfigurable()) {
				// ���û����ѧϰ���ã�������Գ���վ������������ݽ�����ɾ�Ĳ�
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.delete(moId, tableName, bizKey);
			}
			enbBizConfigDAO.delete(moId, tableName, bizKey);
			
			// ɾ��pci��rsi�Ȼ���map
			//EnbBizUniqueIdHelper.postDelete(moId, tableName,bizKey);
			// ɾ�����󣬿��Ƿ�����������Ϊ��վ������
			if (EnbBizHelper.isActiveRelatedTable(tableName)) {
				boolean isActive = validateHelper.checkEnbActive(enb);
				enb.setActive(isActive);
			}
		}
	}

	/**
	 * ���û����ѧϰ���ã�����������ݽ�����ɾ�Ĳ�
	 * 
	 * @param enb
	 * @param tableName
	 * @throws Exception
	 */
	private void checkStudyConfig(Enb enb, String tableName) throws Exception {
		boolean configExist = EnbStudyDataConfigCache.getInstance()
				.isConfigExist(enb.getSoftwareVersion());
		if (!configExist) {
			throw new Exception(
					OmpAppContext.getMessage("current_version_not_study"));
		}
	}

	@Override
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception {
		return enbBizConfigDAO.queryByMetaRef(moId, metaRefList);
	}

	@Override
	public InetSocketAddress queryEmsNetAddress(Long moId) throws Exception {
		String hostname = OmpAppContext.getPropertyByName("platform.server.ip");
		if (StringUtils.isBlank(hostname)) {
			hostname = "127.0.0.1";
		}
		String portStr = OmpAppContext
				.getPropertyByName("enb.connector.server.port");
		if (StringUtils.isBlank(portStr)) {
			portStr = "4999";
		}
		int port = Integer.valueOf(portStr);
		return new InetSocketAddress(hostname, port);
	}

	@Override
	public Map<String, Map<String, Map<String, List<String>>>> getTableFieldLevelConfig(
			int moType) throws Exception {
		// ��վ����δ��ʼ����ȫ�������쳣
		checkEnbConfigInitialized();

		Map<String, Map<String, Map<String, List<String>>>> resultMap = new HashMap<String, Map<String, Map<String, List<String>>>>();
		Set<Integer> enbTypeIds = EnbTypeDD.getSupportedTypeIds();
		for (int enbTypeId : enbTypeIds) {
			int key = XUIMetaCache.getInstance().createKey(moType, enbTypeId);
			Map<String, XCollection> versionMap = XUIMetaCache.getInstance()
					.getUiMap(key);
			if (versionMap == null) {
				continue;
			}
			// �����汾
			for (String version : versionMap.keySet()) {
				Map<String, Map<String, List<String>>> resultTableMap = resultMap
						.get(version);
				if (resultTableMap == null) {
					resultTableMap = new HashMap<String, Map<String, List<String>>>();
					String resultMapKey = createKey(enbTypeId, version);
					resultMap.put(resultMapKey, resultTableMap);
				}
				XCollection xCollection = versionMap.get(version);
				Map<String, XList> collectionMap = xCollection.getBizMap();
				// ����ҵ���
				for (String tableName : collectionMap.keySet()) {
					XList tableConfig = collectionMap.get(tableName);
					Map<String, List<String>> levelMap = getFieldMapByLevel(tableConfig);
					resultTableMap.put(tableName, levelMap);
				}
			}
		}
		return resultMap;
	}

	private String createKey(int enbTypeId, String version) {
		return enbTypeId + "*" + version;
	}

	/**
	 * ��ȡ�ض������ֶ���ȼ�(A��B��C)֮���ӳ���ϵ
	 * 
	 * @param tableConfig
	 * @return
	 */
	private Map<String, List<String>> getFieldMapByLevel(XList tableConfig) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		XList[] fieldConfigs = tableConfig.getList();
		for (XList fieldConfig : fieldConfigs) {
			String fieldName = fieldConfig.getName();
			String level = fieldConfig.getLevel();
			List<String> fieldList = map.get(level);
			if (fieldList == null) {
				fieldList = new ArrayList<String>();
				map.put(level, fieldList);
			}
			fieldList.add(fieldName);
		}
		return map;
	}

	@Override
	public Map<String, Map<String, List<String>>> queryBizConfig(int moTypeId)
			throws Exception {

		// ��վ����δ��ʼ����ȫ�������쳣
		checkEnbConfigInitialized();

		Map<String, Map<String, List<String>>> versionConfigMap = new HashMap<String, Map<String, List<String>>>();
		Map<String, XCollection> uiMap = XUIMetaCache.getInstance().getUiMap(
				moTypeId);
		// �����汾
		for (String version : uiMap.keySet()) {
			XCollection versionConfig = uiMap.get(version);
			Map<String, List<String>> bizConfigMap = versionConfigMap
					.get(version);
			if (bizConfigMap == null) {
				bizConfigMap = new HashMap<String, List<String>>();
				versionConfigMap.put(version, bizConfigMap);
			}

			Map<String, XList> uiBizMap = versionConfig.getBizMap();
			// ������
			for (String tableName : uiBizMap.keySet()) {
				List<String> fieldList = bizConfigMap.get(tableName);
				if (fieldList == null) {
					fieldList = new ArrayList<String>();
					bizConfigMap.put(tableName, fieldList);
				}
				// �����ֶ�
				List<XList> uiFieldList = uiBizMap.get(tableName)
						.getAllFields();
				for (XList uiField : uiFieldList) {
					fieldList.add(uiField.getName());
				}
			}
		}
		return versionConfigMap;
	}

	@Override
	public Map<Integer, List<String>> querySupportedProtocolVersion()
			throws Exception {
		// ��վ����δ��ʼ����ȫ�������쳣
		checkEnbConfigInitialized();
		Map<Integer, List<String>> map = new HashMap();
		Set<Integer> enbTypeIds = EnbTypeDD.getSupportedTypeIds();
		for (int enbTypeId : enbTypeIds) {
			Set<String> versionSet = XUIMetaCache.getInstance()
					.getSupportedVersions(MoTypeDD.ENODEB, enbTypeId);
			List<String> versions = new ArrayList<String>(versionSet);
			Collections.sort(versions);
			map.put(enbTypeId, versions);
		}
		return map;
	}

	/**
	 * ��վ����δ��ʼ����ȫ
	 * 
	 * @throws Exception
	 */
	private void checkEnbConfigInitialized() throws Exception {

		boolean configOk = XUIMetaCache.getInstance().isInitialized();
		if (!configOk) {
			throw new Exception(
					OmpAppContext.getMessage("enb_biz_config_not_initialized"));
		}
	}

	@Override
	public Map<Long, List<XBizTable>> queryDataByMoIdList(List<Long> moIdList)
			throws Exception {
		Map<Long, List<XBizTable>> moDataMap = new HashMap<Long, List<XBizTable>>();
		for (Long moId : moIdList) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			// ���eNB�����ڣ����׳��쳣
			if (enb == null) {
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			}
			// ��ȡ��ǰeNB������ҵ����
			List<String> tableNames = EnbBizHelper.getTopoTableNames(
					enb.getEnbType(), enb.getProtocolVersion());
			for (String tableName : tableNames) {
				// ��ѯҵ������
				XBizTable bizTable = enbBizConfigDAO.query(moId, tableName,
						null);
				List<XBizTable> tableList = moDataMap.get(moId);
				if (tableList == null) {
					tableList = new LinkedList<XBizTable>();
					moDataMap.put(moId, tableList);
				}
				if (bizTable == null || bizTable.getRecords() == null
						|| bizTable.getRecords().isEmpty())
					continue;
				// ����Ҫ������eNB��ҵ������
				tableList.add(bizTable);
			}
		}
		return moDataMap;
	}

	@Override
	public void compareAndSyncNeDataToEms(Long moId) throws Exception {
		compareAndSyncData(moId, false);
	}
	
	@Override
	public void compareAndSyncEmsDataToNe(Long moId) throws Exception {
		compareAndSyncData(moId, true);
	}

	/**
	 * У�������
	 * 
	 * @param moId
	 * @param tableName
	 * @throws Exception
	 */
	private void checkTableSize(Enb enb, String tableName) throws Exception {
		// ��ȡ��ǰ���¼����
		XBizTable emsBizTable = enbBizConfigDAO.query(enb.getMoId(), tableName,
				null);
		int recordNum = emsBizTable.getRecords().size();
		// ��ȡ�����ֵ䶨��ı�����
		XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		int tableSize = bizMeta.getTableSize();
		if (recordNum >= tableSize) {
			// ���¼��������ֵ
			throw new Exception(OmpAppContext.getMessage(
					"table_size_over_threshold", new Integer[] { tableSize }));
		}
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizConfigProxy(EnbBizConfigProxy enbBizConfigProxy) {
		this.enbBizConfigProxy = enbBizConfigProxy;
	}

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	public void setFailedTimeThreshold(int failedTimeThreshold) {
		this.failedTimeThreshold = failedTimeThreshold;
	}

	public int getFailedTimeThreshold() {
		return failedTimeThreshold;
	}

	public void setEnbBasicDAO(EnbBasicDAO enbBasicDAO) {
		this.enbBasicDAO = enbBasicDAO;
	}

	public void setEnbNeighbourService(EnbNeighbourService enbNeighbourService) {
		this.enbNeighbourService = enbNeighbourService;
	}



	/**
	 * �ȽϽ��
	 * 
	 * @author chenjunhua
	 * 
	 */
	class CompareResult {
		private List<XBizRecord> addRecords = new LinkedList<XBizRecord>();
		private List<XBizRecord> updateRecords = new LinkedList<XBizRecord>();
		private List<XBizRecord> deleteRecords = new LinkedList<XBizRecord>();

		public CompareResult(List<XBizRecord> addRecords,
				List<XBizRecord> updateRecords, List<XBizRecord> deleteRecords) {
			this.setAddRecords(addRecords);
			this.setUpdateRecords(updateRecords);
			this.setDeleteRecords(deleteRecords);
		}

		public List<XBizRecord> getAddRecords() {
			return addRecords;
		}

		public void setAddRecords(List<XBizRecord> addRecords) {
			this.addRecords = addRecords;
		}

		public List<XBizRecord> getUpdateRecords() {
			return updateRecords;
		}

		public void setUpdateRecords(List<XBizRecord> updateRecords) {
			this.updateRecords = updateRecords;
		}

		public List<XBizRecord> getDeleteRecords() {
			return deleteRecords;
		}

		public void setDeleteRecords(List<XBizRecord> deleteRecords) {
			this.deleteRecords = deleteRecords;
		}

	}

}
