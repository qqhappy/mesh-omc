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
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * ������Ƶ���ñ�����У����
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbCtfreqDataValidator implements EnbBizDataValidator {

	// u8InterFreqHOMeasCfg ��ֵ��ָʾ��T_ENB_MEASCFG���¼��u8EvtId����Ϊ3

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

		XBizRecord condition = new XBizRecord();
		String meascfgIdx = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG).getValue();
		condition.addField(new XBizField(
				EnbConstantUtils.FIELD_NAME_MEAS_CFG_IDX, meascfgIdx));

		List<XBizRecord> measCfgRecords = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_ENB_MEASCFG, condition);

		if (measCfgRecords != null) {
			for (XBizRecord measCfgRecord : measCfgRecords) {
				String fieldName = EnbConstantUtils.FIELD_NAME_INTER_FREQ_HO_MEAS_CFG;
				validateHelper.checkMeasCfgOk(fieldName, bizRecord,
						measCfgRecord, false);
			}
		}

	}

}
