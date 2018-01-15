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
 * ���˱�����У����
 * <p>
 * (1)Ҫ��topo ���е�ǰ��ļܿ�� �� ����ļܿ�۲�һ�����������Լ������Լ�����Ե�ǰվ�ͣ�����̶�1��1��1��1
 * ��0��2��1��1�����Է���ģ�������� (2)����ͬһ�鵥�壨�ܿ�ۣ��ڶ�����¼��FiberPort ����Ψһ��һ���˿�ֻ������һ���豸
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TRackDataValidator implements EnbBizDataValidator {

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

		checkRackNameDuplicated(moId, bizRecord);
	}

	/**
	 * У����������Ƿ��ظ���ͬһ��վ�»������Ʋ�����ͬ
	 * 
	 * @throws Exception
	 */
	private void checkRackNameDuplicated(long moId, XBizRecord bizRecord)
			throws Exception {
		String currentName = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_RACK_NAME).getValue();
		String currentRack = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_RACK, null);
		if (records == null)
			return;
		for (XBizRecord xBizRecord : records) {
			String rackNo = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
			if (rackNo.equals(currentRack))
				continue;
			String name = xBizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_RACK_NAME).getValue();
			if (currentName.equals(name)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_RACK_NAME,
						"rack_name_duplicated");
			}
		}
	}

}
