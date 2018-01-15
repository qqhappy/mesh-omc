/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

/**
 * 
 * 同步配置接口
 * 
 * @author chenshaohua
 * 
 */

public interface ISyncService {
	/**
	 * 从网管同步数据到网元
	 * @param moId
	 * @throws Exception
	 */
	public void syncFromEms2Ne(Long moId) throws Exception;
}
