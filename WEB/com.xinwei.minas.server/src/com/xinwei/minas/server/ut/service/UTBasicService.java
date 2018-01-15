/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * McBts基本业务服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface UTBasicService {
	/**
	 * 按条件向HLR查询终端
	 * 
	 * @param utc
	 * @return
	 * @throws Exception
	 */
	public UTQueryResult queryUTByCondition(UTCondition utc) throws Exception;

	/**
	 * 向数据库查询所有终端类型
	 * 
	 * @return
	 */
	public List<TerminalVersion> queryUTTypes();

	/**
	 * 获得某个终端的最新状态
	 * 
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public UserTerminal queryUTByPid(String pid) throws Exception;
}
