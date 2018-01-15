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
 * ��վ���õ��뵼��������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsConfigFacade extends Remote {
	/**
	 * ������վ������Ϣ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String[] export() throws RemoteException, Exception;

	public McBtsConfig getMcBtsConfig(boolean withData) throws RemoteException,
			Exception;

	/**
	 * ����ű�
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void importScript(String str) throws RemoteException, Exception;

	/**
	 * ��xlsת�ɵ�McBtsConfig���뵽����
	 * 
	 * @param mcBtsConfig
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void importMcBtsConfig(List<String> idList, McBtsConfig mcBtsConfig)
			throws RemoteException, Exception;
}
