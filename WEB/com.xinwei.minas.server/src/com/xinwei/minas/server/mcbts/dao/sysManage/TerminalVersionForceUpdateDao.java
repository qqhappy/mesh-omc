/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;

/**
 * 
 * 终端版本强制升级Dao接口
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateDao {

	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return
	 */
	public List<TerminalVersion> queryList();

	/**
	 * 从数据库查询一条强制升级的规则
	 * 
	 * @param btsId
	 * @param typeId
	 * @return TVForceUpdate
	 */
	public TerminalVersion query(Long btsId, Integer typeId);
	
	/**
	 * 根据规则列,修改表中的规则
	 * @param ruleList
	 */
	public void update(List<TerminalVersion> ruleList);
}
