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
 * ��Դ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupplyCache {

	// ��Դ����
	private ConcurrentHashMap<String, PowerSupplyMoniter> moniterCache = new ConcurrentHashMap<String, PowerSupplyMoniter>();
	
	//��Դ��ص��̳߳�
	private ScheduledExecutorService executor; 
	
	private final static PowerSupplyCache instance = new PowerSupplyCache();
	
	private PowerSupplyCache() {

	}
	
	public static PowerSupplyCache getInstance() {
		return instance;
	}
	
	/**
	 * ��ʼ������
	 */
	public void initCache() {
		stopServices();
		moniterCache.clear();
	}
	
	/**
	 * ���¼��ص�Դ��ط���
	 * @param interval
	 */
	public void readloadMoniterService(int interval) {
		//��ʼ���̳߳�
		executor = Executors.newScheduledThreadPool(moniterCache.size());	
		for (PowerSupplyMoniter moniter : moniterCache.values()) {
			executor.scheduleAtFixedRate(moniter, interval, interval, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * ��ӵ�Դ��Ϣ
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
	 * ���ݵ�Դ���������̻�ȡ��ͬ�ĵ�Դ�����
	 * @param power
	 * @return
	 */
	public PowerSupplyMoniter createPowerSupplyMoniter(PowerSupply power) {
		
		PowerSupplyMoniter moniter = null;
		
		switch (power.getFactoryType()) {
		case PowerSupply.FACTORY_NINGBO: //�����������
			moniter = new NingBoPowerSupplyMoniter(power);
			break;
		}
		
		return moniter;
	}
	
	/**
	 * ���ݵ�Դ����Ϣ������Key
	 * @param power
	 * @return
	 */
	public String creatCacheKey(PowerSupply power) {
		return power.getIpAddress() + ":" + power.getPort() + ":" + power.getCurrentType();
	}
	
	/**
	 * ͣ�����еļ�ط���
	 */
	public void stopServices() {
		
		//�ͷ�monitor��ռ�е���Դ
		for (PowerSupplyMoniter moniter : moniterCache.values()) {
			moniter.dispose();
		}
		
		//ͣ�����ڵļ�ط���
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
	}
}
