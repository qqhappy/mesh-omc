/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.secu;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;

/**
 * 
 * Ȩ�޹�������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface AuthorityManageFacade extends Remote {

	/**
	 * ��ѯ�û�Ȩ��
	 * 
	 * @param username
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(String username)
			throws RemoteException, Exception;

	/**
	 * ���ݲ���ǩ����ѯҵ�����
	 * 
	 * @param signature
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public OperAction queryOperAction(OperSignature signature)
			throws RemoteException, Exception;

	/**
	 * ���ݲ���ǩ����֤Ȩ��
	 * 
	 * @param sessionId
	 * @param signature
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperSignature signature)
			throws RemoteException, Exception;

	/**
	 * ����ҵ�������֤Ȩ��
	 * 
	 * @param sessionId
	 * @param operAction
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperAction operAction)
			throws RemoteException, Exception;

	/**
	 * ����ϵͳ��־
	 * 
	 * @param sessionId
	 * @param signature
	 * @param operObject
	 * @param params
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void addLog(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) throws RemoteException,
			Exception;

	/**
	 * ���ݲ�ѯ������ѯϵͳ��־
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SystemLogQueryResult queryLog(LogQueryCondition condition)
			throws RemoteException, Exception;

}