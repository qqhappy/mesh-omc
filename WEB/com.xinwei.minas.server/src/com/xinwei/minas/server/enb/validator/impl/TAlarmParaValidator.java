/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-12	| liupengjie 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * �澯������У����
 * 
 * @author liupengjie
 * 
 */

public class TAlarmParaValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord alarmParaRecord,
			String actionType) throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			
			// �澯��+�澯�������ϱ���Ψһ
			String alarmIdAndfaultCode = validateHelper
					.getAlarmIdAndFaultCode(alarmParaRecord);

			List<XBizRecord> records = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_ALARM_PARA, null);
			if (records != null) {
				for (XBizRecord xBizRecord : records) {
					// ����ǵ�ǰ��¼��������
					if (isCurrentRecord(moId, alarmParaRecord, xBizRecord))
						continue;

					boolean duplicated = validateHelper
							.checkCellAlarmIdAndFaultCodeDuplicated(
									alarmIdAndfaultCode, xBizRecord);

					if (duplicated) {
						throw validateHelper
								.newBizException(
										EnbConstantUtils.FIELD_NAME_ALARMID,
										"T_ALARM_PAPA_alarmId_faultCode_combination_is_not_unique");
					}

				}
			}
		}

	}

	private boolean isCurrentRecord(long moId, XBizRecord currentRecord,
			XBizRecord bizRecord) {

		XBizRecord currentKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_ALARM_PARA, currentRecord);
		XBizRecord bizKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_ALARM_PARA, bizRecord);

		return currentKey.equals(bizKey);
	}
}
