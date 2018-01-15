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
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * ������ϵ����������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TCelNbrcelDataValidator implements EnbBizDataValidator {

	// ��1��T_CEL_NBRCEL����u8SvrCID��ͬ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������ȣ�
	// ��2��T_CEL_NBRCEL����u8SvrCID��T_CEL_PARA���е�u8CId�����u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId������T_CEL_PARA��Ӧ��¼�е�u16PhyCellId�ֶ���ȡ�

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

		checkPCI(moId, bizRecord);

		validateHelper.checkNeighborCellAndCellPara(moId, bizRecord, false);
	}

	/**
	 * T_CEL_NBRCEL����u8SvrCID��ͬ��u8CenterFreqCfgIdxΪ255��¼�е�u16PhyCellId�������
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkPCI(long moId, XBizRecord bizRecord) throws Exception {
		// ��ȡС��ID
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_SVR_CID);

		List<XBizRecord> records = validateHelper.queryRecordsByCellId(moId,
				cellId, EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL);
		if (records == null)
			return;
		int currentCfgIndex = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
		if (currentCfgIndex != 255)
			return;
		int currentPci = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
		XBizRecord currentKey = EnbBizHelper.getKeyRecordBy(moId,
				EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, bizRecord);
		for (XBizRecord record : records) {
			XBizRecord key = EnbBizHelper.getKeyRecordBy(moId,
					EnbConstantUtils.TABLE_NAME_T_CEL_NBRCEL, record);
			if (key.equals(currentKey))
				continue;
			int cfgIndex = validateHelper.getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_CENTER_FREQ_CFG_IDX);
			if (cfgIndex == 255) {
				int pci = validateHelper.getIntFieldValue(record,
						EnbConstantUtils.FIELD_NAME_PHY_CELL_ID);
				if (currentPci == pci) {
					throw validateHelper
							.newBizException(
									EnbConstantUtils.FIELD_NAME_PHY_CELL_ID,
									"nbrcel_svrcid_equal_centerfreqcfgidx_255_phycellid_cannot_be_equal");
				}
			}
		}
	}

}
