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
 * PRACH�������ñ�����У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPrachDataValidator implements EnbBizDataValidator {
	
	// (1)u8PrachCfgIndex��T_CEL_PARA���е�
	// ��֡���Ϊ0ʱȡֵ������(11,19)��
	// ��֡���Ϊ1ʱȡֵ������(8,13,14,40-47)��
	// ��֡���Ϊ2ʱȡֵ������(5,7,8,11,13,14,17,19-47)��
	// ��֡���Ϊ3ʱȡֵ������(10,11,19,22,24,32,34,42,44,50,52)��
	// ��֡���Ϊ4ʱȡֵ������(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)��
	// ��֡���Ϊ5ʱȡֵ������(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)��
	// ��֡���Ϊ6ʱȡֵ������(16,17,42,44)
	// (2)u8PrachFreqOffset��T_CEL_PARA����u8SysBandWidth�ֶ�֮��Լ����ϵ��
	// u8PrachFreqOffsetС�ڵ���u8SysBandWidth��������RB������ȥ6
	
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

		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// ��ѯС���������Ӧ�ļ�¼
		List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (cellParaRecords != null) {
			XBizRecord cellParaRecord = cellParaRecords.get(0);
			validateHelper.checkCellParaAndPrach(enb.getEnbType(),
					enb.getProtocolVersion(), cellParaRecord, bizRecord, true);
		}
		// У��rsiȫ��Ψһ
		/*validateHelper.checkUnique(moId, bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
	}
}
