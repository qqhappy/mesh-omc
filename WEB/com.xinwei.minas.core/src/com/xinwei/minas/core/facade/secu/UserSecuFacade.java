/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-22	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.secu;

import java.rmi.Remote;
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

public interface UserSecuFacade extends Remote {
	/**
	 * �����û�
	 * 
	 * @param user
	 */
	@Loggable
	public void addUser(User user) throws RemoteException, Exception;

	/**
	 * �޸��û�
	 */
	@Loggable
	public void modUser(User user) throws RemoteException, Exception;

	/**
	 * �޸��û�����
	 */
	@Loggable
	public void modUserPassword(String userName, String userPassword)
			throws RemoteException, Exception;

	/**
	 * ɾ���û�
	 * 
	 * @param userId
	 */
	@Loggable
	public void delUser(List<String> userIdList) throws RemoteException,
			Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public LoginUser queryUser(String userName) throws RemoteException,
			Exception;

	/**
	 * ��ѯ�����û�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUser() throws RemoteException, Exception;

	/**
	 * ��ѯ���������û�
	 * 
	 * @param isOnline
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LoginUser> queryAllUserByOnline() throws RemoteException,
			Exception;

	/**
	 * �û���¼
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void userLogin(LoginUser loginUser, MinasClientFacade clientFacade)
			throws RemoteException, Exception;

	/**
	 * �û��ǳ�
	 * 
	 * @param userName
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void userLogout(String sessionId) throws RemoteException, Exception;

	/**
	 * ����û�
	 * 
	 * @param sessionId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean checkUser(String sessionId) throws RemoteException,
			Exception;

	/**
	 * ��֤�û�����
	 * 
	 * @param username
	 * @param password
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void checkUserPassword(String username, String password)
			throws RemoteException, Exception;

	/**
	 * 
	 * ����
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws RemoteException, Exception;

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
			String actionType) throws RemoteException, Exception;

	/**
	 * �ỰIDΪsessionId���û�ǿ���û���Ϊusername���û�����
	 * 
	 * @param sessionId
	 * @param username
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void kickUser(String sessionId, String username)
			throws RemoteException, Exception;
}
