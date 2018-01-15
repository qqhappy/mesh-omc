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
 * GPS������ϢDao�ĵڶ��ֽӿ�,��Ҫ���ڴ���MA=7��GPS��Ϣ
 * 
 * 
 * @author tiance
 * 
 */

public interface GPSData2Dao {

	public void batchSaveOrUpdate(List<GPSData> tasks);
}
