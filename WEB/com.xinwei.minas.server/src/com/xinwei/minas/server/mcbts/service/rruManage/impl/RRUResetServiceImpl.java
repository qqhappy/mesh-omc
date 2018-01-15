/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.rruManage.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.proxy.rruManage.RRUResetProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.rruManage.RRUResetService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

public class RRUResetServiceImpl implements RRUResetService {
	private Log log = LogFactory.getLog(RRUResetServiceImpl.class);
	private RRUResetProxy rruResetProxy;

	@Override
	public void config(Mo mo) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(mo.getMoId());
		if (bts == null && mo.getMoId() >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 发送消息
			try {
				rruResetProxy.config(mo.getMoId());
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
	}

	public RRUResetProxy getRruResetProxy() {
		return rruResetProxy;
	}

	public void setRruResetProxy(RRUResetProxy rruResetProxy) {
		this.rruResetProxy = rruResetProxy;
	}

}
