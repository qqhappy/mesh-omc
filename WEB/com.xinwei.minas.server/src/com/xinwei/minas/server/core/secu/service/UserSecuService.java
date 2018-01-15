/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;

/**
 * �û���ȫ����
 * 
 * @author liuzhongyan
 * 
 */

public interface UserSecuService {
	/**
	 * �����û�
	 * 
	 * @param user
	 */
	public void addUser(User user) throws Exception;

	/**
	 * �޸��û�
	 */
	public void modUser(User user) throws Exception;

	/**
	 * �޸��û�����
	 */
	public void modUserPassword(String userName, String userPassword)
			throws Exception;

	/**
	 * ɾ���û�
	 * 
	 * @param userId
	 */
	public void delUser(List<String> userIdList) throws Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public LoginUser queryUser(String userName) throws Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws Exception;

	/**
	 * ��ѯ���������û�
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws Exception;

	/**
	 * �û���¼
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogin(LoginUser loginUser, MinasClientFacade clientFacade)
			throws Exception;

	/**
	 * �û��ǳ�
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void userLogout(String sessionId) throws Exception;

	/**
	 * ����û�
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkUser(String sessionId) throws Exception;

	/**
	 * ��֤�û�����
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void checkUserPassword(String username, String password)
			throws Exception;

	/**
	 * 
	 * ����
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws Exception;

	/**
	 * �жϵ�ǰ��¼�û��Ա������û��Ƿ��в���Ȩ��
	 * <p>
	 * Ȩ�޹���
	 * </p>
	 * <p>
	 * ��������Ա�������û�������ɾ�Ĳ�Ȩ�ޣ��ϼ��û����¼��û�����ɾ�Ĳ�Ȩ�ޣ�ͬ�����û�֮��ֻ�в鿴Ȩ��
	 * </p>
	 * <p>
	 * ����Ա����ɾ��Ȩ�ޣ�����Ա����ɾ�û�Ȩ�ޣ�ֻ���޸ĸ�����Ϣ���û�����ɾ���Լ���
	 * </p>
	 * 
	 * @param loginUser
	 *            ��ǰ��¼�û�
	 * @param operatedUser
	 *            �������û�
	 * @param actionType
	 *            ��������
	 * @return �Ƿ���Ȩ��
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean hasPrivilege(LoginUser loginUser, User operatedUser,
			String actionType) throws Exception;

	/**
	 * �ỰIDΪsessionId���û�ǿ���û���Ϊusername���û�����
	 * 
	 * @param sessionId
	 * @param username
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void kickUser(String sessionId, String username) throws Exception;
}
