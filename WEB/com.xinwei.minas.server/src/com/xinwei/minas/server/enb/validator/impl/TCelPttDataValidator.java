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
 * С����Ⱥ���ò���������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPttDataValidator implements EnbBizDataValidator {

	// (1)u8PttBPagingSubFN
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ0ʱ����ֵ��ȡ0��5��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ1ʱ����ֵ��ȡ0��4��5��9��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ2ʱ����ֵ��ȡ0��3��4��5��8��9��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ3ʱ����ֵ��ȡ0��5��6��7��8��9��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ4ʱ����ֵ��ȡ0��4��5��6��7��8��9��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ5ʱ����ֵ��ȡ0��3��4��5��6��7��8��9��
	// T_CEL_PARA��u8UlDlSlotAlloc����Ϊ6ʱ����ֵ��ȡ0��5��9
	// (2)u8PttDlMaxRbNum
	// T_CEL_PARA����u8SysBandWidthȡֵΪ20M��15Mʱ����ֵȡ4����������
	// T_CEL_PARA����u8SysBandWidthȡֵΪ10Mʱ����ֵȡ3����������
	// T_CEL_PARA����u8SysBandWidthȡֵΪ5Mʱ����ֵȡ2��������
	// u8PtForPDSCHֵ���ܳ���T_CEL_DLPC�������м�¼��u8PAForDTCH�ֶε���Сֵ

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

		// Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// ��ȡС��ID
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);

		List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
				moId, cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (cellParaRecords != null) {
			XBizRecord cellParaRecord = cellParaRecords.get(0);
			int ulDlSlotAlloc = validateHelper.getIntFieldValue(cellParaRecord,
					EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);

			validateHelper.checkUlDlSlotAllocAndCellPtt(ulDlSlotAlloc,
					bizRecord, true);

			List<XBizRecord> dlpcRecords = validateHelper.queryRecordsByCellId(
					moId, cellId, EnbConstantUtils.TABLE_NAME_T_CEL_DLPC);
			if (dlpcRecords != null) {
				XBizRecord dlpcRecord = dlpcRecords.get(0);
				//validateHelper.checkPtForPDSCHAndPAForDTCH(bizRecord,
				//		dlpcRecord, false);
			}

			// ��ȡ�����ڴ�ֵ
			// int sysBandWidth =
			// validateHelper.getIntFieldValue(cellParaRecord,
			// EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// // ��Ⱥ����������RB��
			// XBizField pttDlMaxRbNumField = bizRecord
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_DL_MAX_RB_NUM);
			// XBizField pttDlRbEnableField = bizRecord
			// .getFieldBy(EnbConstantUtils.FIELD_NAME_PTT_DL_RB_ENABLE);
			// validateHelper.checkSysBandWidthAndDlMaxRbNum(
			// enb.getProtocolVersion(), sysBandWidth, pttDlMaxRbNumField,
			// pttDlRbEnableField, false);
		}

	}
}
