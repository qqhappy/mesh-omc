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
 * eNB����������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbParaDataValidator implements EnbBizDataValidator {

	// ��Ⱥ���ش�ʱ��T_CEL_SISCH����ÿһ��С��������SI��¼����������һ����¼����u8SibPtt
	// T_CEL_ALG2������һ����¼��u8PkmodeȡֵΪPing Pk
	// Modeʱ,T_ENB_PARA��au8EEA[3]ȡֵ��Ϊx��000000��

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			// ABC�����У���ж�
			if (ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
				boolean pttEnable = validateHelper.isPttEnabled(bizRecord);
				if (pttEnable) {
					// ��ȡ��ǰ��վ����С��ID
					List<Integer> cellIds = validateHelper
							.getAllCellIdsByMoId(moId);
					if (cellIds == null || cellIds.isEmpty()) {
						return;
					}
					for (Integer cellId : cellIds) {
						boolean pttSibContained = validateHelper
								.isPttSibContained(moId, cellId);
						if (!pttSibContained) {
							throw validateHelper
									.newBizException(
											EnbConstantUtils.FIELD_NAME_PTT_ENABLE,
											"sisch_record_of_all_cell_must_contain_pptsib");
						}

						// �жϻ�վ�Ƿ����С���㷨��2��С���㷨��2���Ƿ���u8pkmode�ֶ�
						if (EnbBizHelper.hasBizMeta(enb.getEnbType(), enb.getProtocolVersion(),
								EnbConstantUtils.TABLE_NAME_T_CEL_ALG2)
								&& EnbBizHelper.fieldIsExist(enb.getEnbType(),
										enb.getProtocolVersion(),
										EnbConstantUtils.TABLE_NAME_T_CEL_ALG2,
										EnbConstantUtils.FIELD_NAME_PK_MODE)) {
							boolean isPingPkMode = validateHelper.isPingPkMode(
									moId, cellId);
							if (isPingPkMode) {
								String eea = bizRecord
										.getStringValue(EnbConstantUtils.FIELD_NAME_EEA);
								if (!"000000".equals(eea)) {
									throw validateHelper.newBizException(
											EnbConstantUtils.FIELD_NAME_EEA,
											"is_ping_pk_mode_eea_must_zero");
								}
							}
						}
					}
				}
			}

		}
	}

}
