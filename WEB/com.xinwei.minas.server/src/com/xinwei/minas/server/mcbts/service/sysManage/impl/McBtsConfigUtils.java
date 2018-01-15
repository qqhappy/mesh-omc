/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-20	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.McBtsBasicFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 基站配置导入导出的工具类
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsConfigUtils {

	private static Log log = LogFactory.getLog(McBtsConfigServiceImpl.class);

	private McBtsConfigUtils() {

	}

	private static McBtsConfigUtils instance = null;

	public synchronized static McBtsConfigUtils getInstance() {
		if (instance == null) {
			instance = new McBtsConfigUtils();
		}
		return instance;
	}

	private static SequenceService getSequenceService() {
		return AppContext.getCtx().getBean(SequenceService.class);
	}
}
