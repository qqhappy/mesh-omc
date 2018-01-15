/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import com.xinwei.minas.ut.core.model.UTPerfData;

/**
 * 
 *查看终端性能数据服务接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTPrefDataService {
	/**
	 * 查询终端性能数据请求
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public UTPerfData query(Long moId, Long pid) throws Exception;
}
