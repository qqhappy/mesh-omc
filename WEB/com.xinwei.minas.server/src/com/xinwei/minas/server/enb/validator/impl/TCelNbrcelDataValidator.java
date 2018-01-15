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
 * 邻区关系参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelNbrcelDataValidator implements EnbBizDataValidator {

	// （1）T_CEL_NBRCEL表中u8SvrCID相同且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能相等；
	// （2）T_CEL_NBRCEL表中u8SvrCID与T_CEL_PARA表中的u8CId相等且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能与T_CEL_PARA对应记录中的u16PhyCellId字段相等。

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;

		checkPCI(moId, bizRecord);

		validateHelper.checkNeighborCellAndCellPara(moId, bizRecord, false);
	}

	/**
	 * T_CEL_NBRCEL表中u8SvrCID相同且u8CenterFreqCfgIdx为255记录中的u16PhyCellId不能相等
	 * 
	 * @param moId
	 * @param bizRecord
	 * @throws Exception
	 */
	private void checkPCI(long moId, XBizRecord bizRecord) throws Exception {
		// 获取小区ID
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
