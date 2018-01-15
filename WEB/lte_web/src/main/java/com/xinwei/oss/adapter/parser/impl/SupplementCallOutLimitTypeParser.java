/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-26	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientSupplementCallOutLimitType;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.supplement.SupplementCallOutLimitType;

/**
 * 
 * 前端SupplementCallOutLimitType与后台SupplementCallOutLimitType的转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementCallOutLimitTypeParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementCallOutLimitType clientSupplementCallOutLimitType = (ClientSupplementCallOutLimitType) object;
		SupplementCallOutLimitType serverSupplementCallOutLimitType = new SupplementCallOutLimitType();
		serverSupplementCallOutLimitType.regStatus = clientSupplementCallOutLimitType
				.getRegStatus();
		serverSupplementCallOutLimitType.callOutLimitType = clientSupplementCallOutLimitType
				.getCallOutLimitType();
		serverSupplementCallOutLimitType.callOutLimitPsw = clientSupplementCallOutLimitType
				.getCallOutLimitPsw();

		return serverSupplementCallOutLimitType;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementCallOutLimitType serverSupplementCallOutLimitType = (SupplementCallOutLimitType) object;
		ClientSupplementCallOutLimitType clientSupplementCallOutLimitType = new ClientSupplementCallOutLimitType();
		clientSupplementCallOutLimitType.setRegStatus(serverSupplementCallOutLimitType.regStatus);
		clientSupplementCallOutLimitType.setCallOutLimitType(serverSupplementCallOutLimitType.callOutLimitType);
		clientSupplementCallOutLimitType.setCallOutLimitPsw(serverSupplementCallOutLimitType.callOutLimitPsw);
		
		return clientSupplementCallOutLimitType;
	}

}
