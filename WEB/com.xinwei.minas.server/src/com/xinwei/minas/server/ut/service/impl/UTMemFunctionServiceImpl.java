/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service.impl;

import java.util.Date;
import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.ut.dao.MemSingalReportDAO;
import com.xinwei.minas.server.ut.proxy.UTMemFunctionProxy;
import com.xinwei.minas.server.ut.service.UTMemFunctionService;
import com.xinwei.minas.ut.core.model.MemSingalReport;
import com.xinwei.minas.ut.core.model.UTLayer3Param;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;

/**
 * 
 * MEM功能服务实现
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTMemFunctionServiceImpl implements UTMemFunctionService {

	private McBtsBizService mcBtsBizService;
	
	private SequenceService sequenceService;
	
	private MemSingalReportDAO memSingalReportDAO;
	
	private UTMemFunctionProxy utMemFunctionProxy;
	


	public void setMcBtsBizService(McBtsBizService mcBtsBizService) {
		this.mcBtsBizService = mcBtsBizService;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
	
	public void setMemSingalReportDAO(MemSingalReportDAO memSingalReportDAO) {
		this.memSingalReportDAO = memSingalReportDAO;
	}
	
	public void setUtMemFunctionProxy(UTMemFunctionProxy utMemFunctionProxy) {
		this.utMemFunctionProxy = utMemFunctionProxy;
	}

	@Override
	public void configMemSingalReport(Long moId, MemSingalReport memSingalReport)
			throws Exception {
		GenericBizData genericBizData = new GenericBizData("ut_mem_singal_report_config");
		genericBizData.addProperty(new GenericBizProperty("eid", memSingalReport.getPid()));
		genericBizData.addProperty(new GenericBizProperty("switchFlag", memSingalReport.getSwitchFlag()));
		genericBizData.addProperty(new GenericBizProperty("timeLength", memSingalReport.getDistance()));
		mcBtsBizService.sendCommand(moId, genericBizData);
		if (memSingalReport.getIdx() == null) {
			memSingalReport.setIdx(sequenceService.getNext());
		}
		memSingalReport.setLastStartTime(new Date());
		memSingalReportDAO.saveOrUpdate(memSingalReport);
	}

	@Override
	public MemSingalReport queryMemSingalReport(Long pid) throws Exception {
		return memSingalReportDAO.queryByPid(pid);
	}

	@Override
	public List<UTLayer3Param> queryMemLayer3Param(Long moId, Long pid)
			throws Exception {
		GenericBizData data = new GenericBizData("ut_mem_layer3_param");
		data.addProperty(new GenericBizProperty("eid", pid));
		return utMemFunctionProxy.queryMemLayer3Param(moId, data);
	}
	
	

}
