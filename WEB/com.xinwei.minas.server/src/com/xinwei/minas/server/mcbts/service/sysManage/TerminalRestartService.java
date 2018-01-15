/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.sysManage;

/**
 * 终端重启服务接口
 * @author zhuxiaozhan
 *
 */
public interface TerminalRestartService {
	/**
	 * 重启终端配置请求
	 * @param modId
	 * @param eid
	 */
	public void restartConfig(Long moId, String eid) throws Exception;
}
