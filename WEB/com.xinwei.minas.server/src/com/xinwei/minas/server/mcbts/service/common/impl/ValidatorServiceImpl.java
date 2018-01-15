/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-4	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import com.xinwei.minas.mcbts.core.model.common.FreqRelatedConfigure;
import com.xinwei.minas.server.mcbts.service.common.ValidatorService;
import com.xinwei.minas.server.mcbts.validator.FreqConfigurationValidator;

/**
 * 
 * 验证服务的实现类
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class ValidatorServiceImpl implements ValidatorService{

	@Override
	public String validateFreqConfiguration(FreqRelatedConfigure freqconf)
			throws Exception {
		return FreqConfigurationValidator.validateFreqConfiguration(freqconf);
	}
	
}
