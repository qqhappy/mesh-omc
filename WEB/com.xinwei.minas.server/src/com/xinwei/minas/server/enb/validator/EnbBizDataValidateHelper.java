/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.dao.EnbBizConfigDAO;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbBizUniqueIdHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaRef;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * У��������
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizDataValidateHelper {
	
	private static final String splitFlag = "#";
	
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
		}
		catch (Exception e) {
			return false;
		}
		return false;
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
	 * У���¼�Ƿ���ڣ��������׳��쳣
	 * 
	 * @param moId
	 * @param tableName
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkRecordExist(long moId, String tableName,
			XBizRecord bizRecord) throws Exception {
		if (!isRecordExist(moId, tableName, bizRecord)) {
			throw new Exception(OmpAppContext.getMessage("record_not_exist"));
		}
	}
	
	/**
	 * �Ƿ�VLAN���ء�VLAN��ʶ��VLAN���ȼ�����ϸı�
	 * 
	 * @param enb
	 * 
	 * @param newVlanRecord
	 * @param oldVlanRecord
	 * @return
	 */
	public boolean checkVlanContentChanged(Enb enb, XBizRecord newVlanRecord,
			XBizRecord oldVlanRecord) {
		// ��ѯ���ð汾VLAN���ֶ�
		List<String> allFieldNames = EnbBizHelper.getAllFieldNames(enb.getEnbType(),
				enb.getProtocolVersion(), EnbConstantUtils.TABLE_NAME_T_VLAN);
		int newVlanTag = 0;
		if(allFieldNames.contains(EnbConstantUtils.FIELD_NAME_VLAN_TAG)) {
			newVlanTag = getIntFieldValue(newVlanRecord,
					EnbConstantUtils.FIELD_NAME_VLAN_TAG);
		}
		int newVlanId = getIntFieldValue(newVlanRecord,
				EnbConstantUtils.FIELD_NAME_VLAN_ID);
		int newVlanPri = getIntFieldValue(newVlanRecord,
				EnbConstantUtils.FIELD_NAME_VLAN_PRI);
		
		int oldVlanTag = 0;
		if(allFieldNames.contains(EnbConstantUtils.FIELD_NAME_VLAN_TAG)) {
			oldVlanTag = getIntFieldValue(oldVlanRecord,
					EnbConstantUtils.FIELD_NAME_VLAN_TAG);
		}
		int oldVlanId = getIntFieldValue(oldVlanRecord,
				EnbConstantUtils.FIELD_NAME_VLAN_ID);
		int oldVlanPri = getIntFieldValue(oldVlanRecord,
				EnbConstantUtils.FIELD_NAME_VLAN_PRI);
		if (newVlanTag == oldVlanTag && newVlanId == oldVlanId
				&& newVlanPri == oldVlanPri)
			return false;
		return true;
	}
	
	/**
	 * u8PtForPDSCHֵ���ܳ���T_CEL_DLPC�������м�¼��u8PAForDTCH�ֶε���Сֵ
	 * 
	 * @param pttRecord
	 * @param dlpcRecord
	 * @param checkPAForDTCH
	 * @throws BizException
	 */
	public void checkPtForPDSCHAndPAForDTCH(XBizRecord pttRecord,
			XBizRecord dlpcRecord, boolean checkPAForDTCH) throws BizException {
		int ptForPDSCH = getIntFieldValue(pttRecord,
				EnbConstantUtils.FIELD_NAME_PT_FOR_PDSCH);
		int pAForDTCH = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
		String targetField = EnbConstantUtils.FIELD_NAME_PT_FOR_PDSCH;
		if (ptForPDSCH > pAForDTCH) {
			if (checkPAForDTCH) {
				targetField = EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH;
			}
			throw newBizException(targetField,
					"u8PtForPDSCH_u8PAForDTCH_formula");
		}
	}
	
	/**
	 * ��u8SysBandWidth����Ϊ5Mʱ��T_CEL_PUCH.u8SrsBwCfgIndex��������Ϊ0��1
	 * ��u8SysBandWidth����Ϊ3Mʱ��T_CEL_PUCH.u8SrsBwCfgIndexֻ������5-7
	 * ��u8SysBandWidth����Ϊ1.4Mʱ��T_CEL_PUCH.u8SrsBwCfgIndexֻ������7
	 * 
	 * @param sysBandWidth
	 * @param srsBwCfgIndex
	 * @param checkSysBandWidth
	 * @throws BizException
	 */
	public void checkSysBandWidthAndSrsBwCfgIndex(int enbTypeId,
			String protocolVersion, int sysBandWidth, int srsBwCfgIndex,
			boolean checkSysBandWidth) throws BizException {
		
		// С����������ϵͳ�����ڴ�ֵ��M�Ķ�Ӧ��ϵ����0--1.4��
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		// ��ȡϵͳ�����Ӧ��Mֵ
		double sysBandWidthMValue = Double.valueOf(mapAsM.get(String
				.valueOf(sysBandWidth)));
		boolean ok = true;
		String errorMsg = "SysBandWidth_SrsBwCfgIndex_formula_";
		if (sysBandWidthMValue == 1.4) {
			if (srsBwCfgIndex != 7) {
				ok = false;
				errorMsg += "1.4";
			}
		}
		else if (sysBandWidthMValue == 3) {
			if (srsBwCfgIndex < 5) {
				ok = false;
				errorMsg += "3";
			}
		}
		else if (sysBandWidthMValue == 5) {
			if (srsBwCfgIndex == 0 || srsBwCfgIndex == 1) {
				ok = false;
				errorMsg += "5";
			}
		}
		String targetField = EnbConstantUtils.FIELD_NAME_SRS_BW_CFG_INDEX;
		if (!ok) {
			if (checkSysBandWidth) {
				targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			}
			throw newBizException(targetField, errorMsg);
		}
	}
	
	public void checkUlDlSlotAllocAndCellAlgPara(int ulDlSlotAlloc,
			XBizRecord algRecord, boolean checkAlg) throws Exception {
		String ulSubfrmFlag = algRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_UL_SUBFRM_FLAG).getValue();
		String dlSubfrmFlag = algRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_DL_SUBFRM_FLAG).getValue();
		List<Integer> ulSubfrmFlagValues = getSubfrmFlagArray(ulSubfrmFlag);
		List<Integer> dlSubfrmFlagValues = getSubfrmFlagArray(dlSubfrmFlag);
		
		boolean ulOk = checkUlDlSlotAllocAndUlSubfrmFlag(ulDlSlotAlloc,
				ulSubfrmFlagValues);
		
		String targetField = EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC;
		if (!ulOk) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_SUBFRM_FLAG;
			}
			throw newBizException(targetField,
					"u8UlDlSlotAlloc_T_CEL_ALG_ab8UlSubfrmFlag_formula_"
							+ ulDlSlotAlloc);
		}
		
		boolean dlOk = checkUlDlSlotAllocAndDlSubfrmFlag(ulDlSlotAlloc,
				dlSubfrmFlagValues);
		if (!dlOk) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_DL_SUBFRM_FLAG;
			}
			throw newBizException(targetField,
					"u8UlDlSlotAlloc_T_CEL_ALG_ab8DlSubfrmFlag_formula_"
							+ ulDlSlotAlloc);
		}
		
	}
	
	private List<Integer> getSubfrmFlagArray(String subfrmFlag) {
		List<Integer> valueList = new ArrayList<Integer>();
		for (int i = 0; i < subfrmFlag.length(); i += 2) {
			valueList.add(Integer.valueOf(subfrmFlag.substring(i, i + 2), 16));
		}
		return valueList;
	}
	
	// (24)u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��7��8������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��7������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8������Ϊ1
	public boolean checkUlDlSlotAllocAndUlSubfrmFlag(int ulDlSlotAlloc,
			List<Integer> valueList) {
		int[] indexs = null;
		switch (ulDlSlotAlloc) {
			case 0:
				// Ҫ��֤�������Ϳ�����Ϊ1�����������෴
				indexs = new int[] { 0, 1, 5, 6 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 1:
				indexs = new int[] { 0, 1, 4, 5, 6, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 2:
				indexs = new int[] { 0, 1, 3, 4, 5, 6, 8, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 3:
				indexs = new int[] { 0, 1, 5, 6, 7, 8, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 4:
				indexs = new int[] { 0, 1, 4, 5, 6, 7, 8, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 5:
				indexs = new int[] { 0, 1, 3, 4, 5, 6, 7, 8, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			case 6:
				indexs = new int[] { 0, 1, 5, 6, 9 };
				return isAllIndexElementsOk(valueList, indexs, 0);
			default:
				return true;
		}
	}
	
	// (25)u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��9������Ϊ1
	public boolean checkUlDlSlotAllocAndDlSubfrmFlag(int ulDlSlotAlloc,
			List<Integer> dlSubfrmFlagList) {
		int[] indexs = null;
		switch (ulDlSlotAlloc) {
			case 0:
				indexs = new int[] { 2, 3, 4, 7, 8, 9 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 1:
				indexs = new int[] { 2, 3, 7, 8 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 2:
				indexs = new int[] { 2, 7 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 3:
				indexs = new int[] { 2, 3, 4 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 4:
				indexs = new int[] { 2, 3 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 5:
				indexs = new int[] { 2 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			case 6:
				indexs = new int[] { 2, 3, 4, 7, 8 };
				return isAllIndexElementsOk(dlSubfrmFlagList, indexs, 0);
			default:
				return true;
		}
	}
	
	private boolean isAllIndexElementsOk(List<Integer> valueList, int[] indexs,
			int target) {
		for (int i = 0; i < indexs.length; i++) {
			int value = valueList.get(indexs[i]);
			if (value != target)
				return false;
		}
		return true;
	}
	
	public void checkUeTransModeAndCellAlgPara(int ueTransMode,
			XBizRecord algRecord, boolean checkAlg) throws BizException {
		int ts = getIntFieldValue(algRecord, EnbConstantUtils.FIELD_NAME_TS);
		boolean tsOk = checkUeTransModeAndAlgTS(ueTransMode, ts);
		String targetField = EnbConstantUtils.FIELD_NAME_UE_TRANS_MODE;
		if (!tsOk) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_TS;
			}
			throw newBizException(targetField,
					"u8UeTransMode_T_CEL_ALG_u8TS_formula_" + ueTransMode);
		}
	}
	
	// u8UeTransMode����Ϊ0ʱ��T_CEL_ALG.u8TS��ȡ0��3��
	// u8UeTransModeΪ1ʱ��T_CEL_ALG.u8TS��ȡ0��1��3��
	// u8UeTransMode����Ϊ2ʱ��T_CEL_ALG.u8TS��ȡ0��1��2��3
	public boolean checkUeTransModeAndAlgTS(int ueTransMode, int ts) {
		int[] values = null;
		switch (ueTransMode) {
			case 0:
				values = new int[] { 0, 3 };
				return isNumberInArray(ts, values);
			case 1:
				values = new int[] { 0, 1, 3 };
				return isNumberInArray(ts, values);
			case 2:
				values = new int[] { 0, 1, 2, 3 };
				return isNumberInArray(ts, values);
			default:
				return true;
		}
	}
	
	/**
	 * ��ȡsib��ʶ��˳��sib2��sib3��sib4��sib5��sibPtt
	 * 
	 * @param bizRecord
	 * @return
	 */
	public int[] getSibFlag(XBizRecord bizRecord) {
		int flag2 = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB2).getValue());
		int flag3 = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB3).getValue());
		int flag4 = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB4).getValue());
		int flag5 = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIB5).getValue());
		int flagPtt = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_SIBPTT).getValue());
		return new int[] { flag2, flag3, flag4, flag5, flagPtt };
	}
	
	/**
	 * ��¼���Ƿ����sib
	 * 
	 * @param record
	 * @return
	 */
	public boolean isSibContained(int[] flags) {
		for (int flag : flags) {
			if (flag == 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ҵ�񹦿ز������е�ҵ��PDSCH��С��RS�Ĺ���ƫ���ֵ���ܳ���С�����й��ز��������м�¼�й�����PDSCH��С��RS�Ĺ���ƫ�õ���Сֵ
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param checkPA
	 * @throws Exception
	 */
	public void checkPAAndPAForDTCH(long moId, XBizRecord bizRecord,
			boolean checkPA) throws Exception {
		if (checkPA) {
			List<XBizRecord> dlpcRecords = queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_DLPC, null);
			if (dlpcRecords != null) {
				// ��ȡPAForDTCH��Сֵ
				int paForDTCHMin = getMinPAForDTCH(dlpcRecords);
				
				int pa = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_PA);
				if (pa > paForDTCHMin) {
					throw newBizException(EnbConstantUtils.FIELD_NAME_PA,
							"u8PA_cannot_larger_than_min_u8PAForDTCH");
				}
			}
			
		}
		else {
			List<XBizRecord> srvpcRecords = queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_ENB_SRVPC, null);
			if (srvpcRecords != null) {
				// ��ȡPA���ֵ
				int maxPA = getMaxPA(srvpcRecords);
				
				int paForDTCH = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
				if (maxPA > paForDTCH) {
					throw newBizException(
							EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH,
							"u8PA_cannot_larger_than_min_u8PAForDTCH");
				}
			}
			
		}
	}
	
	public int getMinPAForDTCH(List<XBizRecord> dlpcRecords) {
		int min = 100;
		for (XBizRecord record : dlpcRecords) {
			int paForDTCH = getIntFieldValue(record,
					EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
			if (paForDTCH < min) {
				min = paForDTCH;
			}
		}
		return min;
	}
	
	public int getMaxPA(List<XBizRecord> srvpcRecords) {
		int max = -100;
		for (XBizRecord record : srvpcRecords) {
			int pa = getIntFieldValue(record, EnbConstantUtils.FIELD_NAME_PA);
			if (pa > max) {
				max = pa;
			}
		}
		return max;
	}
	
	// u8IntraFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
	// u8A2ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
	// u8A1ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1
	// u8A2ForRedirectMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
	// u8IcicA3MeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
	// T_ENB_CTFREQ.u8InterFreqHOMeasCfg��ָʾ��u8EvtId����Ϊ3
	public void checkMeasCfgOk(String fieldName, XBizRecord bizRecord,
			XBizRecord measCfgRecord, boolean checkMeascfg) throws Exception {
		XBizField measCfgField = bizRecord.getFieldBy(fieldName);
		if (measCfgField == null)
			return;
		String meascfgIndex = getStringFieldValue(measCfgRecord,
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX);
		if (!meascfgIndex.equals(measCfgField.getValue()))
			return;
		String eventId = null;
		if (fieldName
				.equals(EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG)
				|| fieldName
						.equals(EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG)
				|| fieldName
						.equals(EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG)) {
			eventId = "3";
		}
		else if (fieldName
				.equals(EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG)
				|| fieldName
						.equals(EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG)) {
			eventId = "2";
		}
		else if (fieldName
				.equals(EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG)) {
			eventId = "1";
		}
		else {
			return;
		}
		String currentEventId = getStringFieldValue(measCfgRecord,
				EnbConstantUtils.FIELD_NAME_EVT_ID);
		if (!eventId.equals(currentEventId)) {
			String errorMsg = fieldName + "_in_t_enb_meascfg_must_" + eventId;
			String targetField = fieldName;
			if (checkMeascfg) {
				targetField = EnbConstantUtils.FIELD_NAME_EVT_ID;
			}
			throw newBizException(targetField, errorMsg);
		}
		
	}
	
	/**
	 * С������������й��ز�����Լ��У��
	 * 
	 * @param protocolVersion
	 * @param dlpcRecord
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void checkCellParaAndDlpc(int enbTypeId, String protocolVersion,
			XBizRecord dlpcRecord, XBizRecord cellParaRecord, boolean checkDlpc)
			throws Exception {
		// С�����й��ز�����--С���ο��źŹ��ʣ��ڴ�ֵ=��ʾֵ+60
		int cellSpeRefSigPwr = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR) - 60;
		// �������ֵ
		double maxPwr = calDlpcCellSpeRefSigPwrMax(enbTypeId, protocolVersion,
				cellParaRecord, dlpcRecord);
		// ������Сֵ
		double minPwr = calDlpcCellSpeRefSigPwrMin(enbTypeId, protocolVersion,
				cellParaRecord, dlpcRecord);
		if (cellSpeRefSigPwr > maxPwr) {
			String errorMsg = MessageFormat.format(OmpAppContext
					.getMessage("dlpc_CellSpeRefSigPwr_cannot_greater_than"),
					(int) maxPwr);
			String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			if (checkDlpc) {
				targetField = EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR;
			}
			throw new BizException(targetField, errorMsg);
		} else if(cellSpeRefSigPwr < minPwr) {
			String errorMsg = MessageFormat.format(OmpAppContext
					.getMessage("dlpc_CellSpeRefSigPwr_cannot_less_than"),
					(int) minPwr);
			String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			if (checkDlpc) {
				targetField = EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR;
			}
			throw new BizException(targetField, errorMsg);
		}
		
	}
	
	/**
	 * T_CEL_DLPC���u16CellSpeRefSigPwr�����õ����ֵ�����¹�ʽ������ CRS_EPRE_max =
	 * T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) + ERS_nor��
	 * ��ʽ���漰���ֶζ��ǰ��ս���ֵ��P�������㣬����ERS_nor���ɵã�����T_CEL_DLPC��ĸ���
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param dlpcRecord
	 * @return
	 */
	public double calDlpcCellSpeRefSigPwrMax(int enbTypeId,
			String protocolVersion, XBizRecord cellParaRecord,
			XBizRecord dlpcRecord) {
		// С�������������ж˿�������ʵֵ����1��2��4;
		String antPortNumStrValue = getStringFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
		// �������߶˿���������˿����Ķ�Ӧ��ϵ
		Map<String, String> antPortMap = getAntPortNumValueMap(enbTypeId,
				protocolVersion);
		int antPortNum = Integer.valueOf(antPortMap.get(antPortNumStrValue));
		
		// С����������ϵͳ�����ڴ�ֵ��M�Ķ�Ӧ��ϵ����0--1.4��
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// ��ȡϵͳ�����Ӧ��Mֵ
		double sysBandWidthMValue = Double.valueOf(mapAsM.get(String
				.valueOf(sysBandWidth)));
		
		// С�����й��ز�����--С��������ܹ���--Э��֮����Ϊdouble
		// u16CellTransPwrDouble���ڴ�ֵ=��ʾֵ*10
		double cellTransPwr = getDoubleFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_CELL_TRANS_PWR) / 10;
		
		// С�����й��ز�����--PA
		int u8PAForDTCH = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
		
		// С�����й��ز�����--PB
		int u8PB = getIntFieldValue(dlpcRecord, EnbConstantUtils.FIELD_NAME_PB);
		double ersNor = 0;
		
		// u8PAForDTCHInt��u8PBInt��SysBandWidthDValue��ȡersNor
		if (5 == sysBandWidthMValue) {
			ersNor = getResNor(BW5MHZ, u8PB, u8PAForDTCH);
		}
		else if (10 == sysBandWidthMValue) {
			ersNor = getResNor(BW10MHZ, u8PB, u8PAForDTCH);
		}
		else if (15 == sysBandWidthMValue) {
			ersNor = getResNor(BW15MHZ, u8PB, u8PAForDTCH);
		}
		else if (20 == sysBandWidthMValue) {
			ersNor = getResNor(BW20MHZ, u8PB, u8PAForDTCH);
		}
		// u16CellTransPwr - 10*log10(T_CEL_PARA.u8DlAntPortNum) + ERS_nor
		double temp = Double.valueOf(String.format("%.1f",
				Math.log10(antPortNum)));
		return cellTransPwr - 10 * temp + ersNor;
	}
	
	
	
	/**
	 * T_CEL_DLPC���u16CellSpeRefSigPwr�����õ���Сֵ�����¹�ʽ������ 
	 * CRS_EPRE_max = 36dbm + ERS_nor��
	 * ��ʽ���漰���ֶζ��ǰ��ս���ֵ��P�������㣬����ERS_nor���ɵã�����T_CEL_DLPC��ĸ���
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param dlpcRecord
	 * @return
	 */
	public double calDlpcCellSpeRefSigPwrMin(int enbTypeId,
			String protocolVersion, XBizRecord cellParaRecord,
			XBizRecord dlpcRecord) {
		// С����������ϵͳ�����ڴ�ֵ��M�Ķ�Ӧ��ϵ����0--1.4��
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// ��ȡϵͳ�����Ӧ��Mֵ
		double sysBandWidthMValue = Double.valueOf(mapAsM.get(String
				.valueOf(sysBandWidth)));
		
		// С�����й��ز�����--PA
		int u8PAForDTCH = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
		
		// С�����й��ز�����--PB
		int u8PB = getIntFieldValue(dlpcRecord, EnbConstantUtils.FIELD_NAME_PB);
		double ersNor = 0;
		
		// u8PAForDTCHInt��u8PBInt��SysBandWidthDValue��ȡersNor
		if (5 == sysBandWidthMValue) {
			ersNor = getResNor(BW5MHZ, u8PB, u8PAForDTCH);
		}
		else if (10 == sysBandWidthMValue) {
			ersNor = getResNor(BW10MHZ, u8PB, u8PAForDTCH);
		}
		else if (15 == sysBandWidthMValue) {
			ersNor = getResNor(BW15MHZ, u8PB, u8PAForDTCH);
		}
		else if (20 == sysBandWidthMValue) {
			ersNor = getResNor(BW20MHZ, u8PB, u8PAForDTCH);
		}
		return 30 + ersNor;
	}
	
	/**
	 * ��ȡERS_nor��ֵ
	 * 
	 * @param resNors
	 * @param x
	 * @param y
	 * @return
	 */
	private int getResNor(int[][] resNors, int x, int y) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				if (i == x && j == y) {
					return resNors[x][y];
				}
			}
		}
		return result;
	}
	
	/**
	 * �������߶˿���Э��ֵP����ʵֵ֮��Ĺ�ϵ��0--port1;1--port2;2--port4
	 * 
	 * @param protocolVersion
	 * @return
	 */
	private Map<String, String> getAntPortNumValueMap(int enbTypeId,
			String protocolVersion) {
		XList fieldConfig = EnbBizHelper.getFieldMetaBy(enbTypeId,
				protocolVersion, EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
		// (0)port1|(1)port2|(2)port4
		String enumValue = fieldConfig.getEnumText();
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
		return map;
	}
	
	/**
	 * T_CEL_NBRCEL����u8SvrCID��T_CEL_PARA���е�u8CId�����u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������T_CEL_PARA��Ӧ��¼�е�u16PhyCellId�ֶ����
	 * 
	 * @param moId
	 * @param bizRecord
	 * @param checkCellPara
	 * @throws Exception
	 */
	public void checkNeighborCellAndCellPara(long moId, XBizRecord bizRecord,
			boolean checkCellPara) throws Exception {
		int currentCellId = 0;
		if (checkCellPara) {
			currentCellId = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_CELL_ID);
			// ��ѯ��Ӧ��������ϵ�������м�¼
			List<XBizRecord> records = queryRecordsByCellId(moId,
					currentCellId, EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL);
			if (records == null)
				return;
			for (XBizRecord nbrRecord : records) {
				int centerFreqCfgIndex = getIntFieldValue(nbrRecord,
						EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
				if (centerFreqCfgIndex == 255) {
					checkPciEquals(bizRecord, nbrRecord);
				}
			}
			
		}
		else {
			currentCellId = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SVR_CID);
			int centerFreqCfgIndex = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
			if (centerFreqCfgIndex == 255) {
				// ��ѯ��Ӧ��С���������м�¼
				List<XBizRecord> records = queryRecordsByCellId(moId,
						currentCellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
				checkPciEquals(records.get(0), bizRecord);
			}
			
		}
		
	}
	
	/**
	 * ����С����ʶ�Ƿ���ͬ
	 * 
	 * @param cellParaRecord
	 * @param nbrRecord
	 * @throws BizException
	 */
	private void checkPciEquals(XBizRecord cellParaRecord, XBizRecord nbrRecord)
			throws BizException {
		int pci1 = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		int pci2 = getIntFieldValue(nbrRecord,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		if (pci1 == pci2) {
			throw newBizException(EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
					"nbrcel_centerfreqcfgidx_255_phycellid_cannot_be_equal");
		}
	}
	
	/**
	 * ͬƵС����PCI������ͬ
	 * 
	 * @param record
	 * @param records
	 * @throws Exception
	 */
	public void checkPCI(long moId, XBizRecord bizRecord) throws Exception {
		List<XBizRecord> cellRecords = queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		if (cellRecords == null)
			return;
		String currentCellId = getStringFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		int currentFreq = getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CENTER_FREQ);
		int currentPci = getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		for (XBizRecord record : cellRecords) {
			String cellId = getStringFieldValue(record,
					EnbConstantUtils.FIELD_NAME_CELL_ID);
			if (currentCellId.equals(cellId))
				continue;
			int freq = getIntFieldValue(record,
					EnbConstantUtils.FIELD_NAME_CENTER_FREQ);
			// �ж��Ƿ���ͬƵС��
			if (currentFreq == freq) {
				int pci = getIntFieldValue(record,
						EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
				if (currentPci == pci) {
					String errorMsg = MessageFormat
							.format(OmpAppContext
									.getMessage("cell_with_same_freq_pci_cannot_equal"),
									currentCellId, cellId);
					throw new BizException(
							EnbConstantUtils.FIELD_NAME_PHY_CELL_ID, errorMsg);
				}
			}
		}
	}
	
	/**
	 * У��С���������PRACH���ò�����Լ��
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param prachRecord
	 * @param checkPrach
	 * @throws Exception
	 */
	public void checkCellParaAndPrach(int enbTypeId, String protocolVersion,
			XBizRecord cellParaRecord, XBizRecord prachRecord,
			boolean checkPrach) throws Exception {
		// ��ȡ�����ڴ�ֵ
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// ��ȡ�������ǰ����ʼRB
		int prachFreqOffset = getIntFieldValue(prachRecord,
				EnbConstantUtils.FIELD_NAME_PRACH_FREQ_OFFSET);
		boolean sysBandWidthOk = checkPrachFreqOffsetAndSysBandWidth(enbTypeId,
				protocolVersion, prachFreqOffset, sysBandWidth);
		// T_CEL_PRACH���е�u8PrachFreqOffsetС�ڵ���u8SysBandWidth��������RB������ȥ6
		if (!sysBandWidthOk) {
			String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			if (checkPrach) {
				targetField = EnbConstantUtils.FIELD_NAME_PRACH_FREQ_OFFSET;
			}
			throw newBizException(targetField,
					"T_CEL_PRACH_prachfreqoffset_T_CEL_PARA_u8SysBandWidth_formula");
		}
		
		int ulDlSlotAlloc = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
		int prachCfgIndex = getIntFieldValue(prachRecord,
				EnbConstantUtils.FIELD_NAME_PRACH_CFG_INDEX);
		boolean prachCfgIndexOk = checkUlDlSlotAllocAndPrachCfgIndex(
				ulDlSlotAlloc, prachCfgIndex);
		if (!prachCfgIndexOk) {
			String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			if (checkPrach) {
				targetField = EnbConstantUtils.FIELD_NAME_PRACH_CFG_INDEX;
			}
			throw newBizException(targetField,
					"u8UlDlSlotAlloc_T_CEL_PRACH_u8PrachCfgIndex_formula_"
							+ ulDlSlotAlloc);
		}
		
	}
	
	// u8UlDlSlotAllocΪ0ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(11,19)��
	// u8UlDlSlotAllocΪ1ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(8,13,14,40-47)��
	// u8UlDlSlotAllocΪ2ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(5,7,8,11,13,14,17,19-47)��
	// u8UlDlSlotAllocΪ3ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(10,11,19,22,24,32,34,42,44,50,52)��
	// u8UlDlSlotAllocΪ4ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)��
	// u8UlDlSlotAllocΪ5ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)��
	// u8UlDlSlotAllocΪ6ʱ��T_CEL_PRACH.u8PrachCfgIndexȡֵ������(16,17,42,44)��
	public boolean checkUlDlSlotAllocAndPrachCfgIndex(int ulDlSlotAlloc,
			int prachCfgIndex) {
		boolean notOk = false;
		int[] values = null;
		switch (ulDlSlotAlloc) {
			case 0:
				notOk = prachCfgIndex == 11 || prachCfgIndex == 19;
				break;
			case 1:
				notOk = prachCfgIndex == 8 || prachCfgIndex == 13
						|| prachCfgIndex == 14;
				notOk = notOk || (prachCfgIndex >= 40 && prachCfgIndex <= 47);
				break;
			case 2:
				values = new int[] { 5, 7, 8, 11, 13, 14, 17 };
				notOk = isNumberInArray(prachCfgIndex, values);
				notOk = notOk || (prachCfgIndex >= 19 && prachCfgIndex <= 47);
				break;
			case 3:
				values = new int[] { 10, 11, 19, 22, 24, 32, 34, 42, 44, 50, 52 };
				break;
			case 4:
				values = new int[] { 5, 7, 8, 11, 13, 14, 17, 19, 22, 24, 32,
						34, 50, 52 };
				notOk = isNumberInArray(prachCfgIndex, values);
				notOk = notOk || (prachCfgIndex >= 40 && prachCfgIndex <= 47);
				break;
			case 5:
				values = new int[] { 2, 4, 5, 7, 8, 10, 11, 13, 14, 16, 17, 50,
						52 };
				notOk = isNumberInArray(prachCfgIndex, values);
				notOk = notOk || (prachCfgIndex >= 19 && prachCfgIndex <= 47);
				break;
			case 6:
				values = new int[] { 16, 17, 42, 44 };
				notOk = isNumberInArray(prachCfgIndex, values);
				break;
			default:
				return true;
		}
		return !notOk;
	}
	
	/**
	 * У��С������������֡��Ⱥ�С����Ⱥ���ò�����Լ��
	 * 
	 * @param ulDlSlotAlloc
	 * @param pttRecord
	 * @param checkPtt
	 * @throws BizException
	 */
	public void checkUlDlSlotAllocAndCellPtt(int ulDlSlotAlloc,
			XBizRecord pttRecord, boolean checkPtt) throws BizException {
		int pttBPagingSubFN = getIntFieldValue(pttRecord,
				EnbConstantUtils.FIELD_NAME_PTT_BPAGING_SUB_FN);
		boolean pttOk = checkUlDlSlotAllocAndPttBPagingSubFN(ulDlSlotAlloc,
				pttBPagingSubFN);
		if (!pttOk) {
			String targetField = EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC;
			if (checkPtt) {
				targetField = EnbConstantUtils.FIELD_NAME_PTT_BPAGING_SUB_FN;
			}
			throw newBizException(targetField,
					"u8UlDlSlotAlloc_T_CEL_PTT_u8PttBPagingSubFN_formula_"
							+ ulDlSlotAlloc);
		}
	}
	
	// u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��4��5��9��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0 ��3��4��5��8��9��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��4��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��3��4��5��6��7��8��9��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_PTT.u8PttBPagingSubFN��ȡ0��5��9��
	public boolean checkUlDlSlotAllocAndPttBPagingSubFN(int ulDlSlotAlloc,
			int pttBPagingSubFN) {
		int[] values = null;
		switch (ulDlSlotAlloc) {
			case 0:
				values = new int[] { 0, 1, 5, 6 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 1:
				values = new int[] { 0, 1, 4, 5, 6, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 2:
				values = new int[] { 0, 1, 3, 4, 5, 6, 8, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 3:
				values = new int[] { 0, 1, 5, 6, 7, 8, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 4:
				values = new int[] { 0, 1, 4, 5, 6, 7, 8, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 5:
				values = new int[] { 0, 1, 3, 4, 5, 6, 7, 8, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			case 6:
				values = new int[] { 0, 1, 5, 6, 9 };
				return isNumberInArray(pttBPagingSubFN, values);
			default:
				return true;
		}
	}
	
	private boolean isNumberInArray(int number, int[] array) {
		for (int i : array) {
			if (number == i)
				return true;
		}
		return false;
	}
	
	public void checkCellParaAndPuch(int enbTypeId, String protocolVersion,
			XBizRecord cellParaRecord, XBizRecord puchRecord, boolean checkPuch)
			throws Exception {
		int ulDlSlotAlloc = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
		int sriTransPrd = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_SRI_TRANS_PRD);
		// sriTransPrd (0)5ms|(1)10ms|(2)20ms|(3)40ms|(4)80ms
		// rptCqiPrd (0)5ms|(1)10ms|(2)20ms|(3)40ms|(4)80ms|(5)160ms|(6)off"
		int rptCqiPrd = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_RPT_CQI_PRD);
		// ��u8UlDlSlotAllocΪ2ʱ��T_CEL_PUCH���е�u8SRITransPrd��u8RptCqiPrd����ͬʱΪ5ms
		if (ulDlSlotAlloc == 2) {
			if (sriTransPrd == 0 && rptCqiPrd == 0) {
				String targetField = EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC;
				if (checkPuch) {
					targetField = EnbConstantUtils.FIELD_NAME_SRI_TRANS_PRD;
				}
				throw newBizException(targetField,
						"when_uldlslotalloc_is_2_sritransprd_rptcqiprd_can_not_5");
			}
		}
	}
	
	/**
	 * T_CEL_PUCH���е�u8DeltaPucchShift��u8N_RB2��
	 * u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ
	 * ��u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))���ܳ���u8SysBandWidth��������RB����</br> �÷��������ڻ�վ3.0.0����ǰ�İ汾
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param puchRecord
	 * @param checkPuch
	 * @throws BizException
	 */
	public void checkSysBandWidthAndPuch(int enbTypeId, String protocolVersion,
			XBizRecord cellParaRecord, XBizRecord puchRecord, boolean checkPuch)
			throws BizException {
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		boolean sysBandWidthOk = checkSysBandWidthAndPuchRecord(enbTypeId,
				protocolVersion, sysBandWidth, puchRecord);
		if (!sysBandWidthOk) {
			String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
			if (checkPuch) {
				targetField = EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT;
			}
			throw newBizException(
					targetField,
					"T_CEL_PUCH_u8DeltaPucchShift_u8N_RB2_u16N_SrChn_T_CEL_PARA_u8SysBandWidth_formula");
		}
	}
	
	// (3)T_CEL_ALG���е�u8UlRbNum�����ֵ������u8SysBandWidth��Ӧ��RB����
	// (4)T_CEL_ALG���е�u8DlRbNum�����ֵ������u8SysBandWidth��Ӧ��RB����
	// (5)T_CEL_ALG���е�u8UlMaxRbNum�����ֵ������100��u8SysBandWidth��Ӧ��RB��ȡС��
	// (6)T_CEL_ALG���е�u8UlMinRbNum�����ֵ������100��u8SysBandWidth��Ӧ��RB��ȡС��
	// (7)T_CEL_ALG���е�u8DlMaxRbNum�����ֵ������100��u8SysBandWidth��Ӧ��RB��ȡС��
	// (8)u8SysBandWidthȡֵΪ20M��15Mʱ��T_CEL_ALG.u8DlMaxRbNumȡ4����������
	// u8SysBandWidthȡֵΪ10Mʱ��T_CEL_ALG.u8DlMaxRbNumȡ3����������
	// u8SysBandWidthȡֵΪ5Mʱ��T_CEL_ALG.u8DlMaxRbNumȡ2����������
	// (9)T_CEL_ALG���е�u8DlMinRbNum�����ֵ������100��u8SysBandWidth��Ӧ��RB��ȡС��
	// (10)T_CEL_ALG.u8CfiȡֵΪ4ʱu8SysBandWidthֻ��ȡֵΪ0
	public void checkAlgAndSysBandWidth(int enbTypeId, String protocolVersion,
			XBizRecord algRecord, int sysBandWidth, boolean checkAlg)
			throws Exception {
		// ϵͳ��������RB��
		Map<String, String> bandWidthRbMap = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(bandWidthRbMap.get(String
				.valueOf(sysBandWidth)));
		
		String targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
		int ulRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_RB_NUM);
		int ulPreRbSchEnable = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_PRE_SCH_ENABLE);
		
		int ulRbNumResult = ulRbNum * ulPreRbSchEnable;
		
		if (checkAlg) {
			// u8UlRbNum�����ֵ������T_CEL_PARA.u8SysBandWidth��Ӧ��RB����100�����ȡС���ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
			if (ulRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_RB_NUM;
				throw newBizException(targetField,
						"u8UlRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG���е�u8UlRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
			if (ulRbNumResult > sysBandWidthRbNum) {
				throw newBizException(targetField,
						"u8UlRbNum_not_than_sys_bandwidth_rb");
			}
		}
		
		int dlRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_DL_RB_NUM);
		int dlPreRbSchEnable = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_DL_PRE_SCH_ENABLE);
		
		int dlRbNumResult = dlRbNum * dlPreRbSchEnable;
		
		// T_CEL_ALG���е�u8DlRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
		if (dlRbNumResult > sysBandWidthRbNum) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_DL_RB_NUM;
			}
			throw newBizException(targetField,
					"u8DlRbNum_not_than_sys_bandwidth_rb");
		}
		
		int ulMaxRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_MAX_RB_NUM);
		int ulRbEnable = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_RB_ENABLE);
		
		int ulMaxRbNumResult = ulMaxRbNum * ulRbEnable;
		
		if (checkAlg) {
			// u8UlMaxRbNum�����ֵ������T_CEL_PARA.u8SysBandWidth��Ӧ��RB�����ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
			if (ulMaxRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulMaxRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_MAX_RB_NUM;
				throw newBizException(targetField,
						"u8UlMaxRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG���е�u8UlMaxRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
			if (ulMaxRbNumResult > sysBandWidthRbNum) {
				throw newBizException(targetField,
						"u8UlMaxRbNum_not_than_sys_bandwidth_rb");
			}
		}
		int ulMinRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM);
		
		int ulMinRbNumResult = ulMinRbNum * ulRbEnable;
		
		if (checkAlg) {
			// u8UlMinRbNum�����ֵ������T_CEL_PARA.u8SysBandWidth��Ӧ��RB�����ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
			if (ulMinRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulMinRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM;
				throw newBizException(targetField,
						"u8UlMinRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG���е�u8UlMinRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
			if (ulMinRbNumResult > sysBandWidthRbNum) {
				throw newBizException(targetField,
						"u8UlMinRbNum_not_than_sys_bandwidth_rb");
			}
		}
		
		int dlRbEnable = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_DL_RB_ENABLE);
		
		XBizField dlMaxRbNumField = algRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_MAX_RB_NUM);
		XBizField dlRbEnableField = algRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_DL_RB_ENABLE);
		checkSysBandWidthAndDlMaxRbNum(enbTypeId, protocolVersion,
				sysBandWidth, dlMaxRbNumField, dlRbEnableField, !checkAlg);
		
		// T_CEL_ALG���е�u8DlMinRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
		int dlMinRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_DL_MIN_RB_NUM);
		
		int dlMinRbNumResult = dlMinRbNum * dlRbEnable;
		
		if (dlMinRbNumResult > sysBandWidthRbNum) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_DL_MIN_RB_NUM;
			}
			throw newBizException(targetField,
					"u8DlMinRbNum_not_than_sys_bandwidth_rb");
		}
		// T_CEL_ALG.u8CfiȡֵΪ4ʱu8SysBandWidthֻ��ȡֵΪ0
		int cfi = getIntFieldValue(algRecord, EnbConstantUtils.FIELD_NAME_CFI);
		if (cfi == 4 && sysBandWidth != 0) {
			if (checkAlg) {
				targetField = EnbConstantUtils.FIELD_NAME_CFI;
			}
			throw newBizException(targetField,
					"cfi_config_is_4_must_sys_brandwidth_is_1.4");
		}
	}
	
	/**
	 * �Ƿ���2��x���ݡ�3��y������5��z���ݵĳ˻�
	 * 
	 * @param number
	 * @return
	 */
	private static boolean isMultiple235(int number) {
		if (number == 0)
			return true;
		double temp = number;
		while (true) {
			double a = temp / 2;
			// ����������1����ֱ�ӷ���true
			if (a == 1) {
				return true;
			}
			// ������1�����ܱ�1������������������break
			if (a % 1 != 0)
				break;
			temp = a;
		}
		while (true) {
			double a = temp / 3;
			// ����������1����ֱ�ӷ���true
			if (a == 1) {
				return true;
			}
			// ������1�����ܱ�1������������������break
			if (a % 1 != 0)
				break;
			temp = a;
		}
		while (true) {
			double a = temp / 5;
			// ����������1����ֱ�ӷ���true
			if (a == 1) {
				return true;
			}
			// ������1�����ܱ�1������������������break
			if (a % 1 != 0)
				break;
			temp = a;
		}
		if (temp == 1)
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isMultiple235(42));
		int a = 0;
		while (a < 100) {
			if (isMultiple235(a)) {
				System.out.println(a);
			}
			a++;
		}
	}
	
	public void checkSysBandWidthAndDlMaxRbNum(int enbTypeId,
			String protocolVersion, int sysBandWidth,
			XBizField dlMaxRbNumField, XBizField dlRbEnableField,
			boolean checkSysBandWidth) throws Exception {
		if (dlMaxRbNumField == null)
			return;
		
		// ��ȡϵͳ�������Ĵ����С
		Map<String, String> bandWidthMMap = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		double sysBandWidthMNum = Double.valueOf(bandWidthMMap.get(String
				.valueOf(sysBandWidth)));
		int dlMaxRbNum = Integer.valueOf(dlMaxRbNumField.getValue());
		
		int dlRbEnable = Integer.valueOf(dlRbEnableField.getValue());
		
		int dlMaxRbNumResult = dlMaxRbNum * dlRbEnable;
		
		String targetField = dlMaxRbNumField.getName();
		String msgField = dlMaxRbNumField.getName();
		if (checkSysBandWidth) {
			targetField = EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH;
		}
		
		if (sysBandWidthMNum == 5) {
			// u8SysBandWidthȡֵΪ5Mʱ��u8DlMaxRbNumȡ2��������
			if (dlMaxRbNumResult % 2 != 0) {
				throw newBizException(targetField, "sysbandwidth_5_" + msgField
						+ "_multiple_2");
			}
		}
		else if (sysBandWidthMNum == 10) {
			// u8SysBandWidthȡֵΪ10Mʱ��u8DlMaxRbNumȡ3����������
			if (dlMaxRbNumResult % 3 != 0) {
				throw newBizException(targetField, "sysbandwidth_10_"
						+ msgField + "_multiple_3");
			}
		}
		else if (sysBandWidthMNum == 15 || sysBandWidthMNum == 20) {
			// u8SysBandWidthȡֵΪ20M��15Mʱ��u8DlMaxRbNumȡ4����������
			if (dlMaxRbNumResult % 4 != 0) {
				throw newBizException(targetField, "sysbandwidth_20_15_"
						+ msgField + "_multiple_4");
			}
		}
		
		// ϵͳ��������RB��
		Map<String, String> bandWidthRbMap = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(bandWidthRbMap.get(String
				.valueOf(sysBandWidth)));
		
		// u8DlMaxRbNum�����ֵ������u8SysBandWidth��Ӧ��RB��
		if (dlMaxRbNumResult > sysBandWidthRbNum) {
			throw newBizException(targetField, msgField
					+ "_not_than_sys_bandwidth_rb");
		}
		
	}
	
	/**
	 * PRACH�������ñ����У������ u8PrachFreqOffset��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	 * u8PrachFreqOffsetС�ڵ���u8SysBandWidth��������RB������ȥ6
	 * 
	 * @param protocolVersion
	 * @param prachFreqOffset
	 * @param sysBandWidth
	 */
	public boolean checkPrachFreqOffsetAndSysBandWidth(int enbTypeId,
			String protocolVersion, int prachFreqOffset, int sysBandWidth) {
		// ϵͳ��������RB��
		Map<String, String> map = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(map.get(String
				.valueOf(sysBandWidth)));
		return (prachFreqOffset <= (sysBandWidthRbNum - 6));
	}
	
	/**
	 * T_CEL_PUCH���е�u8DeltaPucchShift��u8N_RB2��
	 * u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	 * u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))���ܳ���u8SysBandWidth��������RB����</br> �÷��������ڻ�վ3.0.0����ǰ�İ汾
	 * 
	 * @param protocolVersion
	 * @param sysBandWidth
	 * @param puchRecord
	 * @return
	 */
	public boolean checkSysBandWidthAndPuchRecord(int enbTypeId,
			String protocolVersion, int sysBandWidth, XBizRecord puchRecord) {
		// T_CEL_PUCH���е�u8DeltaPucchShift��u8N_RB2��u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
		// u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))���ܳ���u8SysBandWidth��������RB����
		// ϵͳ��������RB��
		Map<String, String> map = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(map.get(String
				.valueOf(sysBandWidth)));
		int u8N_RB2Value = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_RB2);
		int u16N_SrChnValue = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_SR_CHN);
		int u8DeltaPucchShiftValue = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT);
		// ���ڼ����ֵҪ��1
		u8DeltaPucchShiftValue++;
		return (u8N_RB2Value
				+ (u16N_SrChnValue / (36 / u8DeltaPucchShiftValue)) <= sysBandWidthRbNum);
	}
	
	/**
	 * T_CEL_PUCH���е�u8DeltaPucchShift��u8N_RB2��
	 * u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	 * u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))���ܳ���u8SysBandWidth��������RB����</br> �÷��������ڻ�վ3.0.0����ǰ�İ汾
	 * 
	 * @param protocolVersion
	 * @param sysBandWidth
	 * @param puchRecord
	 * @param targetField
	 * @return
	 */
	public void checkSpsANChnAndSysBandWidthAndPuch(int enbTypeId,
			String protocolVersion, int sysBandWidth, XBizRecord spsRecord,
			XBizRecord puchRecord, String targetField) throws BizException {
		
		boolean ok = checkSpsANChnAndSysBandWidthAndPuch(enbTypeId,
				protocolVersion, sysBandWidth, spsRecord, puchRecord);
		if (!ok) {
			throw newBizException(
					targetField,
					"SpsANChn_T_CEL_PUCH_u8DeltaPucchShift_u8N_RB2_u16N_SrChn_T_CEL_PARA_u8SysBandWidth_formula");
		}
	}
	
	/**
	 * u16N_SpsANChn��T_CEL_PUCH���u8DeltaPucchShift��u8N_RB2��
	 * u16N_SrChn�Լ�T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��u8PucchBlankRBNum +
	 * u8N_RB2+((u16N_SrChn+
	 * u16N_SpsANChn)\(36\(u8DeltaPucchShift)))���ܳ���u8SysBandWidth��������RB����</br>
	 * �÷��������ڻ�վ3.0.1���Ժ�汾
	 * 
	 * @param protocolVersion
	 * @param sysBandWidth
	 * @param spsRecord
	 * @param puchRecord
	 * @return
	 */
	public boolean checkSpsANChnAndSysBandWidthAndPuch(int enbTypeId,
			String protocolVersion, int sysBandWidth, XBizRecord spsRecord,
			XBizRecord puchRecord) {
		// ϵͳ��������RB��
		Map<String, String> map = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(map.get(String
				.valueOf(sysBandWidth)));
		int u8PucchBlankRBNum = puchRecord.getIntValue("u8PucchBlankRBNum");
		int u8N_RB2Value = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_RB2);
		int u16N_SrChnValue = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_SR_CHN);
		int u8DeltaPucchShiftValue = getIntFieldValue(puchRecord,
				EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT);
		int u16N_SpsANChnValue = getIntFieldValue(spsRecord,
				EnbConstantUtils.FIELD_NAME_N_SPSANCHN);
		// ���ڼ����ֵҪ��1
		u8DeltaPucchShiftValue++;
		int x = (u16N_SrChnValue + u16N_SpsANChnValue) / (36 / u8DeltaPucchShiftValue);
		int y = (u16N_SrChnValue + u16N_SpsANChnValue) % (36 / u8DeltaPucchShiftValue);
		if(0 != y) {
			x += 1;
		}
		return (u8PucchBlankRBNum
				+ u8N_RB2Value
				+ x <= sysBandWidthRbNum);
	}
	
	/**
	 * ����С��ID��ȡ��¼
	 * 
	 * @param moId
	 * @param cellId
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<XBizRecord> queryRecordsByCellId(long moId, int cellId,
			String tableName) throws Exception {
		XBizRecord condition = new XBizRecord();
		String targetField = EnbConstantUtils.FIELD_NAME_CELL_ID;
		if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL)) {
			targetField = EnbConstantUtils.FIELD_NAME_SVR_CID;
		}
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_DRX)
				|| tableName.equals(EnbConstantUtils.TABLE_NAME_T_CEL_SPS)) {
			targetField = EnbConstantUtils.FIELD_NAME_CELL_ID_;
		}
		condition.addField(new XBizField(targetField, String.valueOf(cellId)));
		return queryRecords(moId, tableName, condition);
	}
	
	/**
	 * ��ȡϵͳ�����ֶ����ã���M����RB��ֵ���з��أ��������RB��Ϊtrue,����Ϊfalse
	 * 
	 * @param moId
	 * @param isRB
	 * @return
	 */
	public Map<String, String> getSysBandWidthAsRBorM(int enbTypeId,
			String protocolVersion, boolean isRB) {
		Map<String, String> map = new HashMap<String, String>();
		XList fieldConfig = EnbBizHelper.getFieldMetaBy(enbTypeId,
				protocolVersion, EnbConstantUtils.TABLE_NAME_T_CELL_PARA,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// enumValue�ĸ�ʽ�磺(0)1.4M(6RB)|(1)3M(15RB)|(2)5M(25RB)|(3)10M(50RB)|(4)15M(75RB)|(5)20M(100RB)
		String enumValue = fieldConfig.getEnumText();
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
		}
		else {
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
			// У�鵥����¼�Ƿ�����
			checkBoardReference(enb, record);
			return;
		}
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG)) {
			// �������ñ��Ƿ�С������������
			checkMeascfgReference(enb, record);
		}
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
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
				List<XBizRecord> records = queryRecords(enb.getMoId(), table,
						condition);
				if (records != null && !records.isEmpty()) {
					throw new Exception(
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
		List<XBizRecord> records = queryRecords(enb.getMoId(), tableName, null);
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
					throw new Exception(
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
		// У���Ƿ񱻻�����ر�����
		checkEnvmonReferenceBoard(enb, record);
		// checkTopoReferenceBoard(enb, record);
	}
	
	/**
	 * У�鵥���Ƿ񱻻�����ر�����
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkEnvmonReferenceBoard(Enb enb, XBizRecord record)
			throws Exception {
		List<XBizRecord> envmonRecords = queryRecords(enb.getMoId(),
				EnbConstantUtils.TABLE_NAME_T_ENVMON, null);
		if (envmonRecords == null)
			return;
		// ������еļܿ��
		int[] noArray = getRackShelfSlotNo(record);
		for (XBizRecord envmonRecord : envmonRecords) {
			// ��ȡ���õļܿ��
			int[] array = getRackShelfSlotNo(envmonRecord);
			// �Ƿ���������
			boolean referenced = noArray[0] == array[0]
					&& noArray[1] == array[1] && noArray[2] == array[2];
			if (referenced) {
				XList tableConfig = EnbBizHelper.getBizMetaBy(enb.getMoId(),
						EnbConstantUtils.TABLE_NAME_T_ENVMON);
				throw new Exception(
						MessageFormat.format(
								OmpAppContext
										.getMessage("this_record_is_related_with_another_table"),
								tableConfig.getDesc()));
			}
		}
		
	}
	
	/**
	 * У�����˱�͵��������ù�ϵ
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkTopoReferenceBoard(Enb enb, XBizRecord record)
			throws Exception {
		String topoTable = EnbConstantUtils.TABLE_NAME_T_TOPO;
		List<XBizRecord> topoRecords = queryRecords(enb.getMoId(), topoTable,
				null);
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
				
				XList tableConfig = EnbBizHelper.getBizMetaBy(enb.getMoId(),
						topoTable);
				throw new Exception(
						MessageFormat.format(
								OmpAppContext
										.getMessage("this_record_is_related_with_another_table"),
								tableConfig.getDesc()));
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
	public int getBoardType(long moId, int rackNo, int shelfNo, int slotNo)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_RACKNO,
				String.valueOf(rackNo)));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SHELFNO,
				String.valueOf(shelfNo)));
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_SLOTNO,
				String.valueOf(slotNo)));
		List<XBizRecord> records = queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_BOARD, condition);
		if (records == null) {
			String boardId = rackNo + "-" + shelfNo + "-" + slotNo;
			throw new Exception(boardId
					+ OmpAppContext.getMessage("board_not_exists"));
		}
		return getIntFieldValue(records.get(0),
				EnbConstantUtils.FIELD_NAME_BDTYPE);
	}
	
	/**
	 * ��ȡ���˱��¼�еļܿ��
	 * 
	 * @param bizRecord
	 * @param main
	 *            ���廹�ǴӰ�
	 */
	public int[] getRackShelfSlotNoOfTopoRecord(XBizRecord bizRecord,
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
		}
		else {
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
	
	public int getIntFieldValue(XBizRecord bizRecord, String fieldName) {
		return Integer.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}
	
	public long getLongFieldValue(XBizRecord bizRecord, String fieldName) {
		return Long.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}
	
	public String getStringFieldValue(XBizRecord bizRecord, String fieldName) {
		return bizRecord.getFieldBy(fieldName).getValue();
	}
	
	public double getDoubleFieldValue(XBizRecord bizRecord, String fieldName) {
		return Double.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}
	
	/**
	 * �ӵ�����¼�л�ȡ�ܡ��򡢲ۺ�
	 * 
	 * @param bizRecord
	 * @return
	 */
	public int[] getRackShelfSlotNo(XBizRecord bizRecord) {
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
	 * eNB�������м�Ⱥ�����Ƿ��
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public boolean isPttEnabled(long moId) throws Exception {
		// ��ȡeNB�������еļ�Ⱥ����
		XBizRecord enbParamRecord = queryEnbParamRecord(moId);
		if (enbParamRecord == null)
			return false;
		return isPttEnabled(enbParamRecord);
	}
	
	/**
	 * eNB�������м�Ⱥ�����Ƿ��
	 * 
	 * @param bizRecord
	 * @return
	 */
	public boolean isPttEnabled(XBizRecord bizRecord) {
		int pttEnable = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PTT_ENABLE).getValue());
		// T_ENB_PARA���u8PttEnable�ֶ�Ϊ1ʱ����������PttSib
		return pttEnable == 1;
	}
	
	/**
	 * SISCH���¼���Ƿ����pttSib
	 * 
	 * @param records
	 * @return
	 * @throws Exception
	 */
	public boolean isPttSibContained(long moId, int cellId) throws Exception {
		List<XBizRecord> records = queryRecordsByCellId(moId, cellId,
				EnbConstantUtils.TABLE_NAME_T_CEL_SISCH);
		if (records == null || records.isEmpty())
			return false;
		for (XBizRecord bizRecord : records) {
			// �Ƿ������Ⱥsib
			int pttSib = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SIBPTT);
			if (pttSib == 1)
				return true;
		}
		return false;
	}
	
	/**
	 * ��ȡĳ��վ����С��ID
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getAllCellIdsByMoId(long moId) throws Exception {
		List<XBizRecord> records = queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		if (records == null || records.isEmpty())
			return Collections.emptyList();
		List<Integer> cellIds = new LinkedList<Integer>();
		for (XBizRecord bizRecord : records) {
			int cellId = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_CELL_ID);
			cellIds.add(cellId);
		}
		return cellIds;
	}
	
	/**
	 * �ж�Ŀ��IP�Ƿ��ָ��IP��ͬһ����
	 * 
	 * @param ip
	 * @param mask
	 * @param targetIp
	 * @return
	 */
	public boolean checkSameNetFragment(String ip, String mask, String targetIp) {
		
		long ipInteger = Long.valueOf(ip, 16);
		long nextHopInteger = Long.valueOf(targetIp, 16);
		long maskInteger = Long.valueOf(mask, 16);
		// IP��ַ�������������
		long netSeg = ipInteger & maskInteger;
		long nextSeg = nextHopInteger & maskInteger;
		return netSeg == nextSeg;
	}
	
	/**
	 * ����moId/�����Ƽ���ѯ������ȡXBizRecord����
	 * 
	 * @param moId
	 * @param tableName
	 * @param condition
	 * @return
	 */
	public List<XBizRecord> queryRecords(long moId, String tableName,
			XBizRecord condition) throws Exception {
		XBizTable ethTable = enbBizConfigDAO.query(moId, tableName, condition);
		if (ethTable != null && !ethTable.getRecords().isEmpty()) {
			return ethTable.getRecords();
		}
		return null;
		
	}
	
	/**
	 * ��ѯIPV4��IP��ʶΪipId�ļ�¼
	 * 
	 * @param moId
	 * @param ipId
	 * @return
	 * @throws Exception
	 */
	public XBizRecord queryIpv4Record(long moId, int ipId) throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_IP_ID,
				String.valueOf(ipId)));
		XBizTable ipv4Table = enbBizConfigDAO.query(moId,
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
	public XBizRecord queryOmcRecord(long moId) throws Exception {
		XBizTable omcTable = enbBizConfigDAO.query(moId,
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
		XBizTable enbParamTable = enbBizConfigDAO.query(moId,
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
	public XBizRecord queryEthernetRecord(long moId, int portId)
			throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_PORT_ID,
				String.valueOf(portId)));
		XBizTable ethTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_ETHPARA, condition);
		if (ethTable != null && !ethTable.getRecords().isEmpty()) {
			return ethTable.getRecords().get(0);
		}
		return null;
	}
	
	public BizException newBizException(String fieldName, String message) {
		return new BizException(fieldName, OmpAppContext.getMessage(message));
	}
	
	public void setEnbBizConfigDAO(EnbBizConfigDAO enbBizConfigDAO) {
		this.enbBizConfigDAO = enbBizConfigDAO;
	}
	
	/**
	 * ���澯������ �澯��+�澯�������ϱ���Ψһ
	 * 
	 * @param alarmParaRecord
	 * @param bizRecord
	 * @param AlarmIdAndFaultCode
	 * @return
	 * @throws Exception
	 */
	public boolean checkCellAlarmIdAndFaultCodeDuplicated(
			String alarmIdAndfaultCode, XBizRecord alarmParaRecord)
			throws Exception {
		// ��ȡ���ݿ��Ӧ�Ĳ���
		String alarmIdAndfaultCode_ = getAlarmIdAndFaultCode(alarmParaRecord);
		// �澯��+�澯�������ϱ���Ψһ
		return alarmIdAndfaultCode.equals(alarmIdAndfaultCode_);
	}
	
	/**
	 * ��ȡ�澯������¼�еĸ澯��͸澯��������
	 * 
	 * @param alarmParaRecord
	 * @return
	 */
	public String getAlarmIdAndFaultCode(XBizRecord alarmParaRecord) {
		
		String alarmId = alarmParaRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ALARMID).getValue();
		String faultCode = alarmParaRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_FAULTCODE).getValue();
		return alarmId + splitFlag + faultCode;
	}
	
	/**
	 * SPS���ñ��е�������SPS���ز�����DRX���ñ��е�DRX����ͬʱ��
	 * 
	 * @param drxRecord
	 * @param spsRecord
	 * @param checkDrxSwitch
	 * @throws Exception
	 */
	public void checkDrxSwitchAndSpsLinkSwitch(XBizRecord drxRecord,
			XBizRecord spsRecord, boolean checkDrxSwitch) throws Exception {
		boolean drxEnable = getIntFieldValue(drxRecord,
				EnbConstantUtils.FIELD_NAME_DRX_ENABLE) == 1;
		boolean spsDownLinkEnable = getIntFieldValue(spsRecord,
				EnbConstantUtils.FIELD_NAME_SPS_DOWNLINK_SWICTH) == 1;
		boolean spsUpLinkEnable = getIntFieldValue(spsRecord,
				EnbConstantUtils.FIELD_NAME_SPS_UPLINK_SWICTH) == 1;
		String targetField = EnbConstantUtils.FIELD_NAME_DRX_ENABLE;
		if (drxEnable && spsDownLinkEnable) {
			if (!checkDrxSwitch) {
				targetField = EnbConstantUtils.FIELD_NAME_SPS_DOWNLINK_SWICTH;
			}
			throw newBizException(targetField,
					"SpsDownLinkSwicth_and_DRX_cannot_enable_at_same_time");
		}
		if (drxEnable && spsUpLinkEnable) {
			if (!checkDrxSwitch) {
				targetField = EnbConstantUtils.FIELD_NAME_SPS_UPLINK_SWICTH;
			}
			throw newBizException(targetField,
					"SpsUpLinkSwicth_and_DRX_cannot_enable_at_same_time");
		}
		
	}
	
	/**
	 * У��ȫ�������ظ����ֶ�
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkUnique(long moId, XBizRecord bizRecord, String tableName,
			String tableParam) throws Exception {
		
		int value = bizRecord.getIntValue(tableParam);
		int cid = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		
		// �Ȼ�ȡ�ɵ�value
		int oldValue = EnbBizUniqueIdHelper.getValueByKey(enbId, cid,
				tableName, tableParam);
		
		// ������δ���value�;ɵ�value��ͬ,���ʾ���ֶα����޸�ʱδ�ı�,��ͨ��У��
		if (value == oldValue) {
			return;
		}
		
		// ��ѯ�����Ѿ���ʹ�õ�value
		List<Integer> valueList = EnbBizUniqueIdHelper.queryAll(tableName,
				tableParam);
		
		// ���Ҫ�޸ĵ�value�Ѿ���ռ���򷵻ش���
		if (valueList.contains(value)) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_exist"));
		}
		
	}
	
	/**
	 * У��ȫ�������ظ����ֶ�,��ǰΪ��ֵ���ظ�Ҳ�����޸�
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkUniqueM(long moId, XBizRecord bizRecord, String tableName,
			String tableParam) throws Exception {
		
		int newValue = bizRecord.getIntValue(tableParam);
		int cid = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		
		// �Ȼ�ȡ�ɵ�value
		int oldValue = EnbBizUniqueIdHelper.getValueByKey(enbId, cid,
				tableName, tableParam);
		
		// �жϸþɵ�value�ж���С����ʹ��
		// ��ѯʹ�ø�valueС���ĸ���
		int sameOldNum = EnbBizUniqueIdHelper.queryNumSameValue(tableName,tableParam,oldValue);
		
		// ������δ���value�;ɵ�value��ͬ,���ʾ���ֶα����޸�ʱδ�ı�,����������С��ʹ�ø��ֶ�
		if (newValue == oldValue && sameOldNum <= 1) {
			return;
		}
		
		// ���Ҫ�޸ĵ�value�Ѿ���ռ���򷵻ش���
		int sameNewNum = EnbBizUniqueIdHelper.queryNumSameValue(tableName,tableParam,newValue);
		if (sameNewNum > 0) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_exist_other_cell"));
		}
		
	}
	
	

	/**
	 * �ж�С���㷨������2�е�pk mode�Ƿ�ΪPingPkMode(2)
	 * @param moId
	 * @param cellId
	 * @return
	 * @throws Exception
	 */
	public boolean isPingPkMode(long moId, Integer cellId) throws Exception {
		XBizRecord condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_CELL_ID,
				String.valueOf(cellId)));
		XBizTable algTable = enbBizConfigDAO.query(moId, EnbConstantUtils.TABLE_NAME_T_CEL_ALG2,
				condition);
		List<XBizRecord> records = algTable.getRecords();
		XBizRecord record = records.get(0);
		int pkMode = record.getIntValue(EnbConstantUtils.FIELD_NAME_PK_MODE);
		if(2 == pkMode) {
			return true;
		}
		return false;
	}

	/**
	 * T_ENB_PARA����EEA��Ϊx'000000'ʱ,T_CEL_ALG2��u8PkMode�ֶβ�������ΪPing Pk Mode
	 * @param moId
	 * @param pkMode
	 * @return
	 * @throws Exception 
	 */
	public boolean checkEeaAndPkMode(long moId, int pkMode) throws Exception {
		// ��ѯT_ENB_PARA����
		XBizTable enbParaTable = enbBizConfigDAO.query(moId, EnbConstantUtils.TABLE_NAME_T_ENB_PARA, null);
		XBizRecord record = enbParaTable.getRecords().get(0);
		String eea = record.getStringValue(EnbConstantUtils.FIELD_NAME_EEA);
		if(!"000000".equals(eea) && 2 == pkMode) {
			return false;
		}
		return true;
	}

	/**
	 * T_ENB_SRVQCI��u8RohcRTP��u8RohcUDP��u8RohcIP����Ϊ1ʱ,T_CEL_ALG2��u8PkMode�ֶβ���ΪPing Pk Mode
	 * @param moId
	 * @param pkMode
	 * @return
	 */
	public boolean checkRohcAndPkMode(long moId, int pkMode) throws Exception {
		// ��ѯT_ENB_SRVQCI����
		XBizTable srvqciTable = enbBizConfigDAO.query(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_SRVQCI, null);
		List<XBizRecord> records = srvqciTable.getRecords();
		for (XBizRecord record : records) {
			int u8RohcRTP = record.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_RTP);
			int u8RohcUDP = record.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_UDP);
			int u8RohcIP = record.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_IP);
			if(1 == u8RohcRTP || 1 == u8RohcUDP || 1 == u8RohcIP) {
				if(2 == pkMode) {
					return false;
				}
			}
			
		}
		return true;
	}
	
	
	
	
	
	
}
