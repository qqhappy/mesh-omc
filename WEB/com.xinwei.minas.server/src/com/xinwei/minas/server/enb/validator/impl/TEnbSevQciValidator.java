package com.xinwei.minas.server.enb.validator.impl;

import java.util.List;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

public class TEnbSevQciValidator implements EnbBizDataValidator {

	// (2) T_CEL_ALG2表中有一条记录的u8Pkmode取值为Ping Pk
	// Mode时,T_ENB_SRVQCI表u8RohcRTP取值须为Close
	// (3) T_CEL_ALG2表中有一条记录的u8Pkmode取值为Ping Pk
	// Mode时,T_ENB_SRVQCI表u8RohcUDP取值须为Close
	// (4) T_CEL_ALG2表中有一条记录的u8Pkmode取值为Ping Pk
	// Mode时,T_ENB_SRVQCI表u8RohcIP取值须为Close

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
			// 判断基站是否包含小区算法表2且小区算法表2中是否有u8pkmode字段
			if (EnbBizHelper.hasBizMeta(enb.getEnbType(), enb.getProtocolVersion(),
					EnbConstantUtils.TABLE_NAME_T_CEL_ALG2)
					&& EnbBizHelper.fieldIsExist(enb.getEnbType(),
							enb.getProtocolVersion(),
							EnbConstantUtils.TABLE_NAME_T_CEL_ALG2,
							EnbConstantUtils.FIELD_NAME_PK_MODE)) {

				// 获取当前基站所有小区ID
				List<Integer> cellIds = validateHelper
						.getAllCellIdsByMoId(moId);
				if (cellIds == null || cellIds.isEmpty()) {
					return;
				}
				for (Integer cellId : cellIds) {
					boolean pingPkMode = validateHelper.isPingPkMode(moId, cellId);
					if(pingPkMode) {
						int u8RohcRTP = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_RTP);
						int u8RohcUDP = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_UDP);
						int u8RohcIP = bizRecord.getIntValue(EnbConstantUtils.FIELD_NAME_ROHC_IP);
						if(0 != u8RohcRTP) {
							throw validateHelper
							.newBizException(
									EnbConstantUtils.FIELD_NAME_ROHC_RTP,
									"is_ping_pk_mode_rohc_rtp_must_close");
						}
						if(0 != u8RohcUDP) {
							throw validateHelper
							.newBizException(
									EnbConstantUtils.FIELD_NAME_ROHC_UDP,
							"is_ping_pk_mode_rohc_udp_must_close");
						}
						if(0 != u8RohcIP) {
							throw validateHelper
							.newBizException(
									EnbConstantUtils.FIELD_NAME_ROHC_IP,
							"is_ping_pk_mode_rohc_ip_must_close");
						}
					}
				}

			}

		}

	}
}
