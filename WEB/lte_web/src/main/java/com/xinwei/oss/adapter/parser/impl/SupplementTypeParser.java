/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientSupplementItem;
import com.xinwei.oss.adapter.model.biz.ClientSupplementType;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.SupplementItem;
import com.xinwei.shlr.acc.dataEntity.other.SupplementType;

/**
 * 
 * 前端SupplementType与后台SupplementType的转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementTypeParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementType clientSupplementType = (ClientSupplementType) object;
		ClientSupplementItem[] clientSupplementItems = clientSupplementType
				.getSupplementItems();
		SupplementType serverSupplementType = new SupplementType();

		for (int i = 0; i < clientSupplementItems.length; i++) {
			serverSupplementType.supplementItems[i] = new SupplementItem();
			serverSupplementType.supplementItems[i]
					.setSupplementNo((byte) clientSupplementItems[i]
							.getSupplementNo());
			serverSupplementType.supplementItems[i].supplementValue = clientSupplementItems[i]
					.getSupplementValue();
		}
		return serverSupplementType;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementType serverSupplementType = (SupplementType) object;
		ClientSupplementType clientSupplementType = new ClientSupplementType();
		ClientSupplementItem[] clientSupplementItems = clientSupplementType
				.getSupplementItems();
		for (int i = 0; i < serverSupplementType.supplementItems.length; i++) {
			clientSupplementItems[i] = new ClientSupplementItem();
			clientSupplementItems[i]
					.setSupplementNo(serverSupplementType.supplementItems[i]
							.getSupplementNo());
			clientSupplementItems[i]
					.setSupplementValue(serverSupplementType.supplementItems[i].supplementValue);
			clientSupplementItems[i]
					.setSupplementName(serverSupplementType.supplementItems[i]
							.getSupplementName());
		}

		return clientSupplementType;
	}

}
