/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser.impl;

import com.xinwei.oss.adapter.model.biz.ClientBaseBusiness;
import com.xinwei.oss.adapter.model.meta.OssBizItem;
import com.xinwei.oss.adapter.parser.IOSSParser;
import com.xinwei.shlr.acc.dataEntity.other.BaseBusiness;

/**
 * 
 * BaseBusiness对象解析器,前端BaseBusiness对象与后台BaseBusiness对象的相互转换
 * 
 * @author chenshaohua
 * 
 */

public class BaseBusinessParser implements IOSSParser {

	@Override
	public Object parse(Object object, OssBizItem item) {
		ClientBaseBusiness clientBaseBusiness = (ClientBaseBusiness) object;
		String businessType = clientBaseBusiness.getBusinessType();
		byte switchs = clientBaseBusiness.getSwitchs();
		byte attribute = clientBaseBusiness.getAttribute();
		BaseBusiness baseBusiness = new BaseBusiness();
		baseBusiness.businessType = businessType;
		baseBusiness.switchs = switchs;
		baseBusiness.attribute = attribute;

		return baseBusiness;
	}

	@Override
	public Object unParse(Object object, OssBizItem item) {
		BaseBusiness baseBusiness = (BaseBusiness) object;
		ClientBaseBusiness clientBaseBusiness = new ClientBaseBusiness();
		clientBaseBusiness.setBusinessType(baseBusiness.businessType);
		clientBaseBusiness.setSwitchs(baseBusiness.switchs);
		clientBaseBusiness.setAttribute(baseBusiness.attribute);
		
		return clientBaseBusiness;
	}

}
