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

public class TTopoDataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord topoRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			// 获取主板架框槽
			int[] mainArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
					topoRecord, true);
			// 获取从板架框槽
			int[] slaveArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
					topoRecord, false);
			// 主架框槽和从架框槽不能相同
			if (mainArray[0] == slaveArray[0] && mainArray[1] == slaveArray[1]
					&& mainArray[2] == slaveArray[2]) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_MRACKNO,
						"topo_record_main_subordinate_cannot_equal");
			}

			// 判断从单板是否是RRU
			// int slaveBoardType = validateHelper.getBoardType(moId,
			// slaveArray[0], slaveArray[1], slaveArray[2]);
			// if (slaveBoardType != EnbConstantUtils.BOARD_TYPE_RRU) {
			// throw validateHelper.newBizException(
			// EnbConstantUtils.FIELD_NAME_SRACKNO,
			// "topo_record_slave_board_must_be_rru");
			// }

			String currentFiberPort = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();
			String currentTopoNo = topoRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
			List<XBizRecord> records = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_TOPO, null);
			if (records == null)
				return;
			for (XBizRecord record : records) {
				String topoNo = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_TOPO_NO).getValue();
				if (currentTopoNo.equals(topoNo))
					continue;
				// 获取主板架框槽
				int[] mArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
						record, true);
				// 获取从板架框槽
				int[] sArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
						record, false);
				String fiberPort = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();

				// 光口号是否被占用
				if (currentFiberPort.equals(fiberPort)) {
					throw validateHelper.newBizException(
							EnbConstantUtils.FIELD_NAME_FIBER_PORT,
							"fiberport_already_used");
				} else {
					// 光口号不同时，从板必须不同
					if (slaveArray[0] == sArray[0]
							&& slaveArray[1] == sArray[1]
							&& slaveArray[2] == sArray[2]) {
						throw validateHelper
								.newBizException(
										EnbConstantUtils.FIELD_NAME_SRACKNO,
										"different_fiberport_must_connect_different_board");
					}
				}
				if (mainArray[0] == mArray[0] && mainArray[1] == mArray[1]
						&& mainArray[2] == mArray[2]
						&& currentFiberPort.equals(fiberPort)) {
					throw validateHelper.newBizException(
							EnbConstantUtils.FIELD_NAME_MRACKNO,
							"topo_record_main_fiberport_cannot_equal");
				}

			}

		}

	}

}
