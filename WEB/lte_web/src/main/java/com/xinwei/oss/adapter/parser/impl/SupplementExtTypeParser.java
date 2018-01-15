/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import java.util.List;

import com.xinwei.oss.adapter.model.biz.ClientSupplementExtType;
import com.xinwei.oss.adapter.model.biz.ClientSupplementItem;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.SupplementExtType;
import com.xinwei.shlr.acc.dataEntity.other.SupplementItem;

/**
 * 
 * 前端SupplementExtType与后台SupplementExtType的转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementExtTypeParser implements IOSSParser {

	@Override
	@SuppressWarnings("unchecked")
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementExtType clientSupplementExtType = (ClientSupplementExtType) object;
		SupplementExtType serverSupplementExtType = new SupplementExtType();
		ClientSupplementItem[] clientSupplementItems = clientSupplementExtType
				.getSupplementItems();
		List<SupplementItem> serverSupplementItems = serverSupplementExtType
				.getSupplementItemsList();
		for (int i = 0; i < clientSupplementItems.length; i++) {
			SupplementItem serverSupplementItem = new SupplementItem();
			serverSupplementItem
					.setSupplementNo((byte) clientSupplementItems[i]
							.getSupplementNo());
			serverSupplementItem.supplementValue = clientSupplementItems[i]
					.getSupplementValue();
			serverSupplementItems.add(serverSupplementItem);
		}
		return serverSupplementExtType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementExtType serverSupplementExtType = (SupplementExtType) object;
		List<SupplementItem> serverSupplementItems = serverSupplementExtType
				.getSupplementItemsList();
		
		ClientSupplementExtType clientSupplementExtType = new ClientSupplementExtType();
		ClientSupplementItem[] clientSupplementItems = clientSupplementExtType
				.getSupplementItems();
		for (int i = 0; i < serverSupplementItems.size(); i++) {
			clientSupplementItems[i] = new ClientSupplementItem();
			clientSupplementItems[i].setSupplementNo(serverSupplementItems.get(
					i).getSupplementNo());
			clientSupplementItems[i].setSupplementValue(serverSupplementItems
					.get(i).supplementValue);
			clientSupplementItems[i].setSupplementName(serverSupplementItems
					.get(i).getSupplementName());
		}
		return clientSupplementExtType;
	}

}
