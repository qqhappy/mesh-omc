/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;

/**
 * 
 * 终端版本强制升级门面接口
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateFacade extends Remote {
	/**
	 * 从数据库查询所有强制升级的规则
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalVersion> queryList() throws RemoteException,
			Exception;

	/**
	 * 获得强制版本升级的开关
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean getSwitchStatus() throws RemoteException, Exception;

	/**
	 * 配置终端版本强制升级
	 * 
	 * @param status
	 * @param ruleList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, boolean status, List<TerminalVersion> ruleList)
			throws RemoteException, Exception;
}
