/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service.impl;

import com.xinwei.minas.server.ut.proxy.UTPrefDataProxy;
import com.xinwei.minas.server.ut.service.UTPrefDataService;
import com.xinwei.minas.ut.core.model.UTPerfData;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * 查看终端性能数据服务
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTPrefDataServiceImpl implements UTPrefDataService{

	private UTPrefDataProxy utPrefDataProxy;

	public void setUtPrefDataProxy(UTPrefDataProxy utPrefDataProxy) {
		this.utPrefDataProxy = utPrefDataProxy;
	}



	@Override
	public UTPerfData query(Long moId, Long pid) throws Exception {
		GenericBizData data = new GenericBizData("ut_performance_data");
		data.addProperty(new GenericBizProperty("eid", pid));
		return utPrefDataProxy.query(moId, data);
	}

}
