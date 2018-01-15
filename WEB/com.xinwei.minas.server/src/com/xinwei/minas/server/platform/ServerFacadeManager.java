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
 * 服务器RMI门面管理器
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
	 * 初始化
	 */
	public void initialize() {
		try {
			// 启动参数 -Drmi.port=xxx 指定RMI端口号
			rmiPort = (Integer) Integer
					.parseInt(System.getProperty("rmi.port"));
		} catch (NumberFormatException e1) {
		}
		// 设置RMI服务对外提供的IP地址
		String serverIp = OmpAppContext.getPropertyByName("platform.server.ip");
		// 该代码已转移到OmpAppContent
//		if (!StringUtils.isEmpty(serverIp)) {
//			System.setProperty("java.rmi.server.hostname", serverIp);
//		}
		// 创建并绑定RMI服务
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
