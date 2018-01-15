/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientSupplementActiveType;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.supplement.SupplementActiveType;

/**
 * 
 * SupplementActiveType前后台实体转换
 * 
 * @author chenshaohua
 * 
 */

public class SupplementActiveTypeParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientSupplementActiveType clientSupplementActiveType = (ClientSupplementActiveType) object;
		SupplementActiveType serverSupplementActiveType = new SupplementActiveType();
		serverSupplementActiveType.regStatus = clientSupplementActiveType
				.getRegStatus();

		return serverSupplementActiveType;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		SupplementActiveType serverSupplementActiveType = (SupplementActiveType) object;
		ClientSupplementActiveType clientSupplementActiveType = new ClientSupplementActiveType();
		clientSupplementActiveType
				.setRegStatus(serverSupplementActiveType.regStatus);
		return clientSupplementActiveType;
	}

}
