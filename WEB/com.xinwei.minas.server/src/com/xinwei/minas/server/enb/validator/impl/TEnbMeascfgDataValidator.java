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
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * 测量配置表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbMeascfgDataValidator implements EnbBizDataValidator {

	// (1)小区参数表中u8IntraFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3；
	// u8A2ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2；
	// u8A1ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1；
	// u8A2ForRedirectMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2；
	// u8IcicA3MeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3；
	// (2)T_ENB_CTFREQ.u8InterFreqHOMeasCfg所指示的u8EvtId必须为3

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {

		if (actionType.equals(ActionTypeDD.MODIFY)) {

			List<XBizRecord> cellParaRecords = validateHelper.queryRecords(
					moId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
			if (cellParaRecords == null)
				return;
			for (XBizRecord cellParaRecord : cellParaRecords) {
				// u8IntraFreqHOMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
				String fieldName = EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A2ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
				fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A1ForInterFreqMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为1
				fieldName = EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A2ForRedirectMeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为2
				fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8IcicA3MeasCfg所指示的T_ENB_MEASCFG表记录的u8EvtId必须为3
				fieldName = EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
			}

			List<XBizRecord> ctfReqRecords = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ, null);
			if (ctfReqRecords != null) {
				for (XBizRecord ctfReqRecord : ctfReqRecords) {
					// (2)T_ENB_CTFREQ.u8InterFreqHOMeasCfg所指示的u8EvtId必须为3
					String fieldName = EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG;
					validateHelper.checkMeasCfgOk(fieldName, ctfReqRecord,
							bizRecord, true);
				}
			}
		}
	}

}
