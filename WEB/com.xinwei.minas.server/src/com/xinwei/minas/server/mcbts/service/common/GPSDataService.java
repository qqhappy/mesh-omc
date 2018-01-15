/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-21	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.common.GPSData;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * GPS管理消息服务接口
 * 
 * @author tiance
 * 
 */

public interface GPSDataService extends ICustomService {

	public GPSData queryFromEMS(Long moId) throws Exception;

	public GPSData queryFromNE(Long moId) throws Exception;

	public void config(GPSData data) throws Exception;
}
