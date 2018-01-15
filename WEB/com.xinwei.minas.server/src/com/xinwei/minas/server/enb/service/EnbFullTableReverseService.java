/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-20	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

/**
 * 
 * 整表反构服务接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableReverseService {
	
	/**
	 * 进行整表反构配置
	 * @param moId
	 * @throws Exception
	 */
	public void config(Long moId) throws Exception;
}
