/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * 终端版本强制升级服务接口
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateService {
	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return List<TerminalVersion>
	 */
	public List<TerminalVersion> queryList();

	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return boolean
	 */
	public boolean getSwitchStatus();

	/**
	 * 配置终端版本强制升级
	 * 
	 * @param status
	 * @param ruleList
	 */
	public void config(boolean status, List<TerminalVersion> ruleList) throws Exception;

	/**
	 * 终端注册任务管理器触发的升级终端请求配置
	 * 
	 * @param eid
	 * @param tv
	 */
	public void upgradeConfig(Long moId, String eid, TerminalVersion tv) throws Exception;

	/**
	 * 获取终端升级进度
	 * 
	 * @param utList
	 * @return Map<pid, 进度>
	 */
	public Map<String, String> getUTProgress(List<UserTerminal> utList);

	/**
	 * BootLoader升级配置
	 * 
	 * @param moId
	 *            基站moid
	 * @param eid
	 *            终端的pid(eid)
	 * @param tv
	 *            目标软件版本
	 */
	public void bootloaderUpgrade(Long moId, String eid, TerminalVersion tv) throws Exception;
}
