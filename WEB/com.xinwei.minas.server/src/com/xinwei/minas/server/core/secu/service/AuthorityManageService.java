/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;

/**
 * 
 * Ȩ�޹������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface AuthorityManageService {

	/**
	 * ��ѯ�û�Ȩ��
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(String username) throws Exception;

	/**
	 * ���ݲ���ǩ����ѯ��Ӧ��ҵ�����
	 * 
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public OperAction queryOperAction(OperSignature signature) throws Exception;

	/**
	 * ��ѯ�Ƿ���ָ������ǩ����Ȩ��
	 * 
	 * @param sessionId
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperSignature signature)
			throws Exception;

	/**
	 * ��ѯ�Ƿ���ָ��ҵ�������Ȩ��
	 * 
	 * @param sessionId
	 * @param operAction
	 * @return
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperAction operAction)
			throws Exception;

	/**
	 * ����ϵͳ��־
	 * 
	 * @param sessionId
	 * @param signature
	 * @param operObject
	 * @param params
	 * @throws Exception
	 */
	public void addLog(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) throws Exception;

	/**
	 * ���ݲ�ѯ������ѯϵͳ��־
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SystemLogQueryResult queryLog(LogQueryCondition condition)
			throws Exception;
}