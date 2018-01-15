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
 * ��֤����ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface ValidatorService {

	/**
	 * �ڽӱ������֤
	 * @param freqconf
	 * @return
	 * @throws Exception
	 */
	public String validateFreqConfiguration(FreqRelatedConfigure freqconf) throws Exception;
}
