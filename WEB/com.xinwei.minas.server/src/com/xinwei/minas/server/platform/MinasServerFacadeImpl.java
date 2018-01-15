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
 * 服务器外观实现
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
	 * 登录
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
		// 生成session id
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
		// 默认版本号不支持
		boolean flag = false;
		// begin:版本号的校验
		String[] clientVersionArray = clientVersion.split("\\.");
		String[] serviceVersionArray = serverVersion.split("\\.");
		// 检测是否符合版本号的规定
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
					// 如果客户端的版本号的前三位与服务端不同
					flag = false;
					break;
				}
			}
			// 循环跳出后，前三位相同

			if (num == 3) {
				if (clientVersionArray[num].length() > 0
						&& serviceVersionArray[num].length() > 0
						&& Integer.valueOf(clientVersionArray[num]) >= Integer
								.valueOf(serviceVersionArray[num])) {
					flag = true;

				}
			}

		}
		// end:版本号校验

		return flag;
	}

	/**
	 * 退出
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
	 * 获取指定接口的门面
	 * 
	 * @param sessionId
	 *            会话ID
	 * @param facadeInterface
	 *            门面接口
	 * @return 指定接口的门面
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
	 * 握手
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
	 * 生成随机数
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
