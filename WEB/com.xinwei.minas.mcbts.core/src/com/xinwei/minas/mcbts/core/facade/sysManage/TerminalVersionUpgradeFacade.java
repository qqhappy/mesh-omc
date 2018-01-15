/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * BootLoader和终端软件升级门面
 * 
 * @author zhuxiaozhan
 * 
 */
public interface TerminalVersionUpgradeFacade extends Remote {
	/**
	 * 终端软件升级配置
	 * 
	 * @param moId
	 *            基站moid
	 * @param eid
	 *            终端的pid(eid)
	 * @param tv
	 *            目标软件版本
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void upgradeConfig(OperObject operObject, Long moId, String eid, TerminalVersion tv)
			throws RemoteException, Exception;

	/**
	 * 获取终端升级进度
	 * 
	 * @param utList
	 * @return Map<pid, 进度>
	 */
	public Map<String, String> getUTProgress(List<UserTerminal> utList)
			throws RemoteException;

	/**
	 * BootLoader升级配置
	 * 
	 * @param moId
	 *            基站moid
	 * @param eid
	 *            终端的pid(eid)
	 * @param tv
	 *            目标软件版本
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void bootloaderUpgrade(OperObject operObject, Long moId, String eid, TerminalVersion tv)
			throws RemoteException, Exception;

}
