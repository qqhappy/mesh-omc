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
 * �ն���������ӿ�
 * @author zhuxiaozhan
 *
 */
public interface TerminalRestartService {
	/**
	 * �����ն���������
	 * @param modId
	 * @param eid
	 */
	public void restartConfig(Long moId, String eid) throws Exception;
}
