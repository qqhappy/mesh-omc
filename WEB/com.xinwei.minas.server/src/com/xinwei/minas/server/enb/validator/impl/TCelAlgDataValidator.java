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
 * 小区算法参数表数据校验类
 * 
 * @author fanhaoyu
 * 
 */

public class TCelAlgDataValidator implements EnbBizDataValidator {

	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	// u8UlRbNum的最大值不超过T_CEL_PARA.u8SysBandWidth对应的RB数，100与带宽取小，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
	// u8DlRbNum的最大值不超过T_CEL_PARA.u8SysBandWidth对应的RB数
	// u8UlMaxRbNum的最大值不超过100与T_CEL_PARA.u8SysBandWidth对应的RB数取小，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
	// u8UlMinRbNum的最大值不超过100与T_CEL_PARA.u8SysBandWidth对应的RB数取小，且必须是2的x次幂、3的y次幂与5的z次幂的乘积
	// (1)u8DlMaxRbNum的最大值不超过100与T_CEL_PARA.u8SysBandWidth对应的RB数取小；
	// (2)T_CEL_PARA表中u8SysBandWidth取值为20M、15M时，该值取4的整数倍；
	// T_CEL_PARA表中u8SysBandWidth取值为10M时，该值取3的整数倍；
	// T_CEL_PARA表中u8SysBandWidth取值为5M时，该值取2的整数倍；
	// u8DlMinRbNum的最大值不超过100与T_CEL_PARA.u8SysBandWidth对应的RB数取小
	// u8Cfi取值为4时T_CEL_PARA.u8SysBandWidth只能取值为0

	// T_CEL_PARA表中u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、7、8可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、7可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8UlSubfrmFlag[10]索引2、3、4、7、8可配置为1
	// T_CEL_PARA表中u8UlDlSlotAlloc配置为0时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6可配置为1；
	// u8UlDlSlotAlloc配置为1时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、9可配置为1；
	// u8UlDlSlotAlloc配置为2时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、8、9可配置为1；
	// u8UlDlSlotAlloc配置为3时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为4时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为5时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、3、4、5、6、7、8、9可配置为1；
	// u8UlDlSlotAlloc配置为6时，T_CEL_ALG.ab8DlSubfrmFlag[10]索引0、1、5、6、9可配置为1

	// T_CEL_PARA表中u8UeTransMode配置为0时，T_CEL_ALG.u8TS可取0、3；
	// u8UeTransMode为1时，T_CEL_ALG.u8TS可取0、1、3；
	// u8UeTransMode配置为2时，T_CEL_ALG.u8TS可取0、1、2、3
	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		// 该表无需校验删除操作
		if (actionType.equals(ActionTypeDD.DELETE))
			return;
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		int cellId = validateHelper.getIntFieldValue(bizRecord,
				EnbConstantUtils.FIELD_NAME_CELL_ID);
		// ABC类参数校验判断
		if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
			// 查询小区参数表对应的记录
			List<XBizRecord> cellParaRecords = validateHelper.queryRecordsByCellId(
					enb.getMoId(), cellId, EnbConstantUtils.TABLE_NAME_T_CELL_PARA);
			if (cellParaRecords != null) {
				XBizRecord cellParaRecord = cellParaRecords.get(0);

				// 获取带宽内存值
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
