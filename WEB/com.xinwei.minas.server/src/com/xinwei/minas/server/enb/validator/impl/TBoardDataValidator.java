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
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ������¼У����
 * <p>
 * ����McLte ��ǰվ�ͣ�Ĭ�� BBU�ܿ�� ����Ϊ 1��1��1��RRU �ļܴ�2 ��ʼ��ţ��ֱ�Ϊ2��1��1 ��3��1��1 �ȣ����Է���Ĭ��������
 * </p>
 * 
 * @author fanhaoyu
 * 
 */

public class TBoardDataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		int[] noArray = validateHelper.getRackShelfSlotNo(bizRecord);
		int rackNo = noArray[0];
		int shelfNo = noArray[1];
		int slotNo = noArray[2];

		if (actionType.equals(ActionTypeDD.DELETE)) {
			List<XBizRecord> ethenetRecords = validateHelper.queryRecords(moId,
					EnbConstantUtils.TABLE_NAME_T_ETHPARA, null);
			// ��̫�����������м�¼ʱ��BBU���岻��ɾ��
			if (ethenetRecords != null && !ethenetRecords.isEmpty()) {
				if (rackNo == 1 && shelfNo == 1 && slotNo == 1) {
					String errorInfo = "board111_cannot_delete_while_ethenet_has_record";
					// �����С��վ
					if(EnbTypeDD.XW7102 == enb.getEnbType()) {
						errorInfo = "board111_cannot_delete_while_ethenet_has_record_xw7102";
					} 
					throw new Exception(
							OmpAppContext
									.getMessage(errorInfo));
				}
			}
		} else if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			XBizField boardTypeField = bizRecord
					.getFieldBy(EnbConstantUtils.FIELD_NAME_BDTYPE);
			// ������б�����һ��BBU�壬�Ҽܿ�۱�����1,1,1
			int boardType = Integer.valueOf(boardTypeField.getValue());
			if (rackNo == 1 && shelfNo == 1 && slotNo == 1) {
				if (boardType != EnbConstantUtils.BOARD_TYPE_BBU) {
					throw validateHelper.newBizException(
							boardTypeField.getName(), "board_111_must_be_bbu");
				}
			}
		}

	}

}
