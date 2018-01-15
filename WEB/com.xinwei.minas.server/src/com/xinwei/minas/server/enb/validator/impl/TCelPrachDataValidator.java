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
 * PRACH参数配置表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPrachDataValidator implements EnbBizDataValidator {
	
	// (1)u8PrachCfgIndex在T_CEL_PARA表中的
	// 子帧配比为0时取值不能是(11,19)，
	// 子帧配比为1时取值不能是(8,13,14,40-47)，
	// 子帧配比为2时取值不能是(5,7,8,11,13,14,17,19-47)，
	// 子帧配比为3时取值不能是(10,11,19,22,24,32,34,42,44,50,52)，
	// 子帧配比为4时取值不能是(5,7,8,11,13,14,17,19,22,24,32,34,40-47,50,52)，
	// 子帧配比为5时取值不能是(2,4,5,7,8,10,11,13,14,16,17,19-47,50,52)，
	// 子帧配比为6时取值不能是(16,17,42,44)
	// (2)u8PrachFreqOffset与T_CEL_PARA表中u8SysBandWidth字段之间约束关系：
	// u8PrachFreqOffset小于等于u8SysBandWidth代表的最大RB个数减去6
	
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

		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// 查询小区参数表对应的记录
		List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
				enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
		if (cellParaRecords != null) {
			XBizRecord cellParaRecord = cellParaRecords.get(0);
			validateHelper.checkCellParaAndPrach(enb.getEnbType(),
					enb.getProtocolVersion(), cellParaRecord, bizRecord, true);
		}
		// 校验rsi全网唯一
		/*validateHelper.checkUnique(moId, bizRecord,
				EnbConstantUtils.TABLE_NAME_T_CEL_PRACH,
				EnbConstantUtils.FIELD_NAME_ROOT_SEQ_INDEX);*/
	}
}
