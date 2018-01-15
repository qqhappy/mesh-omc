/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.helper.McBtsStatusChangeHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsRegisterNotify;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.AutomaticFindMcBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站注册任务管理器
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterTaskManager {

	private static final Log log = LogFactory
			.getLog(McBtsRegisterTaskManager.class);

	private static final McBtsRegisterTaskManager instance = new McBtsRegisterTaskManager();

	// 注册任务Map
	private Map<Long, McBtsRegisterTask> taskMap = new ConcurrentHashMap<Long, McBtsRegisterTask>();
	// 自发现基站上次注册的时间Map
	private Map<Long, Long> lastRegTimeMap = new ConcurrentHashMap<Long, Long>();

	// 注册任务线程池
	private ThreadPoolExecutor threadPoolExecutor;

	// 是否自动创建发现McBTS(通过基站注册消息)
	private boolean createBtsAutomatically = true;

	// 最大基站同时注册个数
	private int maxRegisterNum = 10;

	// 重试次数（发送0x0602）
	private int retryNum = 10;

	// 重试间隔（发送0x0602）
	private long retryInterval = 15000;
	// 基站注册超时时间暂定为2分钟，超过2分钟未收到基站注册消息，将基站从自发现缓存中移除
	private int btsRegTimeout = 120000;
	// 执行扫描超时基站任务的开始延时及间隔，单位秒
	private static final int DELAY = 5;

	private static final int PERIOD = 30;

	// 基站基本服务接口
	private McBtsBasicService mcBtsBasicService;

	private McBtsRegisterTaskManager() {
		threadPoolExecutor = new ThreadPoolExecutor(20, 20, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128),
				new ThreadPoolExecutor.CallerRunsPolicy());
		mcBtsBasicService = AppContext.getCtx()
				.getBean(McBtsBasicService.class);
		// 用于定时扫描注册超时基站的线程
		ScheduledExecutorService removeTimeOutBtsService = Executors
				.newScheduledThreadPool(1);
		removeTimeOutBtsService.scheduleAtFixedRate(new RemoveTimeOutBtsTask(),
				DELAY, PERIOD, TimeUnit.SECONDS);
	}

	public static McBtsRegisterTaskManager getInstance() {
		return instance;
	}

	/**
	 * 处理注册通知
	 * 
	 * @param message
	 */
	public void handleRegisterNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();
		debug(btsId, "received mcbts register notification.");
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			// 如果基站在网管缓存中不存在, 则构造基站模型
			mcBts = createDefaultMcBts(btsId);
			this.updateMcBts(mcBts, message);
			if (createBtsAutomatically) {
				// 如果是自动创建基站，则直接调用服务创建基站(此模式仅在性能测试时使用)
				try {
					mcBtsBasicService.add(mcBts);
				} catch (Exception e) {
					log.error(
							"failed to add bts automatically. btsId=" + btsId,
							e);
				}
			} else {
				// 如果不是自动创建基站, 则将基站加入自发现缓存中
				AutomaticFindMcBtsCache.getInstance().addOrUpdate(mcBts);
				// 记录基站上次注册的时间
				lastRegTimeMap.put(mcBts.getBtsId(), new Date().getTime());
			}
			return;
		}
		// 更新缓存中基站模型
		this.updateMcBts(mcBts, message);
		// 如果有正在执行的注册任务，则忽略注册通知消息
		if (taskMap.containsKey(btsId)) {
			log.warn("bts is in registering, btsId=0x" + mcBts.getHexBtsId());
			debug(btsId, "bts is in registering");
			return;
		}
		// 如果正在执行的注册任务数大于阈值
		if (taskMap.size() >= maxRegisterNum) {
			log.warn("registering bts num is over threshold, reject btsId=0x"
					+ mcBts.getHexBtsId());
			debug(btsId, "registering bts num is over threshold.");
			return;
		}
		// 只有未连接状态下，才接收注册通知消息
		if (mcBts.isDisconnected()) {
			// 设置状态为注册中
			McBtsStatusChangeHelper.setRegistering(mcBts);
			McBtsRegisterTask task = new McBtsRegisterTask(btsId, this);
			taskMap.put(btsId, task);
			debug(btsId, "create register task.");
			threadPoolExecutor.execute(task);
		}
	}

	public void removeTask(Long btsId) {
		taskMap.remove(btsId);
	}

	private void debug(Long btsId, String message) {
		McBtsUtils.log(btsId, "Register", message);
	}

	/**
	 * 根据基站ID构建基站模型
	 * 
	 * @param btsId
	 * @return
	 */
	private McBts createDefaultMcBts(Long btsId) {
		McBts bts = new McBts();
		// 根据基站ID, 构建基站模型
		bts.setTypeId(MoTypeDD.MCBTS);
		bts.setBtsId(btsId);
		bts.setBtsType(McBtsTypeDD.FDDI_MCBTS);
		bts.setName("bts-" + bts.getHexBtsId());
		bts.setNatAPKey(1);
		bts.setNetworkId(1);
		bts.setBtsConfigIp("0.0.0.0");
		// 设置SAG信息
		bts.setSagDeviceId(0);
		bts.setSagSignalPointCode(0);
		bts.setSagSignalIp("0.0.0.0");
		bts.setSagVoiceIp("0.0.0.0");
		//
		int factor = (int) (btsId & 0x00000fff);
		bts.setSagMediaPort(10000 + ((factor * 2) % 3000));
		bts.setBtsMediaPort(10000 + ((factor * 2) % 3000));
		bts.setSagSignalPort(2000 + ((factor * 2) % 3000));
		bts.setBtsSignalPort(2000 + ((factor * 2) % 3000));
		bts.setBtsSignalPointCode(8192 + factor);
		// 位置区
		bts.setLocationAreaId(-1);
		return bts;
	}

	/**
	 * 更新基站模型
	 * 
	 * @param mcBts
	 * @param message
	 */
	private void updateMcBts(McBts mcBts, McBtsMessage message) {
		McBtsRegisterNotify content = new McBtsRegisterNotify(mcBts.getBtsId(),
				message.getContent());
		mcBts.setPublicIp(message.getPublicIp());
		mcBts.setPublicPort(message.getPublicPort());
		mcBts.setHardwareVersion(content.getHardwareVersion());
		mcBts.setSoftwareVersion(content.getSortwareVersion());
		mcBts.setBtsIp(content.getBtsConfigIp());
	}

	public void setCreateBtsAutomatically(boolean createBtsAutomatically) {
		this.createBtsAutomatically = createBtsAutomatically;
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

	private class RemoveTimeOutBtsTask implements Runnable {
		public RemoveTimeOutBtsTask() {
		}

		@Override
		public void run() {
			long currentTime = new Date().getTime();
			List<Long> toBeRemove = new ArrayList<Long>();
			for (Entry<Long, Long> entry : lastRegTimeMap.entrySet()) {
				if (currentTime - entry.getValue() > btsRegTimeout) {
					toBeRemove.add(entry.getKey());
				}
			}
			// 有要移除的基站
			if (toBeRemove.size() > 0) {
				AutomaticFindMcBtsCache cache = AutomaticFindMcBtsCache
						.getInstance();
				for (Long btsId : toBeRemove) {
					lastRegTimeMap.remove(btsId);
					// 从自发现缓存中移除
					cache.delete(btsId);
				}
			}
		}

	}
}
