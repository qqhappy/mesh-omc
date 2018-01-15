/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator.impl;

import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidateHelper;
import com.xinwei.minas.server.enb.validator.EnbBizDataValidator;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * ҵ�񹦿ز���������У����
 * 
 * @author fanhaoyu
 * 
 */

public class TEnbSrvpcDataValidator implements EnbBizDataValidator {

	// u8PA��ֵ���ܳ���T_CEL_DLPC�������м�¼��u8PAForDTCH�ֶε���Сֵ

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

		//validateHelper.checkPAAndPAForDTCH(moId, bizRecord, true);
	}

}
