/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.server.enb.dao.EnbBizTemplateDAO;
import com.xinwei.minas.server.enb.service.EnbBizTemplateService;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB业务数据模板服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbBizTemplateServiceImpl implements EnbBizTemplateService {

	private EnbBizTemplateDAO enbBizTemplateDAO;

	public void setEnbBizTemplateDAO(EnbBizTemplateDAO enbBizTemplateDAO) {
		this.enbBizTemplateDAO = enbBizTemplateDAO;
	}

	@Override
	public Map<String, List<XBizRecord>> queryTemplateData(int enbTypeId,
			String protocolVersion) throws Exception {
		return enbBizTemplateDAO.queryTemplateData(enbTypeId, protocolVersion);
	}

	@Override
	public XBizTable queryTemplateData(int enbTypeId, String protocolVersion,
			String tableName) throws Exception {
		return enbBizTemplateDAO.queryTemplateData(enbTypeId, protocolVersion,
				tableName);
	}

}
