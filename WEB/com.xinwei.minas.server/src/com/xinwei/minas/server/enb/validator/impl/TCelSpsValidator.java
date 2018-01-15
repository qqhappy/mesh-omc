/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-27	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * SPS���ò�����У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelSpsValidator implements EnbBizDataValidator {
	// SPS���ñ��е�������SPS���ز�����DRX���ñ��е�DRX����ͬʱ��

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

		// ABC�����У���ж�
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID_);
		List<XBizRecord> drxRecords = validateHelper.queryRecordsByCellId(
				moId, cellId, EnbConstantUtils.TABLE_NAME_T_CEL_DRX);
		if (drxRecords != null) {
			XBizRecord drxRecord = drxRecords.get(0);
			validateHelper.checkDrxSwitchAndSpsLinkSwitch(drxRecord,
					bizRecord, false);
		}
		
		checkSpsANChnAndSysBandWidthAndPuch(moId, cellId, bizRecord);
	}

	// u16N_SpsANChn��T_CEL_PUCH���u8DeltaPucchShift��u8N_RB2��u16N_SrChn�Լ�
	// T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	// u8N_RB2+((u16N_SrChn+ u16N_SpsANChn)\(36\(u8DeltaPucchShift)))
	// ���ܳ���u8SysBandWidth��������RB����
	private void checkSpsANChnAndSysBandWidthAndPuch(long moId, int cellId,
			XBizRecord spsRecord) throws Exception {

		List<XBizRecord> puchRecords = validateHelper.queryRecordsByCellId(
				moId, cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PUCH);
		if (puchRecords != null) {
			XBizRecord puchRecord = puchRecords.get(0);
			List<XBizRecord> cellRecords = validateHelper.queryRecordsByCellId(
					moId, cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			if (cellRecords != null) {
				XBizRecord cellRecord = cellRecords.get(0);
				Enb enb = EnbCache.getInstance().queryByMoId(moId);
				// ��ȡ�����ڴ�ֵ
				int sysBandWidth = validateHelper.getIntFieldValue(cellRecord,
						EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
				validateHelper.checkSpsANChnAndSysBandWidthAndPuch(
						enb.getEnbType(),
						enb.getProtocolVersion(), sysBandWidth, spsRecord,
						puchRecord, EnbConstantUtils.FIELD_NAME_N_SPSANCHN);

			}
		}

	}

}
