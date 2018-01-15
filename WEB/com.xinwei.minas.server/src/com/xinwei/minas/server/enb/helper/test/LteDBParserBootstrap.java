/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-3-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.platform.ServerFacadeManager;
import com.xinwei.minas.server.platform.startup.CustomFileSystemXmlApplicationContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 系统启动类
 * 
 * @author chenjunhua
 * 
 */

public final class LteDBParserBootstrap {

	public static final String APP_NAME = "MINAS";

	private Logger logger = null;

	private static LteDBParserBootstrap daemon = null;

	private String log4jConfigFile = "etc/log4j.properties";

	private String springConfigFile = "etc/applicationContext.xml";

	private FileSystemXmlApplicationContext applicationContext = null;

	public LteDBParserBootstrap() {
	}

	public void init() {
		long startTime = System.currentTimeMillis();
		// 配置LOG4J
		PropertyConfigurator.configureAndWatch(log4jConfigFile, 5000);
		//
		logger = Logger.getLogger(LteDBParserBootstrap.class);
		logger.info("Starting " + APP_NAME + " ...");
		// 初始化SPRING框架
		applicationContext = new CustomFileSystemXmlApplicationContext(
				springConfigFile);
		logger.info("Set application context ...");
		((OmpAppContext) applicationContext.getBean("platform.appContext"))
				.setApplicationContext(applicationContext);
		logger.info("Done.");
			
		// 初始化基站底层通信模块(此处后续需要优化，解除平台对模块的依赖)
		logger.info("Initialize bts connector module ...");
		McBtsConnector mcBtsConnector = applicationContext.getBean(McBtsConnector.class);
		try {
			mcBtsConnector.initialize();
			logger.info("Done.");
		} catch (Exception e) {
			logger.error("Failed to Initialize bts connector module", e);
		}
		// 初始化RMI
		logger.info("Initailize Server Facade...");
		ServerFacadeManager serverFacadeManager = new ServerFacadeManager();
		serverFacadeManager.initialize();
		logger.info("Done.");
		// 增加系统退出钩子
		ShutdownHook hook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(hook);
		// 计算启动时间
		long endTime = System.currentTimeMillis();
		logger.info(APP_NAME + " started in " + (float) (endTime - startTime)
				/ (float) 1000 + " seconds");
		
		try {
			Thread.sleep(3*60*1000);
		} catch (InterruptedException e) {
			
		}
		
		String pathname = "E:\\网管\\McLTE数据库设计\\V3.0.8.0\\V3.0.8.2_Sqlite.sql";
		String outputFile = "E:\\网管\\McLTE数据库设计\\V3.0.8.0\\V3.0.8.2_Parse.sql";
		List<String> sqls = LteDBParser.createTemplateSql("3.0.8", pathname);
		try {
			FileUtils.writeLines(new File(outputFile), sqls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("OK");
	}

	public static void main(String[] args) {
		daemon = new LteDBParserBootstrap();
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
