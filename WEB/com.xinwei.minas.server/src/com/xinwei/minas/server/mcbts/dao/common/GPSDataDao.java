/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * GPS管理消息Dao接口
 * 
 * 
 * @author tiance
 * 
 */

public interface GPSDataDao extends GenericDAO<GPSData, Long> {

	public GPSData queryByMoId(Long moId);
}
