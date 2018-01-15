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

public class Universal implements Serializable {

	// ͨ������ҵ��
	private ConfigBizName[] configBizName;
	// ͨ�ò�ѯҵ��
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
