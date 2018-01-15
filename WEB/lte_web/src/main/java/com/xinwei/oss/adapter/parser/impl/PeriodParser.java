/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-26	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientPeriod;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.Period;

/**
 * 
 * 前端Period与后台Period转换
 * 
 * @author chenshaohua
 * 
 */

public class PeriodParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientPeriod clientPeriod = (ClientPeriod) object;
		Period serverPeriod = new Period();
		serverPeriod.period = clientPeriod.getPeriod();
		serverPeriod.value = clientPeriod.getValue();
		return serverPeriod;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		Period serverPeriod = (Period) object;
		ClientPeriod clientPeriod = new ClientPeriod();
		clientPeriod.setPeriod(serverPeriod.period);
		clientPeriod.setValue(serverPeriod.value);
		return clientPeriod;
	}

}
