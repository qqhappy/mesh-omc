/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.server.core.conf.dao.SystemPropertyDAO;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;

/**
 * 
 * 获取系统属性的服务
 * 
 * 
 * @author tiance
 * 
 */

public class SystemPropertyServiceImpl implements SystemPropertyService {

	SystemPropertyDAO systemPropertyDao;

	private Log log = LogFactory.getLog(SystemPropertyServiceImpl.class);

	public void setSystemPropertyDao(SystemPropertyDAO systemPropertyDao) {
		this.systemPropertyDao = systemPropertyDao;
	}

	/**
	 * 从sys_properties表中获取一个SystemProperty
	 * 
	 * @param category
	 * @param subCategory
	 * @param property
	 * @return SystemProperty
	 */
	@Override
	public SystemProperty getProperty(String category, String subCategory,
			String property) {
		return systemPropertyDao.queryByCategoryAndProperty(category,
				subCategory, property);
	}

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
			String property, String value) {
		SystemProperty sysProperty = systemPropertyDao
				.queryByCategoryAndProperty(category, subCategory, property);

		if (sysProperty != null
				&& !value.equalsIgnoreCase(sysProperty.getValue())) {
			sysProperty.setValue(value);
			try {
				systemPropertyDao.saveOrUpdate(sysProperty);
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
}
