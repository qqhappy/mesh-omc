/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

import com.xinwei.minas.core.model.SystemProperty;

/**
 * 
 * 获取系统属性的服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface SystemPropertyService {
	/**
	 * 从sys_properties表中获取一个SystemProperty
	 * 
	 * @param category
	 * @param subCategory
	 * @param property
	 * @return SystemProperty
	 */
	public SystemProperty getProperty(String category, String subCategory,
			String property);

	/**
	 * 设置一个属性到sys_properties
	 * 
	 * @param category
	 * @param subCategory
	 * @param property
	 * @param value
	 * @return 0: fail; >0: success
	 */
	public void setProperty(String category, String subCategory,
			String property, String value);
}
