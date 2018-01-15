/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalRestartService;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * ÷’∂À÷ÿ∆Ù∑˛ŒÒ
 * @author zhuxiaozhan
 *
 */
public class TerminalRestartServiceImpl implements TerminalRestartService {

	private Log log = LogFactory.getLog(TerminalRestartServiceImpl.class);
	
	private McBtsBizProxy mcBtsBizProxy;
	
	public TerminalRestartServiceImpl() {
		
	}
	
	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}
	
	/* (non-Javadoc)
	 * @see com.xinwei.minas.server.mcbts.service.sysManage.TerminalRestartService#restartConfig(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public void restartConfig(Long moId, String eid) throws Exception {
		GenericBizData genericBizData = new GenericBizData("utRestartRequest");
		genericBizData.addProperty(new GenericBizProperty("Eid", eid));
		try {
			mcBtsBizProxy.config(moId, genericBizData);
		} catch (Exception e) {
			log.error("Failed to restart the user terminal", e);
			throw e;
		}
	}

}
