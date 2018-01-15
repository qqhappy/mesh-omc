/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-4	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import com.xinwei.minas.mcbts.core.model.common.FreqRelatedConfigure;


/**
 * 
 * 验证服务接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface ValidatorService {

	/**
	 * 邻接表规则验证
	 * @param freqconf
	 * @return
	 * @throws Exception
	 */
	public String validateFreqConfiguration(FreqRelatedConfigure freqconf) throws Exception;
}
