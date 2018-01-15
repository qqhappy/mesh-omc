/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.service;

import java.util.List;

/**
 * 
 * 统计数据实体处理接口
 * 
 * @author fanhaoyu
 * 
 */

public interface StatEntityProcessor<T> {

	/**
	 * 处理统计数据实体列表
	 * 
	 * @param entities
	 * @throws Exception
	 */
	public void process(List<T> entities) throws Exception;

}
