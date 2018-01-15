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
 * eNBҵ������У�鹤����
 * 
 * @author fanhaoyu
 * 
 */
@Deprecated
public class EnbBizDataValidator {

	private static final String splitFlag = "#";
	/**
	 * PRACH����u8PrachCfgIndex��С�������֡��ȹ����󲻿�ѡ�е��ֶΣ�����֡���Ϊ0ʱ������ѡ��11,19,��������ݿ�����ļ�
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
	// Ƶ��ָʾ
	// public static final int[] FREQ_BAND = { 33, 34, 35, 36, 37, 38, 39, 40,
	// 61,
	// 62 };
	// 33:1900-1920, 34:2010-2025, 35:1850-1910, 36:1930-1990, 37:1910-1930,
	// 38:2570-2620, 39:1880-1920, 40:2300-2400MHZ��61:1447-1467��62:1785-1805
	// public static final int[][] FREQ_RANGE = { { 1900, 1920 }, { 2010, 2025
	// },
	// { 1850, 1910 }, { 1930, 1990 }, { 1910, 1930 }, { 2570, 2620 },
	// { 1880, 1920 }, { 2300, 2400 }, { 1447, 1467 }, { 1785, 1805 } };

	private EnbBizConfigDAO enbBizConfigDAO;

	/**
	 * ��֤eNB�Ƿ��ѿ�վ
	 * 
	 * @param enb
	 * @return
	 */
	public boolean checkEnbActive(Enb enb) {
		try {
			// ������ܱ������ݣ���˵�����ܡ����򡢵��塢��̫��������IPV4���ж��Ѿ�������
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

		// eNB������״̬�²��ɶԿ�վ������������
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// ��֤��¼�Ƿ��ظ����
		checkRecordDuplicated(enb.getMoId(), tableName, bizRecord);

		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_RACK)) {
			// �����������ظ�
			checkRackNameDuplicated(enb.getMoId(), bizRecord);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// ������б�����һ��BBU�壬�Ҽܿ�۱�����1,1,1
			checkBoardRecord(bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_OMC)) {
			// �����ܱ��������IPV4���¼������̫����������صļ�¼�мܿ�۱�����1,1,1
			checkOmcRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// IPv4�����¼�е�IP��ַ���벻ͬ
			checkIpv4Record(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_TOPO)) {
			// ���˱�У��
			checkTopoRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			checkEthenetRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_STROUT)) {
			// ��̬·�ɱ�У��
			checkStroutRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// С��������У��
			checkCPARARecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CELL_PUCH))) {
			// С�����������ŵ����ò�����У��
			checkCPUCHRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_PRACH))) {
			// PRACH�������ñ����У��
			checkCPRACHRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_ALG))) {
			// С���㷨������У��
			checkCALGRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ))) {
			// ������Ƶ���ñ�У��
			checkCTFREQRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_DLPC))) {
			// С�����й��ز�����
			checkDLPCRecord(enb, bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL))) {
			// ������ϵ�������ñ�У��
			checkCNBRCELRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PTT)) {
			// С����Ⱥ���ò���
			checkCPTTRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SISCH)) {
			// SI�ĵ������ò�����
			checkCellSISCHRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			// ������ر�
			checkENVMONRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_SRVPC)) {
			// ҵ�񹦿ز�����
			checkEnbSRVPCRecord(enb.getMoId(), bizRecord, ActionTypeDD.ADD);
		}
	}

	public void checkDelete(Enb enb, String tableName, XBizRecord bizKey)
			throws Exception {
		// eNB������״̬�²��ɶԿ�վ������������
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// ���Լ��У��
		checkReference(enb, tableName, bizKey);

		// ���eNB�Ѿ���վ��1�Ż��ܲ���ɾ����1�Ż��򲻿�ɾ����1-1-1���岻��ɾ��
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
		// eNB������״̬�²��ɶԿ�վ������������
		// if (enb.isConnected()) {
		// if (EnbBizHelper.isActiveRelatedTable(tableName)) {
		// throw new Exception(
		// OmpAppContext
		// .getMessage("cannot_config_active_data_while_enb_connected"));
		// }
		// }

		// ��֤��¼�Ƿ����
		if (!isRecordExist(enb.getMoId(), tableName, record)) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}

		// // ��������޸�
		// checkForeignKeyReference(enb, tableName, record);

		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_RACK)) {
			// �����������ظ�
			checkRackNameDuplicated(enb.getMoId(), record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// ������б�����һ��BBU�壬�Ҽܿ�۱�����1,1,1
			checkBoardRecord(record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_TOPO)) {
			// ���˱�У��
			checkTopoRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_OMC)) {
			checkOmcRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// �����ܱ��������IPV4���еļ�¼�Ķ˿ڱ�ʶ�����޸�
			checkIpv4Record(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ETHPARA)) {
			// �����ܱ��������IPV4���еļ�¼�Ķ˿ڱ�ʶ�ļܿ�۱��뱣��1,1,1����
			checkEthenetRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_STROUT)) {
			// ��̬·�ɱ�У��
			checkStroutRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CELL_PARA)) {
			// С��������У��
			checkCPARARecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CELL_PUCH))) {
			// С�����������ŵ����ò�����У��
			checkCPUCHRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_PRACH))) {
			// PRACH�������ñ����У��
			checkCPRACHRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_ALG))) {
			// С���㷨������У��
			checkCALGRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ))) {
			// ������Ƶ���ñ�У��
			checkCTFREQRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_DLPC))) {
			// С�����й��ز�����
			checkDLPCRecord(enb, record, ActionTypeDD.MODIFY);
		} else if (tableName.equals((EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL))) {
			// ������ϵ�������ñ�У��
			checkCNBRCELRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_PTT)) {
			// С����Ⱥ���ò���
			checkCPTTRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SISCH)) {
			// SI�ĵ������ò�����
			checkCellSISCHRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENVMON)) {
			// ������ر�
			checkENVMONRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_SRVPC)) {
			// ҵ�񹦿ز�����
			checkEnbSRVPCRecord(enb.getMoId(), record, ActionTypeDD.MODIFY);
		}
	}

	/**
	 * ��¼�Ƿ����
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 * @return
	 * @throws Exception
	 */
	public boolean isRecordExist(long moId, String tableName,
			XBizRecord bizRecord) throws Exception {
		// ��ȡ����
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId, tableName,
				bizRecord);
		// �������ݿ��¼
		XBizRecord recordInDb = enbBizConfigDAO.queryByKey(moId, tableName,
				bizKey);
		return recordInDb != null;
	}

	/**
	 * У���¼�Ƿ��ظ����
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
	 * У����������Ƿ��ظ���ͬһ��վ�»������Ʋ�����ͬ
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
	 * ������ò����޸ĵ�У��
	 * 
	 * @param enb
	 * @param tableName
	 * @param record
	 * @throws Exception
	 */
	private void checkForeignKeyReference(Enb enb, String tableName,
			XBizRecord record) throws Exception {
		String protocolVersion = enb.getProtocolVersion();
		// ��ѯ���иü�¼
		XBizRecord condition = EnbBizHelper.getKeyRecordBy(enb.getEnbType(),
				protocolVersion, tableName, record);
		XBizRecord dbRecord = getXBizRecordByMoId(enb.getMoId(), tableName,
				condition).get(0);
		XList tableMeta = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				protocolVersion, tableName);
		for (XList fieldMeta : tableMeta.getAllFields()) {
			String fieldName = fieldMeta.getName();
			// ��������
			if (fieldMeta.isRef()) {
				XBizField dbField = dbRecord.getFieldBy(fieldName);
				XBizField field = record.getFieldBy(fieldName);
				// ��������ֵ�ı�
				if (!dbField.equals(field)) {
					throw newBizException(fieldName,
							"foreign_key_cannot_modify");
				}
			}
		}
	}

	/**
	 * У���Ƿ����������ô˼�¼
	 * 
	 * @param enb
	 * @param tableName
	 * @param record
	 * @throws Exception
	 */
	public void checkReference(Enb enb, String tableName, XBizRecord record)
			throws Exception {
		// ���������У��
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_BOARD)) {
			// У�鵥����¼�Ƿ����˱�����
			checkBoardReference(enb, record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG)) {
			// �������ñ��Ƿ�С������������
			checkMeascfgReference(enb, record);
		} else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
			// IPv4���Ƿ����������Э�������
			checkIpv4Reference(enb, record);
		}
		// ͨ��У���߼�
		XCollection collection = EnbBizHelper.getBizMetaBy(enb.getEnbType(),
				enb.getProtocolVersion());
		Map<String, XList> bizMap = collection.getBizMap();
		for (String table : bizMap.keySet()) {
			XList tableConfig = bizMap.get(table);
			List<XList> fields = tableConfig.getAllFields();
			// ���������ֶΣ��ҳ���Ҫɾ���ı������ù�ϵ���ֶ�
			List<String> refFields = new ArrayList<String>();
			for (XList field : fields) {
				List<String> refTables = field.getFieldRefTables();
				if (field.containsRef()) {
					// �鿴���øñ�ı����Ƿ�������øü�¼�ļ�¼
					if (refTables.contains(tableName)) {
						List<XMetaRef> refList = field.getFieldRefs();
						refFields.add(field.getName() + "#"
								+ refList.get(0).getKeyColumn());
					}
				}
			}
			// �����Ҫɾ���ı������ù�ϵ
			if (!refFields.isEmpty()) {
				XBizRecord condition = new XBizRecord();
				// �����������������Ե�ֵ��Ҫɾ�����еļ�ֵ��������ɾ��
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
	 * У��IPv4���¼�Ƿ������ƴ���Э���������
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
	 * У������������Ƿ��Ƿ������������ã�С��������
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
	 * ��ָ֤����Id�Ƿ��������ָ���ֶ�������
	 * 
	 * @param enb
	 * @param targetId
	 *            Ҫɾ��������
	 * @param tableName
	 *            ����Ŀ��Id�ı�
	 * @param refFieldNames
	 *            Ӧ��Ŀ��Id���ֶ�
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
					// ������������ֶΣ������У��
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
	 * У�鱻ɾ���ĵ�����¼�Ƿ����˱�������
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
		// ������еļܿ��
		int[] noArray = getRackShelfSlotNo(record);
		for (XBizRecord topoRecord : topoRecords) {
			// ��ȡ����ܿ��
			int[] mainArray = getRackShelfSlotNoOfTopoRecord(topoRecord, true);
			// ��ȡ�Ӱ�ܿ��
			int[] sArray = getRackShelfSlotNoOfTopoRecord(topoRecord, false);
			// �Ƿ���������
			boolean referenced1 = noArray[0] == mainArray[0]
					&& noArray[1] == mainArray[1] && noArray[2] == mainArray[2];
			// �Ƿ񱻴Ӱ�����
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
	 * ��ȡ���˱��¼�еļܿ��
	 * 
	 * @param bizRecord
	 * @param main
	 *            ���廹�ǴӰ�
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
	 * С���������У�� ����С�������������Լ����
	 * u8IntraFreqHOMeasCfg���ֶ���Ҫ����T_ENB_MEASCFG���¼��u8EvtId����������ݿ����
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
			// ������޸ģ������Ƴ���ǰ��¼
			if (actionType.equals(ActionTypeDD.MODIFY)) {
				int index = getCellIndex(record, records);
				records.remove(index);
			}
			// ͬ��վ��С�����Ʋ�����ͬ
			checkCellNameDuplicated(record, records);
			// ͬƵС����PCI������ͬ
			checkPCI(record, records);
			// ��ͬС�����õ����˱��¼������ͬ
			checkTopoIdOfCellPara(record, records);
		}

		// С�����ڽ����������޸ĵ�ʱ����Ҫ���������ñ��е��¼���־
		// С����������ͬƵ���ڲ�����������
		XBizField intraFreqPrdMeasCfg = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG);
		if (intraFreqPrdMeasCfg == null) {
			// Ӳ�������汾����
			intraFreqPrdMeasCfg = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_INTRA_FREQ_PRD_MEAS_CFG_16);
		}

		// ��װ��ѯ�����ڲ������ñ��в���
		XBizRecord condition = new XBizRecord();
		XBizField tempCondition = new XBizField(
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX,
				intraFreqPrdMeasCfg.getValue());
		condition.addField(tempCondition);

		// ��ȡT_ENB_MEASCFG���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, condition);
		// У��u16IntraFreqPrdMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// �������������
					intraFreqPrdMeasCfg.getName(),
					"IntraFreqPrdMeasCfg_can_not_find_in_t_enb_meascfg");

		}
		// У��u8IntraFreqHOMeasCfg,��ֵ��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG, 3);
		// У��u16IntraFreqPrdMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// �������������
					EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG,
					"u8IntraFreqHOMeasCfg_in_t_enb_meascfg_must_3");

		}

		// У��u8A2ForInterFreqMeasCfg,��ֵ��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG, 2);
		// У��u8A2ForInterFreqMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// �������������
					EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG,
					"u8A2ForInterFreqMeasCfg_in_t_enb_meascfg_must_2");

		}

		// У��u8A1ForInterFreqMeasCfg,��ֵ��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG, 1);
		// У��u8A1ForInterFreqMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// �������������
					EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG,
					"u8A1ForInterFreqMeasCfg_in_t_enb_meascfg_must_1");

		}

		// У��u8A2ForRedirectMeasCfg,��ֵ��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
		bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG, 2);
		// У��u8A2ForRedirectMeasCfg
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					// �������������
					EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG,
					"u8A2ForRedirectMeasCfg_in_t_enb_meascfg_must_2");

		}
	}

	/**
	 * У��С�������Ƿ��ظ���ͬһ��վ��С�����Ʋ�����ͬ
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
	 * ��ͬС�����õ����˱��¼������ͬ
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
	 * ͬƵС����PCI������ͬ
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
	 * ��ѯ������Ĳ������ñ��еļ�¼
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
	 * С����Ⱥ���ò������� ���㣺T_CEL_PARA��u8UlDlSlotAlloc����Ϊ0ʱ����ֵ��ȡ0��5��
	 * T_CEL_PARA��u8UlDlSlotAlloc����Ϊ1ʱ
	 * ����ֵ��ȡ0��4��5��9��T_CEL_PARA��u8UlDlSlotAlloc����Ϊ2ʱ
	 * ����ֵ��ȡ0��3��4��5��8��9��T_CEL_PARA��u8UlDlSlotAlloc����Ϊ3ʱ
	 * ����ֵ��ȡ0��5��6��7��8��9��T_CEL_PARA��u8UlDlSlotAlloc����Ϊ4ʱ
	 * ����ֵ��ȡ0��4��5��6��7��8��9��T_CEL_PARA��u8UlDlSlotAlloc����Ϊ5ʱ
	 * ����ֵ��ȡ0��3��4��5��6��7��8��9��T_CEL_PARA��u8UlDlSlotAlloc����Ϊ6ʱ����ֵ��ȡ0��5��9
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCPTTRecord(Long moId, XBizRecord record, String actionType)
			throws Exception {
		// ��Ⱥ�㲥Ѱ��֡�ű���С�ڼ�Ⱥ�㲥Ѱ������
		checkPttBPagingFN(record);

		// ���ݸ���С����ʶ����ȡС����������XBizField�ļ���
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext.getMessage("relate_cell_parm_is_not_exist"));

		} else {
			XBizRecord cParmRecord = bizRecords.get(0);
			// С������������֡���
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
	 * ��Ⱥ�㲥Ѱ��֡�ű���С�ڼ�Ⱥ�㲥Ѱ������
	 * 
	 * @param record
	 * @throws BizException
	 */
	private void checkPttBPagingFN(XBizRecord record) throws BizException {
		// ��Ⱥ�㲥Ѱ��֡�ű���С�ڼ�Ⱥ�㲥Ѱ�����ڣ���u8PttBPagingFN < 2^(u8PttBPagingSycle + 1)
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
	 * ������ϵ�������ñ�У��
	 * T_CEL_NBRCEL����u8CenterFreqCfgIdx�����Ϊ255������Ҫ����T_ENB_CTFREQ����һ����¼��Ӧ
	 * ����Ӧ��ϵ��u8CenterFreqCfgIdx����T_ENB_CTFREQ���е�����u8CfgIdx
	 * T_CEL_NBRCEL����u8SvrCID��ͬ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������ȣ�
	 * T_CEL_NBRCEL����u8SvrCID��T_CEL_PARA���е�u8CId�����u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������T_CEL_PARA��Ӧ��¼�е�u16PhyCellId�ֶ����
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCNBRCELRecord(Long moId, XBizRecord record,
			String actionType) throws Exception {

		// NBRCEL����u8SvrCID
		XBizField u8SvrCID = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SVR_CID);
		XBizRecord condition = new XBizRecord();
		XBizField u8CId = new XBizField();
		u8CId.setName(EnbConstantUtils.FIELD_NAME_CELL_ID);
		u8CId.setValue(u8SvrCID.getValue());
		condition.addField(u8CId);

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> cPARAbizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (cPARAbizRecords == null || cPARAbizRecords.size() == 0) {
			throw newBizException(u8SvrCID.getName(),
					"relate_cell_parm_is_not_exist");

		} else {
			XBizRecord cParmRecord = cPARAbizRecords.get(0);

			// С���������е�u16PhyCellId
			XBizField u16PhyCellIdFromCParm = cParmRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			int u16PhyCellIdFromCParmInt = Integer
					.valueOf(u16PhyCellIdFromCParm.getValue());

			// NBRCEL����u16PhyCellId
			XBizField u16PhyCellId = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
			int u16PhyCellIdNBRCELInt = Integer
					.valueOf(u16PhyCellId.getValue());

			// NBRCEL����u8CenterFreqCfgIdx
			XBizField u8CenterFreqCfgIdx = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
			Integer u8CenterFreqCfgIdxInt = Integer.valueOf(u8CenterFreqCfgIdx
					.getValue());

			// FIELD_NAME_SVR_CIDΪ255���Ƿ��ж��u8SvrCID/u16PhyCellId����ΪС���������е�u16PhyCellId���
			if (u8CenterFreqCfgIdxInt == 255) {
				if (u16PhyCellIdNBRCELInt == u16PhyCellIdFromCParmInt) {
					throw newBizException(u16PhyCellId.getName(),
							"physics_cell_id_is_not_exist_in_related_cell_parm");
				}
				condition = new XBizRecord();
				condition.addField(u8SvrCID);
				condition.addField(u8CenterFreqCfgIdx);
				condition.addField(u16PhyCellId);

				// ��ѯ��ǰu8SvrCID�����еļ�¼
				List<XBizRecord> nBRCELRecord = getXBizRecordByMoId(moId,
						EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, condition);
				if (nBRCELRecord == null || cPARAbizRecords.size() == 0) {

				} else {
					boolean flag = false;
					if (actionType.equals(ActionTypeDD.MODIFY)) {
						// �޸�У�飺u8SvrCID��ͬ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������ȣ�����Ѿ����ڲ��Ҳ��ǵ�ǰվ��

						if (nBRCELRecord.size() > 1) {
							flag = true;
						} else {
							XBizRecord tempRecord = nBRCELRecord.get(0);
							// ���ݿ�����С����ʶ
							XBizField u32NbreNBID = tempRecord
									.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_ENBID);
							int u32NbreNBIDTempInt = Integer
									.valueOf(u32NbreNBID.getValue());
							// ��������ݹ�������eNB��ʶ
							int u32NbreNBIDInt = Integer
									.valueOf(record
											.getFieldBy(
													EnbConstantUtils.FIELD_NAME_NBR_ENBID)
											.getValue());
							// ���ݿ������ݿ��з���С����ʶ
							XBizField u8NbrCID = tempRecord
									.getFieldBy(EnbConstantUtils.FIELD_NAME_NBR_CID);
							int u8NbrCIDTempInt = Integer.valueOf(u8NbrCID
									.getValue());
							// ��������ݹ����ķ���С����ʶ
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
						// ����У�飺u8SvrCID��ͬ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId�������
						flag = true;
					}
					if (flag) {
						throw newBizException(u16PhyCellId.getName(),
								"nbrcel_table_and_nbtcid_common_and_center_freq_is255_physic_id_duplicate");
					}
				}
			} else {
				// u8CenterFreqCfgIdx��T_ENB_CTFREQ�Ƿ����ӳ���ϵ��
				condition = new XBizRecord();
				condition.addField(new XBizField(
						EnbConstantUtils.FIELD_NAME_CFG_IDX,
						u8CenterFreqCfgIdxInt.toString()));

				// ������Ƶ���ñ������еļ�¼
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
	 * С�����й��ز�����У�飺������С���źŲο����ʵ����ֵ�����¹�ʽ������CRS_EPRE_max = u16CellTransPwr -
	 * 10*log10(T_CEL_PARA.u8DlAntPortNum) +
	 * ERS_nor����ʽ���漰���ֶζ��ǰ���Э��ֵ��P�������㣬����ERS_nor���ɵã���������
	 */

	public void checkDLPCRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		long moId = enb.getMoId();
		// ���ݸ���С����ʶ����ȡС����������XBizField�ļ���
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext.getMessage("relate_cell_parm_is_not_exist"));

		} else {
			XBizRecord cParmRecord = bizRecords.get(0);
			// С����������ϵͳ�����Э��ֵP
			XBizField u8SysBandWidth = cParmRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			int u8SysBandWidthInt = Integer.valueOf(u8SysBandWidth.getValue());

			// ��վ��֧����֮������С����������ϵͳ����Ϊ1.4M��3M
			if (u8SysBandWidthInt == 0 || u8SysBandWidthInt == 1) {
				// ������������������---------��ǰ��վ��֧��1.4M��3M,��ϵͳ����Ϊ1.4M��3Mʱ����վ��Ӧ��С�������������ģ�����Ĭ�ϴ�⼴��

			} else {

				// С����������u8DlAntPortNum[�������߶˿���]
				XBizField u8DlAntPortNum = cParmRecord
						.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
				String u8DlAntPortNumValue = u8DlAntPortNum.getValue();

				// �������߶˿���Э��ֵP����ʵֵ֮��Ĺ�ϵ��0--port1;1--port2;2--port4;
				XList list = EnbBizHelper.getFieldMetaBy(enb.getEnbType(),
						enb.getProtocolVersion(),
						EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
						EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
				// (0)port1|(1)port2|(2)port4
				String enumValue = list.getEnumText();
				// ����Ϊkey--value
				Map<String, String> map = new HashMap<String, String>();
				String[] arrs = enumValue.split("\\|");
				for (int i = 0; i < arrs.length; i++) {
					String temp = arrs[i];
					String key = temp.substring(temp.indexOf("(") + 1,
							temp.indexOf(")"));
					String value = temp.substring(temp.indexOf(")") + 5);
					map.put(key, value);

				}

				// С�������������ж˿�������ʵֵ����1��2��4;
				int u8DlAntPortNumDInt = Integer.valueOf(map
						.get(u8DlAntPortNumValue));

				// С����������ϵͳ����Mֵ����0--1.4��
				Map<String, String> mapAsM = getSysBandWidthAsRBorM(enb, u8CId,
						false);

				// ��ȡϵͳ�����Ӧ��Mֵ
				String SysBandWidthDValue = mapAsM.get(u8SysBandWidth
						.getValue());

				// С�����й��ز�����--С��������ܹ���--Э��֮����Ϊdouble
				XBizField u16CellTransPwr = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_TRANS_PWR);
				// u16CellTransPwrDouble��Э��ֵP:D=p*10
				double u16CellTransPwrDouble = Double.valueOf(u16CellTransPwr
						.getValue()) / 10;

				// С�����й��ز�����--С���ο��źŹ���
				XBizField u16CellSpeRefSigPwr = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR);
				int u16CellSpeRefSigPwrPInt = Integer
						.valueOf(u16CellSpeRefSigPwr.getValue()) - 60;
				// С�����й��ز�����--PA
				XBizField u8PAForDTCH = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
				int u8PAForDTCHInt = Integer.valueOf(u8PAForDTCH.getValue());

				// С�����й��ز�����--PB
				XBizField u8PB = record
						.getFieldBy(EnbConstantUtils.FIELD_NAME_PB);
				int u8PBInt = Integer.valueOf(u8PB.getValue());
				double ersNor = 0;

				// u8PAForDTCHInt��u8PBInt��SysBandWidthDValue��ȡersNor
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
				// ERS_nor�Ľ��ֵ��u16CellSpeRefSigPwrPInt�Ƚ�
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
	 * * С�����������ŵ����ò�����-- ��u8N_RB2��PUCCH format 2/2a/2bʹ�õ�RB�� ��u8SRITransPrd
	 * ��SRI�������� ������������£� A.
	 * "u8DeltaPucchShift��u8N_RB2��u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	 * u8N_RB2
	 * +(u16N_SrChn\(36\(u8DeltaPucchShift+1)))���ܳ���u8SysBandWidth��������RB����" B.
	 * 
	 * @param record
	 * @param moId
	 * @param actionType
	 * @throws Exception
	 */
	public void checkCPUCHRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {

		// ��ȡС�����������ŵ����ò����������С�����
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// ��ȡС�������е�ϵͳ����
			XBizField u8SysBandWidth = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			String u8SysBandWidthValue = u8SysBandWidth.getValue();
			int u8SysBandWidthValueInt = Integer.valueOf(map
					.get(u8SysBandWidthValue));
			// ��ȡС�������е���֡���
			XBizField bizFieldUlDlSlotAlloc = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);

			String u8UlDlSlotAlloc = bizFieldUlDlSlotAlloc.getValue();

			// PUCCH format 1/1a/1bѭ��ƫ����
			XBizField u8DeltaPucchShift = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT);
			int u8DeltaPucchShiftValue = Integer.parseInt(u8DeltaPucchShift
					.getValue());
			// ���ڼ����ֵҪ��1
			u8DeltaPucchShiftValue++;
			// PUCCH format 2/2a/2bʹ�õ�RB��
			XBizField u8N_RB2 = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_RB2);
			int u8N_RB2Value = Integer.parseInt(u8N_RB2.getValue());
			// PUCCH SR�ŵ�����
			XBizField u16N_SrChn = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SR_CHN);
			int u16N_SrChnValue = Integer.parseInt(u16N_SrChn.getValue());
			// SRI��������
			XBizField u8SRITransPrd = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SRI_TRANS_PRD);
			int u8SRITransPrdValue = Integer.valueOf(u8SRITransPrd.getValue());
			// PUCCH�ϱ���CQI/PMI������
			XBizField u8RptCqiPrd = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_RPT_CQI_PRD);
			int u8RptCqiPrdValue = Integer.valueOf(u8RptCqiPrd.getValue());
			int temp1 = u16N_SrChnValue / (36 / u8DeltaPucchShiftValue);
			double temp2 = ((double) u16N_SrChnValue)
					/ (double) (36 / u8DeltaPucchShiftValue);
			// u16N_SrChn����Ϊ(36\(u8DeltaPucchShift+1))��������
			if (temp1 != temp2) {
				throw newBizException(u16N_SrChn.getName(),
						"u16nsrchn_must_divisible_by");
			}
			// u8DeltaPucchShift��u8N_RB2��u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift+1)))���ܳ���u8SysBandWidth��������RB����
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
				// С������������֡���Ϊ2ʱ��u8SRITransPrd��u8RptCqiPrd����ͬʱΪ0
				throw newBizException(u8RptCqiPrd.getName(),
						"when_uldlslotalloc_is_2_sritransprd_rptcqiprd_can_not_5");
			}

		}

	}

	/**
	 * ������Ƶ���ñ����У��
	 * <p>
	 * u8InterFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
	 * </p>
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 */
	public void checkCTFREQRecord(Long moId, XBizRecord record,
			String actionType) throws Exception {
		// ��Ƶ�л���������������ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
		List<XBizRecord> bizRecords = queryRelatedMeasureCfg(moId, record,
				EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG, 3);
		// ���ò���������Ӧ�Ĳ������ñ����ݲ�����
		if (bizRecords == null || bizRecords.size() == 0) {
			throw newBizException(
					EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG,
					"interfreqhomeascfg_in_t_enb_meascfg_must_3");
		}
	}

	/**
	 * PRACH�������ñ����У������ u8PrachFreqOffset��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	 * u8PrachFreqOffsetС�ڵ���u8SysBandWidth��������RB������ȥ6
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

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			// ��ȡС�����ñ���ϵͳ�����ڴ�ֵ����ʾֵ�Ĺ�ϵ����
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// ��ȡС�������е�ϵͳ����
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
			// �������߼�������������u16RootSeqIndex��У��,���㣺T_CEL_PARA���u16PhyCellId�ֶα���һ��
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

			// ������u8PrachCfgIndexУ��,����T_CEL_PARA���е��ֶ�u8UlDlSlotAlloc����֡���Ϊ0ʱȡֵ������(11,19)����֡���Ϊ1ʱȡֵ������(8,13,14,40-47)����֡���Ϊ2ʱȡֵ������(5,7,8,11,13,14,17,19-47)����֡���Ϊ3ʱȡֵ������(10,11,19,22,24,32,34,42,44,50,52)����֡���Ϊ4ʱȡֵ������(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)����֡���Ϊ5ʱȡֵ������(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)����֡���Ϊ6ʱȡֵ������(16,17,42,44)
			XBizField u8UlDlSlotAlloc = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
			int u8UlDlSlotAllocInt = Integer
					.valueOf(u8UlDlSlotAlloc.getValue());
			int[] notSelect = PRACH_NOT_SELECT_PARAM[u8UlDlSlotAllocInt];
			// ��ȡu8PrachCfgIndex��ֵ,�ж��Ƿ������notSelect�У����������У��ʧ��
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
	 * С���㷨������У���߼����£� u8UlRbNum����������ȡֵ������2,3,5��������; u8DlRbNum���������� ;
	 * u8UlMaxRbNum 100�����ȡС���ұ�����2,3,5������; u8UlMinRbNum 100�����ȡС���ұ�����2,3,5������
	 * u8DlMaxRbNum100�����ȡС ;u8DlMinRbNum 100�����ȡС ;ȡС��Ϊ�滻ԭ���ֵ
	 */
	public void checkCALGRecord(Enb enb, XBizRecord record, String actionType)
			throws Exception {
		XBizField u8CId = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
		XBizRecord condition = new XBizRecord();
		condition.addField(u8CId);

		// ��ȡT_CEL_PARA���е�XBizRecord
		List<XBizRecord> bizRecords = getXBizRecordByMoId(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, condition);
		if (bizRecords == null || bizRecords.size() == 0) {
			throw new Exception(
					OmpAppContext
							.getMessage("relate_cell_parm_is_not_exist_or_sys_bandwidth_is_not_exist"));
		} else {
			// ��ȡС�����ñ���ϵͳ�����ڴ�ֵ����ʾֵ�Ĺ�ϵ����
			Map<String, String> map = getSysBandWidthAsRBorM(enb, u8CId, true);

			XBizRecord cParaRecord = bizRecords.get(0);
			// ��ȡС�������е�ϵͳ����
			XBizField u8SysBandWidth = cParaRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			String u8SysBandWidthValue = u8SysBandWidth.getValue();
			// ϵͳ�����RBֵ
			int u8SysBandWidthValueInt = Integer.valueOf(map
					.get(u8SysBandWidthValue));
			// ����Ԥ����RB��
			XBizField u8UlRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_RB_NUM);
			int u8UlRbNumInt = Integer.valueOf(u8UlRbNum.getValue());
			// ����Ԥ����RB��
			XBizField u8DlRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_RB_NUM);
			int u8DlRbNumInt = Integer.valueOf(u8DlRbNum.getValue());
			// ����������RB��
			XBizField u8UlMaxRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_MAX_RB_NUM);
			int u8UlMaxRbNumInt = Integer.valueOf(u8UlMaxRbNum.getValue());
			// ������С����RB��
			XBizField u8UlMinRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM);
			int u8UlMinRbNumInt = Integer.valueOf(u8UlMinRbNum.getValue());
			// ����������RB��
			XBizField u8DlMaxRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_MAX_RB_NUM);
			int u8DlMaxRbNumInt = Integer.valueOf(u8DlMaxRbNum.getValue());
			// ������С����RB��
			XBizField u8DlMinRbNum = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_MIN_RB_NUM);
			int u8DlMinRbNumInt = Integer.valueOf(u8DlMinRbNum.getValue());
			// ����Ԥ����RB�������ڴ����RB������Ϊ2/3/5�ı���
			if (u8UlRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlRbNum.getName(),
						"ulrbnum_not_than_sys_bandwidth_rb");
			} else {
				// �Ƿ���2����3����5�ı���
				if (u8UlRbNumInt % 2 != 0 && u8UlRbNumInt % 3 != 0
						&& u8UlRbNumInt % 5 != 0) {
					throw newBizException(u8DlRbNum.getName(),
							"ulrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// ����Ԥ����RB������������
			if (u8DlRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlRbNum.getName(),
						"dlrbnum_not_than_sys_bandwidth_rb");
			}

			// ����������RB���������ڴ����ұ�����2,3,5������
			if (u8UlMaxRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlMaxRbNum.getName(),
						"ulmaxrbnum_not_than_sys_bandwidth_rb");
			} else {
				// �Ƿ���2����3����5�ı���
				if (u8UlMaxRbNumInt % 2 != 0 && u8UlMaxRbNumInt % 3 != 0
						&& u8UlMaxRbNumInt % 5 != 0) {
					throw newBizException(u8UlMaxRbNum.getName(),
							"ulmaxrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// ������С����RB���������ڴ����ұ�����2,3,5������
			if (u8UlMinRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8UlMinRbNum.getName(),
						"ulminrbnum_not_than_sys_bandwidth_rb");
			} else {
				// �Ƿ���2����3����5�ı���
				if (u8UlMinRbNumInt % 2 != 0 && u8UlMinRbNumInt % 3 != 0
						&& u8UlMinRbNumInt % 5 != 0) {
					throw newBizException(u8UlMinRbNum.getName(),
							"ulminrbnum_can_divide_by_2_or_3_or_5");
				}
			}
			// ����������RB������������
			if (u8DlMaxRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlMaxRbNum.getName(),
						"dlmaxrbnum_not_than_sys_bandwidth_rb");
			}

			// ������С����RB������������
			if (u8DlMinRbNumInt > u8SysBandWidthValueInt) {
				throw newBizException(u8DlMinRbNum.getName(),
						"dlminrbnum_not_than_sys_bandwidth_rb");
			}

			// CFI����
			XBizField u8Cfi = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CFI);
			// �ڴ�ֵת������ʵֵ
			int u8CfiInt = Integer.valueOf(u8Cfi.getValue());
			map = getSysBandWidthAsRBorM(enb, u8CId, false);
			// CFI����ȡֵ4ֻ��С������������ϵͳ����Ϊ1.4Mʱ��Ч
			if (u8CfiInt == 4 && !map.get(u8SysBandWidthValue).equals("1.4")) {
				throw newBizException(u8Cfi.getName(),
						"cfi_config_is_4_must_sys_brandwidth_is_1.4");
			}

		}
		// У��С����ԵƵ��bitmap
		// checkBitMap(record);

	}

	/**
	 * У��С����ԵƵ��bitmap
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
			// �ص���7��
			dlBitMap = dlBitMap.substring(0, dlBitMap.length() - 7);
			// �ж��Ƿ���ȫ0
			if (allMatch(dlBitMap, "0")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_DL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
			// �ж��Ƿ���ȫ1
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
			// �ص���4��
			ulBitMap = ulBitMap.substring(0, ulBitMap.length() - 4);
			// �ж��Ƿ���ȫ0
			if (allMatch(ulBitMap, "0")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_UL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
			// �ж��Ƿ���ȫ1
			if (allMatch(ulBitMap, "1")) {
				throw newBizException(
						EnbConstantUtils.FIELD_NAME_UL_CEB_BITMAP,
						"cell_board_bit_map_cannot_all_0_1_when_alg_switch_on");
			}
		}
	}

	/**
	 * ��ȡbitmap�Ķ������ַ���
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
	 * SI�����õ��Ȳ������¼У��</br>
	 * <p>
	 * У�����1��������ͬu8CId�ļ�¼�в������ظ���sib��2��һ����¼����������sib��
	 * 3��������ͬ��u8CId��¼�б���������sib2��4��T_ENB_PARA���u8PttEnable�ֶ�Ϊ1ʱ����������PttSib
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
			// У���Ƿ����Sib
			if (!isSibContained(currentFlags)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_CELL_ID,
						"sib_must_be_contained");
			}
			// ��ȡ��ǰС����SI����
			XBizField cellIdField = record
					.getFieldBy(EnbConstantUtils.FIELD_NAME_CELL_ID);
			XBizRecord condition = new XBizRecord();
			condition.addField(cellIdField);
			List<XBizRecord> records = getXBizRecordByMoId(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_SISCH, condition);
			String currentSiId = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_SIID).getValue();

			// �ظ���sib��ʶ
			int[] repeatSibFlags = new int[] { 0, 0, 0, 0, 0 };
			if (records != null && !records.isEmpty()) {
				for (XBizRecord xBizRecord : records) {
					String siId = xBizRecord.getFieldBy(
							EnbConstantUtils.FIELD_NAME_SIID).getValue();
					// ������ǰ��ӻ��޸ĵļ�¼
					if (currentSiId.equals(siId))
						continue;
					int[] flags = getSibFlag(xBizRecord);
					// �����Ƿ�����ظ�sib�ı�ʶ
					repeatSibFlags = orSibFlag(repeatSibFlags, flags);
				}
			}
			// �Ƿ���sib
			int[] hasSibFlags = repeatSibFlags;
			repeatSibFlags = andSibFlag(repeatSibFlags, currentFlags);
			String repeatSibName = getRepeatSibName(repeatSibFlags);
			if (!StringUtils.isBlank(repeatSibName)) {
				throw newBizException(repeatSibName,
						"sib_is_open_in_other_record");
			}
			// sib2У���Ƿ�ͨ��
			hasSibFlags = orSibFlag(hasSibFlags, currentFlags);
			boolean hasSib2 = hasSibFlags[0] == 1;
			if (!hasSib2) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_SIB2,
						"sib2_must_be_contained_in_each_cell");
			}

			// ��ǰ��¼�Ƿ���ڳ�pttSib���������sib
			boolean hasOtherPtt = currentFlags[0] == 1 || currentFlags[1] == 1
					|| currentFlags[2] == 1 || currentFlags[3] == 1;
			boolean hasPttSib = currentFlags[4] == 1;
			if (hasPttSib && hasOtherPtt) {
				// ��ȺSib���뵥��������һ����¼��
				throw newBizException(EnbConstantUtils.FIELD_NAME_SIBPTT,
						"pttsib_must_enable_individually");
			}

			// // ��Ⱥ�����Ƿ��
			// boolean pttEnabled = isPttEnabled(moId);
			// // ��Ⱥ���ش�ʱ��pttSib�����
			// if (pttEnabled && hasSibFlags[4] != 1) {
			// throw newBizException(EnbConstantUtils.FIELD_NAME_SIBPTT,
			// "pttsib_must_enable_when_pttenble");
			// }
		}
	}

	/**
	 * ��ȡ�ظ���sib����
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
	 * Sib��ʶ������
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
	 * ��ȡeNB�������еļ�Ⱥ����
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private boolean isPttEnabled(long moId) throws Exception {
		// ��ȡeNB�������еļ�Ⱥ����
		XBizRecord enbParamRecord = queryEnbParamRecord(moId);
		if (enbParamRecord == null)
			return false;
		int pttEnable = Integer.valueOf(enbParamRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PTT_ENABLE).getValue());
		// T_ENB_PARA���u8PttEnable�ֶ�Ϊ1ʱ����������PttSib
		return pttEnable == 1;
	}

	/**
	 * ��¼���Ƿ����sib
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
	 * ��ȡsib��ʶ
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
	 * ��ȡС����������ϵͳ�����map���ϣ���M����RB��ֵ���з��أ��������RB��Ϊtrue,����Ϊfalse
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
		// enumValue�ĸ�ʽ�磺(0)1.4M(6RB)|(1)3M(15RB)|(2)5M(25RB)|(3)10M(50RB)|(4)15M(75RB)|(5)20M(100RB)
		String enumValue = list.getEnumText();
		String[] arrs = enumValue.split("\\|");
		// map�д洢�ڴ�����ʾ֮��Ĺ�ϵ��ʹ��RB
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
	 * ����moId/�����Ƽ���ѯ������ȡXBizRecord����
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
	 * У����ܱ�����
	 * 
	 * @param record
	 * @throws Exception
	 */
	private void checkRackRecord(XBizRecord record) throws Exception {
		// 1�Ż��ܲ���ɾ��
		XBizField rackNoField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		int rackNo = Integer.valueOf(rackNoField.getValue());
		if (rackNo == 1) {
			throw new Exception(OmpAppContext.getMessage("rack1_cannot_delete"));
		}
	}

	/**
	 * У����������
	 * 
	 * @param record
	 * @throws Exception
	 */
	private void checkShelfRecord(XBizRecord record) throws Exception {
		// 1�Ż��򲻿�ɾ��
		XBizField shelfNoField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		// 1�Ż��ܲ���ɾ��
		XBizField u8RackNO = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		int rackNO = Integer.valueOf(u8RackNO.getValue());
		if (shelfNo == 1 && rackNO == 1) {
			throw new Exception(
					OmpAppContext.getMessage("shelf1_cannot_delete"));
		}

	}

	/**
	 * У�鵥��������
	 * 
	 * @param bizRecord
	 * @param actionType
	 *            ������ʽ��ɾ�������
	 * @throws Exception
	 */
	private void checkBoardRecord(XBizRecord bizRecord, String actionType)
			throws Exception {
		int[] noArray = getRackShelfSlotNo(bizRecord);
		int rackNo = noArray[0];
		int shelfNo = noArray[1];
		int slotNo = noArray[2];

		if (actionType.equals(ActionTypeDD.DELETE)) {
			// 1-1-1���岻��ɾ��
			if (rackNo == 1 && shelfNo == 1 && slotNo == 1) {
				throw new BizException(
						OmpAppContext.getMessage("board111_cannot_delete"));
			}
		} else if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			XBizField boardTypeField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_BDTYPE);
			// ������б�����һ��BBU�壬�Ҽܿ�۱�����1,1,1
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
	 * ���˱�У��
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
			// ��ȡ����ܿ��
			int[] mainArray = getRackShelfSlotNoOfTopoRecord(topoRecord, true);
			// ��ȡ�Ӱ�ܿ��
			int[] slaveArray = getRackShelfSlotNoOfTopoRecord(topoRecord, false);
			// ���ܿ�ۺʹӼܿ�۲�����ͬ
			if (mainArray[0] == slaveArray[0] && mainArray[1] == slaveArray[1]
					&& mainArray[2] == slaveArray[2]) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_MRACKNO,
						"topo_record_main_subordinate_cannot_equal");
			}

			// // �ж��������Ƿ���BBU
			// int mainBoardType = getBoardType(moId, mainArray[0],
			// mainArray[1],
			// mainArray[2]);
			// if (mainBoardType != EnbConstantUtils.BOARD_TYPE_BBU) {
			// throw newBizException(EnbConstantUtils.FIELD_NAME_MRACKNO,
			// "topo_record_main_board_must_be_bbu");
			// }
			// �жϴӵ����Ƿ���RRU
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
				// ��ȡ����ܿ��
				int[] mArray = getRackShelfSlotNoOfTopoRecord(record, true);
				// ��ȡ�Ӱ�ܿ��
				int[] sArray = getRackShelfSlotNoOfTopoRecord(record, false);
				String fiberPort = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();

				// ��ں��Ƿ�ռ��
				if (currentFiberPort.equals(fiberPort)) {
					throw newBizException(
							EnbConstantUtils.FIELD_NAME_FIBER_PORT,
							"fiberport_already_used");
				} else {
					// ��ںŲ�ͬʱ���Ӱ���벻ͬ
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
	 * ��ȡ��������
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
	 * У����̫������������
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
				// У��ܡ��򡢲ۡ��˿ںŵ�����Ƿ��ظ�
				checkEthenetRecordDuplicated(record, records);
			} else if (actionType.equals(ActionTypeDD.MODIFY)) {
				// �����ܱ����������̫���������мܿ�۱��뱣��1-1-1����
				checkEthenetRelatedOmcRecord(moId, record);
				int index = getEthenetRecordIndex(record, records);
				if (index != -1) {
					records.remove(index);
				}
				// У��ܡ��򡢲ۡ��˿ںŵ�����Ƿ��ظ�
				checkEthenetRecordDuplicated(record, records);
			}
		}

	}

	/**
	 * �����ܱ����������̫���������мܿ�۱��뱣��1-1-1����
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 */
	private void checkEthenetRelatedOmcRecord(long moId, XBizRecord record)
			throws Exception {
		// ���������ܱ���ص���̫��������¼
		XBizRecord ethRecord = queryRelatedEthernetRecord(moId);
		if (ethRecord == null)
			return;
		XBizField portIdFieldOfEth = ethRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portIdOfEth = Integer.valueOf(portIdFieldOfEth.getValue());
		XBizField portIdField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		// �ж��Ƿ��������ܱ�������ļ�¼
		if (portId == portIdOfEth) {
			// �����ܱ����������̫���������еļܿ�۱��뱣��1,1,1����
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
	 * ��ȡ��̫���������¼���б��е�����
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
	 * У��ܡ��򡢲ۡ��˿ںŵ�����Ƿ��ظ�
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
	 * У��IPV4������
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
			// ������IP������ͬһ����
			String newIp = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
			String mask = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			String gateway = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();
			// ���ص�ַ������IP��ַ��ͬһ����
			if (!checkSameNetFragment(newIp, mask, gateway)) {
				throw newBizException(EnbConstantUtils.FIELD_NAME_GATEWAY,
						"gateway_must_same_net_with_ip");
			}
			// У����ӻ��޸ĵ�IP�ڵ�ǰϵͳ���Ƿ����
			checkIpDuplicated(enb.getMoId(), record);

		} else if (actionType.equals(ActionTypeDD.DELETE)) {
			// �����ܱ��������IPV4���еļ�¼����ɾ��
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
			// IPv4���еļ�¼�����޸�
			if (!recordInDB.equals(record)) {
				throw new Exception(
						OmpAppContext
								.getMessage("record_of_ipv4_cannot_change"));
			}
			// У����ӻ��޸ĵ�IP�ڵ�ǰϵͳ���Ƿ����
			checkIpDuplicated(enb.getMoId(), record);
			// // �����ܱ��������IPV4���ж˿ڱ�ʶ���ɸı�
			// XBizRecord ipv4Record = queryOmcRecord(moId);
			// if (ipv4Record != null) {
			// XBizField portIdFieldNew = record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ID);
			// XBizField portIdField = ipv4Record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
			// int portId = Integer.valueOf(portIdField.getValue());
			// int portIdNew = Integer.valueOf(portIdFieldNew.getValue());
			// if (portId == portIdNew) {
			// // ��ѯ���еĶ˿ڱ�ʶ:����Ip��ʶ��ѯ�˿ڱ�ʶ�໥�Ƚ�
			// XBizRecord oldIpV4Record = queryIpv4Record(moId, portIdNew);
			// if (oldIpV4Record != null) {
			// // ���оɵĶ˿ں�
			// XBizField oldU8PortID = oldIpV4Record
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
			// int oldU8PortIDInt = Integer.valueOf(oldU8PortID
			// .getValue());
			// // ���ݹ����Ķ˿ں�
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
	 * У����ӻ��޸ĵ�IP�ڵ�ǰϵͳ���Ƿ����
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
		// ��ȡϵͳ�����л�վip
		for (Enb enb : enbList) {
			List<XBizRecord> records = getXBizRecordByMoId(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_IPV4, null);
			if (records == null || records.isEmpty())
				continue;
			for (XBizRecord record : records) {
				// ������ǰվ��ǰ�޸ĵļ�¼
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
		// ip��ʹ�ã������
		if (ipSet.contains(currentIp)) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_IP_ADDR,
					"ip_occupied_please_change");
		}
	}

	/**
	 * �ж�Ŀ��IP�Ƿ��ָ��IP��ͬһ����
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
		// IP��ַ�������������
		long netSeg = ipInteger & maskInteger;
		long nextSeg = nextHopInteger & maskInteger;
		return netSeg == nextSeg;
	}

	/**
	 * ��̬·�ɱ��¼У����һ����ַ�����������ַ��ͬһ����
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
		// ���������IPv4���¼
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
	 * У�����ܱ�����
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkOmcRecord(Enb enb, XBizRecord bizRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			long moId = enb.getMoId();
			// �����ܱ��������IPV4���м�¼�Ķ˿������У����ܺš�����š���λ�ű���Ϊ1-1-1
			XBizField ipIdField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
			int ipId = Integer.valueOf(ipIdField.getValue());
			XBizRecord ipv4Record = queryIpv4Record(moId, ipId);
			// IPV4�������ܱ��������¼������
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
			// ���������ļ�¼�мܿ�۲���1,1,1
			boolean ok = rackNo == 1 && shelfNo == 1 && slotNo == 1;
			if (!ok) {
				throw newBizException(ipIdField.getName(),
						"port_config_of_related_record_in_ipv4_must_be_111");
			}
		}
	}

	/**
	 * У��ҵ�񹦿ز������¼
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
		// С�����й��ز��������޼�¼,ֱ�ӷ���
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
		// u8PA��ֵ���ܳ���T_CEL_DLPC�������м�¼��u8PAForDTCH�ֶε���Сֵ
		XBizField paField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PA);
		int pa = Integer.valueOf(paField.getValue());
		if (pa > minPAForDTCH) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_PA,
					"u8PA_cannot_larger_than_min_u8PAForDTCH");
		}
	}

	/**
	 * У�黷����ر��¼
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
		// �ܡ��򡢲ۡ�����������͵���ϲ����ظ�
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
	 * ��ѯ�����ܱ����������̫���������еļ�¼
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
		// IPV4�������ܱ��������¼������
		if (ipv4Record == null) {
			return null;
		}
		XBizField portIdField = ipv4Record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		return queryEthernetRecord(moId, portId);
	}

	/**
	 * ��ѯIPV4��IP��ʶΪipId�ļ�¼
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
	 * ��ѯ���ܱ��м�¼
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
	 * ��ѯeNB�������м�¼
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
	 * ��ѯ��̫���������ж˿ڱ�ʶΪportId�ļ�¼
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
	 * �ӵ�����¼�л�ȡ�ܡ��򡢲ۺ�
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
	 * ��ȡ����ʮ�������ַ�������Ķ�����
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
