/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyMessageManager {
	/**
	 *  ������ѯ����
	 * @param power
	 * @return
	 */
	public byte[] createPollCmdMessage();
	
	/**
	 * ������Ϣ
	 * @param message
	 * @return
	 */
	public String parseMessage(byte[] message) throws Exception;
	
}
