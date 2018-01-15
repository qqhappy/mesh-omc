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

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.common.McBtsSNDAO;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsUserCountsQueryDAO;
import com.xinwei.minas.server.mcbts.proxy.common.McBtsSNProxy;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsUserCountsQueryProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.McBtsSNService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsUserCountsQueryService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ServiceImpl实现
 * 
 * @author fangping
 * 
 */

public class McBtsUserCountsQueryServiceImpl implements
		McBtsUserCountsQueryService {

	private Log log = LogFactory.getLog(McBtsUserCountsQueryServiceImpl.class);

	private McBtsUserCountsQueryDAO mcBtsUserCountsQueryDAO;

	private McBtsUserCountsQueryProxy mcBtsUserCountsQueryProxy;

	@Override
	public List<String> config(Integer restudy, Long moId) throws Exception {
		return null;
	}

	public McBtsUserCountsQueryDAO getMcBtsUserCountsQueryDAO() {
		return mcBtsUserCountsQueryDAO;
	}

	public McBtsUserCountsQueryProxy getMcBtsUserCountsQueryProxy() {
		return mcBtsUserCountsQueryProxy;
	}

	@Override
	public List<McBtsUserCountsQuery> queryInfoFromDB(long moId)
			throws Exception {
		return null;
	}

	@Override
	public McBtsUserCountsQuery queryInfoFromNE(Long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			McBtsUserCountsQuery mcBtsSateQuery = null;
			try {
				mcBtsSateQuery = mcBtsUserCountsQueryProxy
						.queryUserCountsQuery(moId);
				return mcBtsSateQuery;
			} catch (Exception e) {
				log.error("Error querying mcbts user counts", e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		return null;
	}

	public void setMcBtsUserCountsQueryDAO(
			McBtsUserCountsQueryDAO mcBtsUserCountsQueryDAO) {
		this.mcBtsUserCountsQueryDAO = mcBtsUserCountsQueryDAO;
	}

	public void setMcBtsUserCountsQueryProxy(
			McBtsUserCountsQueryProxy mcBtsUserCountsQueryProxy) {
		this.mcBtsUserCountsQueryProxy = mcBtsUserCountsQueryProxy;
	}

}
