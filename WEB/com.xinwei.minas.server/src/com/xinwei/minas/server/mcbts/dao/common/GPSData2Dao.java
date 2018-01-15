/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.GPSData;

/**
 * 
 * GPS管理消息Dao的第二种接口,主要用于处理MA=7的GPS消息
 * 
 * 
 * @author tiance
 * 
 */

public interface GPSData2Dao {

	public void batchSaveOrUpdate(List<GPSData> tasks);
}
