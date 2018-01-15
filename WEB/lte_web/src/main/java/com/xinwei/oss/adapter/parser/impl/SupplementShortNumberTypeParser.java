/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientShortNumberGroup;
import com.xinwei.oss.adapter.model.biz.ClientSupplementShortNumberType;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.supplement.ShortNumberGroup;
import com.xinwei.shlr.acc.dataEntity.other.supplement.SupplementShortNumberType;

/**
 * 
 * SupplementShortNumberType前后台转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementShortNumberTypeParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementShortNumberType clientSupplementShortNumberType = (ClientSupplementShortNumberType) object;
		SupplementShortNumberType serverSupplementShortNumberType = new SupplementShortNumberType();
		serverSupplementShortNumberType.regStatus = clientSupplementShortNumberType
				.getRegStatus();
		serverSupplementShortNumberType.groupNum = clientSupplementShortNumberType
				.getGroupNum();

		ClientShortNumberGroup[] clientShortNumberGroups = clientSupplementShortNumberType
				.getShortNumberGroups();
		if(clientShortNumberGroups != null){
			serverSupplementShortNumberType.shortNumberGroups = new ShortNumberGroup[clientShortNumberGroups.length];
			for (int i = 0; i < serverSupplementShortNumberType.shortNumberGroups.length; i++) {
				serverSupplementShortNumberType.shortNumberGroups[i] = new ShortNumberGroup();
				serverSupplementShortNumberType.shortNumberGroups[i].shortNumber = clientShortNumberGroups[i]
						.getShortNumber();
				serverSupplementShortNumberType.shortNumberGroups[i].realNumber = clientShortNumberGroups[i]
						.getRealNumber();
			}
		}
		return serverSupplementShortNumberType;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementShortNumberType serverSupplementShortNumberType = (SupplementShortNumberType) object;
		ClientSupplementShortNumberType clientSupplementShortNumberType = new ClientSupplementShortNumberType();
		clientSupplementShortNumberType
				.setRegStatus(serverSupplementShortNumberType.regStatus);
		clientSupplementShortNumberType
				.setGroupNum(serverSupplementShortNumberType.groupNum);

		ShortNumberGroup[] serverShortNumberGroups = serverSupplementShortNumberType.shortNumberGroups;
		if (serverShortNumberGroups != null) {
			ClientShortNumberGroup[] clientShortNumberGroups = new ClientShortNumberGroup[serverShortNumberGroups.length];
			for (int i = 0; i < clientShortNumberGroups.length; i++) {
				clientShortNumberGroups[i] = new ClientShortNumberGroup();
				clientShortNumberGroups[i]
						.setShortNumber(serverShortNumberGroups[i].shortNumber);
				clientShortNumberGroups[i]
						.setRealNumber(serverShortNumberGroups[i].realNumber);
			}
			clientSupplementShortNumberType
					.setShortNumberGroups(clientShortNumberGroups);
		} else {
			clientSupplementShortNumberType
					.setShortNumberGroups(new ClientShortNumberGroup[0]);
		}
		return clientSupplementShortNumberType;
	}

}
