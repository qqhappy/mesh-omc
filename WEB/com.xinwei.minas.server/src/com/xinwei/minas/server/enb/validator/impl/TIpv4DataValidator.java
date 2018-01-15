/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * IPv4表数据校验类
 * 
 * <p>
 * 配置多条记录时，ipAddr 值必须唯一</br> 网关地址必须与ip地址在同一网段
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TIpv4DataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			// 网关与IP必须在同一网段
			String newIp = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
			String mask = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			String gateway = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();
			// 网关地址必须与IP地址在同一网段
			if (!validateHelper.checkSameNetFragment(newIp, mask, gateway)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_GATEWAY,
						"gateway_must_same_net_with_ip");
			}
			// 校验添加或修改的IP在当前系统中是否存在
			checkIpDuplicated(moId, bizRecord);
			// 与网管表相关联的IPV4表中端口标识不可改变
			// checkRelatedEthnetPortIdChanged(moId, bizRecord);
		}

	}

	/**
	 * 校验添加或修改的IP在当前系统中是否存在
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkIpDuplicated(long moId, XBizRecord bizRecord)
			throws Exception {

		String currentIpId = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_IP_ID).getValue();

		List<Enb> enbList = EnbCache.getInstance().queryAll();
		Set<String> ipSet = new HashSet<String>();
		// 获取系统中所有基站ip
		for (Enb enb : enbList) {
			List<XBizRecord> records = validateHelper.queryRecords(
					enb.getMoId(), EnbConstantUtils.TABLE_NAME_T_IPV4, null);
			if (records == null || records.isEmpty())
				continue;
			for (XBizRecord record : records) {
				// 跳过当前站当前修改的记录
				if (enb.getMoId() == moId) {
					String ipId = record.getFieldBy(
							EnbConstantUtils.FIELD_NAME_IP_ID).getValue();
					if (ipId.equals(currentIpId))
						continue;
				}
				String ip = record.getFieldBy(
						EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
				ipSet.add(ip);
			}
		}

		String currentIp = bizRecord.getFieldBy(
				EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
		// ip已使用，请更换
		if (ipSet.contains(currentIp)) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_IP_ADDR,
					"ip_occupied_please_change");
		}
	}

	// /**
	// * 与网管表相关联的IPV4表中端口标识不可改变
	// *
	// * @param moId
	// * @param bizRecord
	// * @throws Exception
	// */
	// private void checkRelatedEthnetPortIdChanged(long moId, XBizRecord
	// bizRecord)
	// throws Exception {
	//
	// XBizRecord ipv4Record = validateHelper.queryOmcRecord(moId);
	// if (ipv4Record != null) {
	// XBizField portIdFieldNew = bizRecord
	// .getFieldBy(EnbConstantUtils.FIELD_NAME_IP_ID);
	// XBizField portIdField = ipv4Record
	// .getFieldBy(EnbConstantUtils.FIELD_NAME_ENB_IP_ID);
	// int portId = Integer.valueOf(portIdField.getValue());
	// int portIdNew = Integer.valueOf(portIdFieldNew.getValue());
	// if (portId == portIdNew) {
	// // 查询库中的端口标识:根据Ip标识查询端口标识相互比较
	// XBizRecord oldIpV4Record = validateHelper.queryIpv4Record(moId,
	// portIdNew);
	// if (oldIpV4Record != null) {
	// // 库中旧的端口号
	// XBizField oldU8PortID = oldIpV4Record
	// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
	// int oldU8PortIDInt = Integer
	// .valueOf(oldU8PortID.getValue());
	// // 传递过来的端口号
	// XBizField newU8PortID = bizRecord
	// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
	// int newU8PortIDInt = Integer
	// .valueOf(newU8PortID.getValue());
	// if (oldU8PortIDInt != newU8PortIDInt) {
	// throw validateHelper
	// .newBizException(portIdFieldNew.getName(),
	// "portId_of_record_related_with_omc_cannot_change");
	// }
	// }
	//
	// }
	// }
	// }

}
