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
			// ��ȡ����ܿ��
			int[] mainArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
					topoRecord, true);
			// ��ȡ�Ӱ�ܿ��
			int[] slaveArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
					topoRecord, false);
			// ���ܿ�ۺʹӼܿ�۲�����ͬ
			if (mainArray[0] == slaveArray[0] && mainArray[1] == slaveArray[1]
					&& mainArray[2] == slaveArray[2]) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_MRACKNO,
						"topo_record_main_subordinate_cannot_equal");
			}

			// �жϴӵ����Ƿ���RRU
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
				// ��ȡ����ܿ��
				int[] mArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
						record, true);
				// ��ȡ�Ӱ�ܿ��
				int[] sArray = validateHelper.getRackShelfSlotNoOfTopoRecord(
						record, false);
				String fiberPort = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_FIBER_PORT).getValue();

				// ��ں��Ƿ�ռ��
				if (currentFiberPort.equals(fiberPort)) {
					throw validateHelper.newBizException(
							EnbConstantUtils.FIELD_NAME_FIBER_PORT,
							"fiberport_already_used");
				} else {
					// ��ںŲ�ͬʱ���Ӱ���벻ͬ
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
