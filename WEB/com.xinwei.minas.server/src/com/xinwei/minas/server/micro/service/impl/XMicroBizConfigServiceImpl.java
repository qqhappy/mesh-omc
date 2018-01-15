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
 * eNB通用业务配置服务接口
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

	// 下发失败阈值，默认重发3次，超过阈值则生成数据同步失败告警
	private int failedTimeThreshold = 3;

	@Override
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition) throws Exception {
		// 默认基站在线时向基站查询状态字段
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
		// 从数据库获取数据
		XBizTable bizTableOfEms = enbBizConfigDAO.query(moId, tableName,
				condition);
		if (enb.isConfigurable()) {
			// 在线时向基站查询状态字段
			if (queryStatus) {
				// 如果存在只读字段，则需要向在线设备发送请求进行实时查询
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
		// 如果没有自学习配置，则不允许对除开站参数以外的数据进行增删改查
		// checkStudyConfig(enb, tableName);
		// 按照支持的字段向基站查询数据
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
		// 首先进行TOPO反向排序，得到表的新增和修改顺序
		List<String> topoTableNames = EnbBizHelper.getReverseTopoTableNames(
				enb.getEnbType(), protocolVersion);

		// 动态表不进行数据同步
		List<String> dynamicTables = EnbBizHelper.getDynamicTables(
				enb.getEnbType(), protocolVersion);
		topoTableNames.removeAll(dynamicTables);

		// 比较网管数据和网元数据
		Map<String, CompareResult> compareResults = new HashMap<String, CompareResult>();
		for (String tableName : topoTableNames) {
			// 从网管获取数据
			XBizTable emsBizTable = enbBizConfigDAO
					.query(moId, tableName, null);
			// 从网元获取数据
			XBizTable neBizTable = repeatQueryFromNe(moId, tableName);

			// 需要按照基站查询的数据更新状态字段的值再进行比较
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
			// 计算需要增加的数据
			List<XBizRecord> addRecords = EnbBizHelper.computeAddRecords(
					leftTable, rightTable);
			// 计算需要修改的数据
			List<XBizRecord> updateRecords = EnbBizHelper.computeUpdateRecords(
					leftTable, rightTable);
			// 计算需要删除的数据
			List<XBizRecord> deleteRecords = EnbBizHelper.computeDeleteRecords(
					leftTable, rightTable);
			// 比较结果
			CompareResult compareResult = new CompareResult(addRecords,
					updateRecords, deleteRecords);
			compareResults.put(tableName, compareResult);
		}
		// 先进行删除，再进行添加和修改
		// 反转表顺序
		Collections.reverse(topoTableNames);
		// 执行删除操作
		for (String tableName : topoTableNames) {
			CompareResult compareResult = compareResults.get(tableName);
			List<XBizRecord> deleteRecords = compareResult.getDeleteRecords();
			if (!deleteRecords.isEmpty()) {
				if (emsToNe) {
					// 删除基站数据
					process(moId, tableName, deleteRecords, ActionTypeDD.DELETE);
				} else {
					// 删除库中数据
					for (XBizRecord bizRecord : deleteRecords) {
						XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
								tableName, bizRecord);
						enbBizConfigDAO.delete(moId, tableName, bizKey);
					}
				}
			}
		}
		// 反转表顺序
		Collections.reverse(topoTableNames);
		for (String tableName : topoTableNames) {
			CompareResult compareResult = compareResults.get(tableName);
			// 新增
			List<XBizRecord> addRecords = compareResult.getAddRecords();
			if (!addRecords.isEmpty()) {
				if (emsToNe) {
					// 增加基站数据
					process(moId, tableName, addRecords, ActionTypeDD.ADD);
				} else {
					// 增加库中数据
					for (XBizRecord bizRecord : addRecords) {
						// 设置动态字段的值为默认值
						setDynamicFieldDefaultValue(enb, tableName, bizRecord);
						enbBizConfigDAO.add(moId, tableName, bizRecord);
					}
				}
			}
			// 更新
			List<XBizRecord> updateRecords = compareResult.getUpdateRecords();
			if (!updateRecords.isEmpty()) {
				if (emsToNe) {
					// 更新基站数据
					process(moId, tableName, updateRecords, ActionTypeDD.MODIFY);
				} else {
					if (tableName
							.equals(EnbConstantUtils.TABLE_NAME_T_ENB_PARA)) {
						// 修改eNB模型中的名称
						updateEnbNameOfEms(moId, updateRecords.get(0));
					}
					// 更新库中数据
					for (XBizRecord bizRecord : updateRecords) {
						// 设置动态字段的值为默认值
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
	 * 基站数据同步到网管时，修改eNB模型的名称
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
		// 无变化则不修改
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
	 * 按照操作方式处理记录，返回失败次数
	 * 
	 * @param moId
	 * @param tableName
	 *            业务名
	 * @param records
	 *            要处理的记录
	 * @param actionType
	 *            操作方式，增删改
	 * @return
	 * @throws Exception
	 */
	private void process(long moId, String tableName, List<XBizRecord> records,
			String actionType) throws Exception {
		int failedTimes = 0;
		// 重发三次
		while (true) {
			// 如果不是第一次下发，则重新获取需要处理的项
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
				// 下发
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
				// 如果出现错误，错误次数加一
				failedTimes++;
				log.warn("config failed times=" + failedTimes + ", tableName="
						+ tableName + ", ", e);
				// 如果未超过重发阈值，则重发，否则抛出异常
				if (failedTimes < failedTimeThreshold) {
					continue;
				} else {
					XList tableConfig = EnbBizHelper.getBizMetaBy(moId,
							tableName);
					throw new Exception(tableConfig.getDesc()
							+ OmpAppContext.getMessage("config_data_failed"));
				}
			}
			// 如果下发未出错，则不再重发
			break;
		}
	}

	private XBizTable repeatQueryFromNe(long moId, String tableName)
			throws Exception {
		int failedTimes = 0;
		// 从网元获取数据
		XBizTable neBizTable = null;
		while (true) {
			try {
				neBizTable = this.queryFromNe(moId, tableName);
			} catch (Exception e) {
				failedTimes++;
				log.warn("query failed times=" + failedTimes + ", tableName="
						+ tableName + ", ", e);
				// 如果未超过重发阈值，则重新查询，否则抛出异常
				if (failedTimes < failedTimeThreshold) {
					continue;
				} else {
					XList tableConfig = EnbBizHelper.getBizMetaBy(moId,
							tableName);
					throw new Exception(tableConfig.getDesc()
							+ OmpAppContext.getMessage("query_data_failed"));
				}
			}
			// 如果查询未出错，则不再查询
			break;
		}
		return neBizTable;
	}

	@Override
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception {
		// 基站可配置状态才需要下发配置消息
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 校验数据格式是否与当前基站版本的格式匹配，不匹配则报错
		checkDataFormat(enb.getEnbType(), enb.getProtocolVersion(), tableName,
				bizRecord);
		// 同一时刻只允许一个用户对某一个基站进行添加配置操作
		synchronized (enb) {
			// 校验表容量
			this.checkTableSize(enb, tableName);
			// 是否重复添加
			validateHelper.checkRecordDuplicated(moId, tableName, bizRecord);
			// 数据合法性校验
			validateData(moId, tableName, bizRecord, ActionTypeDD.ADD);
			if (enb.isConfigurable()) {
				// 如果没有自学习配置，则不允许对除开站参数以外的数据进行增删改查
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.add(moId, tableName, bizRecord);
			}
			// 将记录中的hexArray类型字段的值转成小写
			EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
					enb.getProtocolVersion(), tableName, bizRecord);
			enbBizConfigDAO.add(moId, tableName, bizRecord);

			if (EnbBizHelper.isActiveRelatedTable(tableName)) {
				// 增加过后，看是否已满足设置为开站的条件
				boolean isActive = validateHelper.checkEnbActive(enb);
				enb.setActive(isActive);
			}
			
			// 更新pci,rsi缓存
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
		// 校验数据格式是否与当前基站版本的格式匹配，不匹配则报错
		checkDataFormat(enb.getEnbType(), enb.getProtocolVersion(), tableName,
				bizRecord);
		// 同一时刻只允许一个用户对某一个基站进行修改配置操作
		synchronized (enb) {
			// 校验是否是脏数据
			// checkDirtyData(moId, tableName, bizRecord);
			// 修改的记录是否存在
			validateHelper.checkRecordExist(moId, tableName, bizRecord);
			// 数据合法性校验
			validateData(moId, tableName, bizRecord, ActionTypeDD.MODIFY);
//			// vlan表特殊处理
//			if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_VLAN)) {
//				updateVlanRecord(enb, tableName, bizRecord);
//				return;
//			}
			// 基站可配置状态才需要下发配置消息
			if (enb.isConfigurable()) {
				// 如果没有自学习配置，则不允许对除开站参数以外的数据进行增删改查
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.update(moId, tableName, bizRecord);
			}
			// 更新时间戳
			bizRecord.setTimestamp(System.currentTimeMillis());
			// 动态字段的值按照默认值
			setDynamicFieldDefaultValue(enb, tableName, bizRecord);
			// 将记录中的hexArray类型字段的值转成小写
			EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
					enb.getProtocolVersion(), tableName, bizRecord);
			enbBizConfigDAO.update(moId, tableName, bizRecord);
			// 更新pci,rsi缓存
			//EnbBizUniqueIdHelper.postUpdate(moId, tableName, bizRecord);
		}
	}

	/**
	 * 更新Vlan记录，如果任何有一条记录的VlanTag/VlanId/VlanPri修改，其他的自动也修改为相同值
	 * 
	 * @param enb
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	private void updateVlanRecord(Enb enb, String tableName,
			XBizRecord bizRecord) throws Exception {
		long moId = enb.getMoId();
		// 查询出该版本VLAN表字段
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
						// 基站可配置状态才需要下发配置消息
						if (enb.isConfigurable()) {
							// 如果没有自学习配置，则不允许对除开站参数以外的数据进行增删改查
							// checkStudyConfig(enb, tableName);
							enbBizConfigProxy.update(moId, tableName,
									vlanRecord);
						}
						// 更新时间戳
						vlanRecord.setTimestamp(System.currentTimeMillis());
						// 动态字段的值按照库中数据
						updateWritableField(enb, tableName, vlanRecord);
						// 将记录中的hexArray类型字段的值转成小写
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
	 * 将记录中的动态字段值改为默认值，库里存的动态字段值要为默认值
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
			// 设置记录中的动态字段为默认值
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
	 * 更新可写属性的记录
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
		// 获取主键
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName, bizRecord);
		// 查询库中数据
		XBizRecord recordInDb = enbBizConfigDAO.queryByKey(enb.getMoId(),
				tableName, bizKey);
		for (XList fieldMeta : tableMeta.getAllFields()) {
			if (fieldMeta.isWritable())
				continue;
			// 将库中数据中动态字段的值更新到新数据中
			XBizField field = recordInDb.getFieldBy(fieldMeta.getName());
			if (field != null) {

				bizRecord.addField(field);
			}
		}
	}
	

	/**
	 * 校验数据格式是否与当前基站版本的格式匹配，不匹配则报错
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
	 * 校验数据是否是脏数据
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
		// 同一时刻只允许一个用户对某一个基站进行删除配置操作
		synchronized (enb) {
			// 业务逻辑校验
			validateHelper.checkReference(enb, tableName, bizKey);
			// 数据合法性校验
			validateData(moId, tableName, bizKey, ActionTypeDD.DELETE);
			// 基站可配置状态才需要下发配置消息
			if (enb.isConfigurable()) {
				// 如果没有自学习配置，则不允许对除开站参数以外的数据进行增删改查
				// checkStudyConfig(enb, tableName);
				enbBizConfigProxy.delete(moId, tableName, bizKey);
			}
			enbBizConfigDAO.delete(moId, tableName, bizKey);
			
			// 删除pci、rsi等缓存map
			//EnbBizUniqueIdHelper.postDelete(moId, tableName,bizKey);
			// 删除过后，看是否已满足设置为开站的条件
			if (EnbBizHelper.isActiveRelatedTable(tableName)) {
				boolean isActive = validateHelper.checkEnbActive(enb);
				enb.setActive(isActive);
			}
		}
	}

	/**
	 * 如果没有自学习配置，则不允许对数据进行增删改查
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
		// 基站配置未初始化完全，返回异常
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
			// 遍历版本
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
				// 遍历业务表
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
	 * 获取特定表中字段与等级(A、B、C)之间的映射关系
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

		// 基站配置未初始化完全，返回异常
		checkEnbConfigInitialized();

		Map<String, Map<String, List<String>>> versionConfigMap = new HashMap<String, Map<String, List<String>>>();
		Map<String, XCollection> uiMap = XUIMetaCache.getInstance().getUiMap(
				moTypeId);
		// 遍历版本
		for (String version : uiMap.keySet()) {
			XCollection versionConfig = uiMap.get(version);
			Map<String, List<String>> bizConfigMap = versionConfigMap
					.get(version);
			if (bizConfigMap == null) {
				bizConfigMap = new HashMap<String, List<String>>();
				versionConfigMap.put(version, bizConfigMap);
			}

			Map<String, XList> uiBizMap = versionConfig.getBizMap();
			// 遍历表
			for (String tableName : uiBizMap.keySet()) {
				List<String> fieldList = bizConfigMap.get(tableName);
				if (fieldList == null) {
					fieldList = new ArrayList<String>();
					bizConfigMap.put(tableName, fieldList);
				}
				// 遍历字段
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
		// 基站配置未初始化完全，返回异常
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
	 * 基站配置未初始化完全
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
			// 如果eNB不存在，则抛出异常
			if (enb == null) {
				throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
			}
			// 获取当前eNB的所有业务名
			List<String> tableNames = EnbBizHelper.getTopoTableNames(
					enb.getEnbType(), enb.getProtocolVersion());
			for (String tableName : tableNames) {
				// 查询业务数据
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
				// 加入要导出的eNB的业务数据
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
	 * 校验表容量
	 * 
	 * @param moId
	 * @param tableName
	 * @throws Exception
	 */
	private void checkTableSize(Enb enb, String tableName) throws Exception {
		// 获取当前表记录数据
		XBizTable emsBizTable = enbBizConfigDAO.query(enb.getMoId(), tableName,
				null);
		int recordNum = emsBizTable.getRecords().size();
		// 获取数据字典定义的表容量
		XList bizMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(), tableName);
		int tableSize = bizMeta.getTableSize();
		if (recordNum >= tableSize) {
			// 表记录数超过阈值
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
	 * 比较结果
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
