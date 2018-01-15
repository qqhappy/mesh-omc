/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * BTS在线终端列表service
 * 
 * @author fangping
 * 
 */

public interface McBtsOnlineTerminalListService {

	/**
	 * 向基站查询
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<ActiveUserInfo> queryOnlineTerminalListFromNE(Long moId)
			throws Exception;

}
