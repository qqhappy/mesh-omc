/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.rruManage.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.proxy.rruManage.RRUHWInfoProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.rruManage.RRUHWInfoService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * RRUӲ����Ϣ��ѯserviceʵ����
 * 
 * @author chenshaohua
 * 
 */

// TODO:����
public class RRUHWInfoServiceImpl implements RRUHWInfoService {
	private Log log = LogFactory.getLog(RRUHWInfoServiceImpl.class);
	private RRUHWInfoProxy rruHWInfoProxy;

	@Override
	public McBtsSN queryHWInfoFromNE(Long moId) throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ������״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (bts != null && bts.isConfigurable()) {
			// ������Ϣ
			try {
				return rruHWInfoProxy.queryRRUHWInfo(moId);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_query_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		return null;
	}

	public RRUHWInfoProxy getRruHWInfoProxy() {
		return rruHWInfoProxy;
	}

	public void setRruHWInfoProxy(RRUHWInfoProxy rruHWInfoProxy) {
		this.rruHWInfoProxy = rruHWInfoProxy;
	}

}
