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
 * 校验助手类
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
		}
		catch (Exception e) {
			return false;
		}
		return false;
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
	 * 校验记录是否存在，不存在抛出异常
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
	 * 是否VLAN开关、VLAN标识、VLAN优先级的组合改变
	 * 
	 * @param enb
	 * 
	 * @param newVlanRecord
	 * @param oldVlanRecord
	 * @return
	 */
	public boolean checkVlanContentChanged(Enb enb, XBizRecord newVlanRecord,
			XBizRecord oldVlanRecord) {
		// 查询出该版本VLAN表字段
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
	 * u8PtForPDSCH值不能超过T_CEL_DLPC表中所有记录中u8PAForDTCH字段的最小值
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
	 * 当u8SysBandWidth配置为5M时，T_CEL_PUCH.u8SrsBwCfgIndex不能配置为0、1
	 * 当u8SysBandWidth配置为3M时，T_CEL_PUCH.u8SrsBwCfgIndex只能配置5-7
	 * 当u8SysBandWidth配置为1.4M时，T_CEL_PUCH.u8SrsBwCfgIndex只能配置7
	 * 
	 * @param sysBandWidth
	 * @param srsBwCfgIndex
	 * @param checkSysBandWidth
	 * @throws BizException
	 */
	public void checkSysBandWidthAndSrsBwCfgIndex(int enbTypeId,
			String protocolVersion, int sysBandWidth, int srsBwCfgIndex,
			boolean checkSysBandWidth) throws BizException {
		
		// 小区参数表中系统带宽内存值与M的对应关系：如0--1.4等
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		// 获取系统带宽对应的M值
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
	
	// (24)u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、7、8可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、7可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8可配置为1
	public boolean checkUlDlSlotAllocAndUlSubfrmFlag(int ulDlSlotAlloc,
			List<Integer> valueList) {
		int[] indexs = null;
		switch (ulDlSlotAlloc) {
			case 0:
				// 要验证的索引和可配置为1的索引正好相反
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
	
	// (25)u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、9可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、8、9可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、9可配置为1
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
	
	// u8UeTransMode配置为0时，T_CEL_ALG.u8TS可取0、3；
	// u8UeTransMode为1时，T_CEL_ALG.u8TS可取0、1、3；
	// u8UeTransMode配置为2时，T_CEL_ALG.u8TS可取0、1、2、3
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
	 * 获取sib标识，顺序sib2、sib3、sib4、sib5、sibPtt
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
	 * 记录中是否包含sib
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
	 * 业务功控参数表中的业务级PDSCH与小区RS的功率偏差的值不能超过小区下行功控参数表所有记录中公共级PDSCH与小区RS的功率偏置的最小值
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
				// 获取PAForDTCH最小值
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
				// 获取PA最大值
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
	
	// u8IntraFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
	// u8A2ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
	// u8A1ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1
	// u8A2ForRedirectMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
	// u8IcicA3MeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
	// T_ENB_CTFREQ.u8InterFreqHOMeasCfg所指示的u8EvtId必须为3
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
	 * 小区参数表和下行功控参数表约束校验
	 * 
	 * @param protocolVersion
	 * @param dlpcRecord
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void checkCellParaAndDlpc(int enbTypeId, String protocolVersion,
			XBizRecord dlpcRecord, XBizRecord cellParaRecord, boolean checkDlpc)
			throws Exception {
		// 小区下行功控参数表--小区参考信号功率，内存值=显示值+60
		int cellSpeRefSigPwr = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_CELL_SPE_REF_SIG_PWR) - 60;
		// 计算最大值
		double maxPwr = calDlpcCellSpeRefSigPwrMax(enbTypeId, protocolVersion,
				cellParaRecord, dlpcRecord);
		// 计算最小值
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
	 * T_CEL_DLPC表的u16CellSpeRefSigPwr可配置的最大值由如下公式决定： CRS_EPRE_max =
	 * T_CEL_DLPC.u16CellTransPwr - 10*log10(u8DlAntPortNum) + ERS_nor，
	 * 公式中涉及的字段都是按照界面值（P）来计算，其中ERS_nor查表可得，表格见T_CEL_DLPC表的附件
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param dlpcRecord
	 * @return
	 */
	public double calDlpcCellSpeRefSigPwrMax(int enbTypeId,
			String protocolVersion, XBizRecord cellParaRecord,
			XBizRecord dlpcRecord) {
		// 小区参数表中下行端口数的真实值，如1，2，4;
		String antPortNumStrValue = getStringFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_DL_ANT_PORT_NUM);
		// 下行天线端口数索引与端口数的对应关系
		Map<String, String> antPortMap = getAntPortNumValueMap(enbTypeId,
				protocolVersion);
		int antPortNum = Integer.valueOf(antPortMap.get(antPortNumStrValue));
		
		// 小区参数表中系统带宽内存值与M的对应关系：如0--1.4等
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// 获取系统带宽对应的M值
		double sysBandWidthMValue = Double.valueOf(mapAsM.get(String
				.valueOf(sysBandWidth)));
		
		// 小区下行功控参数表--小区最大发射总功率--协议之可以为double
		// u16CellTransPwrDouble的内存值=显示值*10
		double cellTransPwr = getDoubleFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_CELL_TRANS_PWR) / 10;
		
		// 小区下行功控参数表--PA
		int u8PAForDTCH = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
		
		// 小区下行功控参数表--PB
		int u8PB = getIntFieldValue(dlpcRecord, EnbConstantUtils.FIELD_NAME_PB);
		double ersNor = 0;
		
		// u8PAForDTCHInt，u8PBInt，SysBandWidthDValue获取ersNor
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
	 * T_CEL_DLPC表的u16CellSpeRefSigPwr可配置的最小值由如下公式决定： 
	 * CRS_EPRE_max = 36dbm + ERS_nor，
	 * 公式中涉及的字段都是按照界面值（P）来计算，其中ERS_nor查表可得，表格见T_CEL_DLPC表的附件
	 * 
	 * @param protocolVersion
	 * @param cellParaRecord
	 * @param dlpcRecord
	 * @return
	 */
	public double calDlpcCellSpeRefSigPwrMin(int enbTypeId,
			String protocolVersion, XBizRecord cellParaRecord,
			XBizRecord dlpcRecord) {
		// 小区参数表中系统带宽内存值与M的对应关系：如0--1.4等
		Map<String, String> mapAsM = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, false);
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// 获取系统带宽对应的M值
		double sysBandWidthMValue = Double.valueOf(mapAsM.get(String
				.valueOf(sysBandWidth)));
		
		// 小区下行功控参数表--PA
		int u8PAForDTCH = getIntFieldValue(dlpcRecord,
				EnbConstantUtils.FIELD_NAME_PA_FOR_DTCH);
		
		// 小区下行功控参数表--PB
		int u8PB = getIntFieldValue(dlpcRecord, EnbConstantUtils.FIELD_NAME_PB);
		double ersNor = 0;
		
		// u8PAForDTCHInt，u8PBInt，SysBandWidthDValue获取ersNor
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
	 * 获取ERS_nor的值
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
	 * 下行天线端口数协议值P与真实值之间的关系：0--port1;1--port2;2--port4
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
		return map;
	}
	
	/**
	 * T_CEL_NBRCEL表中u8SvrCID与T_CEL_PARA表中的u8CId相等且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能与T_CEL_PARA对应记录中的u16PhyCellId字段相等
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
			// 查询对应的邻区关系参数表中记录
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
				// 查询对应的小区参数表中记录
				List<XBizRecord> records = queryRecordsByCellId(moId,
						currentCellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
				checkPciEquals(records.get(0), bizRecord);
			}
			
		}
		
	}
	
	/**
	 * 物理小区标识是否相同
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
	 * 同频小区的PCI不能相同
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
			// 判断是否是同频小区
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
	 * 校验小区参数表和PRACH配置参数表约束
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
		// 获取带宽内存值
		int sysBandWidth = getIntFieldValue(cellParaRecord,
				EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
		// 获取随机接入前导起始RB
		int prachFreqOffset = getIntFieldValue(prachRecord,
				EnbConstantUtils.FIELD_NAME_PRACH_FREQ_OFFSET);
		boolean sysBandWidthOk = checkPrachFreqOffsetAndSysBandWidth(enbTypeId,
				protocolVersion, prachFreqOffset, sysBandWidth);
		// T_CEL_PRACH表中的u8PrachFreqOffset小于等于u8SysBandWidth代表的最大RB个数减去6
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
	
	// u8UlDlSlotAlloc为0时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(11,19)，
	// u8UlDlSlotAlloc为1时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(8,13,14,40-47)，
	// u8UlDlSlotAlloc为2时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(5,7,8,11,13,14,17,19-47)，
	// u8UlDlSlotAlloc为3时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(10,11,19,22,24,32,34,42,44,50,52)，
	// u8UlDlSlotAlloc为4时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)，
	// u8UlDlSlotAlloc为5时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)，
	// u8UlDlSlotAlloc为6时，T_CEL_PRACH.u8PrachCfgIndex取值不能是(16,17,42,44)；
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
	 * 校验小区参数表中子帧配比和小区集群配置参数表约束
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
	
	// u8UlDlSlotAlloc配置为0时，T_CEL_PTT.u8PttBPagingSubFN可取0、5；
	// u8UlDlSlotAlloc配置为1时，T_CEL_PTT.u8PttBPagingSubFN可取0、4、5、9；
	// u8UlDlSlotAlloc配置为2时，T_CEL_PTT.u8PttBPagingSubFN可取0 、3、4、5、8、9；
	// u8UlDlSlotAlloc配置为3时，T_CEL_PTT.u8PttBPagingSubFN可取0、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为4时，T_CEL_PTT.u8PttBPagingSubFN可取0、4、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为5时，T_CEL_PTT.u8PttBPagingSubFN可取0、3、4、5、6、7、8、9；
	// u8UlDlSlotAlloc配置为6时，T_CEL_PTT.u8PttBPagingSubFN可取0、5、9；
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
		// 当u8UlDlSlotAlloc为2时，T_CEL_PUCH表中的u8SRITransPrd与u8RptCqiPrd不能同时为5ms
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
	 * T_CEL_PUCH表中的u8DeltaPucchShift、u8N_RB2、
	 * u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系
	 * ：u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))不能超过u8SysBandWidth代表的最大RB个数</br> 该方法适用于基站3.0.0及以前的版本
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
	
	// (3)T_CEL_ALG表中的u8UlRbNum的最大值不超过u8SysBandWidth对应的RB数；
	// (4)T_CEL_ALG表中的u8DlRbNum的最大值不超过u8SysBandWidth对应的RB数；
	// (5)T_CEL_ALG表中的u8UlMaxRbNum的最大值不超过100与u8SysBandWidth对应的RB数取小；
	// (6)T_CEL_ALG表中的u8UlMinRbNum的最大值不超过100与u8SysBandWidth对应的RB数取小；
	// (7)T_CEL_ALG表中的u8DlMaxRbNum的最大值不超过100与u8SysBandWidth对应的RB数取小；
	// (8)u8SysBandWidth取值为20M、15M时，T_CEL_ALG.u8DlMaxRbNum取4的整数倍；
	// u8SysBandWidth取值为10M时，T_CEL_ALG.u8DlMaxRbNum取3的整数倍；
	// u8SysBandWidth取值为5M时，T_CEL_ALG.u8DlMaxRbNum取2的整数倍；
	// (9)T_CEL_ALG表中的u8DlMinRbNum的最大值不超过100与u8SysBandWidth对应的RB数取小；
	// (10)T_CEL_ALG.u8Cfi取值为4时u8SysBandWidth只能取值为0
	public void checkAlgAndSysBandWidth(int enbTypeId, String protocolVersion,
			XBizRecord algRecord, int sysBandWidth, boolean checkAlg)
			throws Exception {
		// 系统带宽代表的RB数
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
			// u8UlRbNum的最大值不超过T_CEL_PARA.u8SysBandWidth对应的RB数，100与带宽取小，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
			if (ulRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_RB_NUM;
				throw newBizException(targetField,
						"u8UlRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG表中的u8UlRbNum的最大值不超过u8SysBandWidth对应的RB数
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
		
		// T_CEL_ALG表中的u8DlRbNum的最大值不超过u8SysBandWidth对应的RB数
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
			// u8UlMaxRbNum的最大值不超过T_CEL_PARA.u8SysBandWidth对应的RB数，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
			if (ulMaxRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulMaxRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_MAX_RB_NUM;
				throw newBizException(targetField,
						"u8UlMaxRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG表中的u8UlMaxRbNum的最大值不超过u8SysBandWidth对应的RB数
			if (ulMaxRbNumResult > sysBandWidthRbNum) {
				throw newBizException(targetField,
						"u8UlMaxRbNum_not_than_sys_bandwidth_rb");
			}
		}
		int ulMinRbNum = getIntFieldValue(algRecord,
				EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM);
		
		int ulMinRbNumResult = ulMinRbNum * ulRbEnable;
		
		if (checkAlg) {
			// u8UlMinRbNum的最大值不超过T_CEL_PARA.u8SysBandWidth对应的RB数，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
			if (ulMinRbNumResult > sysBandWidthRbNum
					|| !isMultiple235(ulMinRbNumResult)) {
				targetField = EnbConstantUtils.FIELD_NAME_UL_MIN_RB_NUM;
				throw newBizException(targetField,
						"u8UlMinRbNum_not_than_sys_bandwidth_rb_and_multiple_2_3_5");
			}
		}
		else {
			// T_CEL_ALG表中的u8UlMinRbNum的最大值不超过u8SysBandWidth对应的RB数
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
		
		// T_CEL_ALG表中的u8DlMinRbNum的最大值不超过u8SysBandWidth对应的RB数
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
		// T_CEL_ALG.u8Cfi取值为4时u8SysBandWidth只能取值为0
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
	 * 是否是2的x次幂、3的y次幂与5的z次幂的乘积
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
			// 如果除完等于1，则直接返回true
			if (a == 1) {
				return true;
			}
			// 不等于1，但能被1整除，继续除；否则，break
			if (a % 1 != 0)
				break;
			temp = a;
		}
		while (true) {
			double a = temp / 3;
			// 如果除完等于1，则直接返回true
			if (a == 1) {
				return true;
			}
			// 不等于1，但能被1整除，继续除；否则，break
			if (a % 1 != 0)
				break;
			temp = a;
		}
		while (true) {
			double a = temp / 5;
			// 如果除完等于1，则直接返回true
			if (a == 1) {
				return true;
			}
			// 不等于1，但能被1整除，继续除；否则，break
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
		
		// 获取系统带宽代表的带宽大小
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
			// u8SysBandWidth取值为5M时，u8DlMaxRbNum取2的整数倍
			if (dlMaxRbNumResult % 2 != 0) {
				throw newBizException(targetField, "sysbandwidth_5_" + msgField
						+ "_multiple_2");
			}
		}
		else if (sysBandWidthMNum == 10) {
			// u8SysBandWidth取值为10M时，u8DlMaxRbNum取3的整数倍；
			if (dlMaxRbNumResult % 3 != 0) {
				throw newBizException(targetField, "sysbandwidth_10_"
						+ msgField + "_multiple_3");
			}
		}
		else if (sysBandWidthMNum == 15 || sysBandWidthMNum == 20) {
			// u8SysBandWidth取值为20M、15M时，u8DlMaxRbNum取4的整数倍；
			if (dlMaxRbNumResult % 4 != 0) {
				throw newBizException(targetField, "sysbandwidth_20_15_"
						+ msgField + "_multiple_4");
			}
		}
		
		// 系统带宽代表的RB数
		Map<String, String> bandWidthRbMap = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(bandWidthRbMap.get(String
				.valueOf(sysBandWidth)));
		
		// u8DlMaxRbNum的最大值不超过u8SysBandWidth对应的RB数
		if (dlMaxRbNumResult > sysBandWidthRbNum) {
			throw newBizException(targetField, msgField
					+ "_not_than_sys_bandwidth_rb");
		}
		
	}
	
	/**
	 * PRACH参数配置表规则校验满足 u8PrachFreqOffset与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	 * u8PrachFreqOffset小于等于u8SysBandWidth代表的最大RB个数减去6
	 * 
	 * @param protocolVersion
	 * @param prachFreqOffset
	 * @param sysBandWidth
	 */
	public boolean checkPrachFreqOffsetAndSysBandWidth(int enbTypeId,
			String protocolVersion, int prachFreqOffset, int sysBandWidth) {
		// 系统带宽代表的RB数
		Map<String, String> map = getSysBandWidthAsRBorM(enbTypeId,
				protocolVersion, true);
		int sysBandWidthRbNum = Integer.valueOf(map.get(String
				.valueOf(sysBandWidth)));
		return (prachFreqOffset <= (sysBandWidthRbNum - 6));
	}
	
	/**
	 * T_CEL_PUCH表中的u8DeltaPucchShift、u8N_RB2、
	 * u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	 * u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))不能超过u8SysBandWidth代表的最大RB个数</br> 该方法适用于基站3.0.0及以前的版本
	 * 
	 * @param protocolVersion
	 * @param sysBandWidth
	 * @param puchRecord
	 * @return
	 */
	public boolean checkSysBandWidthAndPuchRecord(int enbTypeId,
			String protocolVersion, int sysBandWidth, XBizRecord puchRecord) {
		// T_CEL_PUCH表中的u8DeltaPucchShift、u8N_RB2、u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
		// u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))不能超过u8SysBandWidth代表的最大RB个数
		// 系统带宽代表的RB数
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
		// 用于计算的值要加1
		u8DeltaPucchShiftValue++;
		return (u8N_RB2Value
				+ (u16N_SrChnValue / (36 / u8DeltaPucchShiftValue)) <= sysBandWidthRbNum);
	}
	
	/**
	 * T_CEL_PUCH表中的u8DeltaPucchShift、u8N_RB2、
	 * u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	 * u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift
	 * )))不能超过u8SysBandWidth代表的最大RB个数</br> 该方法适用于基站3.0.0及以前的版本
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
	 * u16N_SpsANChn与T_CEL_PUCH表的u8DeltaPucchShift、u8N_RB2、
	 * u16N_SrChn以及T_CEL_PARA表中u8SysBandWidth字段之间约束关系：u8PucchBlankRBNum +
	 * u8N_RB2+((u16N_SrChn+
	 * u16N_SpsANChn)\(36\(u8DeltaPucchShift)))不能超过u8SysBandWidth代表的最大RB个数</br>
	 * 该方法适用于基站3.0.1及以后版本
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
		// 系统带宽代表的RB数
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
		// 用于计算的值要加1
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
	 * 根据小区ID获取记录
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
	 * 获取系统带宽字段配置，分M或者RB的值进行返回，如果按照RB则为true,否则为false
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
		// enumValue的格式如：(0)1.4M(6RB)|(1)3M(15RB)|(2)5M(25RB)|(3)10M(50RB)|(4)15M(75RB)|(5)20M(100RB)
		String enumValue = fieldConfig.getEnumText();
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
			// 校验单板表记录是否被引用
			checkBoardReference(enb, record);
			return;
		}
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG)) {
			// 测量配置表是否被小区参数表引用
			checkMeascfgReference(enb, record);
		}
		else if (tableName.equals(EnbConstantUtils.TABLE_NAME_T_IPV4)) {
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
		List<XBizRecord> records = queryRecords(enb.getMoId(), tableName, null);
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
	 * 校验被删除的单板表记录是否被拓扑表所引用
	 * 
	 * @param enb
	 * @param record
	 * @throws Exception
	 */
	private void checkBoardReference(Enb enb, XBizRecord record)
			throws Exception {
		// 校验是否被环境监控表引用
		checkEnvmonReferenceBoard(enb, record);
		// checkTopoReferenceBoard(enb, record);
	}
	
	/**
	 * 校验单板是否被环境监控表引用
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
		// 单板表中的架框槽
		int[] noArray = getRackShelfSlotNo(record);
		for (XBizRecord envmonRecord : envmonRecords) {
			// 获取引用的架框槽
			int[] array = getRackShelfSlotNo(envmonRecord);
			// 是否被主板引用
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
	 * 校验拓扑表和单板表间引用关系
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
	 * 获取单板类型
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
	 * 获取拓扑表记录中的架框槽
	 * 
	 * @param bizRecord
	 * @param main
	 *            主板还是从板
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
	 * 从单板表记录中获取架、框、槽号
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
	 * eNB参数表中集群开关是否打开
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public boolean isPttEnabled(long moId) throws Exception {
		// 获取eNB参数表中的集群开关
		XBizRecord enbParamRecord = queryEnbParamRecord(moId);
		if (enbParamRecord == null)
			return false;
		return isPttEnabled(enbParamRecord);
	}
	
	/**
	 * eNB参数表中集群开关是否打开
	 * 
	 * @param bizRecord
	 * @return
	 */
	public boolean isPttEnabled(XBizRecord bizRecord) {
		int pttEnable = Integer.valueOf(bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PTT_ENABLE).getValue());
		// T_ENB_PARA表的u8PttEnable字段为1时，必须配置PttSib
		return pttEnable == 1;
	}
	
	/**
	 * SISCH表记录中是否包含pttSib
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
			// 是否包含集群sib
			int pttSib = getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SIBPTT);
			if (pttSib == 1)
				return true;
		}
		return false;
	}
	
	/**
	 * 获取某基站所有小区ID
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
	 * 判断目标IP是否和指定IP在同一网段
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
		// IP地址与掩码等于网段
		long netSeg = ipInteger & maskInteger;
		long nextSeg = nextHopInteger & maskInteger;
		return netSeg == nextSeg;
	}
	
	/**
	 * 根据moId/表名称及查询条件获取XBizRecord集合
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
	 * 查询IPV4中IP标识为ipId的记录
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
	 * 查询网管表中记录
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
	 * 查询eNB参数表中记录
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
	 * 查询以太网参数表中端口标识为portId的记录
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
	 * 检查告警参数中 告警码+告警子码的组合必须唯一
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
		// 获取数据库对应的参数
		String alarmIdAndfaultCode_ = getAlarmIdAndFaultCode(alarmParaRecord);
		// 告警码+告警子码的组合必须唯一
		return alarmIdAndfaultCode.equals(alarmIdAndfaultCode_);
	}
	
	/**
	 * 获取告警参数记录中的告警码和告警字码的组合
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
	 * SPS配置表中的上下行SPS开关不能与DRX配置表中的DRX功能同时打开
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
	 * 校验全网不能重复的字段
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkUnique(long moId, XBizRecord bizRecord, String tableName,
			String tableParam) throws Exception {
		
		int value = bizRecord.getIntValue(tableParam);
		int cid = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		
		// 先获取旧的value
		int oldValue = EnbBizUniqueIdHelper.getValueByKey(enbId, cid,
				tableName, tableParam);
		
		// 如果本次传入value和旧的value相同,则表示该字段本次修改时未改变,则通过校验
		if (value == oldValue) {
			return;
		}
		
		// 查询所有已经被使用的value
		List<Integer> valueList = EnbBizUniqueIdHelper.queryAll(tableName,
				tableParam);
		
		// 如果要修改的value已经被占用则返回错误
		if (valueList.contains(value)) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_exist"));
		}
		
	}
	
	/**
	 * 校验全网不能重复的字段,以前为该值若重复也不可修改
	 * 
	 * @param bizRecord
	 * @throws Exception
	 */
	public void checkUniqueM(long moId, XBizRecord bizRecord, String tableName,
			String tableParam) throws Exception {
		
		int newValue = bizRecord.getIntValue(tableParam);
		int cid = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_CELL_ID);
		
		long enbId = EnbBizUniqueIdHelper.getEnbIdByMoId(moId);
		
		// 先获取旧的value
		int oldValue = EnbBizUniqueIdHelper.getValueByKey(enbId, cid,
				tableName, tableParam);
		
		// 判断该旧的value有多少小区在使用
		// 查询使用该value小区的个数
		int sameOldNum = EnbBizUniqueIdHelper.queryNumSameValue(tableName,tableParam,oldValue);
		
		// 如果本次传入value和旧的value相同,则表示该字段本次修改时未改变,并且无其他小区使用该字段
		if (newValue == oldValue && sameOldNum <= 1) {
			return;
		}
		
		// 如果要修改的value已经被占用则返回错误
		int sameNewNum = EnbBizUniqueIdHelper.queryNumSameValue(tableName,tableParam,newValue);
		if (sameNewNum > 0) {
			throw new Exception(OmpAppContext.getMessage(tableName + "_"
					+ tableParam + "_exist_other_cell"));
		}
		
	}
	
	

	/**
	 * 判断小区算法参数表2中的pk mode是否为PingPkMode(2)
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
	 * T_ENB_PARA表中EEA不为x'000000'时,T_CEL_ALG2表u8PkMode字段不能配置为Ping Pk Mode
	 * @param moId
	 * @param pkMode
	 * @return
	 * @throws Exception 
	 */
	public boolean checkEeaAndPkMode(long moId, int pkMode) throws Exception {
		// 查询T_ENB_PARA数据
		XBizTable enbParaTable = enbBizConfigDAO.query(moId, EnbConstantUtils.TABLE_NAME_T_ENB_PARA, null);
		XBizRecord record = enbParaTable.getRecords().get(0);
		String eea = record.getStringValue(EnbConstantUtils.FIELD_NAME_EEA);
		if(!"000000".equals(eea) && 2 == pkMode) {
			return false;
		}
		return true;
	}

	/**
	 * T_ENB_SRVQCI中u8RohcRTP、u8RohcUDP、u8RohcIP配置为1时,T_CEL_ALG2表u8PkMode字段不能为Ping Pk Mode
	 * @param moId
	 * @param pkMode
	 * @return
	 */
	public boolean checkRohcAndPkMode(long moId, int pkMode) throws Exception {
		// 查询T_ENB_SRVQCI数据
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
