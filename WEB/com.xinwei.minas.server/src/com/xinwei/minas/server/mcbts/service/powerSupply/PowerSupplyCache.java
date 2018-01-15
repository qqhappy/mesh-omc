/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * 电源管理
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupplyCache {

	// 电源缓存
	private ConcurrentHashMap<String, PowerSupplyMoniter> moniterCache = new ConcurrentHashMap<String, PowerSupplyMoniter>();
	
	//电源监控的线程池
	private ScheduledExecutorService executor; 
	
	private final static PowerSupplyCache instance = new PowerSupplyCache();
	
	private PowerSupplyCache() {

	}
	
	public static PowerSupplyCache getInstance() {
		return instance;
	}
	
	/**
	 * 初始化缓存
	 */
	public void initCache() {
		stopServices();
		moniterCache.clear();
	}
	
	/**
	 * 重新加载电源监控服务
	 * @param interval
	 */
	public void readloadMoniterService(int interval) {
		//初始化线程池
		executor = Executors.newScheduledThreadPool(moniterCache.size());	
		for (PowerSupplyMoniter moniter : moniterCache.values()) {
			executor.scheduleAtFixedRate(moniter, interval, interval, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * 添加电源信息
	 * @param power
	 */
	public void addPowerSupply(PowerSupply power) {
		String key = creatCacheKey(power);
		PowerSupplyMoniter moniter = createPowerSupplyMoniter(power);
		if (moniter != null) {
			moniterCache.put(key, moniter);
		}
	}
	
	/**
	 * 根据电源的生产厂商获取不同的电源监控类
	 * @param power
	 * @return
	 */
	public PowerSupplyMoniter createPowerSupplyMoniter(PowerSupply power) {
		
		PowerSupplyMoniter moniter = null;
		
		switch (power.getFactoryType()) {
		case PowerSupply.FACTORY_NINGBO: //宁波涉阳电柜
			moniter = new NingBoPowerSupplyMoniter(power);
			break;
		}
		
		return moniter;
	}
	
	/**
	 * 根据电源的信息来创建Key
	 * @param power
	 * @return
	 */
	public String creatCacheKey(PowerSupply power) {
		return power.getIpAddress() + ":" + power.getPort() + ":" + power.getCurrentType();
	}
	
	/**
	 * 停掉所有的监控服务
	 */
	public void stopServices() {
		
		//释放monitor所占有的资源
		for (PowerSupplyMoniter moniter : moniterCache.values()) {
			moniter.dispose();
		}
		
		//停掉现在的监控服务
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}
}
