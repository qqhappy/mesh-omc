/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-27	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * SPS配置参数表校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelSpsValidator implements EnbBizDataValidator {
	// SPS配置表中的上下行SPS开关不能与DRX配置表中的DRX功能同时打开

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

		// ABC类参数校验判断
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID_);
		List<XBizRecord> drxRecords = validateHelper.queryRecordsByCellId(
				moId, cellId, EnbConstantUtils.TABLE_NAME_T_CEL_DRX);
		if (drxRecords != null) {
			XBizRecord drxRecord = drxRecords.get(0);
			validateHelper.checkDrxSwitchAndSpsLinkSwitch(drxRecord,
					bizRecord, false);
		}
		
		checkSpsANChnAndSysBandWidthAndPuch(moId, cellId, bizRecord);
	}

	// u16N_SpsANChn与T_CEL_PUCH表的u8DeltaPucchShift、u8N_RB2、u16N_SrChn以及
	// T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	// u8N_RB2+((u16N_SrChn+ u16N_SpsANChn)\(36\(u8DeltaPucchShift)))
	// 不能超过u8SysBandWidth代表的最大RB个数
	private void checkSpsANChnAndSysBandWidthAndPuch(long moId, int cellId,
			XBizRecord spsRecord) throws Exception {

		List<XBizRecord> puchRecords = validateHelper.queryRecordsByCellId(
				moId, cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
		if (puchRecords != null) {
			XBizRecord puchRecord = puchRecords.get(0);
			List<XBizRecord> cellRecords = validateHelper.queryRecordsByCellId(
					moId, cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			if (cellRecords != null) {
				XBizRecord cellRecord = cellRecords.get(0);
				Enb enb = EnbCache.getInstance().queryByMoId(moId);
				// 获取带宽内存值
				int sysBandWidth = validateHelper.getIntFieldValue(cellRecord,
						EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
				validateHelper.checkSpsANChnAndSysBandWidthAndPuch(
						enb.getEnbType(),
						enb.getProtocolVersion(), sysBandWidth, spsRecord,
						puchRecord, EnbConstantUtils.FIELD_NAME_N_SPSANCHN);

			}
		}

	}

}
