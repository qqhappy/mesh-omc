/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.core.facade.MinasServerFacade;
import com.xinwei.minas.core.facade.secu.UserSecuFacade;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.server.core.secu.service.MinasServerVersionService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ���������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class MinasServerFacadeImpl extends UnicastRemoteObject implements
		MinasServerFacade {

	private static transient final Log log = LogFactory
			.getLog(MinasServerFacadeImpl.class);

	public MinasServerFacadeImpl() throws RemoteException {
		super();
	}

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
			throws BizException, Exception {
		// if (!checkClientVersion(clientVersion)) {
		// throw new
		// BizException(OmpAppContext.getMessage("mcbts_checkVersion"));
		// }
		// ����session id
		String sessionId = this.generateSessionId();
		UserSecuFacade userSecu = AppContext.getCtx().getBean(
				UserSecuFacade.class);
		loginUser.setSessionId(sessionId);
		try {
			userSecu.userLogin(loginUser, clientFacade);
		} catch (Exception e) {
			throw new BizException(e.getLocalizedMessage());
		}
		return sessionId;
	}

	private boolean checkClientVersion(String clientVersion) throws Exception {
		MinasServerVersionService service = AppContext.getCtx().getBean(
				MinasServerVersionService.class);
		String serverVersion = service.getMinasServerVersion().getVersion();
		// Ĭ�ϰ汾�Ų�֧��
		boolean flag = false;
		// begin:�汾�ŵ�У��
		String[] clientVersionArray = clientVersion.split("\\.");
		String[] serviceVersionArray = serverVersion.split("\\.");
		// ����Ƿ���ϰ汾�ŵĹ涨
		if (clientVersionArray.length == serviceVersionArray.length
				&& serviceVersionArray.length == 4) {
			int num = 0;
			for (int i = 0; i < clientVersionArray.length - 1; i++) {
				String serviceTemp = serviceVersionArray[i];
				String clientTemp = clientVersionArray[i];

				if (clientTemp != null && serviceTemp != null
						&& clientTemp.equals(serviceTemp)) {
					num++;
					continue;
				} else {
					// ����ͻ��˵İ汾�ŵ�ǰ��λ�����˲�ͬ
					flag = false;
					break;
				}
			}
			// ѭ��������ǰ��λ��ͬ

			if (num == 3) {
				if (clientVersionArray[num].length() > 0
						&& serviceVersionArray[num].length() > 0
						&& Integer.valueOf(clientVersionArray[num]) >= Integer
								.valueOf(serviceVersionArray[num])) {
					flag = true;

				}
			}

		}
		// end:�汾��У��

		return flag;
	}

	/**
	 * �˳�
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 */
	public void logout(String sessionId) throws Exception {
		UserSecuFacade userSecu = AppContext.getCtx().getBean(
				UserSecuFacade.class);
		userSecu.userLogout(sessionId);

	}

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
			throws Exception {
		UserSecuFacade userSecu = AppContext.getCtx().getBean(
				UserSecuFacade.class);
		if (!userSecu.checkUser(sessionId)) {
			log.debug("unauthorized user. sessionId=" + sessionId);
			throw new Exception(OmpAppContext.getMessage("unauthorized_user"));
		}
		try {
			return (T) AppContext.getCtx().getBean(facadeInterface);
		} catch (Exception e) {
			log.error("failed to getFacade:" + facadeInterface, e);
			throw e;
		}
	}

	/**
	 * ����
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void handshake(String sessionId) throws Exception {
		UserSecuFacade userSecu = AppContext.getCtx().getBean(
				UserSecuFacade.class);
		userSecu.handshake(sessionId);
	}

	/**
	 * ���������
	 * 
	 * @return
	 */
	private String generateSessionId() {
		String sessionId = String.valueOf(System.currentTimeMillis());
		Random r = new Random();
		int randomNumber = r.nextInt(100000000);
		sessionId += String.valueOf(randomNumber);
		return sessionId;
	}

}
