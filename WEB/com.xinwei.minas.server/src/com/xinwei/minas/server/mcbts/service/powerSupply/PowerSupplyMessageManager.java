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
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyMessageManager {
	/**
	 *  创建查询命令
	 * @param power
	 * @return
	 */
	public byte[] createPollCmdMessage();
	
	/**
	 * 解析信息
	 * @param message
	 * @return
	 */
	public String parseMessage(byte[] message) throws Exception;
	
}
