package com.xinwei.minas.server.enb.validator.impl;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

public class TCelAlgTwoValidator implements EnbBizDataValidator {
	
	// T_ENB_PARA表中EEA不为x'000000'时,T_CEL_ALG2表u8PkMode字段不能配置为Ping Pk Mode
	// T_ENB_SRVQCI中u8RohcRTP、u8RohcUDP、u8RohcIP配置为1时,T_CEL_ALG2表u8PkMode字段不能为Ping Pk Mode
	
	private EnbBizDataValidateHelper validateHelper;

	public void setValidateHelper(EnbBizDataValidateHelper validateHelper) {
		this.validateHelper = validateHelper;
	}

	@Override
	public void validate(long moId, XBizRecord bizRecord, String actionType)
			throws Exception {
		if (actionType.equals(ActionTypeDD.ADD)
				|| actionType.equals(ActionTypeDD.MODIFY)) {
			Enb enb = EnbCache.getInstance().queryByMoId(moId);
			// T_CEL_ALG2表中判断当前版本是否包含PKmode
			if (EnbBizHelper.fieldIsExist(enb.getEnbType(),
					enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CEL_ALG2,
					EnbConstantUtils.FIELD_NAME_PK_MODE)) {
				int pkMode = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_PK_MODE);
				if(ValidatorSwitchHelper.AB_C_CHECK_OPEN == ValidatorSwitchHelper.AB_C_CHECK_SWITCH) {
					// T_ENB_PARA表中EEA不为x'000000'时,T_CEL_ALG2表u8PkMode字段不能配置为Ping Pk Mode
					boolean eeaAndPkModeResult = validateHelper.checkEeaAndPkMode(moId,pkMode);
					if(!eeaAndPkModeResult) {
						throw validateHelper
						.newBizException(
								EnbConstantUtils.FIELD_NAME_PK_MODE,
								"eea_is_not_zero_pk_mode_must_ping_pk_mode");
					}
				}
				// T_ENB_SRVQCI中u8RohcRTP、u8RohcUDP、u8RohcIP配置为1时,T_CEL_ALG2表u8PkMode字段不能为Ping Pk Mode
				boolean rohcAndPkModeResult = validateHelper.checkRohcAndPkMode(moId,pkMode);
				if(!rohcAndPkModeResult) {
					throw validateHelper
					.newBizException(
							EnbConstantUtils.FIELD_NAME_PK_MODE,
							"rohc_is_open_pk_mode_must_not_ping_pk_mode");
				}
			}
		}
	}

}
