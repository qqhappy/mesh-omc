/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.service.impl;

import java.util.List;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;
import com.xinwei.minas.server.enb.service.EnbBasicService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.stat.dao.EnbRealtimeItemConfigDAO;
import com.xinwei.minas.server.enb.stat.proxy.EnbRealtimeMonitorProxy;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeMonitorService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB实时性能监控服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeMonitorServiceImpl implements EnbRealtimeMonitorService {

	private EnbBasicService enbBasicService;

	private EnbRealtimeMonitorProxy enbRealtimeMonitorProxy;

	private EnbRealtimeItemConfigDAO enbRealtimeItemConfigDAO;

	@Override
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception {
		return enbRealtimeItemConfigDAO.queryItemConfig();
	}

	@Override
	public void startMonitor(String sessionId, long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 可配置时才下发，不可配置抛出异常
		if (enb.isConfigurable()) {
			enbRealtimeMonitorProxy.start(enb.getEnbId(), 0);
			enb.setMonitorState(Enb.MONITOR_STATE_START);
			enbBasicService.modify(enb);
		} else {
			throw new Exception(OmpAppContext.getMessage("enb_cannot_config"));
		}
	}

	@Override
	public void stopMonitor(String sessionId, long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		if (enb == null) {
			throw new Exception(OmpAppContext.getMessage("enb_not_exists"));
		}
		// 可配置时才下发，不可配置抛出异常
		if (enb.isConfigurable()) {
			enbRealtimeMonitorProxy.stop(enb.getEnbId());
			enb.setMonitorState(Enb.MONITOR_STATE_STOP);
			enbBasicService.modify(enb);
		} else {
			// 基站不在线时，直接设置为停止
			enb.setMonitorState(Enb.MONITOR_STATE_STOP);
			enbBasicService.modify(enb);
		}
	}

	public void setEnbRealtimeItemConfigDAO(
			EnbRealtimeItemConfigDAO enbRealtimeItemConfigDAO) {
		this.enbRealtimeItemConfigDAO = enbRealtimeItemConfigDAO;
	}

	public void setEnbRealtimeMonitorProxy(
			EnbRealtimeMonitorProxy enbRealtimeMonitorProxy) {
		this.enbRealtimeMonitorProxy = enbRealtimeMonitorProxy;
	}

	public void setEnbBasicService(EnbBasicService enbBasicService) {
		this.enbBasicService = enbBasicService;
	}

}
