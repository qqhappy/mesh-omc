/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2014-04-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.helper.EnbStatusChangeHelper;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.server.cache.XUIMetaCache;

/**
 * 
 * eNB注册任务管理器
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterTaskManager {

	private static final Log log = LogFactory
			.getLog(EnbRegisterTaskManager.class);

	private static final EnbRegisterTaskManager instance = new EnbRegisterTaskManager();

	// 注册任务Map
	private Map<Long, EnbRegisterTask> taskMap = new ConcurrentHashMap<Long, EnbRegisterTask>();

	// 注册任务线程池
	private ThreadPoolExecutor threadPoolExecutor;

	// 最大基站同时注册个数
	private int maxRegisterNum = 10;

	// 重试次数
	private int retryNum = 10;

	// 重试间隔
	private long retryInterval = 15000;

	private EnbRegisterTaskManager() {
		threadPoolExecutor = new ThreadPoolExecutor(20, 20, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static EnbRegisterTaskManager getInstance() {
		return instance;
	}

	/**
	 * 处理注册通知
	 * 
	 * @param message
	 */
	public void handleRegisterNotify(EnbAppMessage message) {
		Long enbId = message.getEnbId();
		debug(enbId, "received enb register notification.");
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb == null) {
			debug(enbId, "enb does not exist");
			return;
		}
		// 如果未开站，则不允许注册
		if (!enb.isActive()) {
			debug(enbId, "enb is not active");
			return;
		}
		// 基站配置未初始化完全，不允许注册
		boolean configOk = XUIMetaCache.getInstance().isInitialized();
		if (!configOk) {
			debug(enbId, "XUIMetaCache doesn't finish initializing.");
			return;
		}
		// 设置eNB的公网IP和端口
		enb.setPublicIp(message.getPublicIp());
		enb.setPublicPort(message.getPublicPort());
		// 如果有正在执行的注册任务，则忽略注册通知消息
		if (taskMap.containsKey(enbId)) {
			debug(enbId, "enb is registering");
			return;
		}
		// 如果正在执行的注册任务数大于阈值
		if (taskMap.size() >= maxRegisterNum) {
			debug(enbId, "registering bts num is over threshold.");
			return;
		}
		// 只有未连接状态下，才接收注册通知消息
		if (enb.isDisconnected()) {
			// 设置状态为注册中
			EnbStatusChangeHelper.setRegistering(enb);
			EnbRegisterTask task = new EnbRegisterTask(enbId, this);
			taskMap.put(enbId, task);
			debug(enbId, "create register task.");
			threadPoolExecutor.execute(task);
		}
	}

	public void removeTask(Long btsId) {
		taskMap.remove(btsId);
	}

	private void debug(Long btsId, String message) {
		EnbUtils.log(btsId, "Register", message);
	}

	public void setMaxRegisterNum(int maxRegisterNum) {
		this.maxRegisterNum = maxRegisterNum;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public long getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

}
