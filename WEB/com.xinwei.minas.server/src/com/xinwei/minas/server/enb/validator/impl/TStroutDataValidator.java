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
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��̬·�ɱ�����У����
 * 
 * <p>
 * ���õ���һ����ַ�����������ַ��ͬһ���Σ�����T_IPV4 �е���һip��ַ��ͬһ����
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TStroutDataValidator implements EnbBizDataValidator {

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

		String nextHop = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_NEXT_HOP).getValue();
		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, null);
		// ���������IPv4���¼
		if (records == null) {
			throw new Exception(
					OmpAppContext.getMessage("please_add_ipv4_record_first"));
		}
		boolean ok = false;
		for (XBizRecord record : records) {
			String ip = record.getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ADDR)
					.getValue();
			String mask = record.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			if (validateHelper.checkSameNetFragment(ip, mask, nextHop)) {
				ok = true;
				break;
			}
		}
		// ��IPv4���е�����һ����¼��ͬһ����
		if (!ok) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_NEXT_HOP,
					"strout_nexthop_must_same_net_with_ipv4");
		}

	}

}
