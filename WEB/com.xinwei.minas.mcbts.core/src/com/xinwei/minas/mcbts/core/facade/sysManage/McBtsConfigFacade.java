/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsConfig;

/**
 * 
 * 基站配置导入导出的门面接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsConfigFacade extends Remote {
	/**
	 * 导出基站配置信息
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String[] export() throws RemoteException, Exception;

	public McBtsConfig getMcBtsConfig(boolean withData) throws RemoteException,
			Exception;

	/**
	 * 导入脚本
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void importScript(String str) throws RemoteException, Exception;

	/**
	 * 将xls转成的McBtsConfig导入到网管
	 * 
	 * @param mcBtsConfig
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void importMcBtsConfig(List<String> idList, McBtsConfig mcBtsConfig)
			throws RemoteException, Exception;
}
