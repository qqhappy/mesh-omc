/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 同播资源的持久化接口
 * 
 * @author tiance
 * 
 */

public interface SimulcastManageDAO extends GenericDAO<Simulcast, Long> {
	public List<Simulcast> queryByDistrictId(long districtId);
}
