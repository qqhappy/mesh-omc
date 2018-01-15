/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 系统属性基本DAO
 * 
 * 
 * @author tiance
 * 
 */

public interface SystemPropertyDAO extends GenericDAO<SystemProperty, Long> {
	public SystemProperty queryByCategoryAndProperty(String category,
			String sub_category, String property);
}
