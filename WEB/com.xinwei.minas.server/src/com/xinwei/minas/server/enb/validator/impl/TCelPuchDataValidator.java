/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * С�����й��ز���������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPuchDataValidator implements EnbBizDataValidator {
	
	// u8DeltaPucchShift��u8N_RB2��u16N_SrChn��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))���ܳ���u8SysBandWidth��������RB����
	// ��T_CEL_PARA���е�u8UlDlSlotAllocΪ2ʱ��u8SRITransPrd��u8RptCqiPrd����ͬʱΪ5ms
	// ��1����T_CEL_PARA��u8SysBandWidth����Ϊ5Mʱ��u8SrsBwCfgIndex��������Ϊ0��1
	// ��2����T_CEL_PARA��u8SysBandWidth����Ϊ3Mʱ��u8SrsBwCfgIndexֻ������5-7
	// ��3����T_CEL_PARA��u8SysBandWidth����Ϊ1.4Mʱ��u8SrsBwCfgIndexֻ������7
	
	private EnbBizDataValidateHelper validateHelper;
	
	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}
	
	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// �ñ�����У��ɾ������
		if (actionType.equals(ActionTypeDD.DELETE))
			return;
		
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// ��ѯС���������Ӧ�ļ�¼
		List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (cellParaRecords != null) {
			XBizRecord cellParaRecord = cellParaRecords.get(0);
			validateHelper.checkCellParaAndPuch(enb.getEnbType(),
					enb.getProtocolVersion(), cellParaRecord, bizRecord, true);
			
			// ��ȡ�����ڴ�ֵ
			int sysBandWidth = validateHelper.getIntFieldValue(cellParaRecord,
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// SRS��������
			int srsBwCfgIndex = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SRS_BW_CFG_INDEX);
			
			validateHelper.checkSysBandWidthAndSrsBwCfgIndex(
					enb.getEnbType(), enb.getProtocolVersion(),
					sysBandWidth, srsBwCfgIndex, false);
			
			// �汾�����Դ���
			boolean hasSps = EnbBizHelper.hasBizMeta(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
			if (hasSps) {
				List<XBizRecord> spsRecords = validateHelper
						.queryRecordsByCellId(enb.getMoId(), cellId,
								EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
				if (spsRecords != null) {
					XBizRecord spsRecord = spsRecords.get(0);
					// ��ѯ������
					List<XBizRecord> oldPuchRecords = validateHelper.queryRecordsByCellId(
							enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
					XBizRecord oldPuchRecord = oldPuchRecords.get(0);
					String targetField = EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT;
					// �ж��ĸ��������޸�
					int u8PucchBlankRBNumOld = oldPuchRecord.getIntValue("u8PucchBlankRBNum");
					int u8PucchBlankRBNumNew = bizRecord.getIntValue("u8PucchBlankRBNum");
					int u8N_RB2ValueOld = validateHelper.getIntFieldValue(oldPuchRecord,
							EnbConstantUtils.FIELD_NAME_RB2);
					int u8N_RB2ValueNew = validateHelper.getIntFieldValue(bizRecord,
							EnbConstantUtils.FIELD_NAME_RB2);
					int u16N_SrChnValueOld = validateHelper.getIntFieldValue(oldPuchRecord,
							EnbConstantUtils.FIELD_NAME_SR_CHN);
					int u16N_SrChnValueNew = validateHelper.getIntFieldValue(bizRecord,
							EnbConstantUtils.FIELD_NAME_SR_CHN);
					if (u8PucchBlankRBNumOld != u8PucchBlankRBNumNew) {
						targetField = "u8PucchBlankRBNum";
					}
					else if (u8N_RB2ValueOld != u8N_RB2ValueNew) {
						targetField = EnbConstantUtils.FIELD_NAME_RB2;
					}
					else if (u16N_SrChnValueOld != u16N_SrChnValueNew) {
						targetField = EnbConstantUtils.FIELD_NAME_SR_CHN;
					}
					
					validateHelper.checkSpsANChnAndSysBandWidthAndPuch(
							enb.getEnbType(), enb.getProtocolVersion(),
							sysBandWidth, spsRecord, bizRecord, targetField);
				}
				
			}
			else {
				
				validateHelper.checkSysBandWidthAndPuch(enb.getEnbType(),
						enb.getProtocolVersion(), cellParaRecord, bizRecord,
						true);
			}
			
		}
	}
}
