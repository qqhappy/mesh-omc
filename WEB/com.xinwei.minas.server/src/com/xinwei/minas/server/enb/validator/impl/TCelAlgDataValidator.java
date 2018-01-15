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
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * С���㷨����������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelAlgDataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	// u8UlRbNum�����ֵ������T_CEL_PARA.u8SysBandWidth��Ӧ��RB����100�����ȡС���ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
	// u8DlRbNum�����ֵ������T_CEL_PARA.u8SysBandWidth��Ӧ��RB��
	// u8UlMaxRbNum�����ֵ������100��T_CEL_PARA.u8SysBandWidth��Ӧ��RB��ȡС���ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
	// u8UlMinRbNum�����ֵ������100��T_CEL_PARA.u8SysBandWidth��Ӧ��RB��ȡС���ұ�����2��x���ݡ�3��y������5��z���ݵĳ˻�
	// (1)u8DlMaxRbNum�����ֵ������100��T_CEL_PARA.u8SysBandWidth��Ӧ��RB��ȡС��
	// (2)T_CEL_PARA����u8SysBandWidthȡֵΪ20M��15Mʱ����ֵȡ4����������
	// T_CEL_PARA����u8SysBandWidthȡֵΪ10Mʱ����ֵȡ3����������
	// T_CEL_PARA����u8SysBandWidthȡֵΪ5Mʱ����ֵȡ2����������
	// u8DlMinRbNum�����ֵ������100��T_CEL_PARA.u8SysBandWidth��Ӧ��RB��ȡС
	// u8CfiȡֵΪ4ʱT_CEL_PARA.u8SysBandWidthֻ��ȡֵΪ0

	// T_CEL_PARA����u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��7��8������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��7������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8UlSubfrmFlag[10]����2��3��4��7��8������Ϊ1
	// T_CEL_PARA����u8UlDlSlotAlloc����Ϊ0ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ1ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ2ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ3ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ4ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ5ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��3��4��5��6��7��8��9������Ϊ1��
	// u8UlDlSlotAlloc����Ϊ6ʱ��T_CEL_ALG.ab8DlSubfrmFlag[10]����0��1��5��6��9������Ϊ1

	// T_CEL_PARA����u8UeTransMode����Ϊ0ʱ��T_CEL_ALG.u8TS��ȡ0��3��
	// u8UeTransModeΪ1ʱ��T_CEL_ALG.u8TS��ȡ0��1��3��
	// u8UeTransMode����Ϊ2ʱ��T_CEL_ALG.u8TS��ȡ0��1��2��3
	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// �ñ�����У��ɾ������
		if (actionType.equals(ActionTypeDD.DELETE))
			return;
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// ABC�����У���ж�
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			// ��ѯС���������Ӧ�ļ�¼
			List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
					enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			if (cellParaRecords != null) {
				XBizRecord cellParaRecord = cellParaRecords.get(0);

				// ��ȡ�����ڴ�ֵ
				int sysBandWidth = validateHelper.getIntFieldValue(cellParaRecord,
						EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
				validateHelper.checkAlgAndSysBandWidth(enb.getEnbType(),
						enb.getProtocolVersion(), bizRecord, sysBandWidth, true);

				int ulDlSlotAlloc = validateHelper.getIntFieldValue(cellParaRecord,
						EnbConstantUtils.FIELD_NAME_ULDLSLOTALLOC);
				validateHelper.checkUlDlSlotAllocAndCellAlgPara(ulDlSlotAlloc,
						bizRecord, true);

				int ueTransMode = validateHelper.getIntFieldValue(cellParaRecord,
						EnbConstantUtils.FIELD_NAME_UE_TRANS_MODE);
				validateHelper.checkUeTransModeAndCellAlgPara(ueTransMode,
						bizRecord, true);
			}
		}
	}

}
