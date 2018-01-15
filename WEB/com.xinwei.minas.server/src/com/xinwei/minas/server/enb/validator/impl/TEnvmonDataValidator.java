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
 * 环境监控表数据校验类
 * <p>
 * 环境监控表中的架框槽+u32EnvMType 必须唯一
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TEnvmonDataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	private static final String splitFlag = "#";

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_ENVMON, null);
		if (records == null)
			return;
		// 架、框、槽、环境监控类型的组合不能重复
		int[] array = validateHelper.getRackShelfSlotNo(bizRecord);
		String currentNo = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENV_M_NO).getValue();
		String currentType = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ENV_M_TYPE).getValue();
		String currentUnion = array[0] + splitFlag + array[1] + splitFlag
				+ array[2] + splitFlag + currentType;
		for (XBizRecord record : records) {
			String no = record.getFieldBy(EnbConstantUtils.FIELD_NAME_ENV_M_NO)
					.getValue();
			if (no.equals(currentNo))
				continue;
			int[] noArray = validateHelper.getRackShelfSlotNo(record);
			String type = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_ENV_M_TYPE).getValue();
			String union = noArray[0] + splitFlag + noArray[1] + splitFlag
					+ noArray[2] + splitFlag + type;
			if (currentUnion.equals(union)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_ENV_M_TYPE,
						"rack_shelf_slot_envmtype_cannot_duplicated");
			}
		}

	}

}
