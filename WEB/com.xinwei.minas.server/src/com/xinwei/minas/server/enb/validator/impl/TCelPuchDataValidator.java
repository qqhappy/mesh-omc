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
 * 小区上行功控参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPuchDataValidator implements EnbBizDataValidator {
	
	// u8DeltaPucchShift、u8N_RB2、u16N_SrChn与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：u8N_RB2+(u16N_SrChn\(36\(u8DeltaPucchShift)))不能超过u8SysBandWidth代表的最大RB个数
	// 当T_CEL_PARA表中的u8UlDlSlotAlloc为2时，u8SRITransPrd与u8RptCqiPrd不能同时为5ms
	// （1）当T_CEL_PARA表u8SysBandWidth配置为5M时，u8SrsBwCfgIndex不能配置为0、1
	// （2）当T_CEL_PARA表u8SysBandWidth配置为3M时，u8SrsBwCfgIndex只能配置5-7
	// （3）当T_CEL_PARA表u8SysBandWidth配置为1.4M时，u8SrsBwCfgIndex只能配置7
	
	private EnbBizDataValidateHelper validateHelper;
	
	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}
	
	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;
		
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// 查询小区参数表对应的记录
		List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (cellParaRecords != null) {
			XBizRecord cellParaRecord = cellParaRecords.get(0);
			validateHelper.checkCellParaAndPuch(enb.getEnbType(),
					enb.getProtocolVersion(), cellParaRecord, bizRecord, true);
			
			// 获取带宽内存值
			int sysBandWidth = validateHelper.getIntFieldValue(cellParaRecord,
					EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// SRS带宽配置
			int srsBwCfgIndex = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_SRS_BW_CFG_INDEX);
			
			validateHelper.checkSysBandWidthAndSrsBwCfgIndex(
					enb.getEnbType(), enb.getProtocolVersion(),
					sysBandWidth, srsBwCfgIndex, false);
			
			// 版本兼容性处理
			boolean hasSps = EnbBizHelper.hasBizMeta(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
			if (hasSps) {
				List<XBizRecord> spsRecords = validateHelper
						.queryRecordsByCellId(enb.getMoId(), cellId,
								EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
				if (spsRecords != null) {
					XBizRecord spsRecord = spsRecords.get(0);
					// 查询老数据
					List<XBizRecord> oldPuchRecords = validateHelper.queryRecordsByCellId(
							enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
					XBizRecord oldPuchRecord = oldPuchRecords.get(0);
					String targetField = EnbConstantUtils.FIELD_NAME_DELTA_PUCCH_SHIFT;
					// 判断哪个参数被修改
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
