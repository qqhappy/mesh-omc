/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-2	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.opensymphony.xwork2.ActionContext;
import com.xinwei.lte.web.enb.cache.EnbRealtimeItemConfigCache;
import com.xinwei.lte.web.enb.cache.EnbVersionBizConfigCache;
import com.xinwei.lte.web.enb.cache.FieldLevelCache;
import com.xinwei.lte.web.enb.cache.HttpSessionRegistry;
import com.xinwei.lte.web.enb.cache.StatItemConfigCache;
import com.xinwei.lte.web.enb.proxy.DynamicProxyHandler;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.minas.core.facade.MinasServerFacade;
import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.core.facade.secu.AuthorityManageFacade;
import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogParam;
import com.xinwei.minas.enb.core.facade.EnbRealtimeMonitorFacade;
import com.xinwei.minas.enb.core.facade.EnbStatBizFacade;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * MinasSession
 * 
 * @author chenjunhua
 * 
 */
public class MinasSession {

	public static final String DEFAULT_SESSION_ID = "~!@#$%^&*ABCDEFG1234567";

	private static Logger log = null;

	private static final MinasSession instance = new MinasSession();

	private static final String FACADE_NAME = "MinasServerFacade";

	private String sessionId = "~!@#$%^&*ABCDEFG1234567";

	private MinasServerFacade minasServerFacade;

	// 握手失败次数
	private int handshakeFailedTimes = 0;

	// 握手失败次数阈值
	private int handshakeFailedTimesThreshold = 1;

	private String minasServerIp;

	private int minasServerPort;

	private ScheduledExecutorService schService;

	private AddLogHandler addLogHandler;

	private BlockingQueue<LogParam> logParamQueue = new LinkedBlockingDeque<LogParam>(
			1024);

	private MinasSession() {
		this.createLoggerIfNotExist();
	}

	public static MinasSession getInstance() {
		return instance;
	}

	/**
	 * 初始化session
	 * 
	 */
	public synchronized boolean initialize(String minasServerIp,
			int minasServerPort) throws Exception {
		this.minasServerIp = minasServerIp;
		this.minasServerPort = minasServerPort;
		try {
			// 初始化服务器门面
			this.initializeMinasServerFacade(minasServerIp, minasServerPort);
			// 初始化缓存
			initCache();
			// 启动记录操作日志线程
			startAddLogHandler();
		} catch (Exception e) {
			e.printStackTrace();
			// 初始化失败，将服务器门面接口置空
			minasServerFacade = null;
			log.error("initialize MinasServerFacade failed", e);
		}
		// 启动定时握手
		log.debug("start handshake task ...");
		startHandshakeRunner();
		return minasServerFacade != null;
	}

	/**
	 * 初始化系统所需缓存
	 * 
	 * @throws Exception
	 */
	private void initCache() throws Exception {
		XMoBizConfigFacade facade = minasServerFacade.getFacade(
				DEFAULT_SESSION_ID, XMoBizConfigFacade.class);
		// 初始化基站业务配置缓存
		EnbVersionBizConfigCache.getInstance().initialize(facade);
		// 初始化字段等级缓存
		FieldLevelCache.getInstance().initialize(facade);
		// 初始化性能数据统计项缓存
		EnbStatBizFacade statBizFacade = minasServerFacade.getFacade(
				DEFAULT_SESSION_ID, EnbStatBizFacade.class);
		StatItemConfigCache.getInstance().initialize(statBizFacade);
		// 初始化eNB实时性能统计项配置缓存
		EnbRealtimeMonitorFacade realtimeMonitorFacade = minasServerFacade
				.getFacade(DEFAULT_SESSION_ID, EnbRealtimeMonitorFacade.class);
		EnbRealtimeItemConfigCache.getInstance().initialize(
				realtimeMonitorFacade);
	}
	
	
	/**
	 * 获取指定接口的门面
	 * 
	 * @param facadeInterface
	 *            门面接口
	 * @return 指定接口的门面
	 */
	public <T> T getFacade(Class<T> facadeInterface) throws Exception {
		String sessionId = ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
		return this.getFacade(sessionId, facadeInterface);
	}

	/**
	 * 获取指定接口的门面
	 * 
	 * @param facadeInterface
	 *            门面接口
	 * @return 指定接口的门面
	 */
	public <T> T getFacade(String sessionId, Class<T> facadeInterface)
			throws Exception {
		try {
			MinasServerFacade minasServerFacade = getMinasServerFacade();
			T facade = minasServerFacade.getFacade(sessionId, facadeInterface);
			T proxy = DynamicProxyHandler.getProxy(sessionId, facade,
					facadeInterface, minasServerFacade);
			return proxy;
		} catch (Exception e) {
			log.error("MinasSession getFacade failed. facadeName="
					+ facadeInterface.getCanonicalName(), e);
			throw new Exception("failed to connect minas server.");
		}
	}

	public MinasServerFacade getMinasServerFacade() throws Exception {
		return minasServerFacade;
	}

	/**
	 * 初始化服务端总门面
	 * 
	 * @param ip
	 * @param port
	 * 
	 * @throws Exception
	 */
	private void initializeMinasServerFacade(String ip, int port)
			throws Exception {
		try {
			Registry registry = LocateRegistry.getRegistry(ip, port);
			minasServerFacade = (MinasServerFacade) registry
					.lookup(FACADE_NAME);
		} catch (Exception e) {
			log.error("failed to initialize server facade.", e);
			throw e;
		}
	}

	/**
	 * 创建滚动文件日志
	 */
	private void createLoggerIfNotExist() {
		if (log != null) {
			return;
		}
		String fileName = "log/session.log";

		try {
			log = Logger.getLogger(MinasSession.class);
			log.setAdditivity(false);
			log.setLevel(Level.DEBUG);

			log.removeAllAppenders();

			PatternLayout patterLayout = new PatternLayout();
			patterLayout.setConversionPattern("%d %m%n");
			RollingFileAppender appender = null;
			appender = new RollingFileAppender(patterLayout, fileName);
			appender.setMaxBackupIndex(5);
			appender.setMaxFileSize("4096kb");
			log.addAppender(appender);

		} catch (Exception e) {
			log.error("createLoggerIfNotExist()", e); //$NON-NLS-1$
			log = Logger.getLogger(MinasSession.class);
		}
	}

	/**
	 * 启动记录操作日志线程
	 */
	private void startAddLogHandler() {
		if (addLogHandler != null) {
			addLogHandler.shutdown();
		}
		addLogHandler = new AddLogHandler(logParamQueue);
		addLogHandler.start();
	}

	/**
	 * 启动握手线程
	 */
	public void startHandshakeRunner() {
		if (schService == null || schService.isShutdown()) {
			schService = Executors.newScheduledThreadPool(1);
			schService.scheduleAtFixedRate(new HandshakeTask(), 15, 15,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * 根据操作签名鉴权
	 * 
	 * @param sessionId
	 * @param signature
	 * @throws Exception
	 */
	public void checkPrivilege(String sessionId, OperSignature signature)
			throws Exception {
		try {
			AuthorityManageFacade authorityFacade = minasServerFacade
					.getFacade(sessionId, AuthorityManageFacade.class);
			boolean hasPrivilege = authorityFacade.checkPrivilege(sessionId,
					signature);
			if (!hasPrivilege) {
				throw new NoAuthorityException("当前用户无权进行此操作!");
			}
		} catch (Exception e) {
			throw new NoAuthorityException("当前用户无权进行此操作!");
		}
	}

	/**
	 * 根据业务名和操作方式进行鉴权
	 * 
	 * @param sessionId
	 * @param operName
	 * @param actionType
	 * @throws Exception
	 */
	public void checkPrivilege(String sessionId, String operName,
			String actionType) throws Exception {
		try {
			AuthorityManageFacade authorityFacade = minasServerFacade
					.getFacade(sessionId, AuthorityManageFacade.class);
			OperAction operAction = new OperAction();
			operAction.setOperName(operName);
			List<String> actions = new ArrayList<String>();
			actions.add(actionType);
			operAction.setActions(actions);
			boolean hasPrivilege = authorityFacade.checkPrivilege(sessionId,
					operAction);
			if (!hasPrivilege) {
				throw new NoAuthorityException("当前用户无权进行此操作!");
			}
		} catch (Exception e) {
			throw new NoAuthorityException("当前用户无权进行此操作!");
		}
	}

	/**
	 * 记录日志
	 * 
	 * @param logParam
	 */
	public void addLog(LogParam logParam) {
		logParamQueue.offer(logParam);
	}

	class HandshakeTask implements Runnable {

		@Override
		public void run() {
			try {
				// 握手
				log.debug("handshake ...");
				if (minasServerFacade == null) {
					handshakeFailedTimes++;
					log.debug("handshake failed. serverFacade is null.");
				} else {
					minasServerFacade.handshake(sessionId);
					// 代理所有登录用户向后台发起握手
					handshakeAllUsers();
					handshakeFailedTimes = 0;
					log.debug("handshake ok.");
				}
			} catch (Exception e) {
				handshakeFailedTimes++;
				log.error("handshake failed. ftimes=" + handshakeFailedTimes, e);
			}
			if (handshakeFailedTimes >= handshakeFailedTimesThreshold) {
				// 握手失败次数超过阈值
				try {
					log.debug("relogin server ...");
					boolean initOk = MinasSession.this.initialize(
							minasServerIp, minasServerPort);
					if (initOk) {
						handshakeFailedTimes = 0;
						log.info("relogin server ok.");
					} else {
						log.error("failed to relogin with server.");
					}
				} catch (Exception e) {
					log.error("failed to relogin with server.", e);
				}
			}
		}

	}

	class AddLogHandler extends Thread {

		private Log log = LogFactory.getLog(AddLogHandler.class);

		private BlockingQueue<LogParam> logParamQueue;

		private volatile boolean running = true;

		public AddLogHandler(BlockingQueue<LogParam> logParamQueue) {
			this.logParamQueue = logParamQueue;
		}

		@Override
		public void run() {
			while (running) {
				try {
					LogParam logParam = logParamQueue.take();
					MinasServerFacade minasServerFacade = MinasSession.this
							.getMinasServerFacade();
					AuthorityManageFacade facade = minasServerFacade.getFacade(
							logParam.getSessionId(),
							AuthorityManageFacade.class);
					// 记录日志
					long startTime = System.currentTimeMillis();
					facade.addLog(logParam.getSessionId(),
							logParam.getSignature(), logParam.getOperObject(),
							logParam.getParams());
					long finishTime = System.currentTimeMillis();
					long cost = finishTime - startTime;
					if (cost > 200) {
						StringBuilder msgBuilder = new StringBuilder();
						msgBuilder.append("Add log cost time ").append(cost)
								.append("ms").append(". ")
								.append(logParam.getSignature().toString())
								.append(", ")
								.append(logParam.getOperObject().toString());
						log.debug(msgBuilder.toString());
					}

				} catch (InterruptedException e) {
					break;
				} catch (Exception e) {
					log.error("add log failed.", e);
				}
			}
		}

		public void shutdown() {
			running = false;
			this.interrupt();
		}

	}

	/**
	 * 代理所有登录用户向后台发起握手
	 */
	public void handshakeAllUsers() {
		List<LoginUser> users = HttpSessionRegistry.getInstance()
				.getAllLoginUser();
		for (LoginUser user : users) {
			try {
				MinasSession.getInstance().getMinasServerFacade()
						.handshake(user.getSessionId());
				log.debug(user.getUsername() + " handshake is OK !");
				System.out.println(user.getUsername() + " handshake is OK !");
			} catch (Exception e) {
				log.error(user.getUsername() + " handshake failed. sessionId="
						+ user.getSessionId());
				e.printStackTrace();
			}
		}
		System.out.println("===="
				+ HttpSessionRegistry.getInstance().getSessionLength()
				+ "=====");
	}

}
