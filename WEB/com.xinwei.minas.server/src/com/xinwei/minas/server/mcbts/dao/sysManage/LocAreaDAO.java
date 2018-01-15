/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * Àà¼òÒªÃèÊö
 * 
 * <p>
 * ÀàÏêÏ¸ÃèÊö
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public interface LocAreaDAO extends GenericDAO<LocationArea, Long> {

	public List<LocationArea> queryAll();

	public void saveOrUpdate(List<LocationArea> locationAreaList);
}
