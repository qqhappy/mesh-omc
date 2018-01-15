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
 * �������ñ�����У����
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbMeascfgDataValidator implements EnbBizDataValidator {

	// (1)С����������u8IntraFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3��
	// u8A2ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2��
	// u8A1ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1��
	// u8A2ForRedirectMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2��
	// u8IcicA3MeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3��
	// (2)T_ENB_CTFREQ.u8InterFreqHOMeasCfg��ָʾ��u8EvtId����Ϊ3

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
				// u8IntraFreqHOMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
				String fieldName = EnbConstantUtils.FIELD_NAME_INTRA_FREQ_HO_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A2ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
				fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_INTER_FREQ_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A1ForInterFreqMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ1
				fieldName = EnbConstantUtils.FIELD_NAME_A1_FOR_INTER_FREQ_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8A2ForRedirectMeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ2
				fieldName = EnbConstantUtils.FIELD_NAME_A2_FOR_REDIRECT_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
				// u8IcicA3MeasCfg��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3
				fieldName = EnbConstantUtils.FIELD_NAME_ICIC_A3_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, cellParaRecord,
						bizRecord, true);
			}

			List<XBizRecord> ctfReqRecords = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_ENB_CTFREQ, null);
			if (ctfReqRecords != null) {
				for (XBizRecord ctfReqRecord : ctfReqRecords) {
					// (2)T_ENB_CTFREQ.u8InterFreqHOMeasCfg��ָʾ��u8EvtId����Ϊ3
					String fieldName = EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG;
					validateHelper.checkMeasCfgOk(fieldName, ctfReqRecord,
							bizRecord, true);
				}
			}
		}
	}

}
