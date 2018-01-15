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
 * IPv4������У����
 * 
 * <p>
 * ���ö�����¼ʱ��ipAddr ֵ����Ψһ</br> ���ص�ַ������ip��ַ��ͬһ����
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
			// ������IP������ͬһ����
			String newIp = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_IP_ADDR).getValue();
			String mask = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_NET_MASK).getValue();
			String gateway = bizRecord.getFieldBy(
					EnbConstantUtils.FIELD_NAME_GATEWAY).getValue();
			// ���ص�ַ������IP��ַ��ͬһ����
			if (!validateHelper.checkSameNetFragment(newIp, mask, gateway)) {
				throw validateHelper.newBizException(
						EnbConstantUtils.FIELD_NAME_GATEWAY,
						"gateway_must_same_net_with_ip");
			}
			// У����ӻ��޸ĵ�IP�ڵ�ǰϵͳ���Ƿ����
			checkIpDuplicated(moId, bizRecord);
			// �����ܱ��������IPV4���ж˿ڱ�ʶ���ɸı�
			// checkRelatedEthnetPortIdChanged(moId, bizRecord);
		}

	}

	/**
	 * У����ӻ��޸ĵ�IP�ڵ�ǰϵͳ���Ƿ����
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
		// ��ȡϵͳ�����л�վip
		for (Enb enb : enbList) {
			List<XBizRecord> records = validateHelper.queryRecords(
					enb.getMoId(), EnbConstantUtils.TABLE_NAME_T_IPV4, null);
			if (records == null || records.isEmpty())
				continue;
			for (XBizRecord record : records) {
				// ������ǰվ��ǰ�޸ĵļ�¼
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
		// ip��ʹ�ã������
		if (ipSet.contains(currentIp)) {
			throw validateHelper.newBizException(
					EnbConstantUtils.FIELD_NAME_IP_ADDR,
					"ip_occupied_please_change");
		}
	}

	// /**
	// * �����ܱ��������IPV4���ж˿ڱ�ʶ���ɸı�
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
	// // ��ѯ���еĶ˿ڱ�ʶ:����Ip��ʶ��ѯ�˿ڱ�ʶ�໥�Ƚ�
	// XBizRecord oldIpV4Record = validateHelper.queryIpv4Record(moId,
	// portIdNew);
	// if (oldIpV4Record != null) {
	// // ���оɵĶ˿ں�
	// XBizField oldU8PortID = oldIpV4Record
	// .getFieldBy(EnbConstantUtils.FIELD_NAME_IPV4_PORT_ID);
	// int oldU8PortIDInt = Integer
	// .valueOf(oldU8PortID.getValue());
	// // ���ݹ����Ķ˿ں�
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
