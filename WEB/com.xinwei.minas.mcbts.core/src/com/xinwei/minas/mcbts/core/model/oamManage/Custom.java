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
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class Custom implements Serializable {

	// �Զ������ʹ�õ�����service
	private ConfigServiceName[] configServiceName;
	// �Զ������ʹ�õĲ�ѯservice
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
