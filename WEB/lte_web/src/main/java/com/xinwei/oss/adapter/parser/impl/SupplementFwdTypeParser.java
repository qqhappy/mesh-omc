/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientSupplementFwdType;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.supplement.SupplementFwdType;

/**
 * 
 * 前端SupplementFwdType与后台SupplementFwdType相互转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementFwdTypeParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementFwdType clientSupplementFwdType = (ClientSupplementFwdType) object;
		SupplementFwdType serverSupplementFwdType = new SupplementFwdType();
		serverSupplementFwdType.regStatus = clientSupplementFwdType
				.getRegStatus();
		serverSupplementFwdType.fwdTelNumber = clientSupplementFwdType
				.getFwdTelNumber();
		serverSupplementFwdType.notifyCalling = clientSupplementFwdType
				.getNotifyCalling();
		serverSupplementFwdType.notifyFwd = clientSupplementFwdType
				.getNotifyFwd();
		serverSupplementFwdType.fwdTime = clientSupplementFwdType.getFwdTime();

		return serverSupplementFwdType;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementFwdType serverSupplementFwdType = (SupplementFwdType) object;
		ClientSupplementFwdType clientSupplementFwdType = new ClientSupplementFwdType();
		clientSupplementFwdType.setRegStatus(serverSupplementFwdType.regStatus);
		clientSupplementFwdType
				.setFwdTelNumber(serverSupplementFwdType.fwdTelNumber);
		clientSupplementFwdType
				.setNotifyCalling(serverSupplementFwdType.notifyCalling);
		clientSupplementFwdType.setNotifyFwd(serverSupplementFwdType.notifyFwd);
		clientSupplementFwdType.setFwdTime(serverSupplementFwdType.fwdTime);

		return clientSupplementFwdType;
	}

}
