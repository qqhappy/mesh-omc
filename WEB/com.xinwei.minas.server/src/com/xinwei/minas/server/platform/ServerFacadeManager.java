/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.facade.MinasServerFacade;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ������RMI���������
 * 
 * @author chenjunhua
 * 
 */

public class ServerFacadeManager {

	private int rmiPort = 11099;

	public static final String FACADE_NAME = "MinasServerFacade";

	private static final Log logger = LogFactory
			.getLog(ServerFacadeManager.class);

	/**
	 * ��ʼ��
	 */
	public void initialize() {
		try {
			// �������� -Drmi.port=xxx ָ��RMI�˿ں�
			rmiPort = (Integer) Integer
					.parseInt(System.getProperty("rmi.port"));
		} catch (NumberFormatException e1) {
		}
		// ����RMI��������ṩ��IP��ַ
		String serverIp = OmpAppContext.getPropertyByName("platform.server.ip");
		// �ô�����ת�Ƶ�OmpAppContent
//		if (!StringUtils.isEmpty(serverIp)) {
//			System.setProperty("java.rmi.server.hostname", serverIp);
//		}
		// ��������RMI����
		try {
			LocateRegistry.createRegistry(rmiPort);
			logger.info(FACADE_NAME + " createRegistry, port=" + rmiPort);
			Registry registry = LocateRegistry.getRegistry(serverIp, rmiPort);
			logger.info(FACADE_NAME + " getRegistry, port=" + rmiPort);
			//
			MinasServerFacade serverFacade = new MinasServerFacadeImpl();
			registry.rebind(FACADE_NAME, serverFacade);
			logger.info(FACADE_NAME + " Bound.");
		} catch (Exception e) {
			logger.error(FACADE_NAME + " Bound Exception", e);
		}
	}
}
