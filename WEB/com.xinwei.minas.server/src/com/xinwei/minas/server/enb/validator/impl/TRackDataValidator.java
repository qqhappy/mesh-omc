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
 * 拓扑表数据校验类
 * <p>
 * (1)要求topo 表中的前面的架框槽 和 后面的架框槽不一样，不可能自己连接自己，针对当前站型，建议固定1、1、1、1
 * 、0、2、1、1，可以放在模板数据中 (2)对于同一块单板（架框槽）在多条记录中FiberPort 必须唯一，一个端口只能连接一个设备
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
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		checkRackNameDuplicated(moId, bizRecord);
	}

	/**
	 * 校验机架名称是否重复，同一基站下机架名称不能相同
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
