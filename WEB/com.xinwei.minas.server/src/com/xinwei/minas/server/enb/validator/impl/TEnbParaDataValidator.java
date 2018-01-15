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
 * eNB参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbParaDataValidator implements EnbBizDataValidator {

	// 集群开关打开时，T_CEL_SISCH表中每一个小区的所有SI记录必须至少有一条记录包含u8SibPtt
	// T_CEL_ALG2表中有一条记录的u8Pkmode取值为Ping Pk
	// Mode时,T_ENB_PARA表au8EEA[3]取值须为x‘000000’

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
			// ABC类参数校验判断
			if (ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
				boolean pttEnable = validateHelper.isPttEnabled(bizRecord);
				if (pttEnable) {
					// 获取当前基站所有小区ID
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

						// 判断基站是否包含小区算法表2且小区算法表2中是否有u8pkmode字段
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
