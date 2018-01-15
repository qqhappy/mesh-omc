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

public class Custom implements Serializable {

	// 自定义界面使用的配置service
	private ConfigServiceName[] configServiceName;
	// 自定义界面使用的查询service
	private String[] queryServiceName;

	public ConfigServiceName[] getConfigServiceName() {
		return configServiceName;
	}

	public void setConfigServiceName(ConfigServiceName[] configServiceName) {
		this.configServiceName = configServiceName;
	}

	public String[] getQueryServiceName() {
		return queryServiceName;
	}

	public void setQueryServiceName(String[] queryServiceName) {
		this.queryServiceName = queryServiceName;
	}

}
