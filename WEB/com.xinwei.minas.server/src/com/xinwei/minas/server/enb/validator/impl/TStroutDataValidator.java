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
 * 静态路由表数据校验类
 * 
 * <p>
 * 配置的下一跳地址必须跟本机地址在同一网段，即跟T_IPV4 中的任一ip地址在同一网段
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
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		String nextHop = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_NEXT_HOP).getValue();
		List<XBizRecord> records = validateHelper.queryRecords(moId,
				EnbConstantUtils.TABLE_NAME_T_IPV4, null);
		// 必须先添加IPv4表记录
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
		// 与IPv4表中的其中一条记录在同一网段
		if (!ok) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_NEXT_HOP,
					"strout_nexthop_must_same_net_with_ipv4");
		}

	}

}
