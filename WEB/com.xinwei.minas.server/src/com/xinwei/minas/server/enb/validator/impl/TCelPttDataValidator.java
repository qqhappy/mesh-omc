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
 * 小区集群配置参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelPttDataValidator implements EnbBizDataValidator {

	// (1)u8PttBPagingSubFN
	// T_CEL_PARA表u8UlDlSlotAlloc配置为0时，该值可取0、5；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为1时，该值可取0、4、5、9；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为2时，该值可取0、3、4、5、8、9；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为3时，该值可取0、5、6、7、8、9；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为4时，该值可取0、4、5、6、7、8、9；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为5时，该值可取0、3、4、5、6、7、8、9；
	// T_CEL_PARA表u8UlDlSlotAlloc配置为6时，该值可取0、5、9
	// (2)u8PttDlMaxRbNum
	// T_CEL_PARA表中u8SysBandWidth取值为20M、15M时，该值取4的整数倍；
	// T_CEL_PARA表中u8SysBandWidth取值为10M时，该值取3的整数倍；
	// T_CEL_PARA表中u8SysBandWidth取值为5M时，该值取2的整数倍
	// u8PtForPDSCH值不能超过T_CEL_DLPC表中所有记录中u8PAForDTCH字段的最小值

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

		// Enb enb = EnbCache.getInstance().queryByMoId(moId);
		// 获取小区ID
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

			// 获取带宽内存值
			// int sysBandWidth =
			// validateHelper.getIntFieldValue(cellParaRecord,
			// EnbConstantUtils.FIELD_NAME_SYS_BAND_WIDTH);
			// // 集群下行最大分配RB数
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
