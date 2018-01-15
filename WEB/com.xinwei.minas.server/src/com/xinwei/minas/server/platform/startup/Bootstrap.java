/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-3-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform.startup;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.platform.ServerFacadeManager;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ϵͳ������
 * 
 * @author chenjunhua
 * 
 */

public final class Bootstrap {

	public static final String APP_NAME = "MINAS";
	
	private Logger logger = null;

	private static Bootstrap daemon = null;

	private String log4jConfigFile = "etc/log4j.properties";

	private String springConfigFile = "etc/applicationContext.xml";

	private FileSystemXmlApplicationContext applicationContext = null;

	public Bootstrap() {
	}

	public void init() {
		long startTime = System.currentTimeMillis();
		// ����LOG4J
		PropertyConfigurator.configureAndWatch(log4jConfigFile, 5000);
		//
		logger = Logger.getLogger(Bootstrap.class);
		logger.info("Starting " + APP_NAME + " ...");
		// ��ʼ��SPRING���
		applicationContext = new CustomFileSystemXmlApplicationContext(
				springConfigFile);
		logger.info("Set application context ...");
		((OmpAppContext) applicationContext.getBean("platform.appContext"))
				.setApplicationContext(applicationContext);
		logger.info("Done.");
		// ��ʼ����վ�ײ�ͨ��ģ��(�˴�������Ҫ�Ż������ƽ̨��ģ�������)
		logger.info("Initialize bts connector module ...");
		McBtsConnector mcBtsConnector = applicationContext.getBean(McBtsConnector.class);
		try {
			mcBtsConnector.initialize();
			logger.info("Done.");
		} catch (Exception e) {
			logger.error("Failed to Initialize bts connector module", e);
		}
		// ��ʼ��RMI
		logger.info("Initailize Server Facade...");
		ServerFacadeManager serverFacadeManager = new ServerFacadeManager();
		serverFacadeManager.initialize();
		logger.info("Done.");
		// ����ϵͳ�˳�����
		ShutdownHook hook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(hook);
		// ��������ʱ��
		long endTime = System.currentTimeMillis();
		logger.info(APP_NAME + " started in " + (float) (endTime - startTime)
				/ (float) 1000 + " seconds");
	}

	public static void main(String[] args) {
		daemon = new Bootstrap();
		daemon.init();
	}

	class ShutdownHook extends Thread {
		public void run() {
			if (applicationContext != null) {
				try {
					applicationContext.close();
					if (logger != null) {
						logger.info(APP_NAME + " stopped.");
					}
				} catch (Exception ex) {
				}
			}
		}
	}

}