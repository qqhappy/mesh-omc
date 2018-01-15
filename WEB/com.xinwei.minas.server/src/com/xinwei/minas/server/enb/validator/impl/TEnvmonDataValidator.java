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
 * ������ر�����У����
 * <p>
 * ������ر��еļܿ��+u32EnvMType ����Ψһ
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
		// �ñ�����У��ɾ������
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_ENVMON, null);
		if (records == null)
			return;
		// �ܡ��򡢲ۡ�����������͵���ϲ����ظ�
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
