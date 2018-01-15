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
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * 小区下行功控参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelDlpcDataValidator implements EnbBizDataValidator {

	// (1)u16CellSpeRefSigPwr可配置的最大值由如下公式决定：
	// CRS_EPRE_max = u16CellTransPwr - 10*log10(T_CEL_PARA.u8DlAntPortNum) +
	// ERS_nor，
	// 公式中涉及的字段都是按照界面值（P）来计算，其中ERS_nor查表可得，表格见附件
	// (2)T_ENB_SRVPC.u8PA不能超过u8PAForDTCH字段的值
	// u8PtForPDSCH值不能超过T_CEL_DLPC表中所有记录中u8PAForDTCH字段的最小值

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
		List<XBizRecord> records = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (records != null) {
			XBizRecord cellParaRecord = records.get(0);
			validateHelper.checkCellParaAndDlpc(enb.getEnbType(), enb.getProtocolVersion(),
					bizRecord, cellParaRecord, true);
		}

		//validateHelper.checkPAAndPAForDTCH(moId, bizRecord, false);

		List<XBizRecord> pttRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_PTT);
		if (pttRecords != null) {
			XBizRecord pttRecord = pttRecords.get(0);
			//validateHelper.checkPtForPDSCHAndPAForDTCH(pttRecord, bizRecord,
			//		true);
		}

	}
}
