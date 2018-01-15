/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy;

import com.xinwei.minas.ut.core.model.UTPerfData;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 查看终端性能数据代理接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTPrefDataProxy {
	/**
	 * 查询终端性能数据请求
	 * @param moId
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public UTPerfData query(Long moId, GenericBizData genericBizData) throws Exception;
}
