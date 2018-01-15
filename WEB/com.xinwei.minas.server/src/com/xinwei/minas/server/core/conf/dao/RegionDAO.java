/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao;


import com.xinwei.minas.core.model.Region;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 地区服务DAO
 * 
 * @author chenjunhua
 * 
 */

public interface RegionDAO extends GenericDAO<Region, Long>{
	public Region findByName(String name);
}
