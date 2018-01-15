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
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * 以太网参数表数据校验类
 * 
 * <p>
 * 配置多条记录时，架框槽+port 号必须唯一
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TEthparaDataValidator implements EnbBizDataValidator {

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
				EnbConstantUtils.TABLE_NAME_T_ETHPARA, null);
		if (records != null && !records.isEmpty()) {
			if (actionType.equals(ActionTypeDD.ADD)) {
				// 校验架、框、槽、端口号的组合是否重复
				checkEthenetRecordDuplicated(bizRecord, records);
			} else if (actionType.equals(ActionTypeDD.MODIFY)) {
				// 与网管表相关联的以太网参数表中架框槽必须保持1-1-1不变
				checkEthenetRelatedOmcRecord(moId, bizRecord);
				// 校验架、框、槽、端口号的组合是否重复
				checkEthenetRecordDuplicated(bizRecord, records);
			}
		}

	}

	/**
	 * 校验架、框、槽、端口号的组合是否重复
	 * 
	 * @param record
	 * @param records
	 * @throws Exception
	 */
	private void checkEthenetRecordDuplicated(XBizRecord record,
			List<XBizRecord> records) throws Exception {
		int[] array = validateHelper.getRackShelfSlotNo(record);
		String currentPort = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_ETH_PORT).getValue();
		String currentUnion = array[0] + splitFlag + array[1] + splitFlag
				+ array[2] + splitFlag + currentPort;
		String currentPortId = record.getFieldBy(
				EnbConstantUtils.FIELD_NAME_PORT_ID).getValue();
		for (XBizRecord bizRecord : records) {
			// 跳过本身
			String portId = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_PORT_ID).getValue();
			if (portId.equals(currentPortId))
				continue;
			int[] noArray = validateHelper.getRackShelfSlotNo(bizRecord);
			String port = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_ETH_PORT).getValue();
			String union = noArray[0] + splitFlag + noArray[1] + splitFlag
					+ noArray[2] + splitFlag + port;
			if (currentUnion.equals(union)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_PORT_ID,
						"rack_shelf_slot_port_cannot_duplicated");
			}
		}
	}

	/**
	 * 与网管表相关联的以太网参数表中架框槽必须保持1-1-1不变
	 * 
	 * @param moId
	 * @param record
	 * @param actionType
	 */
	private void checkEthenetRelatedOmcRecord(long moId, XBizRecord record)
			throws Exception {
		// 查找与网管表相关的以太网参数记录
		XBizRecord ethRecord = queryRelatedEthernetRecord(moId);
		if (ethRecord == null)
			return;
		XBizField portIdFieldOfEth = ethRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portIdOfEth = Integer.valueOf(portIdFieldOfEth.getValue());
		XBizField portIdField = record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		// 判断是否是与网管表相关联的记录
		if (portId == portIdOfEth) {
			// 与网管表相关联的以太网参数表中的架框槽必须保持1,1,1不变
			int[] noArray = validateHelper.getRackShelfSlotNo(record);
			boolean unchanged = noArray[0] == 1 && noArray[1] == 1
					&& noArray[2] == 1;
			if (!unchanged) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_RACKNO,
						"port_config_of_related_record_in_ipv4_must_be_111");
			}
		}
	}

	/**
	 * 查询与网管表相关联的以太网参数表中的记录
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private XBizRecord queryRelatedEthernetRecord(long moId) throws Exception {
		XBizRecord omcRecord = validateHelper.queryOmcRecord(moId);
		if (omcRecord == null)
			return null;
		XBizField ipIdFieldOfOmc = omcRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
		int ipId = Integer.valueOf(ipIdFieldOfOmc.getValue());
		XBizRecord ipv4Record = validateHelper.queryIpv4Record(moId, ipId);
		// IPV4中与网管表相关联记录不存在
		if (ipv4Record == null) {
			return null;
		}
		XBizField portIdField = ipv4Record
				.getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
		int portId = Integer.valueOf(portIdField.getValue());
		return validateHelper.queryEthernetRecord(moId, portId);
	}

}
