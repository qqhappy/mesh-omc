/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.minas.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.model.secu.LoginUser;

/**
 * 
 * ���������
 * 
 * @author chenjunhua
 * 
 */
public interface MinasServerFacade extends Remote {

	/**
	 * ��¼
	 * 
	 * @param username
	 * @param password
	 * @param clientFacade
	 * @return
	 * @throws Exception
	 */
	public String login(LoginUser loginUser, MinasClientFacade clientFacade,
			String clientVersion, Map<String, Object> attributes)
			throws RemoteException, BizException, Exception;

	/**
	 * �˳�
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 */
	public void logout(String sessionId) throws RemoteException, Exception;

	/**
	 * ��ȡָ���ӿڵ�����
	 * 
	 * @param sessionId
	 *            �ỰID
	 * @param facadeInterface
	 *            ����ӿ�
	 * @return ָ���ӿڵ�����
	 */
	public <T> T getFacade(String sessionId, Class<T> facadeInterface)
			throws RemoteException, Exception;

	/**
	 * ����
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws RemoteException, Exception;
}
