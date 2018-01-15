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
 * �ն˰汾ǿ����������ӿ�
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateFacade extends Remote {
	/**
	 * �����ݿ��ѯ����ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalVersion> queryList() throws RemoteException,
			Exception;

	/**
	 * ���ǿ�ư汾�����Ŀ���
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean getSwitchStatus() throws RemoteException, Exception;

	/**
	 * �����ն˰汾ǿ������
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
