/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.ut.core.model.UTPerfData;

/**
 * 
 * 查看终端性能数据门面接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTPrefDataFacade extends Remote {
	
	
	/**
	 * 查询终端性能数据请求
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public UTPerfData query(OperObject operObject, Long moId, Long pid) throws Exception;
}
