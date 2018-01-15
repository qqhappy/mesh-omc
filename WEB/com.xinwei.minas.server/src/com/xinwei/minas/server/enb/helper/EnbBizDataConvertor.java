/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.dao.EnbBizTemplateDAO;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * eNB基站配置数据转换器
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataConvertor {

	private Log log = LogFactory.getLog(EnbBizDataConvertor.class);

	private EnbBizConfigDAO enbBizConfigDAO;

	private EnbBizTemplateDAO enbBizTemplateDAO;

	/**
	 * 将库中的基站数据按照目标当前版本的enb-biz.xml进行数据转换
	 * 
	 * @throws Exception
	 */
	public void convert(long moId, String targetVersion) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 获取目标版本中的业务表名称
		Set<String> targetTableList = EnbBizHelper.getAllTableNames(
				enb.getEnbType(), targetVersion);
		// 获取当前版本的业务表名称

		Set<String> currentTableList = EnbBizHelper.getAllTableNames(
				enb.getEnbType(), enb.getProtocolVersion());
		// 如果目标版本无此表，但原版本有此表，需要将此表数据删除
		for (String tableName : currentTableList) {
			boolean hasTable = EnbBizHelper.hasBizMeta(enb.getEnbType(),
					targetVersion, tableName);
			if (!hasTable) {
				enbBizConfigDAO.deleteAll(moId, tableName);
			}
		}

		for (String tableName : targetTableList) {
			// 查询库中某个业务的数据
			XBizTable oldBizTable = enbBizConfigDAO
					.query(moId, tableName, null);

			// 如果无表数据，且原版本无此表，需要添加默认数据
			if (!EnbBizHelper.hasRecord(oldBizTable)) {
				boolean hasTable = EnbBizHelper.hasBizMeta(moId, tableName);
				if (!hasTable) {
					// 如果是小区相关的表，需要按照小区生成数据
					if (EnbBizHelper.isCellRelatedTable(enb.getEnbType(),
							targetVersion, tableName)) {
						generateDefaultCellRelatedRecords(moId, targetVersion,
								tableName);
					} else {
						// 需要查找是否有模板数据，有则需要添加
						generateTemplateData(moId, targetVersion, tableName);
					}
					// TODO: 如果是基站相关的表，需要按照基站模板数据生成数据
				}
			}

			// 如果当前库中数据与元数据不匹配,需要做数据转换
			if (!EnbBizHelper.isDataMatchVersion(enb.getEnbType(),
					targetVersion, oldBizTable)) {

				// 根据旧数据和新版本号创建新数据，并更新库中数据
				for (XBizRecord xBizRecord : oldBizTable.getRecords()) {
					XBizRecord newRecord = EnbBizHelper.convertDataByVersion(
							enb.getEnbType(), targetVersion, tableName,
							xBizRecord);
					// FIXME: 如果主键变化，无法更新
					try {
						// 将记录中的hexArray类型字段的值转成小写
						EnbBizHelper.changeHexArrayToLowerCase(
								enb.getEnbType(), targetVersion, tableName,
								newRecord);
						enbBizConfigDAO.update(moId, tableName, newRecord);
					} catch (Exception e) {
						log.error("convert biz data with error. tableName="
								+ tableName, e);
					}

				}

				// 如果当前版本无此表，则直接跳过
				if (!EnbBizHelper.hasBizMeta(moId, tableName))
					continue;
				// 如果目标版本无此表，则直接跳过
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName))
					continue;

				// REMARK: 以下代码可能有问题，解决的是小区参数表增加了ICIC的A3测量配置索引，
				// 该字段引用测量配置表索引为6的记录，该记录在新版本增加
				// 获取目标版本比当前版本添加的字段
				List<XList> newFieldMetas = EnbBizHelper.getNewFields(
						enb.getEnbType(), enb.getProtocolVersion(),
						targetVersion, tableName);
				// 如果目标版本中新增了字段
				if (newFieldMetas != null && !newFieldMetas.isEmpty()) {
					for (XList fieldMeta : newFieldMetas) {
						// 如果新增字段是外键，则新增默认引用记录
						if (fieldMeta.containsRef()) {
							handleNewRefField(moId, targetVersion, fieldMeta);
						}
					}
				}
				dataNoMatchSpecialProcess(enb, targetVersion, tableName,
						oldBizTable);

			}
			dataMatchSpecialProcess(enb, targetVersion, tableName, oldBizTable);
			// 字段升级时修改了字段类型、或者取值范围时的处理
			sameFieldUpdate(enb,targetVersion,tableName,oldBizTable);
		}
	}

	/**
	 * 处理原版本和目标版本中,相同字段修改了字段类型或者取值范围的情况
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param oldBizTable
	 * @throws Exception 
	 */
	public void sameFieldUpdate(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
		// 获取表在目标版本表的字段信息
		XList tableList = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				targetVersion, tableName);
		XList[] fieldlists = tableList.getFieldMetaList();
		// 拿到需要改动的数据
		List<XBizRecord> records = oldBizTable.getRecords();
		for (XBizRecord record : records) {
			for (XList fieldlist : fieldlists) {
				if(fieldlist.isRef()) {
					continue;
				}
				// 判断新版本的字段旧版本是否存在
				String fieldName = fieldlist.getName();
	 			XBizField oldField = record.getFieldBy(fieldName);
				if (oldField == null) {
					// 如果不存在,则处理下一个字段
					continue;
				} else {
					// 如果是数字类型
					if(fieldlist.isUnsignedNum()) {
						int min = 0;
						int max = 0;
						// 如果是enum
						if (fieldlist.isEnum()) {
							// 获取字段范围
							int[] enumRange = fieldlist.getEnumRange();
							if(null == enumRange || enumRange.length <= 0) {
								continue;
							}
							min = enumRange[0];
							max = enumRange[enumRange.length - 1];
						} 
						// 如果是unsigned32
						else if(fieldlist.isUnsigned32()) {
							int[] rangeText = fieldlist.getRange();
							min = rangeText[0];
							max = rangeText[1];
						}
						// 判断已经转换好的数据是否在范围内
						long fieldValue = record.getLongValue(fieldName);
						if(fieldValue >= min && fieldValue <= max) {
							continue;
						} 
						String defaultValue = fieldlist.getDefault();
						// 如果不在范围内,切存在默认值,则替换为默认值
						if(null != defaultValue && !defaultValue.equals("")) {
							// 为了防止默认值不是数字类型,先转换成数字再转换回字符串,有问题则会抛异常
							record
									.addField(new XBizField(fieldName, String
											.valueOf(Integer.valueOf(defaultValue
													.trim()))));
						} 
						// 如果默认值为空,则用最小值替换
						else {
							record.addField(new XBizField(fieldName, String
									.valueOf(min)));
						}
					}
				}
			}
			enbBizConfigDAO.update(enb.getMoId(), tableName, record);
		}
	}

	/**
	 * 版本变更同步数据的特殊处理:处理新旧数据格式匹配的情况
	 * 
	 * @param enb
	 * @param targetVersion
	 * @param tableName
	 * @param oldBizTable
	 * @throws Exception
	 */
	public void dataMatchSpecialProcess(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
		// 告警参数表要特殊处理
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ALARM_PARA)) {
			// 特殊处理3.0.5以下版本升级到3.0.5及以上版本
			if (enb.getProtocolVersion().compareTo("3.0.5") < 0
					&& targetVersion.compareTo("3.0.5") >= 0) {
				// 判断当前版本是否存在告警参数表,不存在则不处理
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				List<XBizRecord> records = oldBizTable.getRecords();
				for (XBizRecord record : records) {
					if (0 != record.getIntValue("u32DetIntervalTime")) {
						continue;
					}
					XBizTable templateData = enbBizTemplateDAO
							.queryTemplateData(enb.getEnbType(), targetVersion,
									tableName);
					List<XBizRecord> templateRecords = templateData
							.getRecords();
					boolean flag = false;
					for (XBizRecord templateRecord : templateRecords) {
						if (record.getIntValue("u8Indx") == templateRecord
								.getIntValue("u8Indx")) {
							record.addField(new XBizField(
									"u32DetIntervalTime",
									templateRecord
											.getStringValue("u32DetIntervalTime")));
							flag = true;
						}
					}
					if (!flag) {
						record.addField(new XBizField("u32DetIntervalTime", "1"));
					}
					enbBizConfigDAO.update(enb.getMoId(), tableName, record);
				}

			}
		}
	}

	/**
	 * 版本变更同步数据的特殊处理:处理新旧数据格式不匹配的情况
	 * 
	 * @param tableName
	 * @param targetVersion
	 * @param enb
	 * @param oldBizTable
	 * @throws Exception
	 */
	public void dataNoMatchSpecialProcess(Enb enb, String targetVersion,
			String tableName, XBizTable oldBizTable) throws Exception {
		// 特殊处理T_VLAN表
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_VLAN)) {
			// 特殊处理3.0.4及其以上版本到3.0.3及其以下版本的
			if (enb.getProtocolVersion().compareTo("3.0.4") >= 0
					&& targetVersion.compareTo("3.0.3") <= 0) {
				// 判断当前版本是否存在T_VLAN表,不存在则不处理
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				// 将该设备的该表数据清空
				enbBizConfigDAO.deleteAll(enb.getMoId(), tableName);
			}
		}
		// 特殊处理T_ETHPARA(以太网参数表)
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			// 特殊处理3.0.1及其以下版本升级到3.0.2及其以上版本
			if (enb.getProtocolVersion().compareTo("3.0.1") <= 0
					&& targetVersion.compareTo("3.0.2") >= 0) {
				// 判断当前版本是否存在T_ETHPARA表,不存在则不处理
				if (!EnbBizHelper.hasBizMeta(enb.getEnbType(), targetVersion,
						tableName)) {
					return;
				}
				// 查询出当前数据
				XBizTable newBizTable = enbBizConfigDAO.query(enb.getMoId(),
						tableName, null);
				List<XBizRecord> newRecords = newBizTable.getRecords();
				List<XBizRecord> oldRecords = oldBizTable.getRecords();
				for (XBizRecord oldRecord : oldRecords) {
					for (XBizRecord newRecord : newRecords) {
						if (oldRecord
								.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID) == newRecord
								.getIntValue(EnbConstantUtils.FIELD_NAME_PORT_ID)) {
							// 做数据兼容转换
							EnbBizHelper.ethParaDataConvertor(newRecord,oldRecord);
							// 更新数据
							enbBizConfigDAO.update(enb.getMoId(), tableName, newRecord);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 新版本中新增的与小区无关联的表需要查看是否有模板数据，有则需要添加
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @throws Exception
	 */
	private void generateTemplateData(long moId, String targetVersion,
			String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(
				enb.getEnbType(), targetVersion, tableName);
		if (EnbBizHelper.hasRecord(bizTable)) {
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				try {
					// 将记录中的hexArray类型字段的值转成小写
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, tableName, bizRecord);
					// 按照新版本的主键格式添加数据
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, tableName,
							bizRecord);
					enbBizConfigDAO.add(moId, tableName, bizKey, bizRecord);
				} catch (Exception e) {
					log.error("generate template data with error. tableName="
							+ tableName, e);
				}

			}
		}
	}

	/**
	 * 生成小区相关表的默认数据
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @throws Exception
	 */
	private void generateDefaultCellRelatedRecords(long moId,
			String targetVersion, String tableName) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		XBizTable cellTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		List<XBizRecord> newRecords = EnbBizHelper.createCellRelatedRecords(
				enb.getEnbType(), targetVersion, tableName, cellTable);
		if (newRecords != null) {
			// 添加新数据到数据库
			for (XBizRecord newRecord : newRecords) {
				try {
					// 将记录中的hexArray类型字段的值转成小写
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, tableName, newRecord);
					// 按照新版本的主键格式添加数据
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, tableName,
							newRecord);
					enbBizConfigDAO.add(moId, tableName, bizKey, newRecord);
				} catch (Exception e) {
					log.error("createCellRelatedRecords with error. tableName="
							+ tableName, e);
				}
			}
		}

	}

	/**
	 * 处理目标版本中新的外键字段
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param fieldMeta
	 * @throws Exception
	 */
	private void handleNewRefField(long moId, String targetVersion,
			XList fieldMeta) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		XMetaRef metaRef = fieldMeta.getFieldRefs().get(0);
		String refTable = metaRef.getRefTable();
		// 外键字段
		XBizField keyField = new XBizField(metaRef.getKeyColumn(),
				fieldMeta.getPropertyValue(XList.P_DEFAULT));
		// 如果库中引用表中不存在外键对应的记录，则需要创建引用记录
		if (!isRecordExists(moId, refTable, keyField)) {
			try {
				// 创建默认引用记录
				XBizRecord defaultRefRecord = createDefaultRefRecord(
						enb.getEnbType(), targetVersion, refTable, keyField);
				if (defaultRefRecord != null) {
					// 将记录中的hexArray类型字段的值转成小写
					EnbBizHelper.changeHexArrayToLowerCase(enb.getEnbType(),
							targetVersion, refTable, defaultRefRecord);
					// 按照新版本的主键格式添加数据
					XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(
							enb.getEnbType(), targetVersion, refTable,
							defaultRefRecord);
					enbBizConfigDAO.add(moId, refTable, bizKey,
							defaultRefRecord);
				}
			} catch (Exception e) {
				log.error(
						"create default reference record failed. refFieldName="
								+ fieldMeta.getName(), e);
			}
		}
	}

	/**
	 * 指定记录是否存在
	 * 
	 * @param moId
	 * @param tableName
	 * @param keyName
	 * @param keyValue
	 * @return
	 * @throws Exception
	 */
	private boolean isRecordExists(long moId, String tableName,
			XBizField keyField) throws Exception {
		XBizRecord bizKey = new XBizRecord();
		bizKey.addField(keyField);
		// 查找引用表中是否存在外键值对应的记录
		XBizRecord bizRecord = enbBizConfigDAO.queryByKey(moId, tableName,
				bizKey);
		return bizRecord != null;

	}

	/**
	 * 创建添加默认的引用记录
	 * 
	 * @param refBizField
	 * @throws Exception
	 */
	private XBizRecord createDefaultRefRecord(int enbTypeId,
			String protocolVersion, String tableName, XBizField keyField)
			throws Exception {

		// 查询目标版本引用表的模板数据中是否存在外键值对应的模板记录
		XBizTable bizTable = enbBizTemplateDAO.queryTemplateData(enbTypeId,
				protocolVersion, tableName);
		String keyName = keyField.getName();
		String keyValue = keyField.getValue();

		XBizRecord retRecord = null;
		// 如果存在，则返回该条模板记录
		if (EnbBizHelper.hasRecord(bizTable)) {
			for (XBizRecord bizRecord : bizTable.getRecords()) {
				String value = bizRecord.getFieldBy(keyName).getValue();
				if (keyValue.equals(value)) {
					retRecord = bizRecord;
					break;
				}
			}
		}
		// else {
		// // 如果无模板记录，则根据默认值创建记录
		// retRecord = EnbBizHelper.convertDataByVersion(enbTypeId,
		// protocolVersion, tableName, null);
		// }
		return retRecord;
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public void setEnbBizTemplateDAO(EnbBizTemplateDAO enbBizTemplateDAO) {
		this.enbBizTemplateDAO = enbBizTemplateDAO;
	}

}
