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
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * DRX���ò�����У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelDrxValidator implements EnbBizDataValidator {
	// SPS���ñ��е�������SPS���ز�����DRX���ñ��е�DRX����ͬʱ��

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

		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID_);
		List<XBizRecord> spsRecords = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_SPS);
		if (spsRecords != null) {
			XBizRecord spsRecord = spsRecords.get(0);
			validateHelper.checkDrxSwitchAndSpsLinkSwitch(bizRecord, spsRecord,
					true);
		}
	}

}
