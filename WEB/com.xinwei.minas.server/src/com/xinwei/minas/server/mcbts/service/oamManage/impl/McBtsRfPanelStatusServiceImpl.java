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
import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsRfPanelStatusDAO;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsRfPanelStatusProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.BtsRfPanelStatusService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBtsRfPanelStatusServiceImpl实现
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatusServiceImpl implements BtsRfPanelStatusService {
	private Log log = LogFactory.getLog(McBtsRfPanelStatusServiceImpl.class);
	private McBtsRfPanelStatusDAO mcBtsRfPanelStatusDAO;
	private McBtsRfPanelStatusProxy mcBtsRfPanelStatusProxy;

	@Override
	public List<String> config(Integer restudy, Long moId) throws Exception {
		return null;
	}

	public McBtsRfPanelStatusDAO getMcBtsRfPanelStatusDAO() {
		return mcBtsRfPanelStatusDAO;
	}

	public McBtsRfPanelStatusProxy getMcBtsRfPanelStatusProxy() {
		return mcBtsRfPanelStatusProxy;
	}

	@Override
	public List<McBtsRfPanelStatus> queryInfoFromDB(long moId) throws Exception {
		return null;
	}

	@Override
	public McBtsRfPanelStatus queryInfoFromNE(Long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			McBtsRfPanelStatus mcBtsRfPanelStatus = null;
			try {
				mcBtsRfPanelStatus = mcBtsRfPanelStatusProxy
						.queryRfPanelStatus(moId);
				return mcBtsRfPanelStatus;
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		return null;
	}

	public void setMcBtsRfPanelStatusDAO(
			McBtsRfPanelStatusDAO McBtsRfPanelStatusDAO) {
		this.mcBtsRfPanelStatusDAO = McBtsRfPanelStatusDAO;
	}

	public void setMcBtsRfPanelStatusProxy(
			McBtsRfPanelStatusProxy McBtsRfPanelStatusProxy) {
		this.mcBtsRfPanelStatusProxy = McBtsRfPanelStatusProxy;
	}

}
