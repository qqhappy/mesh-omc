/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.common.McBtsModeService;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * 基站工作模式服务类
 * 
 * @author tiance
 * 
 */

public class McBtsModeServiceImpl implements McBtsModeService {

	private McBtsBizProxy proxy;

	public void setProxy(McBtsBizProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public McBts queryMcBtsMode(McBts mcbts) throws Exception {
		long moId = mcbts.getMoId();

		GenericBizData data = new GenericBizData("mcbtsMode");

		GenericBizData result = proxy.query(moId, data);

		int workMode = Integer.parseInt(String.valueOf(result.getProperty(
				"workMode").getValue()));
		mcbts.setWorkMode(workMode);

		int bootSource = Integer.parseInt(String.valueOf(result.getProperty(
				"bootSource").getValue()));
		mcbts.setBootSource(bootSource);

		// TODO 假数据
//		mcbts.setBootSource(McBts.BOOT_SOURCE_BTS);
//		mcbts.setWorkMode(McBts.WORK_MODE_SINGLE);
		// TODO delete above
		return mcbts;
	}
}
