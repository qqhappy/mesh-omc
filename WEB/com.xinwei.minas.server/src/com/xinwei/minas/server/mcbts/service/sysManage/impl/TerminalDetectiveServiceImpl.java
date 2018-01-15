/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-5	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.sysManage.TerminalDetectiveProxy;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalDetectiveService;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;

/**
 * @author zhuxiaozhan
 * 
 */
public class TerminalDetectiveServiceImpl implements TerminalDetectiveService {

	private McBtsBizProxy mcBtsBizProxy;

	private TerminalDetectiveProxy terminalDetectiveProxy;

	public TerminalDetectiveServiceImpl() {

	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setTerminalDetectiveProxy(
			TerminalDetectiveProxy terminalDetectiveProxy) {
		this.terminalDetectiveProxy = terminalDetectiveProxy;
	}

	@Override
	public UserTerminal detectiveQuery(Long moId, String eid) throws Exception {
		GenericBizData utQueryBizData = new GenericBizData("utQueryRequest");
		utQueryBizData.addProperty(new GenericBizProperty("Eid", eid));
		GenericBizData utIpBizData = new GenericBizData("utConnectIpRequest");
		utIpBizData.addProperty(new GenericBizProperty("Eid", eid));

		GenericBizData response1 = mcBtsBizProxy.query(moId, utQueryBizData);

		UserTerminal ut = terminalDetectiveProxy.query(moId, utIpBizData);

		if (combinedResoponse(response1, ut)) {
			return ut;
		}

		return null;
	}

	public boolean combinedResoponse(GenericBizData response1, UserTerminal ut) {
		if (response1 == null || response1.getProperty("Eid") == null)
			return false;

		String pid1 = response1.getProperty("Eid").getValue().toString();
		String pid2 = ut.getPid();

		if (pid1.equals(pid2)) {
			return true;
		}

		return false;
	}

}
