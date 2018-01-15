/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-10	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.oamManage;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class Universal implements Serializable {

	// 通用配置业务
	private ConfigBizName[] configBizName;
	// 通用查询业务
	private String[] queryBizName;

	public ConfigBizName[] getConfigBizName() {
		return configBizName;
	}

	public void setConfigBizName(ConfigBizName[] configBizName) {
		this.configBizName = configBizName;
	}

	public String[] getQueryBizName() {
		return queryBizName;
	}

	public void setQueryBizName(String[] queryBizName) {
		this.queryBizName = queryBizName;
	}

}
