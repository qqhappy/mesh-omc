/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsStateQueryDAO;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsStateQueryProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsStateQueryService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBtsStateQueryServiceImpl实现
 * 
 * @author fangping
 * 
 */

public class McBtsStateQueryServiceImpl implements McBtsStateQueryService {

	private Log log = LogFactory.getLog(McBtsStateQueryServiceImpl.class);
	private McBtsStateQueryDAO mcBtsStateQueryDAO;

	private McBtsStateQueryProxy mcBtsStateQueryProxy;

	@Override
	public List<String> config(Integer restudy, Long moId) throws Exception {
		return null;
	}

	public McBtsStateQueryDAO getMcBtsStateQueryDAO() {
		return mcBtsStateQueryDAO;
	}

	public McBtsStateQueryProxy getMcBtsStateQueryProxy() {
		return mcBtsStateQueryProxy;
	}

	@Override
	public List<McBtsSateQuery> queryInfoFromDB(long moId) throws Exception {
		return null;
	}

	@Override
	public McBtsSateQuery queryInfoFromNE(Long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			McBtsSateQuery mcBtsSateQuery = null;
			try {
				mcBtsSateQuery = mcBtsStateQueryProxy.queryStateQuery(moId);
				return mcBtsSateQuery;
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		return null;
	}

	public void setMcBtsStateQueryDAO(McBtsStateQueryDAO mcBtsStateQueryDAO) {
		this.mcBtsStateQueryDAO = mcBtsStateQueryDAO;
	}

	public void setMcBtsStateQueryProxy(
			McBtsStateQueryProxy mcBtsStateQueryProxy) {
		this.mcBtsStateQueryProxy = mcBtsStateQueryProxy;
	}

}
