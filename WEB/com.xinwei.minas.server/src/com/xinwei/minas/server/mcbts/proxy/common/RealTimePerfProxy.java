/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.common;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 基站实时性能消息代理
 * 
 * @author fanhaoyu
 * 
 */

public interface RealTimePerfProxy {

	public void queryAsync(long moId, GenericBizData data) throws Exception;

}
