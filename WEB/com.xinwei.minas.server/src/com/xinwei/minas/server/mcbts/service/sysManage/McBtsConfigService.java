/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsConfig;

/**
 * 
 * 基站配置导入导出的服务接口
 * 
 * 
 * @author tiance
 * 
 */
public interface McBtsConfigService {
	public String[] export() throws Exception;

	public McBtsConfig getMcBtsConfig(boolean withData) throws RemoteException,
			Exception;

	public void importScript(String str) throws Exception;

	/**
	 * 导入XLS文件的内容
	 * 
	 * @param idList
	 * @param mcBtsConfig
	 * @throws Exception
	 */
	public void importMcBtsConfig(List<String> idList, McBtsConfig mcBtsConfig)
			throws Exception;
}
