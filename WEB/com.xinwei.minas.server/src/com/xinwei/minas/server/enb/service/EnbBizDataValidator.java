/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-31	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB业务数据校验工具类
 * 
 * @author fanhaoyu
 * 
 */
@Deprecated
public class EnbBizDataValidator {

	private static final String splitFlag = "#";
	/**
	 * PRACH表中u8PrachCfgIndex与小区表的子帧配比关联后不可选中的字段：当子帧配比为0时，不能选择11,19,请参照数据库设计文件
	 */
	public static final int[][] PRACH_NOT_SELECT_PARAM = {
			{ 11, 19 },
			{ 8, 13, 14, 40, 41, 42, 43, 44, 45, 46, 47 },
			{ 5, 7, 8, 11, 13, 14, 17, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
					29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
					44, 45, 46, 47 },
			{ 10, 11, 19, 22, 24, 32, 34, 42, 44, 50, 52 },
			{ 5, 7, 8, 11, 13, 14, 17, 19, 22, 24, 32, 34, 40, 41, 42, 43, 44,
					45, 46, 47, 50, 52 },
			{ 2, 4, 5, 7, 8, 10, 11, 13, 14, 16, 17, 19, 20, 21, 22, 23, 24,
					25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
					40, 41, 42, 43, 44, 45, 46, 47, 50, 52 },
			{ 16, 17, 42, 44 } };

	public static int[][] BW5MHZ = {
			{ -21, -22, -23, -24, -25, -26, -27, -28 },
			{ -21, -21, -22, -24, -25, -26, -27, -28 },
			{ -20, -21, -22, -24, -25, -26, -27, -28 },
			{ -19, -21, -22, -24, -25, -26, -27, -28 } };
	public static final int[][] BW10MHZ = {
			{ -24, -25, -26, -27, -28, -29, -30, -31 },
			{ -24, -24, -25, -27, -28, -29, -30, -31 },
			{ -23, -24, -25, -27, -28, -29, -30, -31 },
			{ -22, -24, -25, -27, -28, -29, -30, -31 } };
	public static final int[][] BW15MHZ = {
			{ -26, -27, -28, -29, -30, -31, -32, -33 },
			{ -25, -26, -27, -28, -30, -31, -32, -33 },
			{ -25, -25, -27, -28, -30, -31, -32, -33 },
			{ -24, -25, -27, -28, -30, -31, -32, -33 } };
	public static final int[][] BW20MHZ = {
			{ -27, -28, -29, -30, -31, -32, -33, -34 },
			{ -27, -27, -28, -30, -31, -32, -33, -34 },
			{ -26, -27, -28, -30, -31, -32, -33, -34 },
			{ -25, -27, -28, -30, -31, -32, -33, -34 } };
	// 频段指示
	// public static final int[] FREQ_BAND = { 33, 34, 35, 36, 37, 38, 39, 40,
	// 61,
	// 62 };
	// 33:1900-1920, 34:2010-2025, 35:1850-1910, 36:1930-1990, 37:1910-1930,
	// 38:2570-2620, 39:1880-1920, 40:2300-2400MHZ，61:1447-1467，62:1785-1805
	// public static final int[][] FREQ_RANGE = { { 1900, 1920 }, { 2010, 2025
	// },
	// { 1850, 1910 }, { 1930, 1990 }, { 1910, 1930 }, { 2570, 2620 },
	// { 1880, 1920 }, { 2300, 2400 }, { 1447, 1467 }, { 1785, 1805 } };

	private EnbBizConfigDAO enbBizConfigDAO;

	/**
	 * 验证eNB是否已开站
	 * 
	 * @param enb
	 * @return
	 */
	public boolean checkEnbActive(Enb enb) {
		try {
			// 如果网管表有数据，则说明机架、机框、单板、以太网参数、IPV4表中都已经有数据
			XBizRecord omcRecord = queryOmcRecord(enb.getMoId());
			XBizRecord enbParamRecord = queryEnbParamRecord(enb.getMoId());
			if (omcRecord != null && enbParamRecord != null)
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void checkAdd(Enb enb, String tableName, XBizRecord bizRecord)
			throws Exception {

		// eNB已连接状态下不可对开站参数进行配置
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// 验证记录是否重复添加
		checkRecordDuplicated(enb.getMoId(), tableName, bizRecord);

		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_RACK)) {
			// 机架名不能重复
			checkRackNameDuplicated(enb.getMoId(), bizRecord);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// 单板表中必须有一块BBU板，且架框槽必须是1,1,1
			checkBoardRecord(bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_OMC)) {
			// 与网管表相关联的IPV4表记录中与以太网参数表相关的记录中架框槽必须是1,1,1
			checkOmcRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// IPv4表各记录中的IP地址必须不同
			checkIpv4Record(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_TOPO)) {
			// 拓扑表校验
			checkTopoRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			checkEthenetRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_STROUT)) {
			// 静态路由表校验
			checkStroutRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// 小区参数表校验
			checkCPARARecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CELL_PUCH))) {
			// 小区上行物理信道配置参数表校验
			checkCPUCHRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_PRACH))) {
			// PRACH参数配置表规则校验
			checkCPRACHRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_ALG))) {
			// 小区算法参数表校验
			checkCALGRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ))) {
			// 中心载频配置表校验
			checkCTFREQRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_DLPC))) {
			// 小区下行功控参数表
			checkDLPCRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL))) {
			// 邻区关系参数配置表校验
			checkCNBRCELRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PTT)) {
			// 小区集群配置参数
			checkCPTTRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SISCH)) {
			// SI的调度配置参数表
			checkCellSISCHRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			// 环境监控表
			checkENVMONRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_SRVPC)) {
			// 业务功控参数表
			checkEnbSRVPCRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		}
	}

	public void checkDelete(Enb enb, String tableName, XBizRecord bizKey)
			throws Exception {
		// eNB已连接状态下不可对开站参数进行配置
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// 外键约束校验
		checkReference(enb, tableName, bizKey);

		// 如果eNB已经开站，1号机架不可删除、1号机框不可删除、1-1-1单板不可删除
		if (enb.isActive()) {
			if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_RACK)) {
				checkRackRecord(bizKey);
			} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_SHELF)) {
				checkShelfRecord(bizKey);
			} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
				checkBoardRecord(bizKey, ActionTypeDD.DELETE);
			} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_OMC)) {
				checkOmcRecord(enb, bizKey, ActionTypeDD.DELETE);
			} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
				checkIpv4Record(enb, bizKey, ActionTypeDD.DELETE);
			}
		}
	}

	public void checkModify(Enb enb, String tableName, XBizRecord record)
			throws Exception {
		// eNB已连接状态下不可对开站参数进行配置
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// 验证记录是否存在
		if (!isRecordExist(enb.getMoId(), tableName, record)) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}

		// // 外键不可修改
		// checkForeignKeyReference(enb, tableName, record);

		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_RACK)) {
			// 机架名不能重复
			checkRackNameDuplicated(enb.getMoId(), record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// 单板表中必须有一块BBU板，且架框槽必须是1,1,1
			checkBoardRecord(record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_TOPO)) {
			// 拓扑表校验
			checkTopoRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_OMC)) {
			checkOmcRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// 与网管表相关联的IPV4表中的记录的端口标识不可修改
			checkIpv4Record(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			// 与网管表相关联的IPV4表中的记录的端口标识的架框槽必须保持1,1,1不变
			checkEthenetRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_STROUT)) {
			// 静态路由表校验
			checkStroutRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// 小区参数表校验
			checkCPARARecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CELL_PUCH))) {
			// 小区上行物理信道配置参数表校验
			checkCPUCHRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_PRACH))) {
			// PRACH参数配置表规则校验
			checkCPRACHRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_ALG))) {
			// 小区算法参数表校验
			checkCALGRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ))) {
			// 中心载频配置表校验
			checkCTFREQRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_DLPC))) {
			// 小区下行功控参数表
			checkDLPCRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL))) {
			// 邻区关系参数配置表校验
			checkCNBRCELRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PTT)) {
			// 小区集群配置参数
			checkCPTTRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SISCH)) {
			// SI的调度配置参数表
			checkCellSISCHRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			// 环境监控表
			checkENVMONRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_SRVPC)) {
			// 业务功控参数表
			checkEnbSRVPCRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		}
	}

	/**
	 * 记录是否存在
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 * @return
	 * @throws Exception
	 */
	public boolean isRecordExist(long moId, String tableName,
			XBizRecord bizRecord) throws Exception {
		// 获取主键
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// 查找数据库记录
		XBizRecord recordInDb = enbBizConfigDAO.queryByKey(moId, tableName,
				bizKey);
		return recordInDb != null;
	}

	/**
	 * 校验记录是否重复添加
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkRecordDuplicated(long moId, String tableName,
			XBizRecord bizRecord) throws Exception {
		if (isRecordExist(moId, tableName, bizRecord)) {
			throw new Exception(
					OmpAppContext.getMessage("record_already_exist"));
		}
	}

	/**
	 * 校验机架名称是否重复，同一基站下机架名称不能相同
	 * 
	 * @throws Exception
	 */
	private void checkRackNameDuplicated(long moId, XBizRecord bizRecord)
			throws Exception {
		String currentName = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_RACK_NAME).getValue();
		String currentRack = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_RACK, null);
		if (records == null)
			return;
		for (XBizRecord xBizRecord : records) {
			String rackNo = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
			if (rackNo.equals(currentRack))
				continue;
			String name = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACK_NAME).getValue();
			if (currentName.equals(name)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_RACK_NAME,
						"rack_name_duplicated");
			}
		}
	}

	/**
	 * 外键引用不可修改的校验
	 * 
	 * @param enb
	 * @param tableName
	 * @param record
	 * @throws Exception
	 */
	private void checkForeignKeyReference(Enb enb, String tableName,
			XBizRecord record) throws Exception {
		String protocolVersion = enb.getProtocolVersion();
		// 查询库中该记录
		XBizRecord condition = EnbBizHelper.getKeyRecordBy(enb.getEnbType(),
				protocolVersion, tableName, record);
		XBizRecord dbRecord = getXBizRecordByMoId(enb.getMoId(), tableName,
				condition).get(0);
		XList tableMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				protocolVersion, tableName);
		for (XList fieldMeta : tableMeta.getAllFields()) {
			String fieldName = fieldMeta.getName();
			// 如果是外键
			if (fieldMeta.isRef()) {
				XBizField dbField = dbRecord.getFieldBy(fieldName);
				XBizField field = record.getFieldBy(fieldName);
				// 如果外键的值改变
				if (!dbField.equals(field)) {
					throw newBizException(fieldName,
							"foreign_key_cannot_modify");
				}
			}
		}
	}

	/**
	 * 校验是否有数据引用此记录
	 * 
	 * @param enb
	 * @param tableName
	 * @param record
	 * @throws Exception
	 */
	public void checkReference(Enb enb, String tableName, XBizRecord record)
			throws Exception {
		// 特殊的引用校验
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// 校验单板表记录是否被拓扑表引用
			checkBoardReference(enb, record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG)) {
			// 测量配置表是否被小区参数表引用
			checkMeascfgReference(enb, record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// IPv4表是否被流传输控制协议表引用
			checkIpv4Reference(enb, record);
		}
		// 通用校验逻辑
		XCollection collection = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion());
		Map<String, XList> bizMap = collection.getBizMap();
		for (String table : bizMap.keySet()) {
			XList tableConfig = bizMap.get(table);
			List<XList> fields = tableConfig.getAllFields();
			// 遍历表中字段，找出与要删除的表与引用关系的字段
			List<String> refFields = new ArrayList<String>();
			for (XList field : fields) {
				List<String> refTables = field.getFieldRefTables();
				if (field.containsRef()) {
					// 查看引用该表的表中是否存在引用该记录的记录
					if (refTables.contains(tableName)) {
						List<XMetaRef> refList = field.getFieldRefs();
						refFields.add(field.getName() + "#"
								+ refList.get(0).getKeyColumn());
					}
				}
			}
			// 如果与要删除的表有引用关系
			if (!refFields.isEmpty()) {
				XBizRecord condition = new XBizRecord();
				// 如果表中是外键的属性的值是要删除表中的键值，则不允许删除
				for (String refField : refFields) {
					String[] temp = refField.split("\\#");
					condition.addField(new XBizField(temp[0], record
							.getFieldBy(temp[1]).getValue()));
				}
				XBizTable xBizTable = queryFromEms(enb.getMoId(), table,
						condition);
				List<XBizRecord> records = xBizTable.getRecords();
				if (records != null && !records.isEmpty()) {
					throw new BizException(
							MessageFormat.format(
									OmpAppContext
											.getMessage("this_record_is_related_with_another_table"),
									tableConfig.getDesc()));
				}
			}
		}

	}

	/**
	 * 校验IPv4表记录是否被流控制传输协议表所引用
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkIpv4Reference(Enb enb, XBizRecord record)
			throws Exception {
		String targetId = record.getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ID)
				.getValue();
		String[] refFieldNames = { EnbConstantUtils.FIELD_NAME_SRC_IP_ID1,
				EnbConstantUtils.FIELD_NAME_SRC_IP_ID2,
				EnbConstantUtils.FIELD_NAME_SRC_IP_ID3,
				EnbConstantUtils.FIELD_NAME_SRC_IP_ID4 };
		checkRecordReference(enb, targetId, EnbConstantUtils.TABLE_NAME_T_SCTP,
				refFieldNames);
	}

	/**
	 * 校验测量参数表是否是否被其他表所引用，小区参数表
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkMeascfgReference(Enb enb, XBizRecord record)
			throws Exception {
		String targetId = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX).getValue();
		String[] refFieldNames = {
				EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG,
				EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG,
				EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG,
				EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG,
				EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG,
				EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG_16,
				EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG };
		checkRecordReference(enb, targetId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, refFieldNames);
	}

	/**
	 * 验证指定的Id是否被其他表的指定字段所引用
	 * 
	 * @param enb
	 * @param targetId
	 *            要删除的索引
	 * @param tableName
	 *            引用目标Id的表
	 * @param refFieldNames
	 *            应用目标Id的字段
	 * @throws Exception
	 */
	private void checkRecordReference(Enb enb, String targetId,
			String tableName, String[] refFieldNames) throws Exception {
		List<XBizRecord> records = getXBizRecordByMoId(enb.getMoId(),
				tableName, null);
		if (records == null)
			return;
		for (XBizRecord record : records) {
			for (String fieldName : refFieldNames) {
				if (!record.getFieldMap().containsKey(fieldName)) {
					// 如果不包含该字段，则继续校验
					continue;
				}
				String id = record.getFieldBy(fieldName).getValue();
				if (targetId.equals(id)) {
					XCollection collection = EnbBizHelper.getBizMetaBy(
							enb.getEnbType(), enb.getProtocolVersion());

					XList tableConfig = collection.getBizMap().get(tableName);
					throw new BizException(
							MessageFormat.format(
									OmpAppContext
											.getMessage("this_record_is_related_with_another_table"),
									tableConfig.getDesc()));
				}
			}
		}

	}

	/**
	 * 校验被删除的单板表记录是否被拓扑表所引用
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkBoardReference(Enb enb, XBizRecord record)
			throws Exception {
		XCollection collection = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion());
		Map<String, XList> bizMap = collection.getBizMap();
		String topoTable = EnbConstantUtils.TABLE_NAME_T_TOPO;
		List<XBizRecord> topoRecords = getXBizRecordByMoId(enb.getMoId(),
				topoTable, null);
		if (topoRecords == null)
			return;
		// 单板表中的架框槽
		int[] noArray = getRackShelfSlotNo(record);
		for (XBizRecord topoRecord : topoRecords) {
			// 获取主板架框槽
			int[] mainArray = getRackShelfSlotNoOfTopoRecord(topoRecord, true);
			// 获取从板架框槽
			int[] sArray = getRackShelfSlotNoOfTopoRecord(topoRecord, false);
			// 是否被主板引用
			boolean referenced1 = noArray[0] == mainArray[0]
					&& noArray[1] == mainArray[1] && noArray[2] == mainArray[2];
			// 是否被从板引用
			boolean referenced2 = noArray[0] == sArray[0]
					&& noArray[1] == sArray[1] && noArray[2] == sArray[2];
			if (referenced1 || referenced2) {
				XList tableConfig = bizMap.get(topoTable);
				throw new BizException(
						MessageFormat.format(
								OmpAppContext
										.getMessage("this_record_is_related_with_another_table"),
								tableConfig.getDesc()));
			}
		}

	}

	/**
	 * 获取拓扑表记录中的架框槽
	 * 
	 * @param bizRecord
	 * @param main
	 *            主板还是从板
	 */
	private int[] getRackShelfSlotNoOfTopoRecord(XBizRecord bizRecord,
			boolean main) {
		XBizField rackNoField = null;
		XBizField shelfNoField = null;
		XBizField slotNoField = null;
		if (main) {
			rackNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_MRACKNO);
			shelfNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_MSHELFNO);
			slotNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_MSLOTNO);
		} else {
			rackNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SRACKNO);
			shelfNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SSHELFNO);
			slotNoField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SSLOTNO);
		}
		int rackNo = Integer.valueOf(rackNoField.getValue());
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		int slotNo = Integer.valueOf(slotNoField.getValue());
		return new int[] { rackNo, shelfNo, slotNo };
	}

	/**
	 * 小区参数表的校验 新增小区参数表中外键约束：
	 * u8IntraFreqHOMeasCfg等字段需要参照T_ENB_MEASCFG表记录的u8EvtId详情参照数据库设计
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCPARARecord(Long moId, XBizRecord record, String actionType)
			throws Exception {
		XBizTable tableData = queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		List<XBizRecord> records = tableData.getRecords();
		if (records != null && !records.isEmpty()) {
			// 如果是修改，则先移除当前记录
			if (actionType.equals(ActionTypeDD.MODIFY)) {
				int index = getCellIndex(record, records);
				records.remove(index);
			}
			// 同基站下小区名称不能相同
			checkCellNameDuplicated(record, records);
			// 同频小区的PCI不能相同
			checkPCI(record, records);
			// 不同小区引用的拓扑表记录不能相同
			checkTopoIdOfCellPara(record, records);
		}

		// 小区表在进行新增和修改的时候，需要检查测量配置表中的事件标志
		// 小区参数表中同频周期测量配置索引
		XBizField intraFreqPrdMeasCfg = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG);
		if (intraFreqPrdMeasCfg == null) {
			// 硬编码做版本兼容
			intraFreqPrdMeasCfg = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG_16);
		}

		// 组装查询条件在测量配置表中查找
		XBizRecord condition = new XBizRecord();
		XBizField tempCondition = new XBizField(
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX,
				intraFreqPrdMeasCfg.getValue());
		condition.addField(tempCondition);

		// 获取T_ENB_MEASCFG表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, condition);
		// 校验u16IntraFreqPrdMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// 关联外键不存在
					intraFreqPrdMeasCfg.getName(),
					"IntraFreqPrdMeasCfg_can_not_find_in_t_enb_meascfg");

		}
		// 校验u8IntraFreqHOMeasCfg,该值所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG, 3);
		// 校验u16IntraFreqPrdMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// 关联外键不存在
					EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG,
					"u8IntraFreqHOMeasCfg_in_t_enb_meascfg_must_3");

		}

		// 校验u8A2ForInterFreqMeasCfg,该值所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG, 2);
		// 校验u8A2ForInterFreqMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// 关联外键不存在
					EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG,
					"u8A2ForInterFreqMeasCfg_in_t_enb_meascfg_must_2");

		}

		// 校验u8A1ForInterFreqMeasCfg,该值所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG, 1);
		// 校验u8A1ForInterFreqMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// 关联外键不存在
					EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG,
					"u8A1ForInterFreqMeasCfg_in_t_enb_meascfg_must_1");

		}

		// 校验u8A2ForRedirectMeasCfg,该值所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG, 2);
		// 校验u8A2ForRedirectMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// 关联外键不存在
					EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG,
					"u8A2ForRedirectMeasCfg_in_t_enb_meascfg_must_2");

		}
	}

	/**
	 * 校验小区名称是否重复，同一基站下小区名称不能相同
	 * 
	 * @throws Exception
	 */
	private void checkCellNameDuplicated(XBizRecord record,
			List<XBizRecord> records) throws Exception {
		String currentName = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_CELL_NAME).getValue();
		for (XBizRecord xBizRecord : records) {
			String name = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_CELL_NAME).getValue();
			if (currentName.equals(name)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_CELL_NAME,
						"cell_name_duplicated");
			}
		}
	}

	private int getCellIndex(XBizRecord record, List<XBizRecord> records) {
		int index = 0;
		for (XBizRecord xBizRecord : records) {
			XBizField cidField1 = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			XBizField cidField2 = xBizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			int cid1 = Integer.valueOf(cidField1.getValue());
			int cid2 = Integer.valueOf(cidField2.getValue());
			if (cid1 == cid2) {
				break;
			}
			index++;
		}
		return index;
	}

	/**
	 * 不同小区引用的拓扑表记录不能相同
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkTopoIdOfCellPara(XBizRecord record,
			List<XBizRecord> records) throws Exception {
		String currentTopo = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
		for (XBizRecord xBizRecord : records) {
			String topo = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			if (currentTopo.equals(topo)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_TOPO_NO,
						"topo_no_has_been_referenced");
			}
		}

	}

	/**
	 * 同频小区的PCI不能相同
	 * 
	 * @param record
	 * @param records
	 * @throws Exception
	 */
	private void checkPCI(XBizRecord record, List<XBizRecord> records)
			throws Exception {
		XBizField freqField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ);
		int freq1 = Integer.valueOf(freqField.getValue());
		for (XBizRecord xBizRecord : records) {
			XBizField freqField2 = xBizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ);
			int freq2 = Integer.valueOf(freqField2.getValue());
			if (freq1 == freq2) {
				XBizField pciField1 = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
				XBizField pciField2 = xBizRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
				int pci1 = Integer.valueOf(pciField1.getValue());
				int pci2 = Integer.valueOf(pciField2.getValue());
				if (pci1 == pci2) {
					XBizField cidField1 = record
							.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
					XBizField cidField2 = xBizRecord
							.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
					int cid1 = Integer.valueOf(cidField1.getValue());
					int cid2 = Integer.valueOf(cidField2.getValue());
					throw new BizException(
							EnbConstantUtils.FIELD_NAME_PHY_CELL_ID
									+ splitFlag
									+ MessageFormat.format(
											OmpAppContext
													.getMessage("cell_with_same_freq_pci_cannot_equal"),
											cid1, cid2));

				}
			}
		}
	}

	/**
	 * 查询相关联的测量配置表中的记录
	 * 
	 * @param moId
	 * @param record
	 * @param fieldName
	 * @param eventId
	 * @return
	 * @throws Exception
	 */
	private List<XBizRecord> queryRelatedMeasureCfg(long moId,
			XBizRecord record, String fieldName, int eventId) throws Exception {
		XBizRecord condition = new XBizRecord();
		XBizField u8A2ForRedirectMeasCfg = record.getFieldBy(fieldName);
		XBizField u8A2ForRedirectMeasCfgIntTemp = new XBizField(
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX,
				u8A2ForRedirectMeasCfg.getValue());
		XBizField u8EvtId = new XBizField(EnbConstantUtils.FIELD_NAME_EVT_ID,
				String.valueOf(eventId));

		condition.addField(u8A2ForRedirectMeasCfgIntTemp);
		condition.addField(u8EvtId);

		return getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, condition);
	}

	/**
	 * 小区集群配置参数检验 满足：T_CEL_PARA表u8UlDlSlotAlloc配置为0时，该值可取0、5；
	 * T_CEL_PARA表u8UlDlSlotAlloc配置为1时
	 * ，该值可取0、4、5、9；T_CEL_PARA表u8UlDlSlotAlloc配置为2时
	 * ，该值可取0、3、4、5、8、9；T_CEL_PARA表u8UlDlSlotAlloc配置为3时
	 * ，该值可取0、5、6、7、8、9；T_CEL_PARA表u8UlDlSlotAlloc配置为4时
	 * ，该值可取0、4、5、6、7、8、9；T_CEL_PARA表u8UlDlSlotAlloc配置为5时
	 * ，该值可取0、3、4、5、6、7、8、9；T_CEL_PARA表u8UlDlSlotAlloc配置为6时，该值可取0、5、9
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCPTTRecord(Long moId, XBizRecord record, String actionType)
			throws Exception {
		// 集群广播寻呼帧号必须小于集群广播寻呼周期
		checkPttBPagingFN(record);

		// 传递根据小区标识，获取小区参数表中XBizField的集合
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext.getMessage("relate_cell_parm_is_not_exist"));

		} else {
			XBizRecord cParmRecord = bizRecords.get(0);
			// 小区参数表中子帧配比
			XBizField u8UlDlSlotAlloc = cParmRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
			int u8UlDlSlotAllocInt = Integer
					.valueOf(u8UlDlSlotAlloc.getValue());

			XBizField u8PttBPagingSubFN = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_BPAGING_SUB_FN);
			int u8PttBPagingSubFNInt = Integer.valueOf(u8PttBPagingSubFN
					.getValue());

			switch (u8UlDlSlotAllocInt) {
			case 0: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 5)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,5"));
				}
				break;
			}
			case 1: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 5
						|| u8PttBPagingSubFNInt == 4 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,4,5,9"));
				}
				break;
			}
			case 2: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 5
						|| u8PttBPagingSubFNInt == 4
						|| u8PttBPagingSubFNInt == 3
						|| u8PttBPagingSubFNInt == 8 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,3,4,5,8,9"));
				}
				break;
			}
			case 3: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 5
						|| u8PttBPagingSubFNInt == 6
						|| u8PttBPagingSubFNInt == 7
						|| u8PttBPagingSubFNInt == 8 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,5,6,7,8,9"));
				}
				break;
			}
			case 4: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 4
						|| u8PttBPagingSubFNInt == 5
						|| u8PttBPagingSubFNInt == 6
						|| u8PttBPagingSubFNInt == 7
						|| u8PttBPagingSubFNInt == 8 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,4,5,6,7,8,9"));
				}
				break;
			}
			case 5: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 3
						|| u8PttBPagingSubFNInt == 4
						|| u8PttBPagingSubFNInt == 5
						|| u8PttBPagingSubFNInt == 6
						|| u8PttBPagingSubFNInt == 7
						|| u8PttBPagingSubFNInt == 8 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,3,4,5,6,7,8,9"));
				}
				break;
			}
			case 6: {
				if (!(u8PttBPagingSubFNInt == 0 || u8PttBPagingSubFNInt == 5 || u8PttBPagingSubFNInt == 9)) {
					throw new BizException(u8PttBPagingSubFN.getName()
							+ splitFlag
							+ MessageFormat.format(OmpAppContext
									.getMessage("check_u8PttBPagingSubFN"),
									u8UlDlSlotAllocInt, "0,5,9"));
				}
				break;
			}
			default:
				break;
			}
		}

	}

	/**
	 * 集群广播寻呼帧号必须小于集群广播寻呼周期
	 * 
	 * @param record
	 * @throws BizException
	 */
	private void checkPttBPagingFN(XBizRecord record) throws BizException {
		// 集群广播寻呼帧号必须小于集群广播寻呼周期，即u8PttBPagingFN < 2^(u8PttBPagingSycle + 1)
		XBizField pagingFnField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_BPAGING_FN);
		XBizField pagingCycleField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_BPAGING_CYCLE);
		int pagingFn = Integer.valueOf(pagingFnField.getValue());
		int pagingCycle = Integer.valueOf(pagingCycleField.getValue());
		if (pagingFn >= Math.pow(2, pagingCycle + 1)) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_PTT_BPAGING_FN,
					"pttBPagingFn_must_bigger_than_pttBPagingCycle");
		}
	}

	/**
	 * 邻区关系参数配置表校验
	 * T_CEL_NBRCEL表中u8CenterFreqCfgIdx如果不为255，必须要有与T_ENB_CTFREQ表中一条记录对应
	 * ，对应关系：u8CenterFreqCfgIdx等于T_ENB_CTFREQ表中的主键u8CfgIdx
	 * T_CEL_NBRCEL表中u8SvrCID相同且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能相等；
	 * T_CEL_NBRCEL表中u8SvrCID与T_CEL_PARA表中的u8CId相等且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能与T_CEL_PARA对应记录中的u16PhyCellId字段相等
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCNBRCELRecord(Long moId, XBizRecord record,
			String actionType) throws Exception {

		// NBRCEL表中u8SvrCID
		XBizField u8SvrCID = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		XBizRecord condition = new XBizRecord();
		XBizField u8CId = new XBizField();
		u8CId.setName(EnbConstantUtils.FIELD_NAME_CELL_ID);
		u8CId.setValue(u8SvrCID.getValue());
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> cPARAbizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (cPARAbizRecords == null || cPARAbizRecords.size() == 0) {
			throw newBizException(u8SvrCID.getName(),
					"relate_cell_parm_is_not_exist");

		} else {
			XBizRecord cParmRecord = cPARAbizRecords.get(0);

			// 小区参数表中的u16PhyCellId
			XBizField u16PhyCellIdFromCParm = cParmRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			int u16PhyCellIdFromCParmInt = Integer
					.valueOf(u16PhyCellIdFromCParm.getValue());

			// NBRCEL表中u16PhyCellId
			XBizField u16PhyCellId = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			int u16PhyCellIdNBRCELInt = Integer
					.valueOf(u16PhyCellId.getValue());

			// NBRCEL表中u8CenterFreqCfgIdx
			XBizField u8CenterFreqCfgIdx = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
			Integer u8CenterFreqCfgIdxInt = Integer.valueOf(u8CenterFreqCfgIdx
					.getValue());

			// FIELD_NAME_SVR_CID为255：是否有多个u8SvrCID/u16PhyCellId不能为小区参数表中的u16PhyCellId相等
			if (u8CenterFreqCfgIdxInt == 255) {
				if (u16PhyCellIdNBRCELInt == u16PhyCellIdFromCParmInt) {
					throw newBizException(u16PhyCellId.getName(),
							"physics_cell_id_is_not_exist_in_related_cell_parm");
				}
				condition = new XBizRecord();
				condition.addField(u8SvrCID);
				condition.addField(u8CenterFreqCfgIdx);
				condition.addField(u16PhyCellId);

				// 查询当前u8SvrCID下所有的记录
				List<XBizRecord> nBRCELRecord = getXBizRecordByMoId(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition);
				if (nBRCELRecord == null || cPARAbizRecords.size() == 0) {

				} else {
					boolean flag = false;
					if (actionType.equals(ActionTypeDD.MODIFY)) {
						// 修改校验：u8SvrCID相同且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能相等，如果已经存在并且不是当前站的

						if (nBRCELRecord.size() > 1) {
							flag = true;
						} else {
							XBizRecord tempRecord = nBRCELRecord.get(0);
							// 数据库中邻小区标识
							XBizField u32NbreNBID = tempRecord
									.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
							int u32NbreNBIDTempInt = Integer
									.valueOf(u32NbreNBID.getValue());
							// 浏览器传递过来的邻eNB标识
							int u32NbreNBIDInt = Integer
									.valueOf(record
											.getFieldBy(
													EnbConstantUtils.FIELD_NAME_NBR_ENBID)
											.getValue());
							// 数据库中数据库中服务小区标识
							XBizField u8NbrCID = tempRecord
									.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
							int u8NbrCIDTempInt = Integer.valueOf(u8NbrCID
									.getValue());
							// 浏览器传递过来的服务小区标识
							int u8NbrCIDInt = Integer
									.valueOf(record
											.getFieldBy(
													EnbConstantUtils.FIELD_NAME_NBR_CID)
											.getValue());
							if (!(u32NbreNBIDInt == u32NbreNBIDTempInt && u8NbrCIDInt == u8NbrCIDTempInt)) {
								flag = true;
							}

						}

					} else if (actionType.equals(ActionTypeDD.ADD)) {
						// 新增校验：u8SvrCID相同且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能相等
						flag = true;
					}
					if (flag) {
						throw newBizException(u16PhyCellId.getName(),
								"nbrcel_table_and_nbtcid_common_and_center_freq_is255_physic_id_duplicate");
					}
				}
			} else {
				// u8CenterFreqCfgIdx与T_ENB_CTFREQ是否存在映射关系、
				condition = new XBizRecord();
				condition.addField(new XBizField(
						EnbConstantUtils.FIELD_NAME_CFG_IDX,
						u8CenterFreqCfgIdxInt.toString()));

				// 中心载频配置表下所有的记录
				List<XBizRecord> nBRCELRecord = getXBizRecordByMoId(moId,
						EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ, condition);
				if (nBRCELRecord == null || cPARAbizRecords.size() == 0) {
					throw newBizException(u8CenterFreqCfgIdx.getName(),
							"nbrcel_table_center_freq_can_not_find_in_ctfreq");
				}
			}
		}
	}

	/**
	 * 小区下行功控参数表校验：可配置小区信号参考功率的最大值由如下公式决定：CRS_EPRE_max = u16CellTransPwr -
	 * 10*log10(T_CEL_PARA.u8DlAntPortNum) +
	 * ERS_nor，公式中涉及的字段都是按照协议值（P）来计算，其中ERS_nor查表可得，表格见附件
	 */

	public void checkDLPCRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		long moId = enb.getMoId();
		// 传递根据小区标识，获取小区参数表中XBizField的集合
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext.getMessage("relate_cell_parm_is_not_exist"));

		} else {
			XBizRecord cParmRecord = bizRecords.get(0);
			// 小区参数表中系统带宽的协议值P
			XBizField u8SysBandWidth = cParmRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			int u8SysBandWidthInt = Integer.valueOf(u8SysBandWidth.getValue());

			// 基站不支持与之关联的小区参数表中系统带宽为1.4M或3M
			if (u8SysBandWidthInt == 0 || u8SysBandWidthInt == 1) {
				// 针对这种情况后续待定---------当前基站不支持1.4M与3M,故系统带宽为1.4M或3M时，基站对应的小区建立不起来的，数据默认存库即可

			} else {

				// 小区参数表中u8DlAntPortNum[下行天线端口数]
				XBizField u8DlAntPortNum = cParmRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
				String u8DlAntPortNumValue = u8DlAntPortNum.getValue();

				// 下行天线端口数协议值P与真实值之间的关系：0--port1;1--port2;2--port4;
				XList list = EnbBizHelper.getFieldMetaBy(enb.getEnbType(),
						enb.getProtocolVersion(),
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
						EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
				// (0)port1|(1)port2|(2)port4
				String enumValue = list.getEnumText();
				// 处理为key--value
				Map<String, String> map = new HashMap<String, String>();
				String[] arrs = enumValue.split("\\|");
				for (int i = 0; i < arrs.length; i++) {
					String temp = arrs[i];
					String key = temp.substring(temp.indexOf("(") + 1,
							temp.indexOf(")"));
					String value = temp.substring(temp.indexOf(")") + 5);
					map.put(key, value);

				}

				// 小区参数表中下行端口数的真实值，如1，2，4;
				int u8DlAntPortNumDInt = Integer.valueOf(map
						.get(u8DlAntPortNumValue));

				// 小区参数表中系统带宽M值：如0--1.4等
				Map<String, String> mapAsM = getSysBandWidthAsRBorM(enb, u8CId,
						false);

				// 获取系统带宽对应的M值
				String SysBandWidthDValue = mapAsM.get(u8SysBandWidth
						.getValue());

				// 小区下行功控参数表--小区最大发射总功率--协议之可以为double
				XBizField u16CellTransPwr = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_TRANS_PWR);
				// u16CellTransPwrDouble的协议值P:D=p*10
				double u16CellTransPwrDouble = Double.valueOf(u16CellTransPwr
						.getValue()) / 10;

				// 小区下行功控参数表--小区参考信号功率
				XBizField u16CellSpeRefSigPwr = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR);
				int u16CellSpeRefSigPwrPInt = Integer
						.valueOf(u16CellSpeRefSigPwr.getValue()) - 60;
				// 小区下行功控参数表--PA
				XBizField u8PAForDTCH = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
				int u8PAForDTCHInt = Integer.valueOf(u8PAForDTCH.getValue());

				// 小区下行功控参数表--PB
				XBizField u8PB = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PB);
				int u8PBInt = Integer.valueOf(u8PB.getValue());
				double ersNor = 0;

				// u8PAForDTCHInt，u8PBInt，SysBandWidthDValue获取ersNor
				if (5 == Integer.valueOf(SysBandWidthDValue)) {
					ersNor = getResNor(BW5MHZ, u8PBInt, u8PAForDTCHInt);
				} else if (10 == Integer.valueOf(SysBandWidthDValue)) {
					ersNor = getResNor(BW10MHZ, u8PBInt, u8PAForDTCHInt);
				} else if (15 == Integer.valueOf(SysBandWidthDValue)) {
					ersNor = getResNor(BW15MHZ, u8PBInt, u8PAForDTCHInt);
				} else if (20 == Integer.valueOf(SysBandWidthDValue)) {
					ersNor = getResNor(BW20MHZ, u8PBInt, u8PAForDTCHInt);
				}
				// u16CellTransPwr - 10*log10(T_CEL_PARA.u8DlAntPortNum) +
				// ERS_nor的结果值与u16CellSpeRefSigPwrPInt比较
				double temp = Double.valueOf(String.format("%.1f",
						Math.log10(u8DlAntPortNumDInt)));
				ersNor = u16CellTransPwrDouble - 10 * temp + ersNor;
				if (ersNor < u16CellSpeRefSigPwrPInt) {
					throw new BizException(
							u16CellSpeRefSigPwr.getName()
									+ splitFlag
									+ MessageFormat.format(
											OmpAppContext
													.getMessage("cell_reference_freq_can_not_greater_than"),
											(int) ersNor));
				}

			}
		}
	}

	public int getResNor(int[][] resNors, int x, int y) {
		int temp = 0;
		a: for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 7; j++) {
				if (i == x && j == y) {
					temp = resNors[x][y];
					break a;
				}
			}
		}
		return temp;

	}

	/**
	 * * 小区上行物理信道配置参数表-- 对u8N_RB2【PUCCH format 2/2a/2b使用的RB数 】u8SRITransPrd
	 * 【SRI发送周期 】满足规则如下： A.
	 * "u8DeltaPucchShift、u8N_RB2、u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	 * u8N_RB2
	 * +(u16N_SrChn\(36\(u8DeltaPucchShift+1)))不能超过u8SysBandWidth代表的最大RB个数" B.
	 * 
	 * @param record
	 * @param moId
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCPUCHRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {

		// 获取小区上行物理信道配置参数表关联的小区编号
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// 获取小区配置中的系统带宽
			XBizField u8SysBandWidth = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			String u8SysBandWidthValue = u8SysBandWidth.getValue();
			int u8SysBandWidthValueInt = Integer.valueOf(map
					.get(u8SysBandWidthValue));
			// 获取小区配置中的子帧配比
			XBizField bizFieldUlDlSlotAlloc = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);

			String u8UlDlSlotAlloc = bizFieldUlDlSlotAlloc.getValue();

			// PUCCH format 1/1a/1b循环偏移量
			XBizField u8DeltaPucchShift = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT);
			int u8DeltaPucchShiftValue = Integer.parseInt(u8DeltaPucchShift
					.getValue());
			// 用于计算的值要加1
			u8DeltaPucchShiftValue++;
			// PUCCH format 2/2a/2b使用的RB数
			XBizField u8N_RB2 = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_RB2);
			int u8N_RB2Value = Integer.parseInt(u8N_RB2.getValue());
			// PUCCH SR信道条数
			XBizField u16N_SrChn = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SR_CHN);
			int u16N_SrChnValue = Integer.parseInt(u16N_SrChn.getValue());
			// SRI发送周期
			XBizField u8SRITransPrd = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SRI_TRANS_PRD);
			int u8SRITransPrdValue = Integer.valueOf(u8SRITransPrd.getValue());
			// PUCCH上报告CQI/PMI的周期
			XBizField u8RptCqiPrd = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_RPT_CQI_PRD);
			int u8RptCqiPrdValue = Integer.valueOf(u8RptCqiPrd.getValue());
			int temp1 = u16N_SrChnValue / (36 / u8DeltaPucchShiftValue);
			double temp2 = ((double) u16N_SrChnValue)
					/ (double) (36 / u8DeltaPucchShiftValue);
			// u16N_SrChn必须为(36\(u8DeltaPucchShift+1))的整数倍
			if (temp1 != temp2) {
				throw newBizException(u16N_SrChn.getName(),
						"u16nsrchn_must_divisible_by");
			}
			// u8DeltaPucchShift、u8N_RB2、u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift+1)))不能超过u8SysBandWidth代表的最大RB个数
			if (u8N_RB2Value
					* +(u16N_SrChnValue / (36 / u8DeltaPucchShiftValue)) > u8SysBandWidthValueInt) {
				throw new BizException(
						u16N_SrChn.getName()
								+ splitFlag
								+ MessageFormat.format(
										OmpAppContext
												.getMessage("pucch_format_2/2a/2b_rb_and_pucch_sr_and_pucch_format_1/1a/1b_expression"),
										u8SysBandWidthValueInt));
			} else if (Integer.valueOf(u8UlDlSlotAlloc) == 2
					&& u8SRITransPrdValue == 0 && u8RptCqiPrdValue == 0) {
				// 小区参数表中子帧配比为2时，u8SRITransPrd与u8RptCqiPrd不能同时为0
				throw newBizException(u8RptCqiPrd.getName(),
						"when_uldlslotalloc_is_2_sritransprd_rptcqiprd_can_not_5");
			}

		}

	}

	/**
	 * 中心载频配置表规则校验
	 * <p>
	 * u8InterFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
	 * </p>
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 */
	public void checkCTFREQRecord(Long moId, XBizRecord record,
			String actionType) throws Exception {
		// 异频切换测量配置索引所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
		List<XBizRecord> bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG, 3);
		// 配置测量索引对应的测量配置表数据不存在
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG,
					"interfreqhomeascfg_in_t_enb_meascfg_must_3");
		}
	}

	/**
	 * PRACH参数配置表规则校验满足 u8PrachFreqOffset与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	 * u8PrachFreqOffset小于等于u8SysBandWidth代表的最大RB个数减去6
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */

	public void checkCPRACHRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			// 获取小区配置表中系统带宽内存值与显示值的关系集合
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// 获取小区配置中的系统带宽
			XBizField u8SysBandWidth = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			String u8SysBandWidthValue = u8SysBandWidth.getValue();
			int u8SysBandWidthValueInt = Integer.valueOf(map
					.get(u8SysBandWidthValue));
			XBizField u8PrachFreqOffset = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PRACH_FREQ_OFFSET);
			String u8PrachFreqOffsetValue = u8PrachFreqOffset.getValue();
			int u8PrachFreqOffsetValueInt = Integer
					.valueOf(u8PrachFreqOffsetValue);
			if (u8PrachFreqOffsetValueInt > (u8SysBandWidthValueInt - 6)) {
				throw newBizException(u8PrachFreqOffset.getName(),
						"prachfreqoffset_can_not_than_sys_rb_in_cell_parm");

			}
			// 新增对逻辑根序列索引：u16RootSeqIndex的校验,满足：T_CEL_PARA表的u16PhyCellId字段保持一致
			// XBizField u16PhyCellId = cParaRecord
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			// int u16PhyCellIdInt = Integer.valueOf(u16PhyCellId.getValue());
			// XBizField u16RootSeqIndex = record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);
			// int u16RootSeqIndexInt = Integer
			// .valueOf(u16RootSeqIndex.getValue());
			// if (u16PhyCellIdInt != u16RootSeqIndexInt) {
			// throw newBizException(u16RootSeqIndex.getName(),
			// "u16RootSeqIndex_must_equal_cparm_u16PhyCellId");
			// }

			// 新增对u8PrachCfgIndex校验,满足T_CEL_PARA表中的字段u8UlDlSlotAlloc即子帧配比为0时取值不能是(11,19)，子帧配比为1时取值不能是(8,13,14,40-47)，子帧配比为2时取值不能是(5,7,8,11,13,14,17,19-47)，子帧配比为3时取值不能是(10,11,19,22,24,32,34,42,44,50,52)，子帧配比为4时取值不能是(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)，子帧配比为5时取值不能是(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)，子帧配比为6时取值不能是(16,17,42,44)
			XBizField u8UlDlSlotAlloc = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
			int u8UlDlSlotAllocInt = Integer
					.valueOf(u8UlDlSlotAlloc.getValue());
			int[] notSelect = PRACH_NOT_SELECT_PARAM[u8UlDlSlotAllocInt];
			// 获取u8PrachCfgIndex的值,判断是否包含在notSelect中，如果包含则校验失败
			XBizField u8PrachCfgIndex = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PRACH_CFG_INDEX);
			int u8PrachCfgIndexInt = Integer
					.valueOf(u8PrachCfgIndex.getValue());
			StringBuffer buffer = new StringBuffer();
			boolean flag = false;
			for (int i = 0; i < notSelect.length; i++) {
				if (notSelect[i] == u8PrachCfgIndexInt) {
					flag = true;
				}
				buffer.append(notSelect[i]).append(",");
			}

			if (flag) {
				buffer.substring(0, buffer.length() - 1);
				throw new BizException(
						u8PrachCfgIndex.getName()
								+ splitFlag
								+ MessageFormat.format(
										OmpAppContext
												.getMessage("not_select_u8PrachCfgIndex_when_u8UlDlSlotAlloc"),
										u8UlDlSlotAllocInt, buffer.toString()));
			}

		}
	}

	/**
	 * 小区算法参数表校验逻辑如下： u8UlRbNum不超过带宽，取值必须是2,3,5的整数倍; u8DlRbNum不超过带宽 ;
	 * u8UlMaxRbNum 100与带宽取小，且必须是2,3,5整数倍; u8UlMinRbNum 100与带宽取小，且必须是2,3,5整数倍
	 * u8DlMaxRbNum100与带宽取小 ;u8DlMinRbNum 100与带宽取小 ;取小即为替换原设计值
	 */
	public void checkCALGRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// 获取T_CEL_PARA表中的XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			// 获取小区配置表中系统带宽内存值与显示值的关系集合
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// 获取小区配置中的系统带宽
			XBizField u8SysBandWidth = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			String u8SysBandWidthValue = u8SysBandWidth.getValue();
			// 系统带宽的RB值
			int u8SysBandWidthValueInt = Integer.valueOf(map
					.get(u8SysBandWidthValue));
			// 上行预调度RB数
			XBizField u8UlRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_RB_NUM);
			int u8UlRbNumInt = Integer.valueOf(u8UlRbNum.getValue());
			// 下行预调度RB数
			XBizField u8DlRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_RB_NUM);
			int u8DlRbNumInt = Integer.valueOf(u8DlRbNum.getValue());
			// 上行最大分配RB数
			XBizField u8UlMaxRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_MAX_RB_NUM);
			int u8UlMaxRbNumInt = Integer.valueOf(u8UlMaxRbNum.getValue());
			// 上行最小分配RB数
			XBizField u8UlMinRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM);
			int u8UlMinRbNumInt = Integer.valueOf(u8UlMinRbNum.getValue());
			// 下行最大分配RB数
			XBizField u8DlMaxRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_MAX_RB_NUM);
			int u8DlMaxRbNumInt = Integer.valueOf(u8DlMaxRbNum.getValue());
			// 下行最小分配RB数
			XBizField u8DlMinRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_MIN_RB_NUM);
			int u8DlMinRbNumInt = Integer.valueOf(u8DlMinRbNum.getValue());
			// 上行预调度RB数不大于带宽的RB数，且为2/3/5的倍数
			if (u8UlRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlRbNum.getName(),
						"ulrbnum_not_than_sys_bandwidth_rb");
			} else {
				// 是否是2或者3或者5的倍数
				if (u8UlRbNumInt % 2 != 0 && u8UlRbNumInt % 3 != 0
						&& u8UlRbNumInt % 5 != 0) {
					throw newBizException(u8DlRbNum.getName(),
							"ulrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// 下行预调度RB数不超过带宽
			if (u8DlRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlRbNum.getName(),
						"dlrbnum_not_than_sys_bandwidth_rb");
			}

			// 上行最大分配RB数：不大于带宽，且必须是2,3,5整数倍
			if (u8UlMaxRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlMaxRbNum.getName(),
						"ulmaxrbnum_not_than_sys_bandwidth_rb");
			} else {
				// 是否是2或者3或者5的倍数
				if (u8UlMaxRbNumInt % 2 != 0 && u8UlMaxRbNumInt % 3 != 0
						&& u8UlMaxRbNumInt % 5 != 0) {
					throw newBizException(u8UlMaxRbNum.getName(),
							"ulmaxrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// 上行最小分配RB数：不大于带宽，且必须是2,3,5整数倍
			if (u8UlMinRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlMinRbNum.getName(),
						"ulminrbnum_not_than_sys_bandwidth_rb");
			} else {
				// 是否是2或者3或者5的倍数
				if (u8UlMinRbNumInt % 2 != 0 && u8UlMinRbNumInt % 3 != 0
						&& u8UlMinRbNumInt % 5 != 0) {
					throw newBizException(u8UlMinRbNum.getName(),
							"ulminrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// 下行最大分配RB数不超过带宽
			if (u8DlMaxRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlMaxRbNum.getName(),
						"dlmaxrbnum_not_than_sys_bandwidth_rb");
			}

			// 下行最小分配RB数不超过带宽
			if (u8DlMinRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlMinRbNum.getName(),
						"dlminrbnum_not_than_sys_bandwidth_rb");
			}

			// CFI配置
			XBizField u8Cfi = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CFI);
			// 内存值转换成真实值
			int u8CfiInt = Integer.valueOf(u8Cfi.getValue());
			map = getSysBandWidthAsRBorM(enb, u8CId, false);
			// CFI配置取值4只在小区参数表中中系统带宽为1.4M时有效
			if (u8CfiInt == 4 && !map.get(u8SysBandWidthValue).equals("1.4")) {
				throw newBizException(u8Cfi.getName(),
						"cfi_config_is_4_must_sys_brandwidth_is_1.4");
			}

		}
		// 校验小区边缘频带bitmap
		// checkBitMap(record);

	}

	/**
	 * 校验小区边缘频带bitmap
	 * 
	 * @param bizRecord
	 * @throws BizException
	 */
	private void checkBitMap(XBizRecord bizRecord) throws BizException {
		XBizField bizField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_ICIC_SWITCH);
		if (bizField == null)
			return;
		String dlSwitch = bizField.getValue();

		if (dlSwitch.equals("1")) {
			String dlBitMap = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_DL_CEB_BITMAP).getValue();
			dlBitMap = getBinaryBitMap(dlBitMap);
			// 截掉后7个
			dlBitMap = dlBitMap.substring(0, dlBitMap.length() - 7);
			// 判断是否是全0
			if (allMatch(dlBitMap, "0")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_DL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
			// 判断是否是全1
			if (allMatch(dlBitMap, "1")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_DL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
		}
		String ulSwitch = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_UL_ICIC_SWITCH).getValue();
		if (ulSwitch.equals("1")) {
			String ulBitMap = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_UL_CEB_BITMAP).getValue();
			ulBitMap = getBinaryBitMap(ulBitMap);
			// 截掉后4个
			ulBitMap = ulBitMap.substring(0, ulBitMap.length() - 4);
			// 判断是否是全0
			if (allMatch(ulBitMap, "0")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_UL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
			// 判断是否是全1
			if (allMatch(ulBitMap, "1")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_UL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
		}
	}

	/**
	 * 获取bitmap的二进制字符串
	 * 
	 * @param hexBitMap
	 * @return
	 */
	private String getBinaryBitMap(String hexBitMap) {
		String binaryBitMap = "";
		for (int i = 0; i < hexBitMap.length(); i++) {
			binaryBitMap += getBinaryArray(String.valueOf(hexBitMap.charAt(i)));
		}
		return binaryBitMap;
	}

	private boolean allMatch(String str, String regex) {
		return str.replaceAll(regex, "").equals("");
	}

	/**
	 * SI的配置调度参数表记录校验</br>
	 * <p>
	 * 校验规则：1，对于相同u8CId的记录中不能有重复的sib；2，一条记录不容许不配置sib；
	 * 3，对于相同的u8CId记录中必须至少有sib2；4，T_ENB_PARA表的u8PttEnable字段为1时，必须配置PttSib
	 * </p>
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	private void checkCellSISCHRecord(Long moId, XBizRecord record,
			String actionType) throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			int[] currentFlags = getSibFlag(record);
			// 校验是否包含Sib
			if (!isSibContained(currentFlags)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_CELL_ID,
						"sib_must_be_contained");
			}
			// 获取当前小区的SI配置
			XBizField cellIdField = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			XBizRecord condition = new XBizRecord();
			condition.addField(cellIdField);
			List<XBizRecord> records = getXBizRecordByMoId(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_SISCH, condition);
			String currentSiId = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SIID).getValue();

			// 重复的sib标识
			int[] repeatSibFlags = new int[] { 0, 0, 0, 0, 0 };
			if (records != null && !records.isEmpty()) {
				for (XBizRecord xBizRecord : records) {
					String siId = xBizRecord.getFieldBy(
							EnbConstantUtils.FIELD_NAME_SIID).getValue();
					// 跳过当前添加或修改的记录
					if (currentSiId.equals(siId))
						continue;
					int[] flags = getSibFlag(xBizRecord);
					// 计算是否存在重复sib的标识
					repeatSibFlags = orSibFlag(repeatSibFlags, flags);
				}
			}
			// 是否有sib
			int[] hasSibFlags = repeatSibFlags;
			repeatSibFlags = andSibFlag(repeatSibFlags, currentFlags);
			String repeatSibName = getRepeatSibName(repeatSibFlags);
			if (!StringUtils.isBlank(repeatSibName)) {
				throw newBizException(repeatSibName,
						"sib_is_open_in_other_record");
			}
			// sib2校验是否通过
			hasSibFlags = orSibFlag(hasSibFlags, currentFlags);
			boolean hasSib2 = hasSibFlags[0] == 1;
			if (!hasSib2) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_SIB2,
						"sib2_must_be_contained_in_each_cell");
			}

			// 当前记录是否存在除pttSib以外的其他sib
			boolean hasOtherPtt = currentFlags[0] == 1 || currentFlags[1] == 1
					|| currentFlags[2] == 1 || currentFlags[3] == 1;
			boolean hasPttSib = currentFlags[4] == 1;
			if (hasPttSib && hasOtherPtt) {
				// 集群Sib必须单独配置在一条记录中
				throw newBizException(EnbConstantUtils.FIELD_NAME_SIBPTT,
						"pttsib_must_enable_individually");
			}

			// // 集群开关是否打开
			// boolean pttEnabled = isPttEnabled(moId);
			// // 集群开关打开时，pttSib必须打开
			// if (pttEnabled && hasSibFlags[4] != 1) {
			// throw newBizException(EnbConstantUtils.FIELD_NAME_SIBPTT,
			// "pttsib_must_enable_when_pttenble");
			// }
		}
	}

	/**
	 * 获取重复的sib名称
	 * 
	 * @param flags
	 * @return
	 */
	private String getRepeatSibName(int[] flags) {
		int index = -1;
		for (int i = 0; i < flags.length; i++) {
			if (flags[i] == 1) {
				index = i;
				break;
			}
		}
		switch (index) {
		case 0:
			return EnbConstantUtils.FIELD_NAME_SIB2;
		case 1:
			return EnbConstantUtils.FIELD_NAME_SIB3;
		case 2:
			return EnbConstantUtils.FIELD_NAME_SIB4;
		case 3:
			return EnbConstantUtils.FIELD_NAME_SIB5;
		case 4:
			return EnbConstantUtils.FIELD_NAME_SIBPTT;
		default:
			return "";
		}
	}

	/**
	 * Sib标识与运算
	 * 
	 * @param flags1
	 * @param flags2
	 * @return
	 */
	private int[] andSibFlag(int[] flags1, int[] flags2) {
		int[] flags = new int[flags1.length];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = flags1[i] & flags2[i];
		}
		return flags;
	}

	private int[] orSibFlag(int[] flags1, int[] flags2) {
		int[] flags = new int[flags1.length];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = flags1[i] | flags2[i];
		}
		return flags;
	}

	/**
	 * 获取eNB参数表中的集群开关
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private boolean isPttEnabled(long moId) throws Exception {
		// 获取eNB参数表中的集群开关
		XBizRecord enbParamRecord = queryEnbParamRecord(moId);
		if (enbParamRecord == null)
			return false;
		int pttEnable = Integer.valueOf(enbParamRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PTT_ENABLE).getValue());
		// T_ENB_PARA表的u8PttEnable字段为1时，必须配置PttSib
		return pttEnable == 1;
	}

	/**
	 * 记录中是否包含sib
	 * 
	 * @param record
	 * @return
	 */
	private boolean isSibContained(int[] flags) {
		for (int flag : flags) {
			if (flag == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取sib标识
	 * 
	 * @param record
	 * @return
	 */
	private int[] getSibFlag(XBizRecord record) {
		int flag2 = Integer.valueOf(record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB2).getValue());
		int flag3 = Integer.valueOf(record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB3).getValue());
		int flag4 = Integer.valueOf(record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB4).getValue());
		int flag5 = Integer.valueOf(record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB5).getValue());
		int flagPtt = Integer.valueOf(record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIBPTT).getValue());
		return new int[] { flag2, flag3, flag4, flag5, flagPtt };
	}

	/**
	 * 获取小区参数表中系统带宽的map集合，分M或者RB的值进行返回，如果按照RB则为true,否则为false
	 * 
	 * @param moId
	 * @param u8CId
	 * @param isRB
	 * @return
	 */
	private Map<String, String> getSysBandWidthAsRBorM(Enb enb,
			XBizField u8CId, boolean isRB) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);
		XList list = EnbBizHelper.getFieldMetaBy(enb.getEnbType(),
				enb.getProtocolVersion(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// enumValue的格式如：(0)1.4M(6RB)|(1)3M(15RB)|(2)5M(25RB)|(3)10M(50RB)|(4)15M(75RB)|(5)20M(100RB)
		String enumValue = list.getEnumText();
		String[] arrs = enumValue.split("\\|");
		// map中存储内存于显示之间的关系，使用RB
		if (isRB) {
			for (int i = 0; i < arrs.length; i++) {
				String key = arrs[i].substring(arrs[i].indexOf("(") + 1,
						arrs[i].indexOf(")"));
				String value = arrs[i].substring(arrs[i].lastIndexOf("(") + 1,
						arrs[i].lastIndexOf(")"));
				value = value.substring(0, value.length() - 2);
				map.put(key, value);
			}
		} else {
			for (int i = 0; i < arrs.length; i++) {
				String key = arrs[i].substring(arrs[i].indexOf("(") + 1,
						arrs[i].indexOf(")"));
				String value = arrs[i].substring(arrs[i].indexOf(")") + 1,
						arrs[i].lastIndexOf("("));
				value = value.substring(0, value.length() - 1);
				map.put(key, value);
			}
		}

		return map;
	}

	/**
	 * 根据moId/表名称及查询条件获取XBizRecord集合
	 * 
	 * @param moId
	 * @param tableName
	 * @param condition
	 * @return
	 */
	public List<XBizRecord> getXBizRecordByMoId(long moId, String tableName,
			XBizRecord condition) throws Exception {
		XBizTable ethTable = queryFromEms(moId, tableName, condition);
		if (ethTable != null && !ethTable.getRecords().isEmpty()) {
			return ethTable.getRecords();
		}

		return null;

	}

	/**
	 * 校验机架表数据
	 * 
	 * @param record
	 * @throws Exception
	 */
	private void checkRackRecord(XBizRecord record) throws Exception {
		// 1号机架不可删除
		XBizField rackNoField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		int rackNo = Integer.valueOf(rackNoField.getValue());
		if (rackNo == 1) {
			throw new Exception(OmpAppContext.getMessage("rack1_cannot_delete"));
		}
	}

	/**
	 * 校验机框表数据
	 * 
	 * @param record
	 * @throws Exception
	 */
	private void checkShelfRecord(XBizRecord record) throws Exception {
		// 1号机框不可删除
		XBizField shelfNoField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		// 1号机架不可删除
		XBizField u8RackNO = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		int rackNO = Integer.valueOf(u8RackNO.getValue());
		if (shelfNo == 1 && rackNO == 1) {
			throw new Exception(
					OmpAppContext.getMessage("shelf1_cannot_delete"));
		}

	}

	/**
	 * 校验单板表的数据
	 * 
	 * @param bizRecord
	 * @param actionType
	 *            操作方式，删除或添加
	 * @throws Exception
	 */
	private void checkBoardRecord(XBizRecord bizRecord, String actionType)
			throws Exception {
		int[] noArray = getRackShelfSlotNo(bizRecord);
		int rackNo = noArray[0];
		int shelfNo = noArray[1];
		int slotNo = noArray[2];

		if (actionType.equals(ActionTypeDD.DELETE)) {
			// 1-1-1单板不可删除
			if (rackNo == 1 && shelfNo == 1 && slotNo == 1) {
				throw new BizException(
						OmpAppContext.getMessage("board111_cannot_delete"));
			}
		} else if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			XBizField boardTypeField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_BDTYPE);
			// 单板表中必须有一块BBU板，且架框槽必须是1,1,1
			int boardType = Integer.valueOf(boardTypeField.getValue());
			if (rackNo == 1 && shelfNo == 1 && slotNo == 1) {
				if (boardType != EnbConstantUtils.BOARD_TYPE_BBU) {
					throw newBizException(boardTypeField.getName(),
							"board_111_must_be_bbu");
				}
			}
		}

	}

	/**
	 * 拓扑表校验
	 * 
	 * @param moId
	 * @param topoRecord
	 * @param actionType
	 * @throws Exception
	 */
	private void checkTopoRecord(long moId, XBizRecord topoRecord,
			String actionType) throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			// 获取主板架框槽
			int[] mainArray = getRackShelfSlotNoOfTopoRecord(topoRecord, true);
			// 获取从板架框槽
			int[] slaveArray = getRackShelfSlotNoOfTopoRecord(topoRecord, false);
			// 主架框槽和从架框槽不能相同
			if (mainArray[0] == slaveArray[0] && mainArray[1] == slaveArray[1]
					&& mainArray[2] == slaveArray[2]) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_MRACKNO,
						"topo_record_main_subordinate_cannot_equal");
			}

			// // 判断主单板是否是BBU
			// int mainBoardType = getBoardType(moId, mainArray[0],
			// mainArray[1],
			// mainArray[2]);
			// if (mainBoardType != EnbConstantUtils.BOARD_TYPE_BBU) {
			// throw newBizException(EnbConstantUtils.FIELD_NAME_MRACKNO,
			// "topo_record_main_board_must_be_bbu");
			// }
			// 判断从单板是否是RRU
			int slaveBoardType = getBoardType(moId, slaveArray[0],
					slaveArray[1], slaveArray[2]);
			if (slaveBoardType != EnbConstantUtils.BOARD_TYPE_RRU) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_SRACKNO,
						"topo_record_slave_board_must_be_rru");
			}

			String currentFiberPort = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();
			String currentTopoNo = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			List<XBizRecord> records = getXBizRecordByMoId(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, null);
			if (records == null)
				return;
			for (XBizRecord record : records) {
				String topoNo = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
				if (currentTopoNo.equals(topoNo))
					continue;
				// 获取主板架框槽
				int[] mArray = getRackShelfSlotNoOfTopoRecord(record, true);
				// 获取从板架框槽
				int[] sArray = getRackShelfSlotNoOfTopoRecord(record, false);
				String fiberPort = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();

				// 光口号是否被占用
				if (currentFiberPort.equals(fiberPort)) {
					throw newBizException(
							EnbConstantUtils.FIELD_NAME_FIBER_PORT,
							"fiberport_already_used");
				} else {
					// 光口号不同时，从板必须不同
					if (slaveArray[0] == sArray[0]
							&& slaveArray[1] == sArray[1]
							&& slaveArray[2] == sArray[2]) {
						throw newBizException(
								EnbConstantUtils.FIELD_NAME_SRACKNO,
								"different_fiberport_must_connect_different_board");
					}
				}
				if (mainArray[0] == mArray[0] && mainArray[1] == mArray[1]
						&& mainArray[2] == mArray[2]
						&& currentFiberPort.equals(fiberPort)) {
					throw newBizException(EnbConstantUtils.FIELD_NAME_MRACKNO,
							"topo_record_main_fiberport_cannot_equal");
				}

			}

		}
	}

	/**
	 * 获取单板类型
	 * 
	 * @param moId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @return
	 * @throws Exception
	 */
	private int getBoardType(long moId, int rackNo, int shelfNo, int slotNo)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_RACKNO,
				String.valueOf(rackNo)));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SHELFNO,
				String.valueOf(shelfNo)));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SLOTNO,
				String.valueOf(slotNo)));
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_BOARD, condition);
		if (records == null) {
			String boardId = rackNo + "-" + shelfNo + "-" + slotNo;
			throw new Exception(boardId
					+ OmpAppContext.getMessage("board_not_exists"));
		}
		XBizField boardTypeField = records.get(0).getFieldBy(
				EnbConstantUtils.FIELD_NAME_BDTYPE);
		return Integer.valueOf(boardTypeField.getValue());
	}

	/**
	 * 校验以太网参数表数据
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	private void checkEthenetRecord(long moId, XBizRecord record,
			String actionType) throws Exception {
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_ETHPARA, null);
		if (records != null && !records.isEmpty()) {
			if (actionType.equals(ActionTypeDD.ADD)) {
				// 校验架、框、槽、端口号的组合是否重复
				checkEthenetRecordDuplicated(record, records);
			} else if (actionType.equals(ActionTypeDD.MODIFY)) {
				// 与网管表相关联的以太网参数表中架框槽必须保持1-1-1不变
				checkEthenetRelatedOmcRecord(moId, record);
				int index = getEthenetRecordIndex(record, records);
				if (index != -1) {
					records.remove(index);
				}
				// 校验架、框、槽、端口号的组合是否重复
				checkEthenetRecordDuplicated(record, records);
			}
		}

	}

	/**
	 * 与网管表相关联的以太网参数表中架框槽必须保持1-1-1不变
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 */
	private void checkEthenetRelatedOmcRecord(long moId, XBizRecord record)
			throws Exception {
		// 查找与网管表相关的以太网参数记录
		XBizRecord ethRecord = queryRelatedEthernetRecord(moId);
		if (ethRecord == null)
			return;
		XBizField portIdFieldOfEth = ethRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portIdOfEth = Integer.valueOf(portIdFieldOfEth.getValue());
		XBizField portIdField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		// 判断是否是与网管表相关联的记录
		if (portId == portIdOfEth) {
			// 与网管表相关联的以太网参数表中的架框槽必须保持1,1,1不变
			int[] noArray = getRackShelfSlotNo(record);
			boolean unchanged = noArray[0] == 1 && noArray[1] == 1
					&& noArray[2] == 1;
			if (!unchanged) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_RACKNO,
						"port_config_of_related_record_in_ipv4_must_be_111");
			}
		}
	}

	/**
	 * 获取以太网参数表记录在列表中的索引
	 * 
	 * @param record
	 * @param records
	 * @return
	 */
	private int getEthenetRecordIndex(XBizRecord record,
			List<XBizRecord> records) {
		String currentPortId = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PORT_ID).getValue();
		int index = -1;
		for (int i = 0; i < records.size(); i++) {
			String portId = records.get(i)
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID).getValue();
			if (currentPortId.equals(portId)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 校验架、框、槽、端口号的组合是否重复
	 * 
	 * @param record
	 * @param records
	 * @throws Exception
	 */
	private void checkEthenetRecordDuplicated(XBizRecord record,
			List<XBizRecord> records) throws Exception {
		int[] array = getRackShelfSlotNo(record);
		String currentPort = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ETH_PORT).getValue();
		String currentUnion = array[0] + splitFlag + array[1] + splitFlag
				+ array[2] + splitFlag + currentPort;
		for (XBizRecord bizRecord : records) {
			int[] noArray = getRackShelfSlotNo(bizRecord);
			String port = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_ETH_PORT).getValue();
			String union = noArray[0] + splitFlag + noArray[1] + splitFlag
					+ noArray[2] + splitFlag + port;
			if (currentUnion.equals(union)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_PORT_ID,
						"rack_shelf_slot_port_cannot_duplicated");
			}
		}
	}

	/**
	 * 校验IPV4表数据
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	private void checkIpv4Record(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		long moId = enb.getMoId();
		XBizField ipIdField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ID);
		int ipId = Integer.valueOf(ipIdField.getValue());
		if (actionType.equals(ActionTypeDD.ADD)) {
			// 网关与IP必须在同一网段
			String newIp = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
			String mask = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			String gateway = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();
			// 网关地址必须与IP地址在同一网段
			if (!checkSameNetFragment(newIp, mask, gateway)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_GATEWAY,
						"gateway_must_same_net_with_ip");
			}
			// 校验添加或修改的IP在当前系统中是否存在
			checkIpDuplicated(enb.getMoId(), record);

		} else if (actionType.equals(ActionTypeDD.DELETE)) {
			// 与网管表相关联的IPV4表中的记录不可删除
			XBizRecord omcRecord = queryOmcRecord(moId);
			if (omcRecord != null) {
				XBizField ipIdFieldOfOmc = omcRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
				int ipIdOfOmc = Integer.valueOf(ipIdFieldOfOmc.getValue());
				if (ipId == ipIdOfOmc) {
					throw new BizException(
							OmpAppContext
									.getMessage("record_related_with_omc_cannot_delete"));
				}
			}
		} else if (actionType.equals(ActionTypeDD.MODIFY)) {
			XBizRecord recordInDB = queryIpv4Record(moId, ipId);
			// IPv4表中的记录不可修改
			if (!recordInDB.equals(record)) {
				throw new Exception(
						OmpAppContext
								.getMessage("record_of_ipv4_cannot_change"));
			}
			// 校验添加或修改的IP在当前系统中是否存在
			checkIpDuplicated(enb.getMoId(), record);
			// // 与网管表相关联的IPV4表中端口标识不可改变
			// XBizRecord ipv4Record = queryOmcRecord(moId);
			// if (ipv4Record != null) {
			// XBizField portIdFieldNew = record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ID);
			// XBizField portIdField = ipv4Record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
			// int portId = Integer.valueOf(portIdField.getValue());
			// int portIdNew = Integer.valueOf(portIdFieldNew.getValue());
			// if (portId == portIdNew) {
			// // 查询库中的端口标识:根据Ip标识查询端口标识相互比较
			// XBizRecord oldIpV4Record = queryIpv4Record(moId, portIdNew);
			// if (oldIpV4Record != null) {
			// // 库中旧的端口号
			// XBizField oldU8PortID = oldIpV4Record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
			// int oldU8PortIDInt = Integer.valueOf(oldU8PortID
			// .getValue());
			// // 传递过来的端口号
			// XBizField newU8PortID = record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
			// int newU8PortIDInt = Integer.valueOf(newU8PortID
			// .getValue());
			// if (oldU8PortIDInt != newU8PortIDInt) {
			//
			// throw newBizException(
			// portIdFieldNew.getName()
			// ,
			// + OmpAppContext
			// .getMessage("portId_of_record_related_with_omc_cannot_change"));
			// }
			// }
			//
			// }
			// }
		}

	}

	/**
	 * 校验添加或修改的IP在当前系统中是否存在
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkIpDuplicated(long moId, XBizRecord bizRecord)
			throws Exception {

		String currentIpId = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_IP_ID).getValue();

		List<Enb> enbList = EnbCache.getInstance().queryAll();
		Set<String> ipSet = new HashSet<String>();
		// 获取系统中所有基站ip
		for (Enb enb : enbList) {
			List<XBizRecord> records = getXBizRecordByMoId(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_IPV4, null);
			if (records == null || records.isEmpty())
				continue;
			for (XBizRecord record : records) {
				// 跳过当前站当前修改的记录
				if (enb.getMoId() == moId) {
					String ipId = record.getFieldBy(
							EnbConstantUtils.FIELD_NAME_IP_ID).getValue();
					if (ipId.equals(currentIpId))
						continue;
				}
				String ip = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
				ipSet.add(ip);
			}
		}

		String currentIp = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
		// ip已使用，请更换
		if (ipSet.contains(currentIp)) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_IP_ADDR,
					"ip_occupied_please_change");
		}
	}

	/**
	 * 判断目标IP是否和指定IP在同一网段
	 * 
	 * @param ip
	 * @param mask
	 * @param targetIp
	 * @return
	 */
	private boolean checkSameNetFragment(String ip, String mask, String targetIp) {

		long ipInteger = Long.valueOf(ip, 16);
		long nextHopInteger = Long.valueOf(targetIp, 16);
		long maskInteger = Long.valueOf(mask, 16);
		// IP地址与掩码等于网段
		long netSeg = ipInteger & maskInteger;
		long nextSeg = nextHopInteger & maskInteger;
		return netSeg == nextSeg;
	}

	/**
	 * 静态路由表记录校验下一跳地址必须跟本机地址在同一网段
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	private void checkStroutRecord(long moId, XBizRecord record,
			String actionType) throws Exception {
		String nextHop = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NEXT_HOP).getValue();
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, null);
		// 必须先添加IPv4表记录
		if (records == null) {
			throw new Exception(
					OmpAppContext.getMessage("please_add_ipv4_record_first"));
		}
		String ip = records.get(0)
				.getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
		String mask = records.get(0)
				.getFieldBy(EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();

		if (!checkSameNetFragment(ip, mask, nextHop)) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_NEXT_HOP,
					"strout_nexthop_must_same_net_with_ipv4");
		}

	}

	/**
	 * 校验网管表数据
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkOmcRecord(Enb enb, XBizRecord bizRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			long moId = enb.getMoId();
			// 与网管表相关联的IPV4表中记录的端口配置中，机架号、机框号、槽位号必须为1-1-1
			XBizField ipIdField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
			int ipId = Integer.valueOf(ipIdField.getValue());
			XBizRecord ipv4Record = queryIpv4Record(moId, ipId);
			// IPV4中与网管表相关联记录不存在
			if (ipv4Record == null) {
				throw newBizException(ipIdField.getName(),
						"related_record_in_ipv4_not_exist");
			}
			XBizField portIdField = ipv4Record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
			int portId = Integer.valueOf(portIdField.getValue());
			XBizRecord ethRecord = queryEthernetRecord(moId, portId);

			int[] noArray = getRackShelfSlotNo(ethRecord);
			int rackNo = noArray[0];
			int shelfNo = noArray[1];
			int slotNo = noArray[2];
			// 如果相关联的记录中架框槽不是1,1,1
			boolean ok = rackNo == 1 && shelfNo == 1 && slotNo == 1;
			if (!ok) {
				throw newBizException(ipIdField.getName(),
						"port_config_of_related_record_in_ipv4_must_be_111");
			}
		}
	}

	/**
	 * 校验业务功控参数表记录
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param actionType
	 * @throws Exception
	 */
	private void checkEnbSRVPCRecord(long moId, XBizRecord bizRecord,
			String actionType) throws Exception {
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_DLPC, null);
		// 小区下行功控参数表中无记录,直接返回
		if (records == null)
			return;
		int minPAForDTCH = 100;
		for (XBizRecord xBizRecord : records) {
			int ptch = Integer.valueOf(xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH).getValue());
			if (ptch < minPAForDTCH) {
				minPAForDTCH = ptch;
			}
		}
		// u8PA的值不能超过T_CEL_DLPC表中所有记录中u8PAForDTCH字段的最小值
		XBizField paField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PA);
		int pa = Integer.valueOf(paField.getValue());
		if (pa > minPAForDTCH) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_PA,
					"u8PA_cannot_larger_than_min_u8PAForDTCH");
		}
	}

	/**
	 * 校验环境监控表记录
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param actionType
	 * @throws Exception
	 */
	private void checkENVMONRecord(long moId, XBizRecord bizRecord,
			String actionType) throws Exception {
		List<XBizRecord> records = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_ENVMON, null);
		if (records == null)
			return;
		// 架、框、槽、环境监控类型的组合不能重复
		int[] array = getRackShelfSlotNo(bizRecord);
		String currentNo = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENV_M_NO).getValue();
		String currentType = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENV_M_TYPE).getValue();
		String currentUnion = array[0] + splitFlag + array[1] + splitFlag
				+ array[2] + splitFlag + currentType;
		for (XBizRecord record : records) {
			String no = record.getFieldBy(EnbConstantUtils.FIELD_NAME_ENV_M_NO)
					.getValue();
			if (no.equals(currentNo))
				continue;
			int[] noArray = getRackShelfSlotNo(record);
			String type = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_ENV_M_TYPE).getValue();
			String union = noArray[0] + splitFlag + noArray[1] + splitFlag
					+ noArray[2] + splitFlag + type;
			if (currentUnion.equals(union)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_ENV_M_TYPE,
						"rack_shelf_slot_envmtype_cannot_duplicated");
			}
		}

	}

	/**
	 * 查询与网管表相关联的以太网参数表中的记录
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryRelatedEthernetRecord(long moId) throws Exception {
		XBizRecord omcRecord = queryOmcRecord(moId);
		if (omcRecord == null)
			return null;
		XBizField ipIdFieldOfOmc = omcRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
		int ipId = Integer.valueOf(ipIdFieldOfOmc.getValue());
		XBizRecord ipv4Record = queryIpv4Record(moId, ipId);
		// IPV4中与网管表相关联记录不存在
		if (ipv4Record == null) {
			return null;
		}
		XBizField portIdField = ipv4Record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		return queryEthernetRecord(moId, portId);
	}

	/**
	 * 查询IPV4中IP标识为ipId的记录
	 * 
	 * @param moId
	 * @param ipId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryIpv4Record(long moId, int ipId) throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_IP_ID,
				String.valueOf(ipId)));
		XBizTable ipv4Table = queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, condition);
		if (ipv4Table != null && !ipv4Table.getRecords().isEmpty()) {
			return ipv4Table.getRecords().get(0);
		}
		return null;
	}

	/**
	 * 查询网管表中记录
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryOmcRecord(long moId) throws Exception {
		XBizTable omcTable = queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_OMC, null);
		if (omcTable != null && !omcTable.getRecords().isEmpty()) {
			return omcTable.getRecords().get(0);
		}
		return null;
	}

	/**
	 * 查询eNB参数表中记录
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryEnbParamRecord(long moId) throws Exception {
		XBizTable enbParamTable = queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_PARA, null);
		if (enbParamTable != null && !enbParamTable.getRecords().isEmpty()) {
			return enbParamTable.getRecords().get(0);
		}
		return null;
	}

	/**
	 * 查询以太网参数表中端口标识为portId的记录
	 * 
	 * @param moId
	 * @param portId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryEthernetRecord(long moId, int portId)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_PORT_ID,
				String.valueOf(portId)));
		XBizTable ethTable = queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_ETHPARA, condition);
		if (ethTable != null && !ethTable.getRecords().isEmpty()) {
			return ethTable.getRecords().get(0);
		}
		return null;
	}

	/**
	 * 从单板表记录中获取架、框、槽号
	 * 
	 * @param bizRecord
	 * @return
	 */
	private int[] getRackShelfSlotNo(XBizRecord bizRecord) {
		XBizField rackNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		XBizField shelfNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		XBizField slotNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SLOTNO);
		int rackNo = Integer.valueOf(rackNoField.getValue());
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		int slotNo = Integer.valueOf(slotNoField.getValue());
		return new int[] { rackNo, shelfNo, slotNo };
	}

	/**
	 * 获取单个十六进制字符串代表的二进制
	 * 
	 * @param numberChar
	 * @return
	 */
	private String getBinaryArray(String hexNumChar) {
		int number = Integer.valueOf(hexNumChar, 16);
		if (number == 0) {
			return "0000";
		} else if (number == 1) {
			return "0001";
		} else if (number == 2) {
			return "0010";
		} else if (number == 3) {
			return "0011";
		} else if (number == 4) {
			return "0100";
		} else if (number == 5) {
			return "0101";
		} else if (number == 6) {
			return "0110";
		} else if (number == 7) {
			return "0111";
		} else if (number == 8) {
			return "1000";
		} else if (number == 9) {
			return "1001";
		} else if (number == 10) {
			return "1010";
		} else if (number == 11) {
			return "1011";
		} else if (number == 12) {
			return "1100";
		} else if (number == 13) {
			return "1101";
		} else if (number == 14) {
			return "1110";
		} else if (number == 15) {
			return "1111";
		}
		return "";
	}

	private BizException newBizException(String fieldName, String message) {
		return new BizException(fieldName + splitFlag
				+ OmpAppContext.getMessage(message));
	}

	private XBizTable queryFromEms(long moId, String tableName,
			XBizRecord condition) throws Exception {
		return enbBizConfigDAO.query(moId, tableName, condition);
	}

	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}

	public EnbBizConfigDAO getEnbBizConfigDAO() {
		return enbBizConfigDAO;
	}

}
