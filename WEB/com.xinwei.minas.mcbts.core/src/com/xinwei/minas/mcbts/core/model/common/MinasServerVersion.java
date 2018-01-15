/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ����ƽ̨�������汾ģ��
 * 
 * 
 * @author fangping
 * 
 */

public class MinasServerVersion implements Serializable {
	private Date buildTime;

	private String version;

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
