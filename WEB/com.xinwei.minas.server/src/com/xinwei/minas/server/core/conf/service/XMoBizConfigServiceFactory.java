/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

import java.util.HashMap;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoTypeDD;

/**
 * 
 * XMoBizConfigService 工厂
 * 
 * @author chenjunhua
 * 
 */

public class XMoBizConfigServiceFactory {

	private static final XMoBizConfigServiceFactory instance = new XMoBizConfigServiceFactory();

	private Map<Integer, XMoBizConfigService> serviceMap = new HashMap();

	private XMoBizConfigServiceFactory() {

	}

	public static XMoBizConfigServiceFactory getInstance() {
		return instance;
	}

	public void addService(Integer moTypeId, XMoBizConfigService service) {
		serviceMap.put(moTypeId, service);
	}

	public XMoBizConfigService createXMoBizConfigService(Long moId) {
		// FIXME: eNB伪代码
		// Mo mo = MoCache.getInstance().queryByMoId(moId);
		// if (mo == null) {
		// throw new RuntimeException("Unknown eNB.");
		// }
		// int moTypeId = mo.getTypeId();
		int moTypeId = MoTypeDD.ENODEB;
		return serviceMap.get(moTypeId);
	}

	/**
	 * 根据网元类型获取支持的service
	 * 
	 * @param moType
	 * @return
	 */
	public XMoBizConfigService createXMoBizConfigService(int moType) {
		return serviceMap.get(moType);
	}

}
